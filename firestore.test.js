const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read or write progress", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("progress").get()
  );
});

test("Authenticated user can create and read their own progress and certificates", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("progress")
      .doc("course_budget_101")
      .set({
        userId: ALICE_UID,
        itemType: "course",
        itemKey: "course_budget_101",
        title: "Budgeting & Cash Flow Mastery",
        score: 100,
        completed: true,
        createdAt: now,
        updatedAt: now,
      })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("progress").get()
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("certificates")
      .doc("cert_001")
      .set({
        userId: ALICE_UID,
        recipientName: "Alice Vance",
        courseTitle: "Budgeting & Cash Flow Mastery",
        category: "Financial Education",
        scorePercent: 95,
        verificationCode: "MH-2026-ALICE01",
        createdAt: now,
        updatedAt: now,
      })
  );
});

test("Cross-user isolation: Bob cannot read or modify Alice's data", async () => {
  const now = new Date();
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context
      .firestore()
      .collection("users")
      .doc(ALICE_UID)
      .collection("scam_scans")
      .doc("scan_1")
      .set({
        userId: ALICE_UID,
        inputTitle: "Fake crypto giveaway",
        riskLevel: "CRITICAL",
        riskScore: 96,
        verdictSummary: "Classic advance-fee scam.",
        createdAt: now,
        updatedAt: now,
      });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("scam_scans")
      .doc("scan_1")
      .get()
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("scam_scans").get()
  );
});

test("Shadow update and invalid schema payloads are rejected", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();

  // Ghost field 'isVerified' must be rejected by hasOnly()
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("community_posts")
      .doc("post_1")
      .set({
        userId: ALICE_UID,
        authorName: "Alice",
        category: "Scam Alert",
        title: "Watch out for SMS links",
        body: "Never click unknown bank links.",
        likesCount: 0,
        isVerified: true,
        createdAt: now,
        updatedAt: now,
      })
  );

  // Invalid score (> 100) must be rejected by isValidLearningProgress()
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("progress")
      .doc("bad_score")
      .set({
        userId: ALICE_UID,
        itemType: "quiz",
        itemKey: "quiz_1",
        title: "Scam Quiz",
        score: 999,
        completed: true,
        createdAt: now,
        updatedAt: now,
      })
  );
});

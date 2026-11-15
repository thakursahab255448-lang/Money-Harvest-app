package com.example.data

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import java.util.UUID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MoneyHarvestRepositoryRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun saveProgress_andObserve_authenticatedOwner_succeeds() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repo = MoneyHarvestRepository(firestore)
    val uniqueKey = "course_${UUID.randomUUID().toString().take(8)}"

    val saveResult =
      withTimeout(DEFAULT_TIMEOUT_MS) {
        repo.saveProgress(
          itemType = "course",
          itemKey = uniqueKey,
          title = "Smart Investing 101",
          score = 92,
          completed = true,
        )
      }
    assertTrue("Expected saveProgress to succeed: ${saveResult.exceptionOrNull()}", saveResult.isSuccess)

    val emitted =
      withTimeout(FLOW_TIMEOUT_MS) {
        repo.observeProgress(uid).first { list -> list.any { it.id == uniqueKey } }
      }
    assertTrue(emitted.any { it.id == uniqueKey && it.score == 92L })
  }

  @Test
  fun getProgressById_crossUserAccess_failsWithPermissionDenied() = runBlocking {
    val aliceUid = signInTestUser(ALICE_EMAIL)
    val aliceRepo = MoneyHarvestRepository(firestore)
    val uniqueKey = "quiz_${UUID.randomUUID().toString().take(8)}"

    withTimeout(DEFAULT_TIMEOUT_MS) {
      aliceRepo
        .saveProgress(
          itemType = "quiz",
          itemKey = uniqueKey,
          title = "Cyber Phishing Defense",
          score = 100,
          completed = true,
        )
        .getOrThrow()
    }

    signInTestUser(BOB_EMAIL)
    val bobRepo = MoneyHarvestRepository(firestore)
    val crossResult =
      withTimeout(DEFAULT_TIMEOUT_MS) { bobRepo.getProgressById(aliceUid, uniqueKey) }
    assertTrue(crossResult.isFailure)
    val fsEx = crossResult.exceptionOrNull()?.unwrapFirestoreException()
    assertNotNull("Expected FirebaseFirestoreException", fsEx)
    assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, fsEx?.code)
  }

  @Test
  fun observeProgress_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
    val aliceUid = signInTestUser(ALICE_EMAIL)
    auth.signOut()
    val repo = MoneyHarvestRepository(firestore)

    var caught: Throwable? = null
    try {
      withTimeout(FLOW_TIMEOUT_MS) { repo.observeProgress(aliceUid).first() }
    } catch (e: Throwable) {
      caught = e
    }
    assertNotNull("Expected exception when unauthenticated", caught)
    val fsEx = caught?.unwrapFirestoreException()
    assertNotNull("Expected nested FirebaseFirestoreException, got $caught", fsEx)
    assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, fsEx?.code)
  }

  private companion object {
    const val ALICE_EMAIL = "alice@moneyharvest.test"
    const val BOB_EMAIL = "bob@moneyharvest.test"
    const val DEFAULT_TIMEOUT_MS = 5000L
    const val FLOW_TIMEOUT_MS = 3000L
  }
}

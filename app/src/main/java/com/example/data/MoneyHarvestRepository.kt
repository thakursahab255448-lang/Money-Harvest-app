package com.example.data

import android.content.Context
import com.example.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class MoneyHarvestRepository(private val db: FirebaseFirestore) {

  constructor(
    context: Context
  ) : this(
    FirebaseFirestore.getInstance(
      context.applicationContext.getString(R.string.firestore_database_id)
    )
  )

  private val auth = Firebase.auth

  private fun requireUserId(): String {
    return auth.currentUser?.uid
      ?: throw FirebaseFirestoreException(
        "User must be signed in with Google before accessing Firestore.",
        FirebaseFirestoreException.Code.PERMISSION_DENIED,
      )
  }

  // 1. Learning Progress
  fun observeProgress(userId: String = requireUserId()): Flow<List<LearningProgress>> = flow {
    val path = "users/$userId/progress"
    emitAll(
      db
        .collection("users")
        .document(userId)
        .collection("progress")
        .snapshots()
        .map { snapshot ->
          snapshot.toObjects(
            LearningProgress::class.java,
            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE,
          )
        }
        .catch { error ->
          val firestoreError =
            generateSequence(error) { it.cause }
              .filterIsInstance<FirebaseFirestoreException>()
              .firstOrNull()
          if (firestoreError == null && error is CancellationException) throw error
          val exceptionToLog = firestoreError ?: (error as? Exception)
          if (exceptionToLog != null && auth.currentUser?.uid == userId) {
            handleFirestoreError(exceptionToLog, OperationType.LIST, path)
          }
          throw error
        }
    )
  }

  suspend fun saveProgress(
    itemType: String,
    itemKey: String,
    title: String,
    score: Int,
    completed: Boolean,
  ): Result<String> = runCatching {
    val uid = requireUserId()
    val safeKey = itemKey.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(100).ifBlank { "item_1" }
    val docRef = db.collection("users").document(uid).collection("progress").document(safeKey)
    val payload =
      mapOf(
        "userId" to uid,
        "itemType" to itemType,
        "itemKey" to safeKey,
        "title" to title.take(150),
        "score" to score.coerceIn(0, 100).toLong(),
        "completed" to completed,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp(),
      )
    try {
      docRef.set(payload).await()
      docRef.id
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, docRef.path)
      throw e
    }
  }

  suspend fun getProgressById(targetUserId: String, progressId: String): Result<LearningProgress?> =
    runCatching {
      val docRef =
        db.collection("users").document(targetUserId).collection("progress").document(progressId)
      try {
        docRef
          .get()
          .await()
          .toObject(
            LearningProgress::class.java,
            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE,
          )
      } catch (e: CancellationException) {
        throw e
      } catch (e: Exception) {
        handleFirestoreError(e, OperationType.GET, docRef.path)
        throw e
      }
    }

  // 2. Certificates
  fun observeCertificates(userId: String = requireUserId()): Flow<List<CertificateRecord>> = flow {
    val path = "users/$userId/certificates"
    emitAll(
      db
        .collection("users")
        .document(userId)
        .collection("certificates")
        .snapshots()
        .map { snapshot ->
          snapshot.toObjects(
            CertificateRecord::class.java,
            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE,
          )
        }
        .catch { error ->
          val firestoreError =
            generateSequence(error) { it.cause }
              .filterIsInstance<FirebaseFirestoreException>()
              .firstOrNull()
          if (firestoreError == null && error is CancellationException) throw error
          val exceptionToLog = firestoreError ?: (error as? Exception)
          if (exceptionToLog != null && auth.currentUser?.uid == userId) {
            handleFirestoreError(exceptionToLog, OperationType.LIST, path)
          }
          throw error
        }
    )
  }

  suspend fun issueCertificate(
    recipientName: String,
    courseTitle: String,
    category: String,
    scorePercent: Int,
    verificationCode: String,
  ): Result<String> = runCatching {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("certificates").document()
    val payload =
      mapOf(
        "userId" to uid,
        "recipientName" to recipientName.trim().ifBlank { "Money Harvest Scholar" }.take(100),
        "courseTitle" to courseTitle.trim().take(150),
        "category" to category.trim().ifBlank { "Financial Literacy" }.take(60),
        "scorePercent" to scorePercent.coerceIn(0, 100).toLong(),
        "verificationCode" to verificationCode.trim().take(60),
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp(),
      )
    try {
      docRef.set(payload).await()
      docRef.id
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, docRef.path)
      throw e
    }
  }

  // 3. Scam Detector Scans
  fun observeScamScans(userId: String = requireUserId()): Flow<List<ScamScanRecord>> = flow {
    val path = "users/$userId/scam_scans"
    emitAll(
      db
        .collection("users")
        .document(userId)
        .collection("scam_scans")
        .snapshots()
        .map { snapshot ->
          snapshot.toObjects(
            ScamScanRecord::class.java,
            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE,
          )
        }
        .catch { error ->
          val firestoreError =
            generateSequence(error) { it.cause }
              .filterIsInstance<FirebaseFirestoreException>()
              .firstOrNull()
          if (firestoreError == null && error is CancellationException) throw error
          val exceptionToLog = firestoreError ?: (error as? Exception)
          if (exceptionToLog != null && auth.currentUser?.uid == userId) {
            handleFirestoreError(exceptionToLog, OperationType.LIST, path)
          }
          throw error
        }
    )
  }

  suspend fun saveScamScan(
    inputTitle: String,
    riskLevel: String,
    riskScore: Int,
    verdictSummary: String,
  ): Result<String> = runCatching {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("scam_scans").document()
    val validRisk =
      if (riskLevel in listOf("LOW", "MEDIUM", "HIGH", "CRITICAL")) riskLevel else "MEDIUM"
    val payload =
      mapOf(
        "userId" to uid,
        "inputTitle" to inputTitle.trim().ifBlank { "Scanned Message" }.take(200),
        "riskLevel" to validRisk,
        "riskScore" to riskScore.coerceIn(0, 100).toLong(),
        "verdictSummary" to verdictSummary.trim().ifBlank { "Analysis complete." }.take(1000),
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp(),
      )
    try {
      docRef.set(payload).await()
      docRef.id
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, docRef.path)
      throw e
    }
  }

  // 4. Community Posts
  fun observeCommunityPosts(userId: String = requireUserId()): Flow<List<CommunityPost>> = flow {
    val path = "users/$userId/community_posts"
    emitAll(
      db
        .collection("users")
        .document(userId)
        .collection("community_posts")
        .snapshots()
        .map { snapshot ->
          snapshot.toObjects(
            CommunityPost::class.java,
            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE,
          )
        }
        .catch { error ->
          val firestoreError =
            generateSequence(error) { it.cause }
              .filterIsInstance<FirebaseFirestoreException>()
              .firstOrNull()
          if (firestoreError == null && error is CancellationException) throw error
          val exceptionToLog = firestoreError ?: (error as? Exception)
          if (exceptionToLog != null && auth.currentUser?.uid == userId) {
            handleFirestoreError(exceptionToLog, OperationType.LIST, path)
          }
          throw error
        }
    )
  }

  suspend fun createCommunityPost(
    authorName: String,
    category: String,
    title: String,
    body: String,
  ): Result<String> = runCatching {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("community_posts").document()
    val validCategories =
      listOf("Scam Alert", "Wealth Tip", "Cybersecurity", "Q&A", "Success Story")
    val safeCategory = if (category in validCategories) category else "Wealth Tip"
    val payload =
      mapOf(
        "userId" to uid,
        "authorName" to authorName.trim().ifBlank { "Harvest Member" }.take(80),
        "category" to safeCategory,
        "title" to title.trim().ifBlank { "Community Insight" }.take(150),
        "body" to body.trim().ifBlank { "Shared with Money Harvest Community." }.take(2000),
        "likesCount" to 1L,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp(),
      )
    try {
      docRef.set(payload).await()
      docRef.id
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, docRef.path)
      throw e
    }
  }

  suspend fun incrementPostLikes(post: CommunityPost): Result<Unit> = runCatching {
    val uid = requireUserId()
    require(post.id.isNotBlank()) { "Post ID must not be blank" }
    requireNotNull(post.createdAt) { "Post createdAt must not be null" }
    val docRef = db.collection("users").document(uid).collection("community_posts").document(post.id)
    try {
      docRef.set(post.copy(likesCount = post.likesCount + 1L, updatedAt = null)).await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, docRef.path)
      throw e
    }
  }

  suspend fun deleteCommunityPost(postId: String): Result<Unit> = runCatching {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("community_posts").document(postId)
    try {
      docRef.delete().await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.DELETE, docRef.path)
      throw e
    }
  }
}

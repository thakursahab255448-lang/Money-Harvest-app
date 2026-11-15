package com.example.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class LearningProgress(
  @DocumentId val id: String = "",
  val userId: String = "",
  val itemType: String = "course",
  val itemKey: String = "",
  val title: String = "",
  val score: Long = 0L,
  val completed: Boolean = false,
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

@IgnoreExtraProperties
data class CertificateRecord(
  @DocumentId val id: String = "",
  val userId: String = "",
  val recipientName: String = "",
  val courseTitle: String = "",
  val category: String = "",
  val scorePercent: Long = 0L,
  val verificationCode: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

@IgnoreExtraProperties
data class ScamScanRecord(
  @DocumentId val id: String = "",
  val userId: String = "",
  val inputTitle: String = "",
  val riskLevel: String = "LOW",
  val riskScore: Long = 0L,
  val verdictSummary: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

@IgnoreExtraProperties
data class CommunityPost(
  @DocumentId val id: String = "",
  val userId: String = "",
  val authorName: String = "",
  val category: String = "Wealth Tip",
  val title: String = "",
  val body: String = "",
  val likesCount: Long = 0L,
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null,
)

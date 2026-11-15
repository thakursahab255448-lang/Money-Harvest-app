package com.example.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CertificateRecord
import com.example.data.CommunityPost
import com.example.data.HarvestPage
import com.example.data.LearningProgress
import com.example.data.MoneyHarvestCatalog
import com.example.data.MoneyHarvestRepository
import com.example.data.ScamScanRecord
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import com.google.firebase.firestore.FirebaseFirestoreException
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

sealed interface DataUiState<out T> {
  data object Loading : DataUiState<Nothing>
  data class Success<T>(val data: T) : DataUiState<T>
  data class Error(val message: String) : DataUiState<Nothing>
}

data class ScamAnalysisResult(
  val riskLevel: String,
  val riskScore: Int,
  val verdictHeadline: String,
  val explanation: String,
  val redFlags: List<String>,
  val recommendedActions: List<String>,
)

sealed interface ScamScanUiState {
  data object Idle : ScamScanUiState
  data object Analyzing : ScamScanUiState
  data class Result(val analysis: ScamAnalysisResult) : ScamScanUiState
  data class Error(val message: String) : ScamScanUiState
}

data class ChatMessage(
  val id: String = UUID.randomUUID().toString(),
  val isUser: Boolean,
  val text: String,
  val isStreaming: Boolean = false,
)

class MoneyHarvestViewModel(
  private val repository: MoneyHarvestRepository,
  val currentUserId: String,
  val userDisplayName: String,
) : ViewModel() {

  private val _currentPage = MutableStateFlow(HarvestPage.HOME)
  val currentPage: StateFlow<HarvestPage> = _currentPage.asStateFlow()

  private val _navigationStack = mutableListOf(HarvestPage.HOME)

  private val _statusBanner = MutableStateFlow<String?>(null)
  val statusBanner: StateFlow<String?> = _statusBanner.asStateFlow()

  fun navigateTo(page: HarvestPage) {
    if (_currentPage.value != page) {
      _navigationStack.add(page)
      _currentPage.value = page
    }
  }

  fun navigateBack(): Boolean {
    if (_navigationStack.size > 1) {
      _navigationStack.removeAt(_navigationStack.lastIndex)
      _currentPage.value = _navigationStack.last()
      return true
    }
    return false
  }

  fun clearBanner() {
    _statusBanner.value = null
  }

  fun showBanner(msg: String) {
    _statusBanner.value = msg
  }

  // Real-time Firestore StateFlows with Two-Tier Error Unwrapping
  val progressState: StateFlow<DataUiState<List<LearningProgress>>> =
    repository
      .observeProgress(currentUserId)
      .map<List<LearningProgress>, DataUiState<List<LearningProgress>>> { DataUiState.Success(it) }
      .catch { error ->
        val fsErr =
          generateSequence(error) { it.cause }
            .filterIsInstance<FirebaseFirestoreException>()
            .firstOrNull()
        if (fsErr == null && error is CancellationException) throw error
        val root = fsErr ?: error
        Log.w(TAG, "Error observing progress", root)
        emit(DataUiState.Error(root.message ?: "Failed to load learning progress"))
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DataUiState.Loading,
      )

  val certificatesState: StateFlow<DataUiState<List<CertificateRecord>>> =
    repository
      .observeCertificates(currentUserId)
      .map<List<CertificateRecord>, DataUiState<List<CertificateRecord>>> {
        DataUiState.Success(it)
      }
      .catch { error ->
        val fsErr =
          generateSequence(error) { it.cause }
            .filterIsInstance<FirebaseFirestoreException>()
            .firstOrNull()
        if (fsErr == null && error is CancellationException) throw error
        val root = fsErr ?: error
        Log.w(TAG, "Error observing certificates", root)
        emit(DataUiState.Error(root.message ?: "Failed to load certificates"))
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DataUiState.Loading,
      )

  val scamScansState: StateFlow<DataUiState<List<ScamScanRecord>>> =
    repository
      .observeScamScans(currentUserId)
      .map<List<ScamScanRecord>, DataUiState<List<ScamScanRecord>>> { DataUiState.Success(it) }
      .catch { error ->
        val fsErr =
          generateSequence(error) { it.cause }
            .filterIsInstance<FirebaseFirestoreException>()
            .firstOrNull()
        if (fsErr == null && error is CancellationException) throw error
        val root = fsErr ?: error
        Log.w(TAG, "Error observing scam scans", root)
        emit(DataUiState.Error(root.message ?: "Failed to load scan history"))
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DataUiState.Loading,
      )

  val communityPostsState: StateFlow<DataUiState<List<CommunityPost>>> =
    repository
      .observeCommunityPosts(currentUserId)
      .map<List<CommunityPost>, DataUiState<List<CommunityPost>>> { userPosts ->
        val combined =
          (userPosts.sortedByDescending { it.createdAt?.seconds ?: Long.MAX_VALUE } +
            MoneyHarvestCatalog.curatedCommunityFeed)
        DataUiState.Success(combined)
      }
      .catch { error ->
        val fsErr =
          generateSequence(error) { it.cause }
            .filterIsInstance<FirebaseFirestoreException>()
            .firstOrNull()
        if (fsErr == null && error is CancellationException) throw error
        val root = fsErr ?: error
        Log.w(TAG, "Error observing community posts", root)
        emit(DataUiState.Error(root.message ?: "Failed to load community posts"))
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DataUiState.Success(MoneyHarvestCatalog.curatedCommunityFeed),
      )

  // --- Firebase AI Logic Models (Lazy Initialized per Skill) ---
  private val scamDetectorSchema =
    Schema.obj(
      properties =
        mapOf(
          "riskLevel" to
            Schema.enumeration(
              values = listOf("LOW", "MEDIUM", "HIGH", "CRITICAL"),
              description = "Assessed scam risk level",
            ),
          "riskScore" to
            Schema.integer(description = "Scam probability score from 0 (safe) to 100 (definite scam)"),
          "verdictHeadline" to
            Schema.string(description = "Short punchy verdict headline summarizing the analysis"),
          "explanation" to
            Schema.string(description = "Detailed analysis explaining why this is safe or fraudulent"),
          "redFlags" to
            Schema.array(
              items = Schema.string(),
              description = "Specific psychological, technical, or financial red flags detected",
            ),
          "recommendedActions" to
            Schema.array(
              items = Schema.string(),
              description = "Immediate protective steps the user should take",
            ),
        )
    )

  private val scamDetectorModel by lazy {
    Firebase.ai.generativeModel(
      modelName = "gemini-3.8-flash",
      generationConfig =
        generationConfig {
          responseMimeType = "application/json"
          responseSchema = scamDetectorSchema
        },
    )
  }

  private val assistantModel by lazy {
    Firebase.ai.generativeModel(
      modelName = "gemini-3.8-flash",
    )
  }

  // --- Scam Detector AI Logic ---
  private val _scamScanUiState = MutableStateFlow<ScamScanUiState>(ScamScanUiState.Idle)
  val scamScanUiState: StateFlow<ScamScanUiState> = _scamScanUiState.asStateFlow()

  fun analyzeSuspiciousContent(inputText: String, channelType: String) {
    if (inputText.isBlank()) {
      _scamScanUiState.value =
        ScamScanUiState.Error("Please paste a suspicious message, URL, email, or investment pitch to scan.")
      return
    }
    viewModelScope.launch {
      _scamScanUiState.value = ScamScanUiState.Analyzing
      try {
        val prompt =
          """
          You are Money Harvest's AI Scam & Phishing Detector specializing in financial fraud, investment Ponzis, pig-butchering crypto schemes, UPI/QR payment traps, fake task jobs, and banking phishing.
          Channel/Source: $channelType
          User-submitted content to analyze:
          \"\"\"
          $inputText
          \"\"\"
          Evaluate the content rigorously and return the structured JSON assessment.
          """.trimIndent()

        val response = withTimeout(60_000L) { scamDetectorModel.generateContent(prompt) }
        val jsonText =
          response.text ?: throw IllegalStateException("AI Scam Detector returned an empty response.")

        val json = JSONObject(jsonText)
        val riskLevel = json.optString("riskLevel", "HIGH")
        val riskScore = json.optInt("riskScore", 75).coerceIn(0, 100)
        val headline = json.optString("verdictHeadline", "Suspicious Financial Message Analyzed")
        val explanation = json.optString("explanation", "Review the detected red flags carefully.")
        val redFlagsArr = json.optJSONArray("redFlags")
        val redFlags =
          if (redFlagsArr != null) {
            (0 until redFlagsArr.length()).map { redFlagsArr.getString(it) }
          } else {
            emptyList()
          }
        val actionsArr = json.optJSONArray("recommendedActions")
        val actions =
          if (actionsArr != null) {
            (0 until actionsArr.length()).map { actionsArr.getString(it) }
          } else {
            emptyList()
          }

        val result =
          ScamAnalysisResult(
            riskLevel = riskLevel,
            riskScore = riskScore,
            verdictHeadline = headline,
            explanation = explanation,
            redFlags = redFlags,
            recommendedActions = actions,
          )
        _scamScanUiState.value = ScamScanUiState.Result(result)

        // Save scan record to Firestore
        repository.saveScamScan(
          inputTitle = "[$channelType] ${inputText.take(120)}",
          riskLevel = riskLevel,
          riskScore = riskScore,
          verdictSummary = "$headline — $explanation",
        )
      } catch (e: TimeoutCancellationException) {
        Log.e(TAG, "Scam Detector timed out", e)
        _scamScanUiState.value =
          ScamScanUiState.Error("Analysis timed out after 60 seconds. Please try again.")
      } catch (e: CancellationException) {
        throw e
      } catch (e: Exception) {
        Log.e(TAG, "Scam Detector failed", e)
        _scamScanUiState.value =
          ScamScanUiState.Error(e.localizedMessage ?: "Failed to analyze content.")
      }
    }
  }

  fun resetScamScanner() {
    _scamScanUiState.value = ScamScanUiState.Idle
  }

  // --- AI Financial & Cyber Assistant Chat (Streaming) ---
  private val _chatMessages =
    MutableStateFlow(
      listOf(
        ChatMessage(
          isUser = false,
          text =
            "Welcome to **Money Harvest AI Assistant**! I'm your personal mentor for **Financial Literacy**, **SIP & Wealth Planning**, **Debt Payoff**, and **Cyber & Scam Protection**.\n\nAsk me anything—or tap one of the quick prompts below!",
        )
      )
    )
  val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

  private val _isChatStreaming = MutableStateFlow(false)
  val isChatStreaming: StateFlow<Boolean> = _isChatStreaming.asStateFlow()

  private val _chatError = MutableStateFlow<String?>(null)
  val chatError: StateFlow<String?> = _chatError.asStateFlow()

  fun sendAssistantMessage(userPrompt: String) {
    val trimmed = userPrompt.trim()
    if (trimmed.isEmpty() || _isChatStreaming.value) return

    _chatError.value = null
    val userMessage = ChatMessage(isUser = true, text = trimmed)
    val placeholderId = UUID.randomUUID().toString()
    val assistantPlaceholder =
      ChatMessage(id = placeholderId, isUser = false, text = "Thinking...", isStreaming = true)

    _chatMessages.value = _chatMessages.value + userMessage + assistantPlaceholder
    _isChatStreaming.value = true

    viewModelScope.launch {
      val accumulated = StringBuilder()
      try {
        val fullPrompt =
          content {
            text(
              """
              You are the official Money Harvest AI Financial & Cybersecurity Mentor.
              Give clear, structured, actionable, and educational guidance on personal finance, budgeting (50/30/20), compounding, SIP/index investing, debt avalanche/snowball, tax & retirement planning, cybersecurity hygiene, and scam prevention.
              Keep answers practical, well-organized with bullet points, and remind users to verify financial products with official regulators.
              
              User Question: $trimmed
              """.trimIndent()
            )
          }

        withTimeout(60_000L) {
          assistantModel.generateContentStream(fullPrompt).collect { chunk ->
            val part = chunk.text.orEmpty()
            accumulated.append(part)
            val currentText = accumulated.toString()
            _chatMessages.value =
              _chatMessages.value.map { msg ->
                if (msg.id == placeholderId) {
                  msg.copy(text = currentText.ifBlank { "Thinking..." }, isStreaming = true)
                } else {
                  msg
                }
              }
          }
        }

        val finalReply =
          accumulated.toString().trim().ifEmpty {
            "I couldn't generate a response. Please try rephrasing your question."
          }
        _chatMessages.value =
          _chatMessages.value.map { msg ->
            if (msg.id == placeholderId) {
              msg.copy(text = finalReply, isStreaming = false)
            } else {
              msg
            }
          }
      } catch (e: TimeoutCancellationException) {
        Log.e(TAG, "AI Assistant streaming timed out", e)
        _chatError.value = "Response timed out after 60 seconds. Please try again."
        _chatMessages.value = _chatMessages.value.filterNot { it.id == placeholderId && accumulated.isEmpty() }
      } catch (e: CancellationException) {
        throw e
      } catch (e: Exception) {
        Log.e(TAG, "AI Assistant streaming failed", e)
        _chatError.value = e.localizedMessage ?: "Failed to reach AI Assistant."
        _chatMessages.value =
          _chatMessages.value.map { msg ->
            if (msg.id == placeholderId) {
              msg.copy(
                text = "⚠️ Error: ${e.localizedMessage ?: "Generation failed."}",
                isStreaming = false,
              )
            } else {
              msg
            }
          }
      } finally {
        _isChatStreaming.value = false
      }
    }
  }

  // --- Persistence Actions (Courses, Education, Cyber, Quizzes, Certificates, Community) ---
  fun markItemCompleted(
    itemType: String,
    itemKey: String,
    title: String,
    score: Int = 100,
    issueCertIfEligible: Boolean = false,
    certCategory: String = "Financial Literacy",
  ) {
    viewModelScope.launch {
      val res =
        repository.saveProgress(
          itemType = itemType,
          itemKey = itemKey,
          title = title,
          score = score,
          completed = true,
        )
      if (res.isSuccess) {
        if (issueCertIfEligible && score >= 60) {
          val code =
            "MH-${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)}-${itemKey.takeLast(5).uppercase()}-${(1000..9999).random()}"
          repository.issueCertificate(
            recipientName = userDisplayName.ifBlank { "Money Harvest Scholar" },
            courseTitle = title,
            category = certCategory,
            scorePercent = score,
            verificationCode = code,
          )
          _statusBanner.value = "Completed \"$title\" ($score%) & unlocked Official Certificate!"
        } else {
          _statusBanner.value = "Progress saved: \"$title\" marked complete!"
        }
      } else {
        _statusBanner.value =
          "Could not save progress: ${res.exceptionOrNull()?.localizedMessage ?: "Unknown error"}"
      }
    }
  }

  fun issueCustomCertificate(
    recipientName: String,
    courseTitle: String,
    category: String,
    scorePercent: Int,
  ) {
    viewModelScope.launch {
      val code =
        "MH-${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)}-${UUID.randomUUID().toString().take(6).uppercase()}"
      val res =
        repository.issueCertificate(
          recipientName = recipientName,
          courseTitle = courseTitle,
          category = category,
          scorePercent = scorePercent,
          verificationCode = code,
        )
      if (res.isSuccess) {
        _statusBanner.value = "Official Certificate issued to $recipientName ($code)!"
      } else {
        _statusBanner.value =
          "Failed to issue certificate: ${res.exceptionOrNull()?.localizedMessage ?: "Error"}"
      }
    }
  }

  fun publishCommunityPost(category: String, title: String, body: String) {
    viewModelScope.launch {
      val res =
        repository.createCommunityPost(
          authorName = userDisplayName.ifBlank { "Harvest Member" },
          category = category,
          title = title,
          body = body,
        )
      if (res.isSuccess) {
        _statusBanner.value = "Your post was published to the Money Harvest Community!"
      } else {
        _statusBanner.value =
          "Failed to publish post: ${res.exceptionOrNull()?.localizedMessage ?: "Error"}"
      }
    }
  }

  fun likeUserPost(post: CommunityPost) {
    if (post.userId != currentUserId || post.id.startsWith("curated_")) {
      _statusBanner.value = "Upvoted ${post.authorName}'s insight!"
      return
    }
    viewModelScope.launch { repository.incrementPostLikes(post) }
  }

  fun deleteUserPost(postId: String) {
    viewModelScope.launch {
      val res = repository.deleteCommunityPost(postId)
      if (res.isSuccess) {
        _statusBanner.value = "Community post deleted."
      }
    }
  }

  private companion object {
    const val TAG = "MoneyHarvestVM"
  }
}

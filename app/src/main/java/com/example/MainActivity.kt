package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.data.HarvestPage
import com.example.data.MoneyHarvestRepository
import com.example.ui.AuthScreen
import com.example.ui.DataUiState
import com.example.ui.MoneyHarvestViewModel
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.CertificatesScreen
import com.example.ui.screens.CommunityScreen
import com.example.ui.screens.CoursesAndResourcesScreen
import com.example.ui.screens.CybersecurityScreen
import com.example.ui.screens.FinancialCalculatorsScreen
import com.example.ui.screens.FinancialEducationScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QuizChallengesScreen
import com.example.ui.screens.ScamDetectorScreen
import com.example.ui.signOutUser
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.EmeraldBright
import com.example.ui.theme.EmeraldCardSurface
import com.example.ui.theme.EmeraldDarkBg
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldSurface
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.HarvestGoldLight
import com.example.ui.theme.MyApplicationTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Mandatory Step 1: Configure Firebase App Check with launch intent before any UI or Firebase call
    configureAppCheck(this, intent)

    enableEdgeToEdge()
    setContent {
      MyApplicationTheme(darkTheme = true) {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
        ) {
          MoneyHarvestRootApp()
        }
      }
    }
  }
}

@Composable
fun MoneyHarvestRootApp() {
  var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }

  DisposableEffect(Unit) {
    val listener =
      FirebaseAuth.AuthStateListener { auth ->
        currentUser = auth.currentUser
      }
    Firebase.auth.addAuthStateListener(listener)
    onDispose { Firebase.auth.removeAuthStateListener(listener) }
  }

  val user = currentUser
  if (user == null) {
    AuthScreen(
      onAuthSuccess = { currentUser = Firebase.auth.currentUser }
    )
  } else {
    val uid = user.uid
    val displayName = user.displayName ?: user.email?.substringBefore("@") ?: "Harvest Scholar"
    key(uid) {
      MoneyHarvestAuthenticatedShell(
        currentUserId = uid,
        userDisplayName = displayName,
      )
    }
  }
}

@Composable
fun MoneyHarvestAuthenticatedShell(
  currentUserId: String,
  userDisplayName: String,
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val credentialManager = remember { CredentialManager.create(context) }

  val vm: MoneyHarvestViewModel =
    viewModel(
      key = currentUserId,
      factory =
        viewModelFactory {
          initializer {
            val app =
              checkNotNull(this[APPLICATION_KEY]) {
                "APPLICATION_KEY missing from CreationExtras"
              }
            val databaseId = app.getString(R.string.firestore_database_id)
            val db = FirebaseFirestore.getInstance(databaseId)
            MoneyHarvestViewModel(
              repository = MoneyHarvestRepository(db),
              currentUserId = currentUserId,
              userDisplayName = userDisplayName,
            )
          }
        },
    )

  val currentPage by vm.currentPage.collectAsStateWithLifecycle()
  val statusBanner by vm.statusBanner.collectAsStateWithLifecycle()

  val progressUiState by vm.progressState.collectAsStateWithLifecycle()
  val certificatesUiState by vm.certificatesState.collectAsStateWithLifecycle()
  val scamScansUiState by vm.scamScansState.collectAsStateWithLifecycle()
  val communityPostsUiState by vm.communityPostsState.collectAsStateWithLifecycle()

  val scamScanUiState by vm.scamScanUiState.collectAsStateWithLifecycle()
  val chatMessages by vm.chatMessages.collectAsStateWithLifecycle()
  val isChatStreaming by vm.isChatStreaming.collectAsStateWithLifecycle()
  val chatError by vm.chatError.collectAsStateWithLifecycle()

  val progressList = (progressUiState as? DataUiState.Success)?.data.orEmpty()
  val certificates = (certificatesUiState as? DataUiState.Success)?.data.orEmpty()
  val scamScans = (scamScansUiState as? DataUiState.Success)?.data.orEmpty()
  val communityPosts = (communityPostsUiState as? DataUiState.Success)?.data.orEmpty()

  BackHandler(enabled = currentPage != HarvestPage.HOME) {
    vm.navigateBack()
  }

  Scaffold(
    containerColor = EmeraldDarkBg,
    topBar = {
      Column(
        modifier =
          Modifier.fillMaxWidth()
            .background(EmeraldSurface)
            .windowInsetsPadding(WindowInsets.statusBars)
      ) {
        // Brand Top Bar
        Row(
          modifier =
            Modifier.fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { vm.navigateTo(HarvestPage.HOME) },
          ) {
            Surface(
              color = HarvestGold.copy(alpha = 0.2f),
              shape = CircleShape,
              modifier = Modifier.size(38.dp),
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.AccountBalance,
                  contentDescription = "Money Harvest Logo",
                  tint = HarvestGoldLight,
                  modifier = Modifier.size(20.dp),
                )
              }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Money Harvest",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
              )
              Text(
                text = currentPage.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = EmeraldBright,
              )
            }
          }

          IconButton(
            onClick = {
              signOutUser(
                credentialManager = credentialManager,
                scope = scope,
              )
            },
            modifier = Modifier.testTag("sign_out_button"),
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ExitToApp,
              contentDescription = "Sign Out",
              tint = HarvestGoldLight,
            )
          }
        }

        // Full 10-Page Scrollable Navigation Strip (Matches Website Navbar)
        Row(
          modifier =
            Modifier.fillMaxWidth()
              .horizontalScroll(rememberScrollState())
              .padding(horizontal = 12.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          HarvestPage.entries.forEach { page ->
            val selected = currentPage == page
            FilterChip(
              selected = selected,
              onClick = { vm.navigateTo(page) },
              label = {
                Text(
                  text = page.title,
                  fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                )
              },
              modifier = Modifier.testTag("nav_chip_${page.name.lowercase()}"),
              colors =
                FilterChipDefaults.filterChipColors(
                  selectedContainerColor = EmeraldPrimary,
                  selectedLabelColor = Color(0xFF032215),
                  containerColor = EmeraldCardSurface,
                  labelColor = Color.White,
                ),
            )
          }
        }
        HorizontalDivider(color = EmeraldBorder)
      }
    },
    bottomBar = {
      NavigationBar(
        containerColor = EmeraldSurface,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
      ) {
        val quickTabs =
          listOf(
            Triple(HarvestPage.HOME, "Home", Icons.Default.Home),
            Triple(HarvestPage.SCAM_DETECTOR, "Scam Radar", Icons.Default.GppGood),
            Triple(HarvestPage.FINANCIAL_CALCULATORS, "Calculators", Icons.Default.Calculate),
            Triple(HarvestPage.AI_ASSISTANT, "AI Mentor", Icons.Default.AutoAwesome),
            Triple(HarvestPage.COURSES_RESOURCES, "Academy", Icons.Default.School),
          )
        quickTabs.forEach { (page, label, icon) ->
          val isSelected = currentPage == page
          NavigationBarItem(
            selected = isSelected,
            onClick = { vm.navigateTo(page) },
            icon = { Icon(icon, contentDescription = label) },
            label = { Text(label, style = MaterialTheme.typography.labelSmall) },
            modifier = Modifier.testTag("bottom_nav_${page.name.lowercase()}"),
            colors =
              NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF032215),
                selectedTextColor = EmeraldBright,
                indicatorColor = EmeraldPrimary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
              ),
          )
        }
      }
    },
  ) { innerPadding ->
    Box(
      modifier =
        Modifier.fillMaxSize()
          .padding(innerPadding),
      contentAlignment = Alignment.TopCenter,
    ) {
      Column(
        modifier =
          Modifier.widthIn(max = 700.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
      ) {
        // Optional Toast / Status Banner
        AnimatedVisibility(visible = statusBanner != null) {
          statusBanner?.let { message ->
            Surface(
              color = EmeraldPrimary.copy(alpha = 0.2f),
              shape = RoundedCornerShape(14.dp),
              modifier =
                Modifier.fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
              ) {
                Text(
                  text = message,
                  style = MaterialTheme.typography.bodySmall,
                  color = EmeraldBright,
                  modifier = Modifier.weight(1f),
                )
                IconButton(
                  onClick = { vm.clearBanner() },
                  modifier = Modifier.size(28.dp),
                ) {
                  Icon(
                    Icons.Default.Close,
                    contentDescription = "Dismiss notification",
                    tint = EmeraldBright,
                    modifier = Modifier.size(16.dp),
                  )
                }
              }
            }
          }
        }

        // Active Page Content
        when (currentPage) {
          HarvestPage.HOME ->
            HomeScreen(
              userName = userDisplayName,
              progressList = progressList,
              certificates = certificates,
              scamScans = scamScans,
              onNavigate = { vm.navigateTo(it) },
            )
          HarvestPage.FINANCIAL_EDUCATION ->
            FinancialEducationScreen(
              progressList = progressList,
              onMarkCompleted = { topic ->
                vm.markItemCompleted(
                  itemType = "education",
                  itemKey = topic.key,
                  title = topic.title,
                  score = 100,
                )
              },
              onOpenCalculators = { vm.navigateTo(HarvestPage.FINANCIAL_CALCULATORS) },
            )
          HarvestPage.SCAM_DETECTOR ->
            ScamDetectorScreen(
              scanUiState = scamScanUiState,
              scanHistory = scamScans,
              onAnalyze = { text, channel -> vm.analyzeSuspiciousContent(text, channel) },
              onReset = { vm.resetScamScanner() },
            )
          HarvestPage.FINANCIAL_CALCULATORS -> FinancialCalculatorsScreen()
          HarvestPage.AI_ASSISTANT ->
            AiAssistantScreen(
              messages = chatMessages,
              isStreaming = isChatStreaming,
              chatError = chatError,
              onSendMessage = { vm.sendAssistantMessage(it) },
            )
          HarvestPage.CYBERSECURITY ->
            CybersecurityScreen(
              onSaveCyberAudit = { topic ->
                vm.markItemCompleted(
                  itemType = "cyber",
                  itemKey = topic.key,
                  title = topic.title,
                  score = 100,
                )
              }
            )
          HarvestPage.COURSES_RESOURCES ->
            CoursesAndResourcesScreen(
              progressList = progressList,
              onCompleteCourseAndCertify = { course ->
                vm.markItemCompleted(
                  itemType = "course",
                  itemKey = course.key,
                  title = course.title,
                  score = 100,
                  issueCertIfEligible = true,
                  certCategory = course.category,
                )
              },
              onOpenCertificates = { vm.navigateTo(HarvestPage.CERTIFICATES) },
            )
          HarvestPage.COMMUNITY ->
            CommunityScreen(
              currentUserId = currentUserId,
              posts = communityPosts,
              onPublishPost = { cat, title, body -> vm.publishCommunityPost(cat, title, body) },
              onLikePost = { vm.likeUserPost(it) },
              onDeletePost = { vm.deleteUserPost(it) },
            )
          HarvestPage.CERTIFICATES ->
            CertificatesScreen(
              defaultUserName = userDisplayName,
              certificates = certificates,
              onIssueCertificate = { name, courseTitle, cat, score ->
                vm.issueCustomCertificate(name, courseTitle, cat, score)
              },
              onGoToCourses = { vm.navigateTo(HarvestPage.COURSES_RESOURCES) },
              onGoToQuizzes = { vm.navigateTo(HarvestPage.QUIZ_CHALLENGES) },
            )
          HarvestPage.QUIZ_CHALLENGES ->
            QuizChallengesScreen(
              progressList = progressList,
              onQuizSubmitted = { pack, score ->
                vm.markItemCompleted(
                  itemType = "quiz",
                  itemKey = pack.key,
                  title = pack.title,
                  score = score,
                  issueCertIfEligible = true,
                  certCategory = pack.category,
                )
              },
              onOpenCertificates = { vm.navigateTo(HarvestPage.CERTIFICATES) },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

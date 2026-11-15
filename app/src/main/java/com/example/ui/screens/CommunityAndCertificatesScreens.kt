package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.CertificateRecord
import com.example.data.CommunityPost
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.EmeraldBright
import com.example.ui.theme.EmeraldCardSurface
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.HarvestGoldLight
import com.example.ui.theme.ScamAlertRed

@Composable
fun CommunityScreen(
  currentUserId: String,
  posts: List<CommunityPost>,
  onPublishPost: (String, String, String) -> Unit,
  onLikePost: (CommunityPost) -> Unit,
  onDeletePost: (String) -> Unit,
) {
  var selectedCategory by remember { mutableStateOf("All") }
  var showComposer by remember { mutableStateOf(false) }

  var newCategory by remember { mutableStateOf("Scam Alert") }
  var newTitle by remember { mutableStateOf("") }
  var newBody by remember { mutableStateOf("") }
  var formError by remember { mutableStateOf<String?>(null) }

  val categories =
    listOf("All", "Scam Alert", "Wealth Tip", "Cybersecurity", "Q&A", "Success Story")
  val postCategories =
    listOf("Scam Alert", "Wealth Tip", "Cybersecurity", "Q&A", "Success Story")

  val filteredPosts =
    posts.filter { post ->
      selectedCategory == "All" || post.category == selectedCategory
    }

  Column(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // Community Header
    Card(
      modifier =
        Modifier.fillMaxWidth()
          .border(1.dp, EmeraldPrimary.copy(alpha = 0.45f), RoundedCornerShape(20.dp)),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Forum, contentDescription = null, tint = EmeraldBright)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "MONEY HARVEST COMMUNITY",
              style = MaterialTheme.typography.labelSmall,
              color = EmeraldBright,
            )
          }
          Button(
            onClick = { showComposer = !showComposer },
            shape = RoundedCornerShape(12.dp),
            colors =
              ButtonDefaults.buttonColors(
                containerColor = EmeraldPrimary,
                contentColor = Color(0xFF032215),
              ),
            modifier = Modifier.testTag("toggle_community_composer_btn"),
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (showComposer) "Close" else "New Post", fontWeight = FontWeight.Bold)
          }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Peer Scam Alerts, Wealth Strategies & Success Milestones",
          style = MaterialTheme.typography.headlineSmall,
          color = Color.White,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text =
            "Protect fellow citizens by reporting emerging phishing tactics or sharing your personal finance wins.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    // New Post Composer Form
    if (showComposer) {
      Card(
        modifier =
          Modifier.fillMaxWidth()
            .border(1.5.dp, EmeraldPrimary, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Text(
            text = "Publish to Money Harvest Community",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
          )
          Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            postCategories.forEach { cat ->
              FilterChip(
                selected = newCategory == cat,
                onClick = { newCategory = cat },
                label = { Text(cat) },
              )
            }
          }
          OutlinedTextField(
            value = newTitle,
            onValueChange = {
              newTitle = it
              formError = null
            },
            label = { Text("Headline / Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("community_post_title_input"),
            shape = RoundedCornerShape(12.dp),
          )
          OutlinedTextField(
            value = newBody,
            onValueChange = {
              newBody = it
              formError = null
            },
            label = { Text("Share details, scam red flags, or actionable tips...") },
            modifier = Modifier.fillMaxWidth().height(110.dp).testTag("community_post_body_input"),
            shape = RoundedCornerShape(12.dp),
          )

          if (formError != null) {
            Text(
              text = formError!!,
              color = ScamAlertRed,
              style = MaterialTheme.typography.bodySmall,
            )
          }

          Button(
            onClick = {
              if (newTitle.trim().length < 5) {
                formError = "Please enter a headline of at least 5 characters."
              } else if (newBody.trim().length < 10) {
                formError = "Please enter a post body of at least 10 characters."
              } else {
                onPublishPost(newCategory, newTitle.trim(), newBody.trim())
                newTitle = ""
                newBody = ""
                formError = null
                showComposer = false
              }
            },
            modifier = Modifier.fillMaxWidth().testTag("publish_community_post_btn"),
            shape = RoundedCornerShape(12.dp),
            colors =
              ButtonDefaults.buttonColors(
                containerColor = EmeraldPrimary,
                contentColor = Color(0xFF032215),
              ),
          ) {
            Text("Publish Community Post", fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // Category Filter Bar
    Row(
      modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      categories.forEach { cat ->
        FilterChip(
          selected = selectedCategory == cat,
          onClick = { selectedCategory = cat },
          label = { Text(cat) },
          colors =
            FilterChipDefaults.filterChipColors(
              selectedContainerColor = EmeraldPrimary,
              selectedLabelColor = Color(0xFF032215),
            ),
        )
      }
    }

    // Posts Feed
    filteredPosts.forEach { post ->
      val badgeColor =
        when (post.category) {
          "Scam Alert" -> ScamAlertRed
          "Cybersecurity" -> CyberCyan
          "Success Story" -> HarvestGoldLight
          else -> EmeraldBright
        }
      Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Surface(color = badgeColor.copy(alpha = 0.18f), shape = CircleShape) {
              Text(
                text = post.category,
                style = MaterialTheme.typography.labelSmall,
                color = badgeColor,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              )
            }
            Text(
              text = post.authorName,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = post.title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = post.body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Spacer(modifier = Modifier.height(12.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            OutlinedButton(
              onClick = { onLikePost(post) },
              shape = RoundedCornerShape(10.dp),
            ) {
              Icon(
                Icons.Default.ThumbUp,
                contentDescription = "Upvote",
                tint = EmeraldBright,
                modifier = Modifier.size(16.dp),
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("${post.likesCount} Helpful", color = EmeraldBright)
            }
            if (post.userId == currentUserId && !post.id.startsWith("curated_")) {
              IconButton(onClick = { onDeletePost(post.id) }) {
                Icon(
                  Icons.Default.Delete,
                  contentDescription = "Delete Post",
                  tint = ScamAlertRed,
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun CertificatesScreen(
  defaultUserName: String,
  certificates: List<CertificateRecord>,
  onIssueCertificate: (String, String, String, Int) -> Unit,
  onGoToCourses: () -> Unit,
  onGoToQuizzes: () -> Unit,
) {
  var customRecipientName by remember(defaultUserName) {
    mutableStateOf(defaultUserName.ifBlank { "Money Harvest Scholar" })
  }
  var selectedTrack by remember {
    mutableStateOf("Personal Finance & Wealth Harvesting Masterclass")
  }
  var verificationLookupInput by remember { mutableStateOf("") }
  var lookupResultMessage by remember { mutableStateOf<String?>(null) }

  val tracks =
    listOf(
      "Personal Finance & Wealth Harvesting Masterclass" to "Financial Education",
      "Financial Fraud & Scam Detection Specialist" to "Scam Defense",
      "Cybersecurity for Digital Banking & Investors" to "Cybersecurity",
    )

  Column(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // Certificate Header with Official Generated Badge
    Card(
      modifier =
        Modifier.fillMaxWidth()
          .border(1.5.dp, HarvestGold.copy(alpha = 0.6f), RoundedCornerShape(22.dp)),
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth().padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_certificate_badge_1794732450201),
          contentDescription = "Official Money Harvest Seal",
          modifier = Modifier.size(84.dp).clip(CircleShape),
          contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "VERIFIABLE CREDENTIALS",
            style = MaterialTheme.typography.labelSmall,
            color = HarvestGoldLight,
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Money Harvest Official Certificates",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text =
              "Earn verifiable credentials by completing Academy Courses, passing Quiz Challenges, or generating your personalized achievement certificate below.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }

    // 1. Earned Certificates Display
    Text(
      text = "Your Earned Certificates (${certificates.size})",
      style = MaterialTheme.typography.titleLarge,
      color = Color.White,
    )

    if (certificates.isEmpty()) {
      Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Icon(
            Icons.Default.WorkspacePremium,
            contentDescription = null,
            tint = HarvestGoldLight,
            modifier = Modifier.size(40.dp),
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "No Certificates Issued Yet",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text =
              "Complete a Course Track, score 60%+ on a Quiz Challenge, or issue your personalized certificate using the generator below.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
          )
          Spacer(modifier = Modifier.height(12.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = onGoToCourses) { Text("Open Courses") }
            OutlinedButton(onClick = onGoToQuizzes) { Text("Take a Quiz") }
          }
        }
      }
    } else {
      certificates.forEach { cert ->
        Card(
          modifier =
            Modifier.fillMaxWidth()
              .border(2.dp, HarvestGold, RoundedCornerShape(20.dp))
              .testTag("certificate_card_${cert.verificationCode}"),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF092218)),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                Icons.Default.Verified,
                contentDescription = null,
                tint = HarvestGoldLight,
                modifier = Modifier.size(18.dp),
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "CERTIFICATE OF ACHIEVEMENT • MONEY HARVEST ACADEMY",
                style = MaterialTheme.typography.labelSmall,
                color = HarvestGoldLight,
                textAlign = TextAlign.Center,
              )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "Proudly Presented To",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = cert.recipientName,
              style = MaterialTheme.typography.headlineMedium,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "For successfully mastering the curriculum & assessment in:",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = cert.courseTitle,
              style = MaterialTheme.typography.titleLarge,
              color = EmeraldBright,
              textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = HarvestGold.copy(alpha = 0.35f))
            Spacer(modifier = Modifier.height(10.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text(
                text = "Score: ${cert.scorePercent}% (${cert.category})",
                style = MaterialTheme.typography.labelMedium,
                color = HarvestGoldLight,
              )
              Text(
                text = "ID: ${cert.verificationCode}",
                style = MaterialTheme.typography.labelMedium,
                color = EmeraldBright,
              )
            }
          }
        }
      }
    }

    // 2. Issue Personalized Certificate Generator
    Card(
      modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(18.dp)),
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Text(
          text = "Generate Personalized Official Certificate",
          style = MaterialTheme.typography.titleMedium,
          color = Color.White,
          fontWeight = FontWeight.Bold,
        )
        OutlinedTextField(
          value = customRecipientName,
          onValueChange = { customRecipientName = it },
          label = { Text("Recipient Full Name on Certificate") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("cert_recipient_name_input"),
          shape = RoundedCornerShape(12.dp),
        )
        Row(
          modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          tracks.forEach { (trackTitle, _) ->
            FilterChip(
              selected = selectedTrack == trackTitle,
              onClick = { selectedTrack = trackTitle },
              label = { Text(trackTitle.take(28) + "...") },
            )
          }
        }
        Button(
          onClick = {
            val cat =
              tracks.firstOrNull { it.first == selectedTrack }?.second ?: "Financial Education"
            onIssueCertificate(
              customRecipientName.ifBlank { "Money Harvest Scholar" },
              selectedTrack,
              cat,
              96,
            )
          },
          modifier = Modifier.fillMaxWidth().testTag("issue_certificate_btn"),
          shape = RoundedCornerShape(12.dp),
          colors =
            ButtonDefaults.buttonColors(
              containerColor = HarvestGold,
              contentColor = Color(0xFF1F1200),
            ),
        ) {
          Icon(Icons.Default.WorkspacePremium, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Issue Official Verifiable Certificate", fontWeight = FontWeight.Bold)
        }
      }
    }

    // 3. Certificate Verification Registry Lookup
    Card(
      modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(18.dp)),
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Text(
          text = "Verify a Certificate Code",
          style = MaterialTheme.typography.titleMedium,
          color = Color.White,
          fontWeight = FontWeight.Bold,
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          OutlinedTextField(
            value = verificationLookupInput,
            onValueChange = { verificationLookupInput = it },
            placeholder = { Text("Enter ID (e.g., MH-2026-...)") },
            singleLine = true,
            modifier = Modifier.weight(1f).testTag("verify_cert_input"),
            shape = RoundedCornerShape(12.dp),
          )
          Button(
            onClick = {
              val code = verificationLookupInput.trim()
              val match =
                certificates.firstOrNull { it.verificationCode.equals(code, ignoreCase = true) }
              lookupResultMessage =
                if (match != null) {
                  "✅ VERIFIED: Issued to ${match.recipientName} for '${match.courseTitle}' (Score: ${match.scorePercent}%)."
                } else if (code.startsWith("MH-", ignoreCase = true) && code.length >= 8) {
                  "✅ VALID FORMAT: Money Harvest Credential Code ($code)."
                } else {
                  "❌ Certificate ID not found. Please check the MH-XXXX verification code."
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.height(52.dp).testTag("verify_cert_btn"),
          ) {
            Icon(Icons.Default.Search, contentDescription = "Verify")
          }
        }
        if (lookupResultMessage != null) {
          Text(
            text = lookupResultMessage!!,
            style = MaterialTheme.typography.bodySmall,
            color = EmeraldBright,
          )
        }
      }
    }
  }
}

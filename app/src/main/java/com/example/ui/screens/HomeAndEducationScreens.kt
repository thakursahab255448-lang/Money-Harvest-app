package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.CertificateRecord
import com.example.data.EducationTopic
import com.example.data.HarvestPage
import com.example.data.LearningProgress
import com.example.data.MoneyHarvestCatalog
import com.example.data.ScamScanRecord
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.EmeraldBright
import com.example.ui.theme.EmeraldCardSurface
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.HarvestGoldLight

@Composable
fun HomeScreen(
  userName: String,
  progressList: List<LearningProgress>,
  certificates: List<CertificateRecord>,
  scamScans: List<ScamScanRecord>,
  onNavigate: (HarvestPage) -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp),
  ) {
    // 1. Hero Banner Card
    Card(
      modifier =
        Modifier.fillMaxWidth()
          .border(1.dp, EmeraldPrimary.copy(alpha = 0.45f), RoundedCornerShape(24.dp)),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth().height(190.dp)) {
          Image(
            painter = painterResource(id = R.drawable.img_hero_money_harvest_1794732448362),
            contentDescription = "Money Harvest Hero Banner",
            modifier = Modifier.fillMaxWidth().height(190.dp),
            contentScale = ContentScale.Crop,
          )
          Box(
            modifier =
              Modifier.matchParentSize()
                .background(
                  Brush.verticalGradient(
                    colors =
                      listOf(
                        Color.Transparent,
                        Color(0xCC061610),
                        EmeraldCardSurface,
                      )
                  )
                )
          )
          Surface(
            color = HarvestGold.copy(alpha = 0.2f),
            shape = CircleShape,
            modifier = Modifier.padding(16.dp).align(Alignment.TopStart),
          ) {
            Text(
              text = "✨ MONEY HARVEST ECOSYSTEM",
              style = MaterialTheme.typography.labelSmall,
              color = HarvestGoldLight,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            )
          }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
          Text(
            text = "Harvest Smarter Wealth. Defend Against Digital Fraud.",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text =
              "Welcome back, ${userName.ifBlank { "Harvest Scholar" }}! Explore interactive financial education, real-time AI scam detection, precision wealth calculators, and verifiable certifications.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            Button(
              onClick = { onNavigate(HarvestPage.SCAM_DETECTOR) },
              modifier = Modifier.weight(1f).testTag("home_cta_scam_detector"),
              shape = RoundedCornerShape(12.dp),
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = EmeraldPrimary,
                  contentColor = Color(0xFF032215),
                ),
            ) {
              Icon(Icons.Default.GppGood, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Scan Scam", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
              onClick = { onNavigate(HarvestPage.FINANCIAL_CALCULATORS) },
              modifier = Modifier.weight(1f).testTag("home_cta_calculators"),
              shape = RoundedCornerShape(12.dp),
            ) {
              Icon(
                Icons.Default.Calculate,
                contentDescription = null,
                tint = HarvestGoldLight,
                modifier = Modifier.size(18.dp),
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("Calculators", color = HarvestGoldLight)
            }
          }
        }
      }
    }

    // 2. Live Metrics Dashboard Strip
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      MetricStatCard(
        label = "Completed",
        value = "${progressList.count { it.completed }}",
        sub = "Modules & Quizzes",
        accent = EmeraldBright,
        modifier = Modifier.weight(1f),
        onClick = { onNavigate(HarvestPage.COURSES_RESOURCES) },
      )
      MetricStatCard(
        label = "Certificates",
        value = "${certificates.size}",
        sub = "Official Credentials",
        accent = HarvestGoldLight,
        modifier = Modifier.weight(1f),
        onClick = { onNavigate(HarvestPage.CERTIFICATES) },
      )
      MetricStatCard(
        label = "Scam Scans",
        value = "${scamScans.size}",
        sub = "AI Radar Checks",
        accent = CyberCyan,
        modifier = Modifier.weight(1f),
        onClick = { onNavigate(HarvestPage.SCAM_DETECTOR) },
      )
    }

    // 3. Complete 10-Section Platform Directory
    Text(
      text = "Explore All Money Harvest Modules",
      style = MaterialTheme.typography.titleLarge,
      color = Color.White,
    )

    val hubCards =
      listOf(
        Triple(
          HarvestPage.FINANCIAL_EDUCATION,
          Icons.Default.MenuBook,
          "50/30/20 budgeting, index investing, debt avalanche & tax strategy.",
        ),
        Triple(
          HarvestPage.SCAM_DETECTOR,
          Icons.Default.GppGood,
          "Scan suspicious SMS, WhatsApp pitches, URLs & KYC messages with Gemini AI.",
        ),
        Triple(
          HarvestPage.FINANCIAL_CALCULATORS,
          Icons.Default.Calculate,
          "SIP wealth builder, Compound Interest, Loan EMI, Retirement FI & Budget tools.",
        ),
        Triple(
          HarvestPage.AI_ASSISTANT,
          Icons.Default.AutoAwesome,
          "Chat 24/7 with your personal AI Financial & Cybersecurity Mentor.",
        ),
        Triple(
          HarvestPage.CYBERSECURITY,
          Icons.Default.Security,
          "Live password entropy auditor, SIM-swap defense & UPI/QR safety lab.",
        ),
        Triple(
          HarvestPage.COURSES_RESOURCES,
          Icons.Default.School,
          "Structured academy tracks, lessons & Golden Hour Scam Emergency Playbooks.",
        ),
        Triple(
          HarvestPage.COMMUNITY,
          Icons.Default.Forum,
          "Share scam alerts, wealth tips & milestones with verified members.",
        ),
        Triple(
          HarvestPage.CERTIFICATES,
          Icons.Default.WorkspacePremium,
          "View, verify & generate official Money Harvest literacy credentials.",
        ),
        Triple(
          HarvestPage.QUIZ_CHALLENGES,
          Icons.Default.Quiz,
          "Test your Scam Spotter, Wealth IQ & Cyber Defense skills to earn badges.",
        ),
      )

    hubCards.forEach { (page, icon, description) ->
      Card(
        modifier =
          Modifier.fillMaxWidth()
            .clickable { onNavigate(page) }
            .border(1.dp, EmeraldBorder, RoundedCornerShape(18.dp))
            .testTag("hub_card_${page.name.lowercase()}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Surface(
            color = EmeraldPrimary.copy(alpha = 0.16f),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.size(48.dp),
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = icon,
                contentDescription = page.title,
                tint = EmeraldBright,
                modifier = Modifier.size(24.dp),
              )
            }
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = page.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
              )
              Spacer(modifier = Modifier.width(8.dp))
              Surface(
                color = HarvestGold.copy(alpha = 0.18f),
                shape = CircleShape,
              ) {
                Text(
                  text = page.badge,
                  style = MaterialTheme.typography.labelSmall,
                  color = HarvestGoldLight,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                )
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = description,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Open ${page.title}",
            tint = EmeraldBright,
          )
        }
      }
    }

    // 4. Footer Section matching reference identity
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF081C14)),
    ) {
      Column(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AccountBalance,
            contentDescription = null,
            tint = HarvestGold,
            modifier = Modifier.size(20.dp),
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Money Harvest • Financial Literacy & Cyber Safety",
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text =
            "Empowering citizens with practical financial education, fraud detection tools, and digital banking resilience.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}

@Composable
private fun MetricStatCard(
  label: String,
  value: String,
  sub: String,
  accent: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  Card(
    modifier =
      modifier
        .clickable { onClick() }
        .border(1.dp, accent.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(text = label, style = MaterialTheme.typography.labelSmall, color = accent)
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.headlineMedium,
        color = Color.White,
        fontWeight = FontWeight.Bold,
      )
      Text(
        text = sub,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@Composable
fun FinancialEducationScreen(
  progressList: List<LearningProgress>,
  onMarkCompleted: (EducationTopic) -> Unit,
  onOpenCalculators: () -> Unit,
) {
  var selectedCategory by remember { mutableStateOf("All") }
  var searchQuery by remember { mutableStateOf("") }
  var expandedTopicKey by remember {
    mutableStateOf(MoneyHarvestCatalog.educationTopics.first().key)
  }

  val categories =
    listOf(
      "All",
      "Budgeting & Cash Flow",
      "Investing & Wealth",
      "Debt & Credit",
      "Retirement & Tax",
      "Financial Psychology",
    )

  val filteredTopics =
    MoneyHarvestCatalog.educationTopics.filter { topic ->
      val matchesCat = selectedCategory == "All" || topic.category == selectedCategory
      val matchesSearch =
        searchQuery.isBlank() ||
          topic.title.contains(searchQuery, ignoreCase = true) ||
          topic.summary.contains(searchQuery, ignoreCase = true)
      matchesCat && matchesSearch
    }

  val completedKeys = remember(progressList) { progressList.filter { it.completed }.map { it.itemKey }.toSet() }

  Column(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // Header Banner
    Card(
      modifier =
        Modifier.fillMaxWidth()
          .border(1.dp, EmeraldPrimary.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "FINANCIAL EDUCATION HUB",
          style = MaterialTheme.typography.labelSmall,
          color = HarvestGoldLight,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Build Unshakeable Financial Intelligence",
          style = MaterialTheme.typography.headlineSmall,
          color = Color.White,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text =
            "Master budgeting, compounding index funds, high-APR debt elimination, tax-efficient retirement, and behavioral finance.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Text(
            text =
              "Completed: ${MoneyHarvestCatalog.educationTopics.count { it.key in completedKeys }} / ${MoneyHarvestCatalog.educationTopics.size} Guides",
            style = MaterialTheme.typography.labelMedium,
            color = EmeraldBright,
          )
          OutlinedButton(
            onClick = onOpenCalculators,
            shape = RoundedCornerShape(10.dp),
          ) {
            Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Try Calculators")
          }
        }
      }
    }

    // Search Field
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier.fillMaxWidth().testTag("education_search_input"),
      placeholder = { Text("Search budgeting, compounding, debt, tax...") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
      singleLine = true,
      shape = RoundedCornerShape(14.dp),
    )

    // Category Filter Chips
    Row(
      modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      categories.forEach { category ->
        FilterChip(
          selected = selectedCategory == category,
          onClick = { selectedCategory = category },
          label = { Text(category) },
          colors =
            FilterChipDefaults.filterChipColors(
              selectedContainerColor = EmeraldPrimary,
              selectedLabelColor = Color(0xFF032215),
            ),
        )
      }
    }

    // Topics List
    filteredTopics.forEach { topic ->
      val isCompleted = topic.key in completedKeys
      val isExpanded = expandedTopicKey == topic.key

      Card(
        modifier =
          Modifier.fillMaxWidth()
            .clickable { expandedTopicKey = if (isExpanded) "" else topic.key }
            .border(
              1.dp,
              if (isCompleted) EmeraldPrimary else EmeraldBorder,
              RoundedCornerShape(18.dp),
            )
            .testTag("education_topic_${topic.key}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
              ) {
                Surface(
                  color = EmeraldPrimary.copy(alpha = 0.18f),
                  shape = CircleShape,
                ) {
                  Text(
                    text = topic.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldBright,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                  )
                }
                Text(
                  text = "${topic.readingMinutes} min • ${topic.difficulty}",
                  style = MaterialTheme.typography.labelSmall,
                  color = HarvestGoldLight,
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = topic.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
              )
            }
            if (isCompleted) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Completed",
                tint = EmeraldBright,
                modifier = Modifier.size(24.dp),
              )
              Spacer(modifier = Modifier.width(6.dp))
            }
            Icon(
              imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = if (isExpanded) "Collapse" else "Expand",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = topic.summary,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )

          if (isExpanded) {
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = EmeraldBorder)
            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "Core Wealth Principles",
              style = MaterialTheme.typography.titleSmall,
              color = EmeraldBright,
            )
            Spacer(modifier = Modifier.height(6.dp))
            topic.keyPrinciples.forEach { principle ->
              Text(
                text = "• $principle",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier.padding(vertical = 3.dp),
              )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "Actionable Implementation Steps",
              style = MaterialTheme.typography.titleSmall,
              color = HarvestGoldLight,
            )
            Spacer(modifier = Modifier.height(6.dp))
            topic.actionableSteps.forEachIndexed { idx, step ->
              Text(
                text = "${idx + 1}. $step",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier.padding(vertical = 3.dp),
              )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Surface(
              color = Color(0xFF082017),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth(),
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = "Real-World Harvest Example",
                  style = MaterialTheme.typography.labelMedium,
                  color = HarvestGold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = topic.realWorldExample,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Button(
              onClick = { onMarkCompleted(topic) },
              modifier = Modifier.fillMaxWidth().testTag("complete_edu_${topic.key}"),
              shape = RoundedCornerShape(12.dp),
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = if (isCompleted) Color(0xFF1E4D3B) else EmeraldPrimary,
                  contentColor = if (isCompleted) EmeraldBright else Color(0xFF032215),
                ),
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (isCompleted) "Completed • Saved to Cloud" else "Mark Module Completed",
                fontWeight = FontWeight.Bold,
              )
            }
          }
        }
      }
    }
  }
}

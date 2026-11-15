package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.MoneyHarvestCatalog
import com.example.data.ScamScanRecord
import com.example.ui.ChatMessage
import com.example.ui.ScamScanUiState
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.EmeraldBright
import com.example.ui.theme.EmeraldCardSurface
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.HarvestGoldLight
import com.example.ui.theme.ScamAlertRed
import com.example.ui.theme.ScamWarningOrange

@Composable
fun ScamDetectorScreen(
  scanUiState: ScamScanUiState,
  scanHistory: List<ScamScanRecord>,
  onAnalyze: (String, String) -> Unit,
  onReset: () -> Unit,
) {
  var selectedChannel by remember { mutableStateOf("SMS / WhatsApp") }
  var suspiciousInput by remember { mutableStateOf("") }
  var validationError by remember { mutableStateOf<String?>(null) }

  val channels =
    listOf(
      "SMS / WhatsApp",
      "Website / URL",
      "Investment Pitch",
      "Email / KYC Alert",
      "Job / Task Offer",
    )

  Column(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // Banner Card
    Card(
      modifier =
        Modifier.fillMaxWidth()
          .border(1.dp, EmeraldPrimary.copy(alpha = 0.4f), RoundedCornerShape(22.dp)),
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Column {
        Image(
          painter = painterResource(id = R.drawable.img_scam_shield_banner_1794732449016),
          contentDescription = "AI Scam Detector Banner",
          modifier = Modifier.fillMaxWidth().height(160.dp),
          contentScale = ContentScale.Crop,
        )
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "AI-POWERED SCAM & PHISHING RADAR",
            style = MaterialTheme.typography.labelSmall,
            color = HarvestGoldLight,
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Instant Fraud, Phishing & Ponzi Detector",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text =
              "Paste any suspicious SMS, WhatsApp investment group pitch, unknown URL, KYC block notice, or job offer. Gemini AI inspects urgency traps, domain spoofing, and advance-fee red flags.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }

    // Channel Selector
    Text(
      text = "1. Select Source Channel",
      style = MaterialTheme.typography.titleSmall,
      color = EmeraldBright,
    )
    Row(
      modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      channels.forEach { channel ->
        FilterChip(
          selected = selectedChannel == channel,
          onClick = { selectedChannel = channel },
          label = { Text(channel) },
          colors =
            FilterChipDefaults.filterChipColors(
              selectedContainerColor = EmeraldPrimary,
              selectedLabelColor = Color(0xFF032215),
            ),
        )
      }
    }

    // Quick Test Scam Examples
    Text(
      text = "2. Paste Message/URL or Try a Real-World Scam Sample",
      style = MaterialTheme.typography.titleSmall,
      color = EmeraldBright,
    )
    Row(
      modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      val sampleLabels =
        listOf(
          "Sample: Fake Bank KYC SMS",
          "Sample: Crypto VIP 15% Daily",
          "Sample: YouTube Task Scam",
          "Sample: Tax Arrest Threat",
        )
      sampleLabels.forEachIndexed { idx, label ->
        OutlinedButton(
          onClick = {
            suspiciousInput = MoneyHarvestCatalog.sampleScamTemplates[idx]
            validationError = null
          },
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("scam_sample_btn_$idx"),
        ) {
          Text(label, style = MaterialTheme.typography.bodySmall, color = HarvestGoldLight)
        }
      }
    }

    OutlinedTextField(
      value = suspiciousInput,
      onValueChange = {
        suspiciousInput = it
        if (it.isNotBlank()) validationError = null
      },
      modifier = Modifier.fillMaxWidth().height(135.dp).testTag("scam_detector_input"),
      placeholder = {
        Text("Paste suspicious text, link, email, or investment offer here...")
      },
      shape = RoundedCornerShape(14.dp),
      isError = validationError != null,
    )

    if (validationError != null) {
      Text(
        text = validationError!!,
        color = ScamAlertRed,
        style = MaterialTheme.typography.bodySmall,
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Button(
        onClick = {
          if (suspiciousInput.trim().length < 8) {
            validationError = "Please enter at least 8 characters of a message or URL to analyze."
          } else {
            validationError = null
            onAnalyze(suspiciousInput, selectedChannel)
          }
        },
        enabled = scanUiState !is ScamScanUiState.Analyzing,
        modifier = Modifier.weight(1f).height(50.dp).testTag("analyze_scam_button"),
        shape = RoundedCornerShape(12.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = EmeraldPrimary,
            contentColor = Color(0xFF032215),
          ),
      ) {
        if (scanUiState is ScamScanUiState.Analyzing) {
          CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            color = Color(0xFF032215),
            strokeWidth = 2.dp,
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("Scanning with Gemini AI...", fontWeight = FontWeight.Bold)
        } else {
          Icon(Icons.Default.GppGood, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Scan for Scam Red Flags", fontWeight = FontWeight.Bold)
        }
      }

      if (suspiciousInput.isNotBlank() || scanUiState !is ScamScanUiState.Idle) {
        OutlinedButton(
          onClick = {
            suspiciousInput = ""
            validationError = null
            onReset()
          },
          modifier = Modifier.height(50.dp),
          shape = RoundedCornerShape(12.dp),
        ) {
          Icon(Icons.Default.Refresh, contentDescription = "Reset")
        }
      }
    }

    // Analysis Result Card
    when (scanUiState) {
      is ScamScanUiState.Idle -> {}
      is ScamScanUiState.Analyzing -> {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Text(
              text = "Inspecting linguistic urgency, URL patterns & financial fraud signatures...",
              style = MaterialTheme.typography.bodyMedium,
              color = EmeraldBright,
            )
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
              modifier = Modifier.fillMaxWidth(),
              color = EmeraldPrimary,
            )
          }
        }
      }
      is ScamScanUiState.Error -> {
        Card(
          modifier = Modifier.fillMaxWidth().border(1.dp, ScamAlertRed, RoundedCornerShape(16.dp)),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF2A0E11)),
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Scan Error",
              style = MaterialTheme.typography.titleMedium,
              color = ScamAlertRed,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = scanUiState.message,
              style = MaterialTheme.typography.bodySmall,
              color = Color.White,
            )
          }
        }
      }
      is ScamScanUiState.Result -> {
        val res = scanUiState.analysis
        val badgeColor =
          when (res.riskLevel.uppercase()) {
            "CRITICAL", "HIGH" -> ScamAlertRed
            "MEDIUM" -> ScamWarningOrange
            else -> EmeraldBright
          }

        Card(
          modifier =
            Modifier.fillMaxWidth()
              .border(1.5.dp, badgeColor, RoundedCornerShape(20.dp))
              .testTag("scam_result_card"),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Surface(
                color = badgeColor.copy(alpha = 0.2f),
                shape = CircleShape,
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Icon(
                    imageVector =
                      if (res.riskScore >= 50) Icons.Default.GppBad else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = badgeColor,
                    modifier = Modifier.size(18.dp),
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "RISK LEVEL: ${res.riskLevel}",
                    style = MaterialTheme.typography.labelMedium,
                    color = badgeColor,
                  )
                }
              }
              Text(
                text = "Risk Score: ${res.riskScore}/100",
                style = MaterialTheme.typography.titleMedium,
                color = badgeColor,
                fontWeight = FontWeight.Bold,
              )
            }

            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
              progress = { res.riskScore / 100f },
              modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
              color = badgeColor,
              trackColor = Color(0xFF071A12),
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(
              text = res.verdictHeadline,
              style = MaterialTheme.typography.titleLarge,
              color = Color.White,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = res.explanation,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (res.redFlags.isNotEmpty()) {
              Spacer(modifier = Modifier.height(14.dp))
              Text(
                text = "🚩 Detected Red Flags",
                style = MaterialTheme.typography.titleSmall,
                color = ScamAlertRed,
              )
              Spacer(modifier = Modifier.height(6.dp))
              res.redFlags.forEach { flag ->
                Text(
                  text = "• $flag",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color.White,
                  modifier = Modifier.padding(vertical = 2.dp),
                )
              }
            }

            if (res.recommendedActions.isNotEmpty()) {
              Spacer(modifier = Modifier.height(14.dp))
              Text(
                text = "🛡️ Recommended Protective Actions",
                style = MaterialTheme.typography.titleSmall,
                color = EmeraldBright,
              )
              Spacer(modifier = Modifier.height(6.dp))
              res.recommendedActions.forEachIndexed { i, act ->
                Text(
                  text = "${i + 1}. $act",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color.White,
                  modifier = Modifier.padding(vertical = 2.dp),
                )
              }
            }
          }
        }
      }
    }

    // Recent Scan History from Cloud Firestore
    if (scanHistory.isNotEmpty()) {
      Spacer(modifier = Modifier.height(6.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.History, contentDescription = null, tint = HarvestGoldLight)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Your Saved Scam Radar History (${scanHistory.size})",
          style = MaterialTheme.typography.titleMedium,
          color = Color.White,
        )
      }
      scanHistory.take(5).forEach { item ->
        val riskColor =
          when (item.riskLevel) {
            "CRITICAL", "HIGH" -> ScamAlertRed
            "MEDIUM" -> ScamWarningOrange
            else -> EmeraldBright
          }
        Card(
          modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(14.dp)),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text(
                text = "${item.riskLevel} (${item.riskScore}/100)",
                style = MaterialTheme.typography.labelSmall,
                color = riskColor,
              )
              Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = riskColor,
                modifier = Modifier.size(16.dp),
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = item.inputTitle,
              style = MaterialTheme.typography.bodySmall,
              color = Color.White,
              fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = item.verdictSummary,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }
    }
  }
}

@Composable
fun AiAssistantScreen(
  messages: List<ChatMessage>,
  isStreaming: Boolean,
  chatError: String?,
  onSendMessage: (String) -> Unit,
) {
  var inputPrompt by remember { mutableStateOf("") }

  val quickPrompts =
    listOf(
      "Create a 50/30/20 budget for $4,000/month",
      "How do I spot a pig-butchering crypto scam?",
      "SIP vs Lump Sum investing for 10 years",
      "How to pay off credit card debt fastest?",
      "What to do in the first 60 mins if scammed?",
    )

  Column(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    // AI Assistant Header
    Card(
      modifier =
        Modifier.fillMaxWidth()
          .border(1.dp, CyberCyan.copy(alpha = 0.45f), RoundedCornerShape(20.dp)),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            color = CyberCyan.copy(alpha = 0.2f),
            shape = CircleShape,
            modifier = Modifier.size(40.dp),
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = CyberCyan,
              )
            }
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "MONEY HARVEST AI MENTOR",
              style = MaterialTheme.typography.labelSmall,
              color = CyberCyan,
            )
            Text(
              text = "24/7 Financial & Cyber Safety Assistant",
              style = MaterialTheme.typography.titleLarge,
              color = Color.White,
            )
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text =
            "Ask personalized questions about budgeting, SIP compounding, loan prepayments, tax planning, or verifying suspicious financial offers.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    // Quick Prompts Row
    Row(
      modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      quickPrompts.forEachIndexed { idx, prompt ->
        OutlinedButton(
          onClick = { onSendMessage(prompt) },
          enabled = !isStreaming,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("ai_quick_prompt_$idx"),
        ) {
          Text(prompt, style = MaterialTheme.typography.bodySmall, color = HarvestGoldLight)
        }
      }
    }

    // Chat Messages Feed
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      messages.forEach { msg ->
        val isUser = msg.isUser
        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart,
        ) {
          Card(
            modifier =
              Modifier.fillMaxWidth(0.92f)
                .border(
                  1.dp,
                  if (isUser) HarvestGold.copy(alpha = 0.5f) else EmeraldBorder,
                  RoundedCornerShape(16.dp),
                ),
            shape = RoundedCornerShape(16.dp),
            colors =
              CardDefaults.cardColors(
                containerColor = if (isUser) Color(0xFF1A3A2A) else EmeraldCardSurface
              ),
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = if (isUser) "You" else "Money Harvest AI Mentor",
                style = MaterialTheme.typography.labelSmall,
                color = if (isUser) HarvestGoldLight else EmeraldBright,
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = msg.text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
              )
              if (msg.isStreaming) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                  modifier = Modifier.fillMaxWidth(),
                  color = EmeraldPrimary,
                )
              }
            }
          }
        }
      }
    }

    if (chatError != null) {
      Text(
        text = chatError,
        color = ScamAlertRed,
        style = MaterialTheme.typography.bodySmall,
      )
    }

    HorizontalDivider(color = EmeraldBorder)

    // Chat Input Row
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      OutlinedTextField(
        value = inputPrompt,
        onValueChange = { inputPrompt = it },
        modifier = Modifier.weight(1f).testTag("ai_chat_input"),
        placeholder = { Text("Ask about SIPs, debt, taxes, or scams...") },
        shape = RoundedCornerShape(14.dp),
        maxLines = 3,
      )
      Button(
        onClick = {
          if (inputPrompt.isNotBlank()) {
            val toSend = inputPrompt
            inputPrompt = ""
            onSendMessage(toSend)
          }
        },
        enabled = !isStreaming && inputPrompt.isNotBlank(),
        modifier = Modifier.height(54.dp).testTag("ai_chat_send_btn"),
        shape = RoundedCornerShape(14.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = EmeraldPrimary,
            contentColor = Color(0xFF032215),
          ),
      ) {
        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
      }
    }
  }
}

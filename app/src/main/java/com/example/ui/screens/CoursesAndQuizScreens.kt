package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CourseTrack
import com.example.data.LearningProgress
import com.example.data.MoneyHarvestCatalog
import com.example.data.QuizChallengePack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.EmeraldBright
import com.example.ui.theme.EmeraldCardSurface
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.HarvestGoldLight
import com.example.ui.theme.ScamAlertRed

@Composable
fun CoursesAndResourcesScreen(
  progressList: List<LearningProgress>,
  onCompleteCourseAndCertify: (CourseTrack) -> Unit,
  onOpenCertificates: () -> Unit,
) {
  var expandedCourseKey by remember {
    mutableStateOf(MoneyHarvestCatalog.courses.first().key)
  }
  val completedKeys =
    remember(progressList) { progressList.filter { it.completed }.map { it.itemKey }.toSet() }

  Column(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // Academy Banner
    Card(
      modifier =
        Modifier.fillMaxWidth()
          .border(1.dp, EmeraldPrimary.copy(alpha = 0.45f), RoundedCornerShape(20.dp)),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.School, contentDescription = null, tint = HarvestGoldLight)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "MONEY HARVEST ACADEMY & RESOURCE VAULT",
            style = MaterialTheme.typography.labelSmall,
            color = HarvestGoldLight,
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Structured Courses & Emergency Action Playbooks",
          style = MaterialTheme.typography.headlineSmall,
          color = Color.White,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text =
            "Complete every lesson in a course track to unlock an official verifiable Money Harvest Certificate.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    // Courses Section
    Text(
      text = "Certification Course Tracks",
      style = MaterialTheme.typography.titleLarge,
      color = Color.White,
    )

    MoneyHarvestCatalog.courses.forEach { course ->
      val isCompleted = course.key in completedKeys
      val isExpanded = expandedCourseKey == course.key

      Card(
        modifier =
          Modifier.fillMaxWidth()
            .clickable { expandedCourseKey = if (isExpanded) "" else course.key }
            .border(
              1.dp,
              if (isCompleted) HarvestGold else EmeraldBorder,
              RoundedCornerShape(18.dp),
            )
            .testTag("course_card_${course.key}"),
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
                Surface(color = EmeraldPrimary.copy(alpha = 0.2f), shape = CircleShape) {
                  Text(
                    text = course.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldBright,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                  )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = HarvestGoldLight,
                    modifier = Modifier.size(14.dp),
                  )
                  Spacer(modifier = Modifier.width(3.dp))
                  Text(
                    text = "${course.rating} (${course.enrolledCount})",
                    style = MaterialTheme.typography.labelSmall,
                    color = HarvestGoldLight,
                  )
                }
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = course.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "${course.level} • ${course.duration}",
                style = MaterialTheme.typography.bodySmall,
                color = EmeraldBright,
              )
            }
            if (isCompleted) {
              Icon(
                Icons.Default.WorkspacePremium,
                contentDescription = "Certified",
                tint = HarvestGoldLight,
                modifier = Modifier.size(26.dp),
              )
              Spacer(modifier = Modifier.width(6.dp))
            }
            Icon(
              imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = course.overview,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )

          if (isExpanded) {
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = EmeraldBorder)
            Spacer(modifier = Modifier.height(14.dp))

            course.lessons.forEach { lesson ->
              Surface(
                color = Color(0xFF082017),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      Icons.Default.PlayCircle,
                      contentDescription = null,
                      tint = EmeraldBright,
                      modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "Module ${lesson.number}: ${lesson.title} (${lesson.durationMinutes}m)",
                      style = MaterialTheme.typography.titleSmall,
                      color = Color.White,
                    )
                  }
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = lesson.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = "💡 Key Takeaway: ${lesson.keyTakeaway}",
                    style = MaterialTheme.typography.labelSmall,
                    color = HarvestGoldLight,
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
              Button(
                onClick = { onCompleteCourseAndCertify(course) },
                modifier = Modifier.weight(1f).testTag("complete_course_${course.key}"),
                shape = RoundedCornerShape(12.dp),
                colors =
                  ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) HarvestGold else EmeraldPrimary,
                    contentColor = Color(0xFF032215),
                  ),
              ) {
                Icon(
                  Icons.Default.WorkspacePremium,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text =
                    if (isCompleted) "Course Certified • Re-Issue" else "Complete & Claim Certificate",
                  fontWeight = FontWeight.Bold,
                )
              }
              if (isCompleted) {
                OutlinedButton(
                  onClick = onOpenCertificates,
                  shape = RoundedCornerShape(12.dp),
                ) {
                  Text("View Cert", color = HarvestGoldLight)
                }
              }
            }
          }
        }
      }
    }

    // Downloadable / Interactive Resource Checklists
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "Quick-Reference Playbooks & Audit Checklists",
      style = MaterialTheme.typography.titleLarge,
      color = Color.White,
    )

    MoneyHarvestCatalog.resourceGuides.forEach { guide ->
      Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.MenuBook, contentDescription = null, tint = CyberCyan)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = guide.type.uppercase(),
              style = MaterialTheme.typography.labelSmall,
              color = CyberCyan,
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = guide.title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = guide.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Spacer(modifier = Modifier.height(10.dp))
          guide.checklistItems.forEach { item ->
            Text(
              text = item,
              style = MaterialTheme.typography.bodySmall,
              color = Color.White,
              modifier = Modifier.padding(vertical = 3.dp),
            )
          }
        }
      }
    }
  }
}

@Composable
fun QuizChallengesScreen(
  progressList: List<LearningProgress>,
  onQuizSubmitted: (QuizChallengePack, Int) -> Unit,
  onOpenCertificates: () -> Unit,
) {
  var selectedPackIndex by remember { mutableIntStateOf(0) }
  val pack = MoneyHarvestCatalog.quizPacks[selectedPackIndex]

  // Map of questionId -> selectedOptionIndex for the active pack
  val selectedAnswers = remember(selectedPackIndex) { mutableStateMapOf<Int, Int>() }
  var isSubmitted by remember(selectedPackIndex) { mutableStateOf(false) }

  val correctCount =
    pack.questions.count { q -> selectedAnswers[q.id] == q.correctIndex }
  val scorePercent =
    if (pack.questions.isNotEmpty()) ((correctCount * 100) / pack.questions.size) else 0

  Column(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // Quiz Header
    Card(
      modifier =
        Modifier.fillMaxWidth()
          .border(1.dp, HarvestGold.copy(alpha = 0.45f), RoundedCornerShape(20.dp)),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Quiz, contentDescription = null, tint = HarvestGoldLight)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "INTERACTIVE QUIZ CHALLENGES",
            style = MaterialTheme.typography.labelSmall,
            color = HarvestGoldLight,
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Test Your Financial & Fraud-Defense Reflexes",
          style = MaterialTheme.typography.headlineSmall,
          color = Color.White,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text =
            "Score 60% or higher on any quiz pack to automatically earn your official Money Harvest Certificate.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    // Pack Selector Cards
    MoneyHarvestCatalog.quizPacks.forEachIndexed { idx, quizPack ->
      val savedBest =
        progressList.firstOrNull { it.itemKey == quizPack.key }?.score
      val isSelected = selectedPackIndex == idx
      Card(
        modifier =
          Modifier.fillMaxWidth()
            .clickable { selectedPackIndex = idx }
            .border(
              1.5.dp,
              if (isSelected) EmeraldPrimary else EmeraldBorder,
              RoundedCornerShape(16.dp),
            )
            .testTag("quiz_pack_card_$idx"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "${quizPack.category} • ${quizPack.difficulty}",
              style = MaterialTheme.typography.labelSmall,
              color = EmeraldBright,
            )
            Text(
              text = quizPack.title,
              style = MaterialTheme.typography.titleMedium,
              color = Color.White,
              fontWeight = FontWeight.Bold,
            )
            Text(
              text = "Reward: ${quizPack.rewardBadge}",
              style = MaterialTheme.typography.bodySmall,
              color = HarvestGoldLight,
            )
          }
          if (savedBest != null) {
            Surface(color = EmeraldPrimary.copy(alpha = 0.2f), shape = CircleShape) {
              Text(
                text = "Best: $savedBest%",
                style = MaterialTheme.typography.labelMedium,
                color = EmeraldBright,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              )
            }
          }
        }
      }
    }

    HorizontalDivider(color = EmeraldBorder)

    // Active Quiz Progress Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = pack.title,
        style = MaterialTheme.typography.titleLarge,
        color = Color.White,
      )
      Text(
        text = "${selectedAnswers.size} / ${pack.questions.size} Answered",
        style = MaterialTheme.typography.labelMedium,
        color = EmeraldBright,
      )
    }
    LinearProgressIndicator(
      progress = { selectedAnswers.size.toFloat() / pack.questions.size.coerceAtLeast(1) },
      modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
      color = EmeraldPrimary,
    )

    // Questions List
    pack.questions.forEachIndexed { qIdx, q ->
      val chosenOpt = selectedAnswers[q.id]
      Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Question ${qIdx + 1} of ${pack.questions.size}",
            style = MaterialTheme.typography.labelSmall,
            color = HarvestGoldLight,
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = q.question,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
          )
          Spacer(modifier = Modifier.height(12.dp))

          q.options.forEachIndexed { optIdx, optText ->
            val isThisSelected = chosenOpt == optIdx
            val isCorrectOption = optIdx == q.correctIndex

            val borderColor =
              when {
                !isSubmitted && isThisSelected -> EmeraldPrimary
                isSubmitted && isCorrectOption -> EmeraldBright
                isSubmitted && isThisSelected && !isCorrectOption -> ScamAlertRed
                else -> EmeraldBorder
              }
            val bgColor =
              when {
                !isSubmitted && isThisSelected -> EmeraldPrimary.copy(alpha = 0.2f)
                isSubmitted && isCorrectOption -> EmeraldPrimary.copy(alpha = 0.25f)
                isSubmitted && isThisSelected && !isCorrectOption -> ScamAlertRed.copy(alpha = 0.2f)
                else -> Color(0xFF082017)
              }

            Surface(
              color = bgColor,
              shape = RoundedCornerShape(12.dp),
              modifier =
                Modifier.fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                  .clickable(enabled = !isSubmitted) { selectedAnswers[q.id] = optIdx }
                  .testTag("quiz_q${q.id}_opt$optIdx"),
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                  text = "${('A' + optIdx)}. $optText",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color.White,
                  modifier = Modifier.weight(1f),
                )
                if (isSubmitted && isCorrectOption) {
                  Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Correct",
                    tint = EmeraldBright,
                    modifier = Modifier.size(18.dp),
                  )
                }
              }
            }
          }

          if (isSubmitted) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              color = Color(0xFF061610),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth(),
            ) {
              Text(
                text = "Explanation: ${q.explanation}",
                style = MaterialTheme.typography.bodySmall,
                color = EmeraldBright,
                modifier = Modifier.padding(12.dp),
              )
            }
          }
        }
      }
    }

    // Submit / Results Section
    if (!isSubmitted) {
      Button(
        onClick = {
          isSubmitted = true
          onQuizSubmitted(pack, scorePercent)
        },
        enabled = selectedAnswers.size == pack.questions.size,
        modifier = Modifier.fillMaxWidth().height(52.dp).testTag("submit_quiz_button"),
        shape = RoundedCornerShape(14.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = EmeraldPrimary,
            contentColor = Color(0xFF032215),
          ),
      ) {
        Icon(Icons.Default.EmojiEvents, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text =
            if (selectedAnswers.size == pack.questions.size) {
              "Submit Quiz & Calculate Score"
            } else {
              "Answer All ${pack.questions.size} Questions to Submit"
            },
          fontWeight = FontWeight.Bold,
        )
      }
    } else {
      Card(
        modifier =
          Modifier.fillMaxWidth()
            .border(1.5.dp, HarvestGold, RoundedCornerShape(20.dp))
            .testTag("quiz_result_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Icon(
            Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = HarvestGoldLight,
            modifier = Modifier.size(44.dp),
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Quiz Score: $scorePercent% ($correctCount / ${pack.questions.size} Correct)",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text =
              if (scorePercent >= 60) {
                "Congratulations! You earned the '${pack.rewardBadge}' credential and unlocked your Official Certificate!"
              } else {
                "Review the explanations above and retry to score 60%+ for your Official Certificate."
              },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Spacer(modifier = Modifier.height(14.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            OutlinedButton(
              onClick = {
                selectedAnswers.clear()
                isSubmitted = false
              },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp),
            ) {
              Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Retake Quiz")
            }
            if (scorePercent >= 60) {
              Button(
                onClick = onOpenCertificates,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors =
                  ButtonDefaults.buttonColors(
                    containerColor = HarvestGold,
                    contentColor = Color(0xFF1F1200),
                  ),
              ) {
                Text("View Certificate", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}

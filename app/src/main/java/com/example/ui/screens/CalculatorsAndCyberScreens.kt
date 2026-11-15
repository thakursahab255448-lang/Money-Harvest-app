package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.CyberTopic
import com.example.data.MoneyHarvestCatalog
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.EmeraldBright
import com.example.ui.theme.EmeraldCardSurface
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.HarvestGoldLight
import com.example.ui.theme.ScamAlertRed
import com.example.ui.theme.ScamWarningOrange
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToLong

@Composable
fun FinancialCalculatorsScreen() {
  var selectedCalcIndex by remember { mutableIntStateOf(0) }
  val calcTabs =
    listOf(
      "SIP Wealth",
      "Compound Interest",
      "Loan EMI",
      "Retirement FI",
      "50/30/20 & Emergency",
    )

  Column(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // Header
    Card(
      modifier =
        Modifier.fillMaxWidth()
          .border(1.dp, HarvestGold.copy(alpha = 0.45f), RoundedCornerShape(20.dp)),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Calculate, contentDescription = null, tint = HarvestGoldLight)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "PRECISION FINANCIAL CALCULATORS",
            style = MaterialTheme.typography.labelSmall,
            color = HarvestGoldLight,
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Plan Your SIP, Compound Growth, Loans & Retirement",
          style = MaterialTheme.typography.headlineSmall,
          color = Color.White,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text =
            "Adjust sliders or type exact numbers to model your long-term wealth harvest in real time.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    // Calculator Selector Tabs
    Row(
      modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      calcTabs.forEachIndexed { idx, tab ->
        FilterChip(
          selected = selectedCalcIndex == idx,
          onClick = { selectedCalcIndex = idx },
          label = { Text(tab) },
          modifier = Modifier.testTag("calc_tab_$idx"),
          colors =
            FilterChipDefaults.filterChipColors(
              selectedContainerColor = HarvestGold,
              selectedLabelColor = Color(0xFF1F1200),
            ),
        )
      }
    }

    when (selectedCalcIndex) {
      0 -> SipCalculatorCard()
      1 -> CompoundInterestCalculatorCard()
      2 -> LoanEmiCalculatorCard()
      3 -> RetirementCalculatorCard()
      4 -> BudgetEmergencyCalculatorCard()
    }
  }
}

private fun formatCurrency(amount: Double): String {
  val fmt = NumberFormat.getCurrencyInstance(Locale.US)
  fmt.maximumFractionDigits = 0
  return fmt.format(amount.coerceAtLeast(0.0))
}

@Composable
private fun SipCalculatorCard() {
  var monthlySip by remember { mutableFloatStateOf(500f) }
  var annualReturn by remember { mutableFloatStateOf(12f) }
  var years by remember { mutableFloatStateOf(15f) }

  val months = (years.toInt() * 12).coerceAtLeast(1)
  val monthlyRate = (annualReturn.toDouble() / 100.0) / 12.0
  val totalInvested = monthlySip.toDouble() * months
  val maturityValue =
    if (monthlyRate > 0) {
      monthlySip.toDouble() *
        (((1.0 + monthlyRate).pow(months.toDouble()) - 1.0) / monthlyRate) *
        (1.0 + monthlyRate)
    } else {
      totalInvested
    }
  val estimatedReturns = (maturityValue - totalInvested).coerceAtLeast(0.0)
  val gainRatio =
    if (maturityValue > 0) (estimatedReturns / maturityValue).toFloat().coerceIn(0f, 1f) else 0f

  Card(
    modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
  ) {
    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
      Text(
        text = "SIP (Systematic Investment Plan) Calculator",
        style = MaterialTheme.typography.titleLarge,
        color = Color.White,
      )

      CalcSliderControl(
        label = "Monthly SIP Contribution",
        valueDisplay = formatCurrency(monthlySip.toDouble()),
        value = monthlySip,
        onValueChange = { monthlySip = it },
        valueRange = 50f..10000f,
      )
      CalcSliderControl(
        label = "Expected Annual Return Rate",
        valueDisplay = "${String.format(Locale.US, "%.1f", annualReturn)}%",
        value = annualReturn,
        onValueChange = { annualReturn = it },
        valueRange = 2f..25f,
      )
      CalcSliderControl(
        label = "Investment Horizon",
        valueDisplay = "${years.toInt()} Years ($months Months)",
        value = years,
        onValueChange = { years = it },
        valueRange = 1f..40f,
      )

      HorizontalDivider(color = EmeraldBorder)

      ResultHighlightBox(
        primaryLabel = "Projected Total SIP Maturity Value",
        primaryValue = formatCurrency(maturityValue),
        leftLabel = "Total Principal Invested",
        leftValue = formatCurrency(totalInvested),
        rightLabel = "Compounded Wealth Gain",
        rightValue = formatCurrency(estimatedReturns),
        ratio = gainRatio,
      )
    }
  }
}

@Composable
private fun CompoundInterestCalculatorCard() {
  var initialPrincipal by remember { mutableFloatStateOf(10000f) }
  var rate by remember { mutableFloatStateOf(9f) }
  var years by remember { mutableFloatStateOf(12f) }
  var compoundFreq by remember { mutableIntStateOf(12) } // 1=Yearly, 4=Quarterly, 12=Monthly

  val r = rate.toDouble() / 100.0
  val n = compoundFreq.toDouble()
  val t = years.toDouble()
  val finalAmount = initialPrincipal.toDouble() * (1.0 + (r / n)).pow(n * t)
  val totalInterest = (finalAmount - initialPrincipal.toDouble()).coerceAtLeast(0.0)
  val interestRatio =
    if (finalAmount > 0) (totalInterest / finalAmount).toFloat().coerceIn(0f, 1f) else 0f

  Card(
    modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
  ) {
    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
      Text(
        text = "Lump-Sum Compound Interest Calculator",
        style = MaterialTheme.typography.titleLarge,
        color = Color.White,
      )

      CalcSliderControl(
        label = "Initial Principal Deposit",
        valueDisplay = formatCurrency(initialPrincipal.toDouble()),
        value = initialPrincipal,
        onValueChange = { initialPrincipal = it },
        valueRange = 500f..250000f,
      )
      CalcSliderControl(
        label = "Annual Interest Rate",
        valueDisplay = "${String.format(Locale.US, "%.1f", rate)}%",
        value = rate,
        onValueChange = { rate = it },
        valueRange = 1f..20f,
      )
      CalcSliderControl(
        label = "Time Period",
        valueDisplay = "${years.toInt()} Years",
        value = years,
        onValueChange = { years = it },
        valueRange = 1f..40f,
      )

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(12 to "Monthly", 4 to "Quarterly", 1 to "Annually").forEach { (freq, name) ->
          FilterChip(
            selected = compoundFreq == freq,
            onClick = { compoundFreq = freq },
            label = { Text(name) },
          )
        }
      }

      HorizontalDivider(color = EmeraldBorder)

      ResultHighlightBox(
        primaryLabel = "Total Compounded Balance",
        primaryValue = formatCurrency(finalAmount),
        leftLabel = "Starting Principal",
        leftValue = formatCurrency(initialPrincipal.toDouble()),
        rightLabel = "Compound Interest Earned",
        rightValue = formatCurrency(totalInterest),
        ratio = interestRatio,
      )
    }
  }
}

@Composable
private fun LoanEmiCalculatorCard() {
  var loanAmount by remember { mutableFloatStateOf(50000f) }
  var annualRate by remember { mutableFloatStateOf(8.5f) }
  var tenureYears by remember { mutableFloatStateOf(5f) }

  val months = (tenureYears.toInt() * 12).coerceAtLeast(1)
  val monthlyRate = (annualRate.toDouble() / 100.0) / 12.0
  val emi =
    if (monthlyRate > 0) {
      (loanAmount.toDouble() * monthlyRate * (1.0 + monthlyRate).pow(months.toDouble())) /
        ((1.0 + monthlyRate).pow(months.toDouble()) - 1.0)
    } else {
      loanAmount.toDouble() / months
    }
  val totalPayment = emi * months
  val totalInterest = (totalPayment - loanAmount.toDouble()).coerceAtLeast(0.0)
  val interestShare =
    if (totalPayment > 0) (totalInterest / totalPayment).toFloat().coerceIn(0f, 1f) else 0f

  Card(
    modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
  ) {
    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
      Text(
        text = "Loan & Mortgage EMI Calculator",
        style = MaterialTheme.typography.titleLarge,
        color = Color.White,
      )

      CalcSliderControl(
        label = "Loan Principal Amount",
        valueDisplay = formatCurrency(loanAmount.toDouble()),
        value = loanAmount,
        onValueChange = { loanAmount = it },
        valueRange = 2000f..500000f,
      )
      CalcSliderControl(
        label = "Annual Interest Rate (APR)",
        valueDisplay = "${String.format(Locale.US, "%.1f", annualRate)}%",
        value = annualRate,
        onValueChange = { annualRate = it },
        valueRange = 2f..28f,
      )
      CalcSliderControl(
        label = "Loan Tenure",
        valueDisplay = "${tenureYears.toInt()} Years ($months Months)",
        value = tenureYears,
        onValueChange = { tenureYears = it },
        valueRange = 1f..30f,
      )

      HorizontalDivider(color = EmeraldBorder)

      ResultHighlightBox(
        primaryLabel = "Estimated Monthly EMI Payment",
        primaryValue = formatCurrency(emi),
        leftLabel = "Total Interest Payable",
        leftValue = formatCurrency(totalInterest),
        rightLabel = "Total Repayment (Principal + Interest)",
        rightValue = formatCurrency(totalPayment),
        ratio = interestShare,
      )
    }
  }
}

@Composable
private fun RetirementCalculatorCard() {
  var currentAge by remember { mutableFloatStateOf(28f) }
  var retirementAge by remember { mutableFloatStateOf(60f) }
  var monthlyExpense by remember { mutableFloatStateOf(3000f) }
  var inflationRate by remember { mutableFloatStateOf(4.5f) }

  val yearsToRetire = (retirementAge.toInt() - currentAge.toInt()).coerceAtLeast(1)
  val futureMonthlyExpense =
    monthlyExpense.toDouble() *
      (1.0 + (inflationRate.toDouble() / 100.0)).pow(yearsToRetire.toDouble())
  val futureAnnualExpense = futureMonthlyExpense * 12.0
  // 4% Safe Withdrawal Rule -> 25x Annual Expense
  val requiredCorpus = futureAnnualExpense * 25.0

  Card(
    modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
  ) {
    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
      Text(
        text = "Retirement & Financial Independence (25x Rule)",
        style = MaterialTheme.typography.titleLarge,
        color = Color.White,
      )

      CalcSliderControl(
        label = "Current Age",
        valueDisplay = "${currentAge.toInt()} yrs",
        value = currentAge,
        onValueChange = {
          currentAge = it
          if (retirementAge <= currentAge) retirementAge = currentAge + 5f
        },
        valueRange = 18f..65f,
      )
      CalcSliderControl(
        label = "Target Retirement Age",
        valueDisplay = "${retirementAge.toInt()} yrs ($yearsToRetire yrs away)",
        value = retirementAge,
        onValueChange = { if (it > currentAge) retirementAge = it },
        valueRange = 25f..75f,
      )
      CalcSliderControl(
        label = "Current Monthly Living Expenses",
        valueDisplay = formatCurrency(monthlyExpense.toDouble()),
        value = monthlyExpense,
        onValueChange = { monthlyExpense = it },
        valueRange = 500f..15000f,
      )
      CalcSliderControl(
        label = "Expected Annual Inflation Rate",
        valueDisplay = "${String.format(Locale.US, "%.1f", inflationRate)}%",
        value = inflationRate,
        onValueChange = { inflationRate = it },
        valueRange = 2f..10f,
      )

      HorizontalDivider(color = EmeraldBorder)

      ResultHighlightBox(
        primaryLabel = "Target Retirement Nest Egg (25x Rule)",
        primaryValue = formatCurrency(requiredCorpus),
        leftLabel = "Inflation-Adjusted Monthly Need",
        leftValue = formatCurrency(futureMonthlyExpense),
        rightLabel = "Inflation-Adjusted Annual Need",
        rightValue = formatCurrency(futureAnnualExpense),
        ratio = 0.72f,
      )
    }
  }
}

@Composable
private fun BudgetEmergencyCalculatorCard() {
  var monthlyIncomeText by remember { mutableStateOf("4500") }
  val income = monthlyIncomeText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0

  val needs50 = income * 0.50
  val wants30 = income * 0.30
  val wealth20 = income * 0.20
  val emergency6Mo = needs50 * 6.0

  Card(
    modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
  ) {
    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
      Text(
        text = "50/30/20 Budget & 6-Month Emergency Reserve",
        style = MaterialTheme.typography.titleLarge,
        color = Color.White,
      )

      OutlinedTextField(
        value = monthlyIncomeText,
        onValueChange = { monthlyIncomeText = it.filter { ch -> ch.isDigit() || ch == '.' } },
        label = { Text("Monthly After-Tax Income ($)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier.fillMaxWidth().testTag("budget_income_input"),
        shape = RoundedCornerShape(14.dp),
      )

      ResultHighlightBox(
        primaryLabel = "Recommended 6-Month Emergency Fortress",
        primaryValue = formatCurrency(emergency6Mo),
        leftLabel = "50% Needs (Housing, Food, Bills)",
        leftValue = "${formatCurrency(needs50)} / mo",
        rightLabel = "20% Automated Wealth Harvest",
        rightValue = "${formatCurrency(wealth20)} / mo",
        ratio = 0.5f,
      )

      Surface(
        color = Color(0xFF082017),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(
          text =
            "30% Guilt-Free Lifestyle Allocation: ${formatCurrency(wants30)} / month (Dining, travel, entertainment).",
          style = MaterialTheme.typography.bodySmall,
          color = HarvestGoldLight,
          modifier = Modifier.padding(12.dp),
        )
      }
    }
  }
}

@Composable
private fun CalcSliderControl(
  label: String,
  valueDisplay: String,
  value: Float,
  onValueChange: (Float) -> Unit,
  valueRange: ClosedFloatingPointRange<Float>,
) {
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      Text(
        text = valueDisplay,
        style = MaterialTheme.typography.titleMedium,
        color = EmeraldBright,
        fontWeight = FontWeight.Bold,
      )
    }
    Slider(
      value = value,
      onValueChange = onValueChange,
      valueRange = valueRange,
      colors =
        SliderDefaults.colors(
          thumbColor = HarvestGoldLight,
          activeTrackColor = EmeraldPrimary,
        ),
    )
  }
}

@Composable
private fun ResultHighlightBox(
  primaryLabel: String,
  primaryValue: String,
  leftLabel: String,
  leftValue: String,
  rightLabel: String,
  rightValue: String,
  ratio: Float,
) {
  Surface(
    color = Color(0xFF082017),
    shape = RoundedCornerShape(16.dp),
    modifier = Modifier.fillMaxWidth(),
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = primaryLabel,
        style = MaterialTheme.typography.labelMedium,
        color = HarvestGoldLight,
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = primaryValue,
        style = MaterialTheme.typography.displayMedium,
        color = Color.White,
        fontWeight = FontWeight.Bold,
      )
      Spacer(modifier = Modifier.height(10.dp))
      LinearProgressIndicator(
        progress = { ratio.coerceIn(0.05f, 0.95f) },
        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
        color = EmeraldPrimary,
        trackColor = HarvestGold,
      )
      Spacer(modifier = Modifier.height(12.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = leftLabel,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Text(
            text = leftValue,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
          )
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
          Text(
            text = rightLabel,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Text(
            text = rightValue,
            style = MaterialTheme.typography.titleMedium,
            color = EmeraldBright,
            fontWeight = FontWeight.SemiBold,
          )
        }
      }
    }
  }
}

@Composable
fun CybersecurityScreen(
  onSaveCyberAudit: (CyberTopic) -> Unit,
) {
  var passwordToTest by remember { mutableStateOf("") }
  val checkedDefenseItems = remember {
    mutableStateListOf(
      "Authenticator App 2FA Enabled on Primary Email & Bank",
      "Never Scan QR Code or Enter PIN to Receive Money",
    )
  }

  val allChecklistItems =
    listOf(
      "Authenticator App 2FA Enabled on Primary Email & Bank",
      "Never Scan QR Code or Enter PIN to Receive Money",
      "Unique 16+ Character Passwords via Password Manager",
      "Carrier SIM Lock / Port-Freeze PIN Activated",
      "Family Secret Safe-Word Established Against AI Voice Clones",
      "Zero Remote Screen-Sharing Apps (AnyDesk/TeamViewer) Installed",
    )

  val cyberScore = ((checkedDefenseItems.size.toFloat() / allChecklistItems.size) * 100).roundToLong()

  Column(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // Cyber Header
    Card(
      modifier =
        Modifier.fillMaxWidth()
          .border(1.dp, CyberCyan.copy(alpha = 0.45f), RoundedCornerShape(20.dp)),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Security, contentDescription = null, tint = CyberCyan)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "DIGITAL BANKING & CYBERSECURITY FORTRESS",
            style = MaterialTheme.typography.labelSmall,
            color = CyberCyan,
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Protect Your Accounts from Phishing, SIM-Swaps & Malware",
          style = MaterialTheme.typography.headlineSmall,
          color = Color.White,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text =
            "Audit your password strength locally on-device, track your Digital Banking Hygiene Score, and master defense against modern cyber threats.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    // 1. Live On-Device Password Entropy & Strength Auditor
    Card(
      modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldBorder, RoundedCornerShape(18.dp)),
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = EmeraldCardSurface),
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Key, contentDescription = null, tint = HarvestGoldLight)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Interactive Password Strength & Breach-Risk Auditor",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text =
            "Test a sample password structure below (100% evaluated locally in memory—never transmitted).",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = passwordToTest,
          onValueChange = { passwordToTest = it },
          modifier = Modifier.fillMaxWidth().testTag("cyber_password_input"),
          placeholder = { Text("Type a sample passphrase to audit strength...") },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
        )

        val hasLen12 = passwordToTest.length >= 12
        val hasLen16 = passwordToTest.length >= 16
        val hasUpper = passwordToTest.any { it.isUpperCase() }
        val hasLower = passwordToTest.any { it.isLowerCase() }
        val hasDigit = passwordToTest.any { it.isDigit() }
        val hasSymbol = passwordToTest.any { !it.isLetterOrDigit() }

        val rawScore =
          listOf(hasLen12, hasLen16, hasUpper, hasLower, hasDigit, hasSymbol).count { it } * 16 +
            (if (passwordToTest.isNotEmpty()) 4 else 0)
        val strengthScore = if (passwordToTest.isEmpty()) 0 else rawScore.coerceIn(10, 100)
        val strengthColor =
          when {
            strengthScore >= 80 -> EmeraldBright
            strengthScore >= 50 -> HarvestGoldLight
            else -> ScamAlertRed
          }
        val crackEstimate =
          when {
            passwordToTest.isEmpty() -> "Enter a sample password above"
            strengthScore < 40 -> "Instant (< 2 seconds via GPU brute-force)"
            strengthScore < 70 -> "~3 Hours to 4 Days"
            strengthScore < 85 -> "~140 Years (Strong)"
            else -> "Millions of Centuries (Quantum-Resilient Passphrase)"
          }

        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
          progress = { strengthScore / 100f },
          modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
          color = strengthColor,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Security Score: $strengthScore% • Estimated Crack Time: $crackEstimate",
          style = MaterialTheme.typography.labelMedium,
          color = strengthColor,
        )
      }
    }

    // 2. Digital Banking Defense Checklist
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
          Text(
            text = "Personal Cyber Shield Score",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "$cyberScore%",
            style = MaterialTheme.typography.titleLarge,
            color = EmeraldBright,
          )
        }
        Spacer(modifier = Modifier.height(10.dp))
        allChecklistItems.forEach { item ->
          val isChecked = item in checkedDefenseItems
          Row(
            modifier =
              Modifier.fillMaxWidth()
                .clickable {
                  if (isChecked) checkedDefenseItems.remove(item) else checkedDefenseItems.add(item)
                }
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
              imageVector =
                if (isChecked) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
              contentDescription = null,
              tint = if (isChecked) EmeraldBright else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = item,
              style = MaterialTheme.typography.bodySmall,
              color = Color.White,
            )
          }
        }
      }
    }

    // 3. Threat Lab Playbooks
    Text(
      text = "Cyber Threat Intelligence & Defense Playbooks",
      style = MaterialTheme.typography.titleLarge,
      color = Color.White,
    )
    MoneyHarvestCatalog.cyberTopics.forEach { topic ->
      val badgeColor =
        if (topic.threatLevel == "CRITICAL") ScamAlertRed else ScamWarningOrange
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
            Surface(color = badgeColor.copy(alpha = 0.2f), shape = CircleShape) {
              Text(
                text = "THREAT: ${topic.threatLevel}",
                style = MaterialTheme.typography.labelSmall,
                color = badgeColor,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              )
            }
            Surface(
              color = EmeraldPrimary.copy(alpha = 0.2f),
              shape = CircleShape,
              modifier = Modifier.clickable { onSaveCyberAudit(topic) },
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Icon(
                  Icons.Default.Shield,
                  contentDescription = null,
                  tint = EmeraldBright,
                  modifier = Modifier.size(14.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Log Audit Complete",
                  style = MaterialTheme.typography.labelSmall,
                  color = EmeraldBright,
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = topic.title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = topic.summary,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Common Attack Vectors:",
            style = MaterialTheme.typography.labelMedium,
            color = ScamAlertRed,
          )
          topic.attackVectors.forEach { vec ->
            Text(
              text = "• $vec",
              style = MaterialTheme.typography.bodySmall,
              color = Color.White,
              modifier = Modifier.padding(vertical = 2.dp),
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Hardened Defense Protocol:",
            style = MaterialTheme.typography.labelMedium,
            color = EmeraldBright,
          )
          topic.defenseChecklist.forEach { def ->
            Text(
              text = "✓ $def",
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

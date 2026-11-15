package com.example.data

enum class HarvestPage(val title: String, val subtitle: String, val badge: String) {
  HOME("Home", "Money Harvest Financial & Cyber Hub", "Overview"),
  FINANCIAL_EDUCATION("Financial Education", "Master Wealth Building & Money Fundamentals", "Learn"),
  SCAM_DETECTOR("Scam Detector", "AI-Powered Fraud, Phishing & Investment Scam Radar", "AI Shield"),
  FINANCIAL_CALCULATORS("Financial Calculators", "SIP, Compound Interest, EMI, Retirement & Tax", "Tools"),
  AI_ASSISTANT("AI Assistant", "24/7 Personal Financial & Cyber Safety Mentor", "Gemini AI"),
  CYBERSECURITY("Cybersecurity", "Digital Banking Defense, Password Audit & Threat Lab", "Security"),
  COURSES_RESOURCES("Courses & Resources", "Structured Academy Tracks & Downloadable Guides", "Academy"),
  COMMUNITY("Community", "Verified Scam Alerts, Wealth Tips & Peer Discussions", "Network"),
  CERTIFICATES("Certificates", "Official Verifiable Money Harvest Credentials", "Credentials"),
  QUIZ_CHALLENGES("Quiz Challenges", "Interactive Financial & Fraud Defense Assessments", "Quizzes"),
}

data class EducationTopic(
  val key: String,
  val title: String,
  val category: String,
  val readingMinutes: Int,
  val difficulty: String,
  val summary: String,
  val keyPrinciples: List<String>,
  val actionableSteps: List<String>,
  val realWorldExample: String,
)

data class CyberTopic(
  val key: String,
  val title: String,
  val threatLevel: String,
  val summary: String,
  val attackVectors: List<String>,
  val defenseChecklist: List<String>,
)

data class CourseTrack(
  val key: String,
  val title: String,
  val category: String,
  val level: String,
  val duration: String,
  val rating: Double,
  val enrolledCount: String,
  val overview: String,
  val lessons: List<CourseLesson>,
)

data class CourseLesson(
  val number: Int,
  val title: String,
  val durationMinutes: Int,
  val content: String,
  val keyTakeaway: String,
)

data class ResourceGuide(
  val id: String,
  val title: String,
  val type: String,
  val description: String,
  val checklistItems: List<String>,
)

data class QuizChallengePack(
  val key: String,
  val title: String,
  val category: String,
  val difficulty: String,
  val rewardBadge: String,
  val questions: List<QuizQuestion>,
)

data class QuizQuestion(
  val id: Int,
  val question: String,
  val options: List<String>,
  val correctIndex: Int,
  val explanation: String,
)

object MoneyHarvestCatalog {

  val educationTopics =
    listOf(
      EducationTopic(
        key = "edu_budgeting_50_30_20",
        title = "The 50/30/20 Wealth Harvest Framework",
        category = "Budgeting & Cash Flow",
        readingMinutes = 6,
        difficulty = "Beginner",
        summary =
          "Build a resilient personal cash-flow engine by dividing after-tax income into Essentials (50%), Lifestyle (30%), and Wealth Harvesting & Debt Elimination (20%).",
        keyPrinciples =
          listOf(
            "Automate your 20% savings transfer on payday before discretionary spending begins.",
            "Track fixed vs. variable expenses monthly to spot silent subscription leaks.",
            "Keep 3 to 6 months of essential living expenses in a liquid, FDIC/DICGC-insured high-yield account.",
          ),
        actionableSteps =
          listOf(
            "Audit your last 60 days of bank statements and tag every expense as Need, Want, or Investment.",
            "Set up an automatic standing instruction on the 1st of every month to your emergency & SIP accounts.",
            "Cap high-interest lifestyle credit card utilization below 15% of your total limit.",
          ),
        realWorldExample =
          "On a $4,000 monthly net income: $2,000 covers housing, groceries, utilities & insurance; $1,200 funds dining and hobbies; $800 goes automatically into index funds and emergency reserves—growing to over $138,000 in 10 years at 7% annual return.",
      ),
      EducationTopic(
        key = "edu_compounding_index",
        title = "Compound Growth & Diversified Index Investing",
        category = "Investing & Wealth",
        readingMinutes = 8,
        difficulty = "Intermediate",
        summary =
          "Understand how low-cost broad-market index funds, dividend reinvestment, and time in the market exponentially outpace market timing.",
        keyPrinciples =
          listOf(
            "The Rule of 72: Divide 72 by your annual return rate to estimate how many years it takes to double your money.",
            "Expense ratios matter: A 1.5% management fee can erode nearly 30% of your terminal portfolio over 30 years compared to a 0.10% index fund.",
            "Dollar-Cost Averaging (SIP) removes emotional panic during market corrections.",
          ),
        actionableSteps =
          listOf(
            "Compare expense ratios and tracking error before selecting any mutual fund or ETF.",
            "Diversify across equities, sovereign bonds, and inflation-hedged assets based on your time horizon.",
            "Never invest money you need within the next 36 months into volatile equity markets.",
          ),
        realWorldExample =
          "Investing $500/month from age 25 to 60 at 9% annualized return yields ~$1,470,000 from just $210,000 in principal contributions—over $1.26M is pure compounding harvest.",
      ),
      EducationTopic(
        key = "edu_debt_avalanche",
        title = "Debt Avalanche vs. Debt Snowball Mastery",
        category = "Debt & Credit",
        readingMinutes = 7,
        difficulty = "Beginner",
        summary =
          "Eliminate toxic high-APR debt systematically while raising your credit score above 780 for prime borrowing rates.",
        keyPrinciples =
          listOf(
            "Debt Avalanche targets the highest interest rate (APR) first—mathematically saving the most money.",
            "Debt Snowball targets the smallest balance first—creating rapid psychological wins.",
            "Credit utilization (30%) and on-time payment history (35%) drive 65% of your credit score.",
          ),
        actionableSteps =
          listOf(
            "List all debts ranked from highest APR (e.g., 24% credit cards) to lowest APR.",
            "Pay minimums on all accounts and direct 100% of extra cash flow to the #1 highest-APR debt.",
            "Avoid closing your oldest zero-fee credit card to preserve average credit history length.",
          ),
        realWorldExample =
          "Clearing a $6,000 credit card balance at 24% APR using the Avalanche method with $400/month saves $1,840 in interest compared to making minimum payments.",
      ),
      EducationTopic(
        key = "edu_tax_shield_retirement",
        title = "Tax-Advantaged Retirement & Inflation Protection",
        category = "Retirement & Tax",
        readingMinutes = 9,
        difficulty = "Advanced",
        summary =
          "Protect your purchasing power from silent inflation erosion using tax-deferred, tax-free, and sovereign retirement vehicles.",
        keyPrinciples =
          listOf(
            "Real Return = Nominal Return minus Inflation Rate. Always plan retirement in inflation-adjusted dollars.",
            "4% Safe Withdrawal Rule: A retirement corpus of 25x your annual expenses can sustain a 30-year retirement.",
            "Maximize employer matching contributions—it is an immediate 50% to 100% risk-free return.",
          ),
        actionableSteps =
          listOf(
            "Calculate your FI (Financial Independence) number: Annual Expenses × 25.",
            "Capture 100% of employer retirement match before investing in taxable brokerage accounts.",
            "Rebalance your asset allocation annually to lock in gains and control risk exposure.",
          ),
        realWorldExample =
          "If your household needs $48,000/year in today's purchasing power, your target Financial Independence portfolio is $1,200,000 in inflation-adjusted assets.",
      ),
      EducationTopic(
        key = "edu_psychology_of_money",
        title = "Behavioral Finance & Avoiding FOMO Traps",
        category = "Financial Psychology",
        readingMinutes = 6,
        difficulty = "Intermediate",
        summary =
          "Recognize cognitive biases—loss aversion, herd mentality, sunk cost fallacy, and " +
            "get-rich-quick euphoria—that scammers and market bubbles exploit.",
        keyPrinciples =
          listOf(
            "High return NEVER comes with zero risk: Any 'guaranteed 5% daily return' is a Ponzi or pig-butchering scam.",
            "Create a 48-hour cooling-off rule before committing to any major purchase or unfamiliar investment.",
            "Wealth is what you don't see: True financial freedom is measured in net worth and autonomy, not flashy liabilities.",
          ),
        actionableSteps =
          listOf(
            "Write a 1-page Personal Investment Policy Statement (IPS) defining what you will and won't invest in.",
            "Verify every broker or advisor on official regulatory registries (SEC/FINRA, SEBI, FCA, ASIC) before transferring funds.",
            "Discuss large financial decisions with a trusted family member to break urgency manipulation.",
          ),
        realWorldExample =
          "Investors who stayed disciplined during market downturns and ignored viral '100x crypto Telegram VIP' groups preserved 100% of their capital and captured the recovery.",
      ),
    )

  val sampleScamTemplates =
    listOf(
      "URGENT ALERT: Your bank account has been suspended due to suspicious KYC activity. Click http://secure-verify-kyc-update.xyz/login within 2 hours or your funds will be frozen permanently!",
      "Hello dear! Join our VIP Quantum AI Crypto Harvest WhatsApp group. We guarantee 15% daily profit with zero risk! Deposit $500 USDT today to unlock your $4,500 payout.",
      "Congratulations! You have been selected for a remote part-time YouTube task job earning $300-$800/day. Pay a refundable $99 VIP activation fee via gift card or crypto to claim your salary.",
      "Official IRS / Tax Department Notice: You have an unpaid tax penalty of $1,420. Arrest warrant will be issued in 45 minutes unless settled immediately via wire transfer.",
    )

  val cyberTopics =
    listOf(
      CyberTopic(
        key = "cyber_phishing_smishing",
        title = "Phishing, Smishing & AI Voice Cloning Defense",
        threatLevel = "CRITICAL",
        summary =
          "Modern attackers use spoofed domains, urgent SMS links, and 3-second AI voice clones of family members or bank officers to steal OTPs and wire transfers.",
        attackVectors =
          listOf(
            "Lookalike domains (e.g., 'paypaI-security.com' or '.xyz/.top' banking links).",
            "Emergency family crisis calls generated with AI voice cloning asking for instant crypto or wire transfers.",
            "Fake customer support numbers planted in search results or social media comments.",
          ),
        defenseChecklist =
          listOf(
            "Establish a secret family safe-word to verify identity during any urgent phone request for money.",
            "Never click links in SMS or WhatsApp claiming your bank account, parcel, or tax refund is blocked.",
            "Only call the official number printed on the back of your physical bank debit/credit card.",
          ),
      ),
      CyberTopic(
        key = "cyber_2fa_sim_swap",
        title = "Hardware 2FA, Authenticator Apps & SIM-Swap Protection",
        threatLevel = "HIGH",
        summary =
          "SMS One-Time Passwords (OTPs) can be hijacked via SIM-swap fraud or malicious call-forwarding codes (*21* / *401*). Upgrade to app-based TOTP or Passkeys.",
        attackVectors =
          listOf(
            "Social engineering mobile carrier support to port your phone number to a scammer's eSIM.",
            "Tricking users into dialing USSD call/SMS forwarding codes.",
            "Real-time adversary-in-the-middle (AiTM) phishing proxies capturing session cookies.",
          ),
        defenseChecklist =
          listOf(
            "Switch banking, email, and brokerage 2FA from SMS to Authenticator apps (Google/Aegis/Authy) or Passkeys.",
            "Set a Carrier Port-Out PIN / SIM Lock with your mobile network provider.",
            "Never share an OTP or approve a push notification you did not initiate yourself.",
          ),
      ),
      CyberTopic(
        key = "cyber_upi_banking_safety",
        title = "Digital Banking, UPI & QR Code Payment Security",
        threatLevel = "HIGH",
        summary =
          "Scammers abuse 'Collect Request' notifications and malicious QR codes by claiming you need to scan a QR code or enter your PIN to 'receive' money.",
        attackVectors =
          listOf(
            "Fake marketplace buyers sending a QR code claiming 'Scan to receive $200 payment'.",
            "Screen-sharing apps (AnyDesk, TeamViewer, QuickSupport) installed under the guise of KYC help.",
            "Public Wi-Fi packet snooping and rogue charging station juice-jacking.",
          ),
        defenseChecklist =
          listOf(
            "Remember the Golden Rule: You NEVER need to scan a QR code or enter your PIN to RECEIVE money.",
            "Never install remote-access or screen-sharing apps when speaking to someone claiming to be bank support.",
            "Set daily transaction limits on your checking account and enable instant SMS/email debit alerts.",
          ),
      ),
      CyberTopic(
        key = "cyber_crypto_wallet_drainers",
        title = "Web3 Wallet Drainers, Pig-Butchering & Fake Exchanges",
        threatLevel = "CRITICAL",
        summary =
          "Fraudulent trading platforms display fabricated profits on a realistic dashboard, then demand 'tax fees' or 'liquidity verification deposits' when you try to withdraw.",
        attackVectors =
          listOf(
            "Long-term romance or mentorship 'Pig-Butchering' scams directing victims to fake trading apps.",
            "Malicious smart-contract 'Unlimited Token Approval' signatures that drain wallets silently.",
            "Fake airdrop claims asking for your 12/24-word seed recovery phrase.",
          ),
        defenseChecklist =
          listOf(
            "Never share or type your 12/24-word seed phrase on any website—no legitimate support will ever ask for it.",
            "If a platform demands an upfront 'tax' or 'unlock fee' to withdraw your own balance, stop paying immediately—it is a scam.",
            "Verify domain registration age and regulatory licensing before depositing into any trading app.",
          ),
      ),
    )

  val courses =
    listOf(
      CourseTrack(
        key = "course_wealth_foundations",
        title = "Personal Finance & Wealth Harvesting Masterclass",
        category = "Financial Education",
        level = "Beginner",
        duration = "4 Modules • 45 mins",
        rating = 4.9,
        enrolledCount = "14,820 Learners",
        overview =
          "Master cash-flow architecture, emergency funds, debt elimination, and automated index investing to build lasting financial security.",
        lessons =
          listOf(
            CourseLesson(
              number = 1,
              title = "Building Your Zero-Leak Cash Flow System",
              durationMinutes = 10,
              content =
                "Every sustainable wealth plan starts with positive cash flow. Separate your accounts into three distinct buckets: (1) Fixed Bills & Essentials (50%), (2) Guilt-Free Lifestyle (30%), and (3) Automated Wealth Harvest (20%). By routing 20% of your paycheck automatically on day one, you eliminate willpower fatigue.",
              keyTakeaway = "Pay your future self first via automated payday transfers.",
            ),
            CourseLesson(
              number = 2,
              title = "The 6-Month Fortress Emergency Fund",
              durationMinutes = 12,
              content =
                "An emergency fund is insurance against forced debt. Calculate your bare-bones monthly survival cost (housing, food, utilities, insurance, minimum debt) and multiply by 6. Park this capital in a high-yield savings account or liquid money-market fund—never in volatile stocks or locked schemes.",
              keyTakeaway = "Liquidity and principal safety beat yield for your emergency reserve.",
            ),
            CourseLesson(
              number = 3,
              title = "Crushing High-APR Debt & Credit Optimization",
              durationMinutes = 11,
              content =
                "Credit card interest at 20%–30% APR compounds against you faster than the stock market compounds for you. Use the Debt Avalanche method to attack the highest APR balance first while keeping credit utilization under 15% and paying statements in full every billing cycle.",
              keyTakeaway = "Eliminating 24% APR debt is an instant, guaranteed 24% tax-free return.",
            ),
            CourseLesson(
              number = 4,
              title = "Long-Term Compounding with Low-Cost Index Funds",
              durationMinutes = 12,
              content =
                "Broad-market index funds give you instant ownership of hundreds of leading companies at expense ratios under 0.15%. Combine systematic monthly SIP contributions with annual portfolio rebalancing to harvest decades of global economic growth.",
              keyTakeaway = "Time in the market and low fees outperform speculative trading.",
            ),
          ),
      ),
      CourseTrack(
        key = "course_scam_defense_pro",
        title = "Financial Fraud & Scam Detection Specialist",
        category = "Scam Defense",
        level = "Intermediate",
        duration = "4 Modules • 50 mins",
        rating = 5.0,
        enrolledCount = "19,340 Learners",
        overview =
          "Learn to dissect Ponzi schemes, pig-butchering investment traps, phishing links, fake job tasks, and deepfake social engineering before losing a cent.",
        lessons =
          listOf(
            CourseLesson(
              number = 1,
              title = "Anatomy of Modern Investment & Pig-Butchering Scams",
              durationMinutes = 12,
              content =
                "Fraudsters spend weeks building trust on WhatsApp, Telegram, LinkedIn, or dating apps before introducing an 'exclusive crypto, forex, or AI quantitative trading platform'. They even allow a small initial withdrawal ($50–$100) to hook you into depositing life savings—then freeze your account and demand fake taxes.",
              keyTakeaway = "Small early payouts are bait; never deposit into unlicensed trading apps.",
            ),
            CourseLesson(
              number = 2,
              title = "Spotting Phishing URLs, Fake KYC & Urgency Traps",
              durationMinutes = 13,
              content =
                "Scammers manufacture panic ('Account blocked in 30 mins', 'Customs parcel seized', 'Electricity disconnection tonight'). Inspect the root domain carefully: legitimate banks never use '.xyz', '.top', ngrok tunnels, or free form builders for KYC updates.",
              keyTakeaway = "Manufactured urgency is the #1 signature of social engineering.",
            ),
            CourseLesson(
              number = 3,
              title = "Part-Time Task Scams & Advance-Fee Traps",
              durationMinutes = 12,
              content =
                "Task scams promise easy daily income for liking videos, rating hotels, or boosting e-commerce orders. Once you complete a few tasks, they trap you in a 'Prepaid Combo Task' requiring you to deposit increasing sums to unlock your frozen commission.",
              keyTakeaway = "No legitimate employer ever asks you to pay money to unlock your salary.",
            ),
            CourseLesson(
              number = 4,
              title = "Emergency Incident Response: What to Do If Scammed",
              durationMinutes = 13,
              content =
                "The first 60 minutes ('The Golden Hour') are critical. Immediately contact your bank's fraud desk to freeze cards/accounts, file a report on the national cybercrime portal (e.g., IC3.gov / 1930 Cybercrime Helpline / ActionFraud), preserve all transaction IDs and screenshots, and beware of 'Recovery Scammers' who claim they can hack your money back for a fee.",
              keyTakeaway = "Act within the Golden Hour and never pay 'recovery agents'.",
            ),
          ),
      ),
      CourseTrack(
        key = "course_cyber_banking_fortress",
        title = "Cybersecurity for Digital Banking & Investors",
        category = "Cybersecurity",
        level = "Intermediate",
        duration = "3 Modules • 35 mins",
        rating = 4.9,
        enrolledCount = "11,600 Learners",
        overview =
          "Harden your smartphone, email, brokerage accounts, and digital wallets against SIM swaps, malware, credential stuffing, and identity theft.",
        lessons =
          listOf(
            CourseLesson(
              number = 1,
              title = "Zero-Reuse Password Architecture & Passkeys",
              durationMinutes = 12,
              content =
                "Over 80% of account takeovers happen because users reuse the same password across leaked websites. Use a reputable encrypted password manager to generate 16+ character unique passphrases for every financial account, and enable hardware-backed Passkeys wherever supported.",
              keyTakeaway = "One unique passphrase per account stops credential stuffing cold.",
            ),
            CourseLesson(
              number = 2,
              title = "Securing Your Primary Email & SIM Card",
              durationMinutes = 11,
              content =
                "Your primary email and phone number are the master reset keys to your entire financial life. Protect your email with an Authenticator app (never SMS-only), review active logged-in devices monthly, and enable a SIM PIN + Carrier Port-Freeze.",
              keyTakeaway = "Secure the recovery email and SIM card that control password resets.",
            ),
            CourseLesson(
              number = 3,
              title = "Safe Mobile Banking & Device Hygiene",
              durationMinutes = 12,
              content =
                "Never sideload `.apk` files sent over Telegram or WhatsApp ('Wedding Invitation.apk', 'Bank_KYC_Update.apk')—they contain SMS-stealing Trojans that silently forward your banking OTPs to attackers. Keep OS security patches updated and use a dedicated clean browser profile for banking.",
              keyTakeaway = "Never install APK files from messaging apps or grant SMS permissions to unknown apps.",
            ),
          ),
      ),
    )

  val resourceGuides =
    listOf(
      ResourceGuide(
        id = "res_emergency_scam_playbook",
        title = "Golden Hour Scam Emergency Playbook",
        type = "Emergency Checklist",
        description =
          "Step-by-step action checklist if you or a family member accidentally clicked a phishing link or transferred money to a scammer.",
        checklistItems =
          listOf(
            "1. Call your bank's 24/7 fraud hotline immediately to block cards, UPI, and net-banking.",
            "2. Disconnect your phone from Wi-Fi and mobile data if a screen-sharing or APK file was installed.",
            "3. File an official cybercrime complaint with transaction UTR/reference numbers and screenshots.",
            "4. Rotate passwords for your primary email and financial accounts from a clean device.",
            "5. Place a fraud alert / credit freeze with major credit bureaus to block unauthorized loans.",
          ),
      ),
      ResourceGuide(
        id = "res_investment_due_diligence",
        title = "10-Point Investment Due Diligence Audit",
        type = "Verification Guide",
        description =
          "Run any investment opportunity, broker, or wealth scheme through this 10-point filter before committing capital.",
        checklistItems =
          listOf(
            "1. Is the firm registered with the official national regulator (SEC/FINRA, SEBI, FCA, ASIC)?",
            "2. Does the pitch promise 'guaranteed high returns with zero risk'?",
            "3. Are you being pressured to act urgently or keep the investment secret?",
            "4. Can you independently verify how the underlying business generates revenue?",
            "5. Are withdrawals free of arbitrary 'unlock taxes' or 'VIP upgrade fees'?",
          ),
      ),
      ResourceGuide(
        id = "res_monthly_wealth_audit",
        title = "Monthly Net Worth & Cyber Hygiene Review",
        type = "Monthly Template",
        description =
          "A 15-minute end-of-month ritual to track net worth growth, audit subscriptions, and check account security.",
        checklistItems =
          listOf(
            "1. Record total liquid assets, retirement investments, and remaining debt balances.",
            "2. Review credit card and bank statements for unrecognized recurring charges.",
            "3. Verify automated SIP / savings transfers executed on schedule.",
            "4. Check 'Have I Been Pwned' / password breach alerts and active login sessions.",
          ),
      ),
    )

  val quizPacks =
    listOf(
      QuizChallengePack(
        key = "quiz_scam_spotter_pro",
        title = "Scam & Phishing Spotter Challenge",
        category = "Scam Defense",
        difficulty = "Beginner • 5 Questions",
        rewardBadge = "Certified Scam Spotter",
        questions =
          listOf(
            QuizQuestion(
              id = 1,
              question =
                "You receive an SMS saying your bank account will be frozen in 30 minutes unless you update your PAN/KYC at 'http://secure-bank-kyc-update.xyz'. What should you do?",
              options =
                listOf(
                  "Click the link quickly and enter your net-banking credentials",
                  "Ignore the link, delete the SMS, and check your official banking app directly if concerned",
                  "Reply to the SMS with your account number and OTP",
                  "Forward the link to friends so they can update too",
                ),
              correctIndex = 1,
              explanation =
                "Legitimate banks never send urgent account-freeze threats with '.xyz' lookalike links. Always verify inside your official banking app.",
            ),
            QuizQuestion(
              id = 2,
              question =
                "A buyer on an online marketplace wants to buy your sofa and sends you a QR code saying: 'Scan this QR code and enter your PIN to receive $150 immediately.' Is this legitimate?",
              options =
                listOf(
                  "Yes, QR codes are required to receive instant marketplace transfers",
                  "No! Scanning a QR code and entering a PIN always SENDS money out of your account, never receives it",
                  "Yes, as long as you do it within 5 minutes",
                  "Only if they send it via WhatsApp",
                ),
              correctIndex = 1,
              explanation =
                "Golden Rule of Digital Payments: You NEVER need to scan a QR code or enter your PIN to receive money.",
            ),
            QuizQuestion(
              id = 3,
              question =
                "You join a 'Part-Time YouTube Like Task' group that pays you $20 for your first 3 tasks, then asks you to deposit $300 to unlock a 'VIP High-Commission Task'. What is happening?",
              options =
                listOf(
                  "It is a legitimate corporate promotion",
                  "It is a classic Advance-Fee / Task Scam using small initial payouts as bait to steal larger deposits",
                  "It is a safe way to build passive income",
                  "You should deposit $300 and stop after one VIP round",
                ),
              correctIndex = 1,
              explanation =
                "Task scammers deliberately pay $10–$30 upfront to build false trust before trapping victims with escalating prepaid deposits.",
            ),
            QuizQuestion(
              id = 4,
              question =
                "Someone claiming to be from your bank's fraud department calls and asks you to install 'AnyDesk' or 'QuickSupport' to help cancel an unauthorized transaction. What is the safest response?",
              options =
                listOf(
                  "Install the app and share the 9-digit access code",
                  "Hang up immediately—legitimate banks never ask you to install remote screen-sharing apps",
                  "Share only your debit card CVV instead",
                  "Keep the call connected while logging into your bank account",
                ),
              correctIndex = 1,
              explanation =
                "Remote screen-sharing apps allow scammers to view your screen, read incoming OTPs, and drain your accounts in real time.",
            ),
            QuizQuestion(
              id = 5,
              question =
                "After losing money to an online scam, an account on social media messages you claiming they are 'Ethical Cyber Recovery Hackers' who can recover 100% of your funds for a $150 upfront fee. Who are they?",
              options =
                listOf(
                  "Official government cyber police",
                  "Recovery Scammers targeting victims a second time with a fake recovery promise",
                  "White-hat blockchain auditors",
                  "Bank insurance agents",
                ),
              correctIndex = 1,
              explanation =
                "Recovery scams prey on fraud victims. Only official law enforcement and your bank's dispute channel can freeze or trace stolen funds—never pay upfront 'recovery fees'.",
            ),
          ),
      ),
      QuizChallengePack(
        key = "quiz_wealth_iq_mastery",
        title = "Personal Finance & Wealth IQ Challenge",
        category = "Financial Education",
        difficulty = "Intermediate • 5 Questions",
        rewardBadge = "Wealth Harvest Strategist",
        questions =
          listOf(
            QuizQuestion(
              id = 1,
              question =
                "According to the Rule of 72, approximately how many years will it take for an investment earning a 9% compounded annual return to double in value?",
              options = listOf("6 years", "8 years", "12 years", "15 years"),
              correctIndex = 1,
              explanation = "72 ÷ 9% annual return = 8 years to double your capital.",
            ),
            QuizQuestion(
              id = 2,
              question =
                "In the 50/30/20 budgeting framework, how should your after-tax income ideally be allocated?",
              options =
                listOf(
                  "50% Needs, 30% Wants, 20% Savings/Investing & Extra Debt Payoff",
                  "50% Wants, 30% Crypto, 20% Needs",
                  "50% Savings, 30% Rent, 20% Luxury",
                  "50% Taxes, 30% Groceries, 20% Entertainment",
                ),
              correctIndex = 0,
              explanation =
                "The 50/30/20 rule balances essential living costs (50%), lifestyle enjoyment (30%), and future financial security (20%).",
            ),
            QuizQuestion(
              id = 3,
              question =
                "Which debt repayment strategy mathematically minimizes the total interest you pay over time?",
              options =
                listOf(
                  "Paying only the minimum balance on all cards",
                  "The Debt Avalanche method (paying extra toward the debt with the highest interest rate first)",
                  "Taking payday loans to pay credit cards",
                  "Ignoring small balances",
                ),
              correctIndex = 1,
              explanation =
                "Targeting the highest-APR debt first (Debt Avalanche) eliminates the most expensive interest charges fastest.",
            ),
            QuizQuestion(
              id = 4,
              question =
                "Where is the most appropriate place to keep your 6-month Emergency Fund?",
              options =
                listOf(
                  "High-volatility meme coins and penny stocks",
                  "A liquid, insured High-Yield Savings Account or low-risk Money Market Fund",
                  "Locked 10-year real estate syndications",
                  "Unregulated offshore lending platforms",
                ),
              correctIndex = 1,
              explanation =
                "Your emergency fund must be 100% liquid and protected from market crashes when an unexpected job loss or medical expense occurs.",
            ),
            QuizQuestion(
              id = 5,
              question =
                "If your investment portfolio earns a 10% nominal return in a year when inflation is 4%, what is your approximate real purchasing-power return?",
              options = listOf("14%", "6%", "10%", "2.5%"),
              correctIndex = 1,
              explanation =
                "Approximate Real Return = Nominal Return (10%) − Inflation Rate (4%) = 6%.",
            ),
          ),
      ),
      QuizChallengePack(
        key = "quiz_cyber_fortress",
        title = "Cybersecurity & Digital Banking Defense Quiz",
        category = "Cybersecurity",
        difficulty = "Advanced • 5 Questions",
        rewardBadge = "Cyber Shield Commander",
        questions =
          listOf(
            QuizQuestion(
              id = 1,
              question =
                "Which form of Two-Factor Authentication (2FA) is most vulnerable to SIM-Swap attacks?",
              options =
                listOf(
                  "Hardware Security Keys (FIDO2 / YubiKey)",
                  "SMS / Text Message OTPs",
                  "Authenticator Apps (TOTP like Google Authenticator / Aegis)",
                  "Cryptographic Passkeys",
                ),
              correctIndex = 1,
              explanation =
                "If an attacker hijacks your phone number via a fraudulent SIM swap, they intercept all SMS text OTPs.",
            ),
            QuizQuestion(
              id = 2,
              question =
                "You receive a file named 'Income_Tax_Refund_Statement.apk' on WhatsApp from an unknown number. What happens if you install it on Android?",
              options =
                listOf(
                  "It opens a PDF of your tax refund",
                  "It installs an Android app that can steal your SMS OTPs, contacts, and banking notifications",
                  "It automatically speeds up your phone",
                  "Nothing, APK files are harmless text documents",
                ),
              correctIndex = 1,
              explanation =
                "`.apk` is an executable Android application package. Scammers disguise banking malware as PDF/invitation APKs.",
            ),
            QuizQuestion(
              id = 3,
              question =
                "What is 'Credential Stuffing' and how do you prevent it?",
              options =
                listOf(
                  "Attackers testing leaked email/password pairs from one breached site across banking and shopping sites; prevented by using a unique password for every account",
                  "Writing passwords on a sticky note",
                  "Using a 4-digit PIN on your lock screen",
                  "Clearing browser history once a year",
                ),
              correctIndex = 0,
              explanation =
                "Using a password manager with unique passphrases for every site ensures a breach on one website never compromises your bank or email.",
            ),
            QuizQuestion(
              id = 4,
              question =
                "A family member calls you in tears from an unknown number saying they were in an accident and need $2,000 wired or sent via crypto immediately. What should you do first?",
              options =
                listOf(
                  "Wire the money immediately without asking questions",
                  "Ask for your family's pre-agreed secret safe-word, or hang up and call their real phone number directly to verify against AI voice cloning",
                  "Send your credit card photo over text",
                  "Post about it on social media",
                ),
              correctIndex = 1,
              explanation =
                "Scammers use AI voice cloning from short social media clips to impersonate relatives in crisis. Always verify via a direct call back or family safe-word.",
            ),
            QuizQuestion(
              id = 5,
              question =
                "When using a Web3 / crypto wallet, what happens if you enter your 12-word Secret Recovery Phrase (Seed Phrase) into a 'Wallet Validation / Airdrop' website?",
              options =
                listOf(
                  "You receive double tokens",
                  "The attackers gain full cryptographic control of your wallet and permanently drain all assets across all chains",
                  "Your wallet gets customer support protection",
                  "Only the last transaction is reversed",
                ),
              correctIndex = 1,
              explanation =
                "Your 12/24-word seed phrase IS your master private key. Anyone who has it owns all funds in the wallet irrevocably.",
            ),
          ),
      ),
    )

  val curatedCommunityFeed =
    listOf(
      CommunityPost(
        id = "curated_1",
        userId = "system_verified",
        authorName = "Aarav Mehta • Cyber Analyst",
        category = "Scam Alert",
        title = "Warning: Fake 'FedEx / Customs Parcel Seized' IVR Calls",
        body =
          "Scammers are using automated calls claiming a parcel in your name contains illegal items and transferring you to 'Cyber Police' on Skype/WhatsApp video to demand a 'security verification transfer'. Police NEVER conduct digital arrests or ask for money transfers!",
        likesCount = 142L,
      ),
      CommunityPost(
        id = "curated_2",
        userId = "system_verified",
        authorName = "Elena Rostova • CFP®",
        category = "Wealth Tip",
        title = "How Automating a 20% SIP Transformed My Emergency & Index Portfolio",
        body =
          "Switching my SIP date to the 2nd of every month (right after payday) completely stopped lifestyle creep. Even starting with $150/month builds the habit before the numbers get big.",
        likesCount = 98L,
      ),
      CommunityPost(
        id = "curated_3",
        userId = "system_verified",
        authorName = "Rohan Kapoor • Security Lead",
        category = "Cybersecurity",
        title = "3-Minute Weekend Audit: Check Your Google & Bank Active Sessions",
        body =
          "Go to your Google Account -> Security -> Your Devices, and revoke any old phones or unrecognized browser sessions. Also switch SMS 2FA to an Authenticator app today!",
        likesCount = 87L,
      ),
      CommunityPost(
        id = "curated_4",
        userId = "system_verified",
        authorName = "Maya Lin • Harvest Scholar",
        category = "Success Story",
        title = "Paid Off $8,400 in High-APR Credit Card Debt Using the Debt Avalanche!",
        body =
          "Used the Money Harvest EMI & Debt payoff strategy to knock out my 26% APR card first. My credit score jumped 68 points in 7 months and I just earned my Wealth Strategist Certificate!",
        likesCount = 215L,
      ),
    )
}

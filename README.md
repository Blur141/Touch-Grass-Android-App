<div align="center">

# 🌿 Touch Grass

**A gamified digital detox app for Android.**  
Beat your screen addiction. Earn XP. Touch some actual grass.

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat&logo=android&logoColor=white)](https://android.com)
[![API](https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-blue)](https://developer.android.com/about/versions/oreo)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-7F52FF?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?style=flat&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Version](https://img.shields.io/badge/Version-1.0.0-brightgreen)](https://github.com)

</div>

---

## What is Touch Grass?

Most screen time apps just show you a number and make you feel bad about it. Touch Grass does something different — it turns breaking your phone addiction into a game you actually want to play.

Complete daily challenges. Start focus sessions that lock your distracting apps. Watch your virtual plant thrive (or wither) based on your habits. Climb levels with names like *"Anti-Doomscroller"* and *"Offline Monk"*. Earn achievements. Build streaks. Be human.

> *"You added this app to your block list. Go touch some grass instead."*

---

## Screenshots

| Home | Focus Mode | App Blocking | Pet |
|:----:|:----------:|:------------:|:---:|
| ![Home](docs/screenshots/home.png) | ![Focus](docs/screenshots/focus.png) | ![Blocking](docs/screenshots/blocking.png) | ![Pet](docs/screenshots/pet.png) |

| Challenges | Achievements | Stats | Settings |
|:----------:|:------------:|:-----:|:--------:|
| ![Challenges](docs/screenshots/challenges.png) | ![Achievements](docs/screenshots/achievements.png) | ![Stats](docs/screenshots/stats.png) | ![Settings](docs/screenshots/settings.png) |

---

## Features

### 📱 Screen Time Tracking
Real-time screen time monitoring powered by Android's `UsageStatsManager`. See your daily total, unlock count, longest session, and a weekly bar chart. Compared against your custom daily goal — set anywhere from 30 minutes to 8 hours.

### 🚫 App Blocking (Focus-Gated)
Add any installed app to your block list. When a focus session is active, trying to open a blocked app shows a full-screen overlay (drawn via `WindowManager.TYPE_APPLICATION_OVERLAY`) that physically prevents interaction with the app underneath. There is no "swipe to dismiss" — it means business.

**Emergency Bypass** is available after a 30-second cooldown at a cost of 50 XP. A 5-minute bypass window is granted.

### ⚡ Focus Sessions
Five session types built in:

| Mode | Duration | Description |
|------|----------|-------------|
| 🍅 Pomodoro | 25 min | Classic. 25 focus + 5 break. |
| ⚡ Deep Work | 90 min | 90 minutes of pure output. |
| 🏃 Quick Sprint | 15 min | Just do the thing. |
| 💪 Power Hour | 60 min | No excuses. |
| ⚙️ Custom | 5–180 min | Your rules. |

Sessions run as a foreground service with a live countdown notification. Completing a session awards XP, logs focus minutes, and unlocks focus-based achievements.

### 🌿 Daily Challenges
A fresh set of challenges every day — things that get you off your phone and back into the real world. Each has a difficulty tier, XP reward, and an optional fun fact. Complete all of them in one session to unlock the **Speed Demon** achievement.

### 🏆 Gamification — XP, Levels & Achievements
Everything you do earns XP. XP fills a level bar. Levels have names:

> Grass Rookie → Phone Putter-Downer → Offline Warrior → Algorithm Avoider → Dopamine Deprogrammer → Social Media Escapist → Offline Monk → **Touch Grass Master** (Level 50) → *Offline Legend* (Legendary)

**30 achievements** across 6 categories, from trivially easy to genuinely hard:

| Category | Examples |
|----------|---------|
| 🌱 Challenges | First Blade of Grass (1 done) · Challenge Addict (250 done) |
| 🔥 Streaks | Week Warrior (7d) · Century Run (100d) · Year-Long Warrior (365d) |
| 🧠 Focus | Focus Beast (10 sessions) · Flow State Master (50h total) |
| ✅ Goals | Goal Getter (1 day) · Digital Discipline (30-day goal streak) |
| 📈 Levels | Grass Rookie (L1) · Touch Grass Expert (L20) · Offline Legend (L50) |
| ⭐ Special | Hardcore Mode (≤1h daily goal) · XP Millionaire (10,000 XP) · Anti-Doomscroller |

### 🌱 Virtual Plant Pet
A digital plant that reflects your habits. Its health is directly tied to your screen usage:

| Action | Health |
|--------|--------|
| Under daily screen time goal | +15 |
| Completing a challenge | +10 |
| Completing a focus session | +10 |
| Over your screen time goal | −10 |
| 2× over your screen limit | −20 |

The plant levels up as it gains health. Keep it alive.

### 😴 Sleep Schedule
Set a bedtime and wake-up time. Touch Grass sends a notification when it's time to put the phone down — and a good morning nudge when it's time to wake up. Uses `AlarmManager.setExactAndAllowWhileIdle()` with self-rescheduling for reliability on Android 6+. Randomised motivational quotes so it doesn't feel robotic.

### 🌙 Dark / Light Mode
Full Material 3 dark and light themes. All colors are theme-reactive via `MaterialTheme.colorScheme` — no hardcoded dark values leaking into the light UI.

### 📊 Statistics
Weekly screen time bar chart, top-used apps, focus time total, best streak, pet health history, level progress, and a **Touch Grass Score** (0–100) that summarises how well you're doing today.

---

## Architecture

```
com.touchgrass/
├── data/
│   ├── local/           # Room database, DAOs, entities
│   ├── datastore/       # DataStore preferences (UserPreferencesDataStore)
│   └── repository/      # GamificationRepository, UsageStatsRepository, etc.
├── domain/
│   └── model/           # Pure Kotlin data classes + business logic
├── service/
│   ├── AppBlockingService.kt    # Foreground service + WindowManager overlay
│   ├── FocusSessionService.kt   # Focus timer foreground service
│   └── SleepAlarmReceiver.kt    # BroadcastReceiver for bedtime / wake alarms
├── ui/
│   ├── components/      # Reusable Compose components
│   ├── navigation/      # NavGraph + Screen sealed class
│   ├── screens/         # One package per screen — Screen.kt + ViewModel.kt
│   └── theme/           # Material 3 color scheme, typography, shapes
├── utils/               # NotificationHelper, PermissionUtils, HapticUtils
└── worker/              # DailyResetWorker (WorkManager, streak + pet health)
```

**Pattern**: MVVM + Unidirectional Data Flow
- ViewModels expose `StateFlow<UiState>` collected with `collectAsStateWithLifecycle()`
- Repository layer abstracts Room + DataStore behind clean interfaces
- Hilt for dependency injection end-to-end
- Coroutines + Flow for all async and reactive work

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.1.0 |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM, Clean Architecture |
| DI | Hilt 2.59.2 |
| Database | Room (KSP) |
| Preferences | DataStore Preferences |
| Background work | WorkManager (HiltWorker) |
| Navigation | Navigation Compose |
| Concurrency | Kotlin Coroutines + Flow |
| Build tooling | AGP 9.2.1, Gradle 9.4.1, KSP |
| Min SDK | 26 (Android 8.0 Oreo) |
| Target SDK | 35 (Android 15) |

---

## Permissions

| Permission | Why |
|-----------|-----|
| `PACKAGE_USAGE_STATS` | Screen time tracking and foreground app detection |
| `SYSTEM_ALERT_WINDOW` | Draw the blocking overlay over other apps |
| `FOREGROUND_SERVICE` | Focus session timer and app blocker run as foreground services |
| `POST_NOTIFICATIONS` | Sleep reminders, focus countdown, blocking alerts |
| `SCHEDULE_EXACT_ALARM` | Reliable bedtime and wake-up alarms |
| `QUERY_ALL_PACKAGES` | Populate the block list with all installed apps |
| `RECEIVE_BOOT_COMPLETED` | Reschedule sleep alarms after device reboot |
| `VIBRATE` | Haptic feedback on XP gains and challenge completions |

`PACKAGE_USAGE_STATS` and `SYSTEM_ALERT_WINDOW` cannot be granted via a runtime dialog. The user is guided to the relevant system settings pages from a banner on the Home screen.

---

## Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17
- Android device or emulator running API 26+

### Clone & Build

```bash
git clone https://github.com/yourusername/touch-grass.git
cd touch-grass
# Open in Android Studio and let Gradle sync, then Run
```

> **Use a real device.** `UsageStatsManager` returns empty data on most emulators since there is no real app usage to observe. App blocking also requires real apps to block.

### First-Run Setup

On first launch you will be asked for your name and a daily screen time goal. Then from the Home screen, tap each permission banner to grant the two special permissions:

**1. Screen Time Access**
```
Settings → Apps → Special App Access → Usage Access → Touch Grass → Allow
```

**2. Display Over Other Apps** *(required for blocking to work)*
```
Settings → Apps → Special App Access → Display over other apps → Touch Grass → Allow
```

Once both are granted, add apps to your block list in Settings, start a focus session, and try opening a blocked app.

---

## How App Blocking Works

```
User opens a blocked app (e.g. Instagram)
        │
        ▼
AppBlockingService polls foreground app every 1 second
        │
        ├── Focus session active? (FocusSessionService.isFocusActive)
        │         │ No  → do nothing
        │         │ Yes ↓
        ├── App in blocked list?
        │         │ No  → remove overlay if showing
        │         │ Yes ↓
        ├── App within bypass window?
        │         │ Yes → allow through (5-min grace after Emergency Bypass)
        │         │ No  ↓
        └── canDrawOverlays?
                  ├── Yes → WindowManager.TYPE_APPLICATION_OVERLAY
                  │         Full-screen view drawn over the blocked app
                  │         • 30-second countdown
                  │         • "Take a Break 🌿" → sends to Android home screen
                  │         • Emergency Bypass (−50 XP) appears after 30s
                  │         • Back key intercepted → goes to home screen
                  └── No  → Fullscreen-intent notification (fallback for
                            devices where overlay permission was not granted)
```

The overlay sits at the top of the window stack. It cannot be swiped away. It intercepts the hardware back key. The only exits are the two buttons provided.

---

## Key Design Decisions

**Why `WindowManager` overlay instead of Accessibility Service?**
Accessibility Service gives finer control (can execute `GLOBAL_ACTION_HOME` automatically) but requires a scary permission dialog and a detailed Play Store declaration justifying the use. `SYSTEM_ALERT_WINDOW` achieves the same user-visible result with a simpler permission model and no Play Store policy complications.

**Why a static `isFocusActive` flag instead of DataStore?**
`FocusSessionService` and `AppBlockingService` run in the same process. A `@Volatile` companion object `Boolean` is zero-latency and automatically resets if the process is killed — eliminating any risk of stale "focus is active" state surviving a crash in DataStore.

**Why remove color from Typography?**
Material 3 typography styles define *shape* (size, weight, line height) — not color. Color is injected at runtime by `LocalContentColor`, which the theme sets via `onBackground`/`onSurface`. Embedding `color = TextPrimary` in `TextStyle` hardcodes a dark-only value into a non-composable context, breaking light mode.

**Why `@Composable` property getters for theme colors?**
Instead of replacing every `TextPrimary` call across 20+ files with `MaterialTheme.colorScheme.onBackground`, the color aliases (`TextPrimary`, `DarkCard`, etc.) are redefined as top-level `@Composable` property getters that delegate to the current color scheme. All existing call sites work unchanged — they are already inside `@Composable` functions.

---

## Contributing

Pull requests are welcome. For major changes please open an issue first to discuss the approach.

```bash
git checkout -b feature/your-feature-name

# Verify before submitting
./gradlew :app:compileDebugKotlin   # no compile errors
./gradlew :app:lintDebug             # no new lint warnings
```

Code conventions: MVVM, Hilt injection, `StateFlow<UiState>` from ViewModels, no business logic in composables, no hardcoded colors (use `MaterialTheme.colorScheme`).

---

## License

```
MIT License

Copyright (c) 2026 Touch Grass

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

<div align="center">

Built with 🌿 and a concerning amount of irony — an app about phone addiction, built on a phone.

**[Report a Bug](https://github.com/yourusername/touch-grass/issues) · [Request a Feature](https://github.com/yourusername/touch-grass/issues) · [Discussions](https://github.com/yourusername/touch-grass/discussions)**

</div>

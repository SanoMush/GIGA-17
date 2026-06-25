# GIGA 17 — Progress Tracker
## Aplikasi Pembelajaran Informatika berbasis Gamifikasi

---

## Fase 1 — Setup & Data Layer ✅
- [x] Buat file `PROGRESS.md` (tracker ini)
- [x] Update `gradle/libs.versions.toml` — tambah Room, KSP, Navigation, Lifecycle, Coroutines
- [x] Update `build.gradle.kts` (root) — tambah plugin KSP & Serialization
- [x] Update `app/build.gradle.kts` — apply plugin & tambah semua dependencies
- [x] Buat struktur folder sesuai blueprint
- [x] Buat `SiswaEntity.kt` — profil & kredensial siswa (NIM + Password)
- [x] Buat `ProgresEntity.kt` — progres siswa per materi
- [x] Buat `PencapaianEntity.kt` — badge/achievement
- [x] Buat `MateriEntity.kt` — konten materi (teori, quiz, XP)
- [x] Buat `LogEventEntity.kt` — event log untuk observasi eksperimen
- [x] Verifikasi Gradle Sync berhasil — BUILD SUCCESSFUL (3m 29s, 37 tasks)

## Fase 2 — DAO & Database
- [x] Buat `SiswaDao.kt` — CRUD + autentikasi lokal
- [x] Buat `ProgresDao.kt` — tracking progres
- [x] Buat `PencapaianDao.kt` — manajemen badge
- [x] Buat `MateriDao.kt` — read konten materi
- [x] Buat `LogEventDao.kt` — logging observasi
- [x] Buat `TypeConverters.kt` — konversi tipe data Room
- [x] Buat `Giga17Database.kt` — RoomDatabase + Seeder callback
- [x] Verifikasi build berhasil

## Fase 3 — Domain Models & Repository
- [x] Buat domain models (`Siswa.kt`, `Progres.kt`, `Pencapaian.kt`, `Materi.kt`, `GamifikasiState.kt`)
- [x] Buat `SiswaRepository.kt`
- [x] Buat `ProgresRepository.kt`
- [x] Buat `PencapaianRepository.kt`
- [x] Buat `MateriRepository.kt`
- [x] Buat `ObservationRepository.kt`
- [x] Verifikasi build berhasil

## Fase 4 — Gamification Engine
- [x] Buat gamification models (`XpEvent.kt`, `LevelDefinition.kt`, `BadgeDefinition.kt`, `GamificationResult.kt`) / `GamificationEngine.kt`
- [x] Buat rule sets (`XpRuleSet.kt`, `LevelRuleSet.kt`, `BadgeRuleSet.kt`)
- [x] Buat `XpCalculator.kt` — kalkulasi XP (pure function)
- [x] Buat `LevelingSystem.kt` — XP → Level mapping (pure function)
- [x] Buat `BadgeEvaluator.kt` — evaluasi trigger badge (pure function)
- [ ] Tulis unit test untuk Gamification Engine
- [ ] Verifikasi semua test pass

## Fase 5 — Observation Layer
- [ ] Buat observation models (`ObservationEvent.kt`, `MetricType.kt`)
- [ ] Buat `ObservationManager.kt` — facade logging API
- [ ] Buat `MetricCollector.kt` — kolektor metrik
- [ ] Buat `ObservationExporter.kt` — ekspor data ke CSV/JSON
- [ ] Verifikasi build berhasil

## Fase 6 — DI & Utilities
- [x] Buat `AppContainer.kt` — manual dependency injection container
- [ ] Buat `Constants.kt` — app-wide constants
- [ ] Buat `DateTimeUtils.kt` — helper tanggal/waktu
- [ ] Buat `Extensions.kt` — Kotlin extension functions
- [x] Buat `Giga17Application.kt` — Application class (init DI)
- [ ] Update `AndroidManifest.xml` — register Application class
- [ ] Verifikasi build berhasil

## Fase 7 — ViewModels & State Management
- [x] Buat sealed interfaces (`SiswaUiState.kt`, `PembelajaranUiState.kt`, `GamifikasiUiState.kt`, `DashboardUiState.kt`)
- [x] Buat `SiswaViewModel.kt` — autentikasi & profil (AuthViewModel)
- [x] Buat `PembelajaranViewModel.kt` — sesi belajar & quiz (KuisViewModel)
- [ ] Buat `GamifikasiViewModel.kt` — XP, level, badge
- [x] Buat `DashboardViewModel.kt` — halaman utama (completed)
- [ ] Verifikasi build berhasil

## Fase 8 — UI Layer (Navigation & Theme)
- [ ] Update `Color.kt`, `Type.kt`, `Theme.kt` — design system GIGA 17
- [x] Buat `Screen.kt` — sealed class route definitions
- [x] Buat `Giga17NavGraph.kt` — NavHost & routing
- [x] Update `MainActivity.kt` — wire NavGraph

## Fase 9 — UI Layer (Screens & Components)
- [x] Buat reusable components (`XpProgressBar`, `LevelBadge`, `BadgeCard`, `MateriCard`, `QuizOptionItem`, `StatisticCard`) (Partial: MateriCard, ProgressBar)
- [x] Buat Bottom Navigation Bar & MainScreen wrapper
- [ ] Buat `SplashScreen.kt`
- [x] Buat `LoginScreen.kt` & `RegisterScreen.kt`
- [x] Buat `DashboardScreen.kt`
- [x] Buat `ProfilScreen.kt`
- [x] Buat `DaftarMateriScreen.kt` & `DetailMateriScreen.kt`
- [x] Buat `QuizScreen.kt` & `HasilQuizScreen.kt`
- [x] Buat `PencapaianScreen.kt`

## Fase 10 — Integrasi & Verifikasi Akhir
- [ ] Full build verification (`gradlew assembleDebug`)
- [ ] Navigasi end-to-end test
- [ ] Seed data & database verification
- [ ] Observation logging verification
- [ ] Review final & dokumentasi

## Fase 11 — Ekspansi Bottom Navigation
- [x] Tambahkan 4 Tab (Dashboard, Materi, Kuis, Profil)
- [x] Pisahkan DaftarMateri dan DaftarKuis
- [x] Pindahkan Profil dan Logout

## Fase 12 — Real Content Injection
- [x] Update DatabaseSeeder dengan Materi & Kuis Informatika SMA
- [x] Perbarui Inisialisasi Badge
- [x] Instruksi Clear Data

## Fase 13 — Logic & State (Gamifikasi)
- [x] Evaluasi status Progres
- [x] Update DashboardViewModel
- [x] Efek Visual (Gembok & Opacity)

## Fase 14 — Anti-Farming XP
- [x] Update KuisViewModel logika XP
- [x] Tambahkan state isAlreadyCompleted
- [x] Update UI DetailMateriScreen & KuisScreen feedback

## Fase 15 — Migrasi Cloud Backend & Auth
- [x] Setup Dependencies Firebase
- [x] Auth Sistem Tertutup (NIM -> Email)
- [x] SiswaRepository Sync dengan Firestore Cache

## Fase 16 — Refaktorasi Navigasi & Integrasi Kuis ke Materi
- [x] Rombak Bottom Navigation Bar (Dashboard, Materi, Leaderboard, Profile)
- [x] Integrasi Kuis sebagai Cabang Materi
- [x] Implementasi Progression Gate (Kuis terkunci jika materi belum selesai)

## Fase 17 — UI Revamp & Gamified Profile
- [x] Perkecil Header di `DaftarMateriScreen`, `LeaderboardScreen`, dan `ProfilScreen`
- [x] Update UI Dashboard (Statistik Belajar, Login Streak, Aktivitas Terakhir)
- [x] Tambah sistem ganti Avatar menggunakan Icons bawaan (sinkronisasi Room & Firestore)

---
> **Metodologi:** Kuasi-eksperimen dengan desain iteratif.  
> **Aturan:** Setiap fase di-review dan disetujui sebelum lanjut ke fase berikutnya.

# GIGA 17

**Aplikasi Pembelajaran Informatika berbasis Gamifikasi**

GIGA 17 adalah sebuah aplikasi pembelajaran interaktif untuk materi Informatika Sekolah Menengah Atas (SMA) yang menggunakan pendekatan gamifikasi. Aplikasi ini dibangun dengan Android Native (Kotlin & Jetpack Compose) dan dirancang untuk metode penelitian kuasi-eksperimen.

## Fitur Utama

- **Sistem Gamifikasi**: Dilengkapi dengan fitur Experience Points (XP), sistem Leveling, dan Pencapaian (Badges) untuk meningkatkan motivasi belajar siswa.
- **Pembelajaran Interaktif**: Menyediakan materi pembelajaran yang terstruktur mulai dari teori dasar hingga kuis interaktif.
- **Progression Gate**: Sistem penguncian materi dan kuis secara dinamis. Siswa harus menyelesaikan materi prasyarat sebelum dapat mengakses kuis atau bab selanjutnya.
- **Leaderboard**: Papan peringkat siswa untuk menumbuhkan rasa kompetisi yang sehat.
- **Terminal Pascal Simulator**: Simulator terminal bawaan yang memungkinkan siswa menulis, mengompilasi, dan menjalankan kode Pascal langsung dari aplikasi (menggunakan Jdoodle API).
- **Integrasi Cloud**: Menggunakan sinkronisasi data lokal (Room Database) ke cloud (Firebase Firestore) dan sistem autentikasi.
- **Anti-Farming XP**: Logika yang mencegah eksploitasi poin XP dari materi yang sudah pernah diselesaikan.
- **Profil Gamifikasi**: Avatar yang dapat dikustomisasi dan statistik pembelajaran yang komprehensif.

## Teknologi yang Digunakan

- **Bahasa**: Kotlin
- **UI Framework**: Jetpack Compose
- **Database Lokal**: Room Database (SQLite)
- **Database Cloud & Auth**: Firebase Firestore & Firebase Authentication
- **Arsitektur**: MVVM (Model-View-ViewModel) + Clean Architecture
- **Dependency Injection**: Manual DI via AppContainer
- **Asynchronous & State**: Kotlin Coroutines & StateFlow

## Cara Menjalankan Proyek

1. Clone repositori ini.
2. Buka proyek menggunakan **Android Studio**.
3. Pastikan Anda telah menghubungkan proyek dengan Firebase (tambahkan file `google-services.json` ke dalam direktori `app/` yang terhubung dengan akun Firebase Anda).
4. Lakukan sinkronisasi Gradle (Sync Project with Gradle Files).
5. Jalankan aplikasi pada emulator atau perangkat fisik Android.

---
*Proyek ini dikembangkan menggunakan desain iteratif berdasarkan blueprint gamifikasi.*

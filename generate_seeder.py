import json
import os

def get_5_questions(subbab_title):
    questions = []
    for i in range(1, 6):
        questions.append({
            "question": f"Pertanyaan {i} mengenai {subbab_title}: Apa konsep utamanya?",
            "options": ["A. Opsi Salah 1", "B. Opsi Benar", "C. Opsi Salah 2", "D. Opsi Salah 3"],
            "correctIndex": 1,
            "explanation": f"Penjelasan singkat untuk pertanyaan {i} mengenai {subbab_title}."
        })
    return json.dumps({"xpReward": 20, "questions": questions}).replace("'", "''")

def get_5_questions_bab(bab_title):
    questions = []
    for i in range(1, 6):
        questions.append({
            "question": f"Evaluasi Bab {i}: Bagaimana penerapan {bab_title}?",
            "options": ["Penerapan Salah", "Penerapan Benar", "Tidak Tahu", "Semua Salah"],
            "correctIndex": 1,
            "explanation": f"Penjelasan untuk kuis akhir bab {bab_title}."
        })
    return json.dumps({"xpReward": 100, "questions": questions}).replace("'", "''")

bab_data = [
    {
        "title": "Pemrograman 1: Dasar Sintaks dan Memori",
        "simulasi": "Kotak Memori",
        "subbabs": [
            {
                "title": "A. Pengenalan Bahasa Pascal dan Struktur Kode",
                "text": "Bahasa Pascal adalah bahasa pemrograman yang dikembangkan pada tahun 1970 oleh Niklaus Wirth di Swiss Federal Institute of Technology, Zurich, untuk tujuan pendidikan. Bahasa ini dinamai dari Blaise Pascal, seorang matematikawan dan filsuf Prancis abad ke-17. Pascal dirancang sebagai bahasa pemrograman terstruktur dengan sintaks yang jelas dan mudah dibaca, menjadikannya populer untuk pengajaran konsep pemrograman dan pengembangan perangkat lunak.\n\nWhy Pascal?\n\nSyntax Jelas dan mudah dibaca\nPascal punya aturan penulisan yang rapi, cocok untuk pemula supaya cepat paham logika pemrograman.\n\nStruktur Teratur\nDibuat untuk mengajarkan konsep structured programming, jadi siswa terbiasa menulis kode yang sistematis.\n\nCocok untuk Pendidikan\nMemang dirancang untuk pembelajaran, jadi banyak digunakan di sekolah dan universitas.\n\nKaya Fitur Dasar\nMendukung tipe data, prosedur, fungsi, dan kontrol alur, cukup untuk membangun program sederhana hingga menengah.\n\n![Blaise Pascal](https://upload.wikimedia.org/wikipedia/commons/2/26/Blaise_Pascal_Versailles.JPG)"
            },
            {
                "title": "B. Fungsi Input dan Output Dasar",
                "text": "Untuk berinteraksi dengan pengguna, kita membutuhkan Input dan Output. Di Pascal, output dilakukan dengan menggunakan fungsi Write() atau WriteLn(). Fungsi Write() mencetak teks tanpa pindah baris, sedangkan WriteLn() mencetak teks lalu pindah ke baris baru.\n\nSedangkan untuk menerima input atau masukan dari pengguna, kita menggunakan fungsi Read() atau ReadLn(). Fungsi ini akan menunggu pengguna mengetikkan sesuatu di keyboard dan menyimpannya ke dalam variabel.\n\n![Terminal Input Output](https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=600&q=80)"
            },
            {
                "title": "C. Variabel: Tempat Menyimpan Data",
                "text": "Variabel adalah ibarat sebuah wadah di dalam memori komputer yang diberi nama spesifik, digunakan untuk menyimpan data yang nilainya dapat berubah-ubah selama program dijalankan. Sebelum digunakan, variabel harus dideklarasikan terlebih dahulu pada bagian VAR.\n\nSetiap variabel memiliki alamat di memori komputer (RAM). Penamaan variabel tidak boleh menggunakan spasi dan tidak boleh diawali dengan angka. Variabel memudahkan kita mengelola aliran data dari satu proses ke proses lain secara dinamis.\n\n![Ilustrasi RAM](https://images.unsplash.com/photo-1562976540-1502c2145186?w=600&q=80)"
            },
            {
                "title": "D. Tipe Data Dasar (Integer, Float, Char, Boolean)",
                "text": "Tipe data menentukan jenis nilai yang bisa disimpan dalam variabel. \n\n1. Integer untuk bilangan bulat (contoh: 5, -10, 1000).\n2. Float / Real untuk bilangan desimal (contoh: 3.14, 0.5).\n3. Char untuk satu buah karakter huruf/angka (contoh: \"A\", \"7\", \"?\").\n4. Boolean untuk nilai logika True atau False.\n\nDengan mendefinisikan tipe data, komputer tahu seberapa besar memori yang harus dialokasikan untuk variabel tersebut dan operasi apa saja yang valid untuk dikenakan padanya."
            },
            {
                "title": "E. Operator Aritmatika dan Logika",
                "text": "Operator adalah simbol khusus yang memerintahkan komputer untuk melakukan operasi matematika atau logika.\n\nOperator Aritmatika digunakan untuk perhitungan matematika: penambahan (+), pengurangan (-), perkalian (*), pembagian (/), pembagian bulat (div), sisa bagi (mod).\n\nOperator Logika digunakan untuk operasi kebenaran: AND, OR, dan NOT. Operator ini biasanya digunakan dalam pengambilan keputusan atau perulangan yang membutuhkan lebih dari satu kondisi untuk dievaluasi secara bersamaan."
            }
        ]
    },
    {
        "title": "Pemrograman 2: Percabangan (Membangun Logika)",
        "simulasi": "Saklar Logika",
        "subbabs": [
            {
                "title": "A. Konsep Pengambilan Keputusan dalam Kode",
                "text": "Dalam dunia nyata, kita sering mengambil keputusan berdasarkan kondisi tertentu (misal: jika hujan, maka pakai payung). Dalam kode, pengambilan keputusan ini disebut percabangan atau branch.\n\nPercabangan memungkinkan program untuk tidak hanya berjalan lurus dari atas ke bawah, melainkan melompat atau mengabaikan blok kode tertentu berdasarkan status dari sebuah variabel atau input. Ini adalah fondasi dari semua Artificial Intelligence dan logika aplikasi."
            },
            {
                "title": "B. Percabangan Tunggal (If)",
                "text": "Kondisi IF adalah bentuk percabangan paling sederhana. Blok kode di dalam IF hanya akan dieksekusi apabila kondisi yang dievaluasi bernilai True.\n\nJika kondisinya bernilai False, maka program akan melewati blok kode tersebut dan melanjutkan eksekusi ke baris kode di bawahnya tanpa melakukan tindakan apa-apa.\n\n![Logika If](https://images.unsplash.com/photo-1506452305024-9d3f02d1c9b5?w=600&q=80)"
            },
            {
                "title": "C. Percabangan Ganda (If-Else)",
                "text": "Jika kita memiliki dua kemungkinan jalan alternatif, kita gunakan IF-ELSE. Jika kondisi IF bernilai True, blok IF dijalankan. \n\nJika kondisi IF tersebut ternyata bernilai False, maka secara otomatis program akan melompat dan menjalankan blok ELSE yang telah didefinisikan. Tidak ada kondisi di mana kedua blok dijalankan secara bersamaan."
            },
            {
                "title": "D. Percabangan Lebih dari Dua Kondisi (If-Else If)",
                "text": "Digunakan ketika terdapat lebih dari 2 pilihan. Program akan mengecek dari IF pertama. Jika IF pertama False, ia akan berlanjut mengecek ELSE IF di bawahnya secara berurutan.\n\nEvaluasi akan berhenti seketika saat ada satu kondisi yang bernilai True, menjalankan bloknya, lalu melompat keluar dari seluruh struktur percabangan tersebut."
            },
            {
                "title": "E. Pemilihan Kasus (Switch-Case)",
                "text": "Switch-Case (atau Case-Of di Pascal) adalah alternatif dari If-Else If bertingkat. Fitur ini sangat berguna dan lebih rapi ketika kita perlu memeriksa satu buah variabel spesifik terhadap banyak kemungkinan nilai (misalnya menu pilihan 1, 2, 3).\n\nBerbeda dengan IF yang bisa mengevaluasi rentang (misal A > 10), Case-Of lebih dioptimalkan untuk pencocokan nilai pasti (exact match)."
            }
        ]
    },
    {
        "title": "Pemrograman 3: Perulangan (Otomatisasi Proses)",
        "simulasi": "Mesin Cetak Loop",
        "subbabs": [
            {
                "title": "A. Konsep Perulangan dan Efisiensi Kode",
                "text": "Perulangan (Looping) digunakan untuk mengeksekusi satu blok kode berkali-kali tanpa harus menulis ulang kode tersebut. Ini membuat kode menjadi sangat efisien, mudah dirawat, dan menghindari redundansi.\n\nBayangkan jika Anda harus mencetak teks ke layar sebanyak 1000 kali. Menulis WriteLn 1000 kali tentu tidak efisien; dengan loop, Anda hanya butuh 3 baris kode.\n\n![Loop Konsep](https://images.unsplash.com/photo-1496262967815-132206202600?w=600&q=80)"
            },
            {
                "title": "B. Perulangan dengan Batas Pasti (For Loop)",
                "text": "FOR Loop digunakan ketika kita sudah mengetahui dengan pasti berapa kali perulangan akan dilakukan (misal: mencetak angka 1 sampai 10). Terdapat pencacah (counter) otomatis di dalamnya.\n\nDi Pascal, sintaksnya berbunyi \"FOR i := 1 TO 10 DO\". Variabel \"i\" akan terus bertambah 1 secara otomatis setiap kali blok selesai dijalankan, hingga mencapai angka batas akhir (10)."
            },
            {
                "title": "C. Perulangan dengan Kondisi (While Loop)",
                "text": "WHILE Loop akan terus berjalan SELAMA kondisinya bernilai True. Biasanya kita menggunakannya ketika batas akhirnya belum diketahui pasti secara numerik, misalnya \"membaca data sampai baris di dalam file habis\".\n\nJika kondisi awalnya sudah False, maka blok di dalam WHILE Loop tidak akan pernah dijalankan sama sekali (0 iterasi)."
            },
            {
                "title": "D. Perbedaan Karakteristik For dan While",
                "text": "FOR lebih cocok digunakan untuk iterasi yang terstruktur dengan counter numerik yang jelas. Sifatnya terprediksi (deterministic).\n\nSebaliknya, WHILE digunakan ketika iterasi sangat bergantung pada perubahan state dari luar atau dinamika variabel kondisi di dalam blok kode. Sering dipakai pada pemrograman game untuk Game Loop."
            }
        ]
    },
    {
        "title": "Pemrograman 4: Struktur Data Dasar",
        "simulasi": "Loker Indeks",
        "subbabs": [
            {
                "title": "A. Pengenalan Larik (Array) Satu Dimensi",
                "text": "Array adalah sebuah wadah atau struktur data yang dapat menyimpan banyak elemen data dengan tipe yang SAMA secara berurutan dalam memori.\n\nIbarat sebuah loker di sekolah, satu loker besar (Array) bisa terdiri dari banyak kotak kecil (Elemen) yang masing-masing bisa menyimpan buku pelajaran. Array menghindari kita dari pembuatan variabel terpisah yang terlalu banyak."
            },
            {
                "title": "B. Konsep Indeks pada Array (Dimulai dari 0)",
                "text": "Untuk mengakses isi sebuah Array, kita menggunakan nomor identifikasi yang disebut Indeks. Perlu diingat, di mayoritas bahasa pemrograman modern, indeks Array dimulai dari angka 0, bukan 1.\n\nJadi jika sebuah array bernama A memiliki 5 elemen, elemen pertamanya ada di A[0], dan elemen terakhirnya ada di A[4].\n\n![Indeks Array](https://images.unsplash.com/photo-1629654297299-c8506221ca97?w=600&q=80)"
            },
            {
                "title": "C. Karakter dan String (Kumpulan Huruf)",
                "text": "String pada dasarnya sering diimplementasikan sebagai sebuah Array yang khusus menyimpan elemen berupa Karakter (huruf/simbol). Oleh karena itu, kita juga bisa mengakses huruf ke-N dalam sebuah kata menggunakan konsep Indeks Array.\n\nSebagai contoh, jika variabel kata = \"PASCAL\", maka kata[0] merujuk pada huruf 'P'."
            },
            {
                "title": "D. Cara Membaca dan Mengubah Isi Array",
                "text": "Kita bisa membaca dan mengubah isi Array secara spesifik. Contoh: scores[2] := 90 akan mengubah nilai pada indeks ke-2 menjadi 90. \n\nUntuk membaca atau mengubah seluruh isi array, kita hampir selalu memadukannya dengan struktur kendali For Loop, karena variabel counternya (i) bisa langsung digunakan sebagai penunjuk indeks Array."
            }
        ]
    }
]

out = []
out.append("package com.example.giga17.data.local")
out.append("")
out.append("import androidx.sqlite.db.SupportSQLiteDatabase")
out.append("")
out.append("object DatabaseSeeder {")
out.append("    fun populateData(db: SupportSQLiteDatabase) {")
out.append("        // 1. Seed Siswa Dummy")
out.append('        db.execSQL("""')
out.append("            INSERT INTO siswa (nim, password, nama, kelas, avatar_res_id, total_xp, current_level, login_streak, created_at, last_active_at)")
out.append("            VALUES ('123456', '123', 'Siswa SMAN 17 Garut', 'X IPA 1', 'avatar_default', 150, 2, 1, 0, 0)")
out.append('        """.trimIndent())')
out.append("")
out.append("        // 2. Pencapaian Awal")
out.append('        db.execSQL("""')
out.append("            INSERT INTO pencapaian (siswa_id, badge_id, badge_name, badge_description, badge_category, badge_icon, unlocked_at)")
out.append("            VALUES (1, 'first_login', 'Siswa Baru', 'Melakukan login pertama kali', 'MILESTONE', 'ic_badge_1', 0)")
out.append('        """.trimIndent())')

bab_id = 1
for bab in bab_data:
    out.append("")
    out.append(f"        // --- BAB {bab_id}: {bab['title']} ---")
    
    kuis_bab_json = get_5_questions_bab(bab['title'])
    out.append("        val kuisBab{}Json = \"\"\"{}\"\"\".trimIndent()".format(bab_id, kuis_bab_json))
    out.append('        db.execSQL("""')
    out.append("            INSERT INTO bab (judul_bab, kuis_akhir_bab_json, tipe_simulasi, is_active)")
    out.append(f"            VALUES ('{bab['title']}', '$kuisBab{bab_id}Json', '{bab['simulasi']}', 1)")
    out.append('        """.trimIndent())')
    
    for subbab in bab['subbabs']:
        out.append("")
        isi_escaped = subbab['text'].replace("'", "''")
        kuis_subbab_json = get_5_questions(subbab['title'])
        out.append("        val kuisSubBabJson_{}_{} = \"\"\"{}\"\"\".trimIndent()".format(bab_id, bab['subbabs'].index(subbab), kuis_subbab_json))
        out.append('        db.execSQL("""')
        out.append("            INSERT INTO sub_bab (bab_id, judul_sub_bab, isi_teks, kuis_sub_bab_json)")
        out.append(f"            VALUES ({bab_id}, '{subbab['title']}', '{isi_escaped}', '$kuisSubBabJson_{bab_id}_{bab['subbabs'].index(subbab)}')")
        out.append('        """.trimIndent())')
        
    bab_id += 1

out.append("    }")
out.append("}")

with open("C:/Users/Admin/AndroidStudioProjects/GIGA17/app/src/main/java/com/example/giga17/data/local/DatabaseSeeder.kt", "w", encoding="utf-8") as f:
    f.write("\n".join(out))

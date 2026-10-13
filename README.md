<div align="center">

# 🧮 Glass Calculator

**Aplikasi kalkulator desktop bergaya iOS dengan tampilan glass (kaca), dibuat menggunakan Java Swing.**

![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![UI](https://img.shields.io/badge/UI-Java%20Swing-FF9500?style=for-the-badge)
![Dependencies](https://img.shields.io/badge/Dependencies-0-success?style=for-the-badge)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-lightgrey?style=for-the-badge)

</div>

## 📌 Deskripsi dan Tujuan Aplikasi

**Glass Calculator** adalah aplikasi kalkulator desktop yang dikembangkan menggunakan bahasa pemrograman Java dan pustaka Java Swing. Aplikasi ini dirancang untuk membantu pengguna melakukan operasi aritmatika dasar, yaitu penjumlahan, pengurangan, perkalian, dan pembagian, melalui antarmuka yang sederhana dan modern.

Proyek ini juga menjadi penerapan konsep pemrograman berorientasi objek (PBO), pembuatan antarmuka grafis (GUI), serta penanganan input dan kesalahan dalam aplikasi.

## 👥 Identitas Pengembang

**Kelompok 9** · Pemrograman Berorientasi Objek

| No. | Nama | NPM |
|:---:|---|:---:|
| 1 | Muhammad Isyraq Akramin | 250810701100053 |
| 2 | Tiara Balqis | 250810701100074 |
| 3 | M. Hafidh Farras | 250810701100098 |
| 4 | Farras Arthada Siregar | 250810701100111 |

## ✨ Fitur Aplikasi

- **Operasi aritmatika dasar:** penjumlahan, pengurangan, perkalian, dan pembagian.
- **Tampilan glass:** latar berwarna krem, lingkaran oranye, dan kartu putih transparan dengan sudut membulat.
- **Tombol bulat:** tombol digambar menggunakan `Graphics2D`, dilengkapi efek kilau, garis tepi, dan bayangan. Tombol `0` berbentuk pil lebar.
- **Layar dua baris:** ekspresi ditampilkan di bagian atas dan hasil perhitungan di bagian bawah.
- **Pemisah ribuan otomatis:** misalnya `1234567` ditampilkan sebagai `1,234,567`.
- **Ukuran font adaptif:** ukuran teks hasil menyesuaikan apabila angka terlalu panjang.
- **Riwayat perhitungan:** dua riwayat terakhir ditampilkan pada layar; ikon jam membuka riwayat lengkap dan tombol untuk menghapus riwayat.
- **Dukungan keyboard:** operasi dapat dilakukan melalui tombol kalkulator maupun keyboard.
- **Presisi angka:** menggunakan `BigDecimal` untuk mengurangi masalah presisi pada perhitungan desimal, seperti `0.1 + 0.2 = 0.3`.
- **Penanganan kesalahan:** pembagian dengan nol menampilkan `Error`; tombol `AC` digunakan untuk mengembalikan kalkulator ke kondisi awal.

## 🧰 Teknologi yang Digunakan

- **Bahasa pemrograman:** Java
- **Antarmuka grafis:** Java Swing
- **Penggambaran komponen dan efek visual:** `Graphics2D`, `RadialGradientPaint`, dan gradasi warna
- **Pengolahan angka:** `BigDecimal`
- **Dependensi eksternal:** tidak ada

## 📁 Struktur Proyek

```text
.
├── Calculator.java    # Kode aplikasi, antarmuka, dan logika perhitungan
└── README.md          # Dokumentasi proyek
```

Seluruh kode aplikasi berada dalam satu file Java. Beberapa bagian utama di dalamnya adalah:

| Bagian | Fungsi |
|---|---|
| `Calculator` | Jendela utama, logika perhitungan, riwayat, dan dukungan keyboard |
| `GlassBackground` | Panel latar dengan gradasi, lingkaran oranye, dan kartu bergaya glass |
| `GlassButton` | Komponen tombol bulat dengan efek visual kustom |

## 🚀 Instalasi dan Cara Menjalankan

### Prasyarat

Pastikan **JDK 8 atau versi yang lebih baru** sudah terpasang di komputer. Periksa instalasi melalui terminal atau Command Prompt:

```bash
java -version
javac -version
```

Jika kedua perintah menampilkan informasi versi Java, lanjutkan ke langkah berikutnya.

### Langkah menjalankan aplikasi

1. Unduh atau klon repositori GitHub proyek ini.
2. Buka terminal atau Command Prompt pada folder yang berisi `Calculator.java`.
3. Kompilasi kode dengan perintah:

   ```bash
   javac Calculator.java
   ```

4. Jalankan aplikasi dengan perintah:

   ```bash
   java Calculator
   ```

Setelah kompilasi berhasil, beberapa file `.class` akan dibuat. Biarkan file-file tersebut berada di folder yang sama saat aplikasi dijalankan.

## 🎮 Cara Menggunakan Aplikasi

1. Masukkan angka pertama menggunakan tombol pada aplikasi atau keyboard.
2. Pilih operator yang diperlukan: `+`, `−`, `×`, atau `÷`.
3. Masukkan angka kedua.
4. Tekan tombol `=` atau `Enter` untuk menampilkan hasil.
5. Gunakan `AC` untuk mengatur ulang input. Riwayat perhitungan tetap tersimpan.

### Fungsi tombol

| Tombol | Fungsi |
|:---:|---|
| `AC` | Mengatur ulang angka, operator, dan ekspresi; riwayat tetap tersimpan |
| `+/-` | Mengubah tanda angka menjadi positif atau negatif |
| `%` | Membagi angka yang sedang ditampilkan dengan 100 |
| `÷` `×` `−` `+` | Memilih operasi aritmatika |
| `=` | Menghitung hasil dan menyimpan perhitungan ke riwayat |
| `.` | Memasukkan tanda desimal |
| Ikon jam | Membuka daftar riwayat perhitungan |

### Pintasan keyboard

| Tombol keyboard | Fungsi |
|---|---|
| `0`–`9` | Memasukkan angka |
| `.` | Memasukkan tanda desimal |
| `+` `-` `*` `/` | Penjumlahan, pengurangan, perkalian, dan pembagian |
| `%` | Operasi persen |
| `Enter` atau `=` | Menghitung hasil |
| `Backspace` | Menghapus satu digit terakhir |
| `Esc` | Mengatur ulang kalkulator (fungsi `AC`) |

## 🧪 Contoh Input dan Output

Contoh berikut menunjukkan penggunaan operasi aritmatika dasar. Urutan input dapat dimasukkan melalui tombol kalkulator atau keyboard.

| Contoh input | Operasi | Output yang diharapkan |
|---|---|---:|
| `10` → `+` → `5` → `=` | Penjumlahan | `15` |
| `10` → `−` → `5` → `=` | Pengurangan | `5` |
| `10` → `×` → `5` → `=` | Perkalian | `50` |
| `10` → `÷` → `5` → `=` | Pembagian | `2` |
| `0.1` → `+` → `0.2` → `=` | Penjumlahan desimal | `0.3` |
| `10` → `÷` → `0` → `=` | Pembagian dengan nol | `Error` |

*Catatan:* Pembagian menggunakan pembulatan hingga 12 angka desimal dengan mode `HALF_UP`, kemudian angka nol di belakang desimal dihilangkan. Input dibatasi hingga 12 digit agar tampilan tetap rapi.

## ⚙️ Gambaran Cara Kerja

### Logika perhitungan

Kalkulator menggunakan pola berbasis keadaan (*state*), dengan beberapa variabel utama:

```text
cur       → angka yang sedang dimasukkan atau ditampilkan
left      → operand kiri yang disimpan
op        → operator yang menunggu operand berikutnya
newEntry  → penanda bahwa input angka baru harus dimulai
exprText  → teks ekspresi yang ditampilkan di baris atas
```

Secara umum, alur perhitungannya adalah:

1. Saat operator dipilih, angka saat ini disimpan sebagai operand kiri dan operator disimpan.
2. Setelah angka berikutnya dimasukkan dan tombol `=` ditekan, perhitungan dilakukan.
3. Hasil dirapikan dan diformat dengan pemisah ribuan sebelum ditampilkan.

### Efek visual

Java Swing tidak menyediakan efek blur latar secara langsung. Karena itu, tampilan bergaya glass dibuat melalui beberapa lapisan visual: latar krem, lingkaran oranye dengan gradasi, kartu transparan bersudut membulat, serta tombol dengan gradasi, kilau, garis tepi, dan bayangan.

## 🗺️ Rencana Pengembangan

- [ ] Menambahkan tema gelap
- [ ] Menambahkan mode kalkulator ilmiah
- [ ] Menyimpan riwayat ke file
- [ ] Menambahkan fitur salin hasil ke clipboard
- [ ] Mengembangkan versi JavaFX dengan efek blur

## 📄 Lisensi dan Kontributor

Proyek ini dibuat oleh **Kelompok 9  Pemrograman Berorientasi Objek** untuk keperluan pembelajaran. Lisensi distribusi khusus belum ditentukan.

---

<div align="center">

Dibuat dengan ☕ dan Java Swing

</div>

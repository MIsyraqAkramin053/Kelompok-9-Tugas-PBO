<div align="center">

# 🧮 Glass Calculator

**Kalkulator desktop bergaya iOS dengan tampilan _glass_ (kaca), dibuat murni dengan Java Swing.**

![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/UI-Swing-FF9500?style=for-the-badge)
![Dependencies](https://img.shields.io/badge/Dependencies-0-success?style=for-the-badge)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-lightgrey?style=for-the-badge)

</div>

## 👥 Anggota Kelompok
 
**Kelompok 9** · Pemrograman Berorientasi Objek
 
| No | Nama | NPM |
|:---:|---|:---:|
| 1 | Muhammad Isyraq Akramin | 250810701100053 |
| 2 | Tiara Balqis | 250810701100074 |
| 3 | M. Hafidh Farras | 250810701100098 |
| 4 | Farras Arthada Siregar | 250810701100111 |
 
---
 
## ✨ Fitur

- 🪟 **Tampilan glass**: latar krem, lingkaran oranye di belakang, dan kartu putih tembus pandang dengan sudut membulat.
- ⭕ **Tombol bulat kaca**: digambar manual dengan `Graphics2D` (anti-aliasing), lengkap dengan kilau, garis tepi, dan bayangan lembut. Tombol `0` berbentuk pil lebar.
- 🔢 **Layar dua baris**: ekspresi kecil di atas, hasil besar di bawah, misalnya `2,122 ÷ 2` lalu `1,061`.
- 🔠 **Pemisah ribuan otomatis**: `1234567` tampil sebagai `1,234,567`.
- 📏 **Font hasil adaptif**: mengecil sendiri kalau angkanya panjang.
- 🕘 **Riwayat perhitungan**: dua riwayat terakhir tampil kecil di layar, dan ikon jam membuka daftar lengkap beserta tombol hapus riwayat.
- ⌨️ **Dukungan keyboard** penuh.
- 🎯 **Presisi tinggi** dengan `BigDecimal`, jadi `0.1 + 0.2` menghasilkan tepat `0.3`.
- 🛡️ **Penanganan error**: pembagian dengan nol menampilkan `Error`, dan `AC` mengembalikan kalkulator ke kondisi awal.

---

## 📁 Struktur Proyek

```
.
├── Calculator.java    # Seluruh aplikasi (UI + logika)
└── README.md
```

Seluruh kode ada dalam satu file dan terdiri dari:

| Bagian | Fungsi |
|---|---|
| `Calculator` | Jendela utama, logika perhitungan, riwayat, dan keyboard |
| `GlassBackground` | Panel latar: gradasi krem, lingkaran oranye, dan kartu kaca |
| `GlassButton` | Tombol bulat kaca (komponen kustom) |

---

## 🚀 Cara Menjalankan

### Prasyarat

- **JDK 8 atau lebih baru** (cek dengan `java -version` dan `javac -version`)

### Langkah

```bash
# 1. Masuk ke folder proyek
cd nama-folder-proyek

# 2. Kompilasi
javac Calculator.java

# 3. Jalankan
java Calculator
```

> Hasil kompilasi berupa beberapa file `.class` (termasuk `Calculator$GlassButton.class` dan `Calculator$GlassBackground.class`). Semuanya harus berada di folder yang sama saat menjalankan.

---

## 🎮 Cara Pakai

### Tombol

| Tombol | Fungsi |
|:---:|---|
| `AC` | Reset angka, operator, dan ekspresi. Riwayat tetap tersimpan |
| `+/-` | Balik tanda positif/negatif |
| `%` | Bagi angka saat ini dengan 100 |
| `÷` `×` `−` `+` | Operator hitung. Menekan operator berturut-turut akan menghitung berantai |
| `=` | Hitung hasil dan simpan ke riwayat |
| `.` | Titik desimal |
| 🕘 (ikon jam) | Buka riwayat lengkap |

### Pintasan Keyboard

| Tombol Keyboard | Aksi |
|:---:|---|
| `0` – `9` | Input angka |
| `.` | Desimal |
| `+` `-` `*` `/` | Tambah, kurang, kali, bagi |
| `%` | Persen |
| `Enter` atau `=` | Hitung hasil |
| `Backspace` | Hapus satu digit terakhir |
| `Esc` | AC (reset) |

---

## ⚙️ Cara Kerja

### Logika perhitungan

Kalkulator memakai pola sederhana berbasis keadaan (*state*):

```
cur        → angka yang sedang diketik / ditampilkan
left       → operand kiri yang tersimpan
op         → operator yang sedang menunggu
newEntry   → true jika angka berikutnya harus memulai input baru
exprText   → teks ekspresi di baris atas, misalnya "2,122 ÷ 2"
```

Alurnya:

1. Saat operator ditekan, `cur` dipindah ke `left` dan `op` disimpan.
2. Setelah angka kedua diketik dan `=` ditekan, hasil dihitung dengan `calc(left, cur, op)`.
3. Hasil dibersihkan (`stripTrailingZeros`) lalu diformat dengan pemisah ribuan untuk ditampilkan.

### Aturan numerik

- Semua operasi memakai **`BigDecimal`**, bukan `double`.
- Pembagian dibulatkan ke **12 angka desimal** dengan mode `HALF_UP`, lalu angka nol di belakang dibuang.
- Input dibatasi **12 digit** supaya layar tetap rapi.
- Pembagian dengan nol menampilkan `Error`, dan hanya `AC` yang bisa dipakai untuk keluar dari kondisi itu.

### Rendering efek glass

Swing tidak punya blur latar sungguhan, jadi efek kaca ditiru dengan beberapa lapisan:

1. **Latar**: warna krem polos, ditambah lingkaran oranye dengan `RadialGradientPaint` yang memudar di tepinya.
2. **Kartu**: persegi panjang membulat berwarna putih dengan gradasi transparansi (lebih pekat di atas, lebih tipis di bawah), garis tepi, dan bayangan berlapis.
3. **Tombol**: gradasi vertikal tembus pandang, kilau putih di separuh bagian atas, garis tepi putih, dan bayangan lembut. Warna menjadi sedikit lebih terang saat kursor di atas tombol, dan tombol turun 1 piksel saat ditekan.

Diameter tombol selalu `min(lebar, tinggi)` dari selnya dan diletakkan di tengah, jadi bentuk lingkarannya tidak pernah terpotong.

---

## 🗺️ Ide Pengembangan

- [ ] Tema gelap sebagai alternatif
- [ ] Mode kalkulator ilmiah
- [ ] Menyimpan riwayat ke file
- [ ] Salin hasil ke clipboard
- [ ] Versi JavaFX dengan efek blur sungguhan

---

## 📄 Lisensi

Kelompok 9 Pemrograman Berorientasi Objek

---

<div align="center">

Dibuat dengan ☕ dan Java Swing

</div>

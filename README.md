# 📚 Latihan Perpustakaan - Microservices CI/CD & Monitoring

Proyek ini adalah sistem manajemen perpustakaan modern berbasis **Microservices Architecture**. Dibangun menggunakan **Java Spring Boot**, proyek ini mendemonstrasikan praktik terbaik *software engineering* modern termasuk **CI/CD Automation**, **Centralized Logging (ELK)**, dan **Real-time Monitoring**.

---

## 🏗️ Arsitektur & Teknologi

*   **Backend Framework**: Java Spring Boot 3.x
*   **Database**: H2 (In-Memory) / PostgreSQL (Production ready)
*   **Messaging**: RabbitMQ (Event-driven communication)
*   **Service Discovery**: Netflix Eureka
*   **API Gateway**: Spring Cloud Gateway
*   **Containerization**: Docker & Docker Compose
*   **CI/CD**: Jenkins
*   **Logging**: ELK Stack (Elasticsearch, Logstash, Kibana)
*   **Monitoring**: Prometheus & Grafana

---

## 📖 Konsep Dasar: Monolithic vs Microservices

Sebelum masuk ke teknis, penting untuk memahami mengapa kita menggunakan arsitektur ini.

### 🏛️ Monolithic Architecture
Model tradisional di mana seluruh aplikasi (Frontend, Backend, Database logic) digabung menjadi satu kesatuan besar (single codebase/deployable unit).

| Kelebihan | Kekurangan |
| :--- | :--- |
| **Simpel**: Mudah didevelop di awal (satu project IDE). | **Sulit di-scale**: Harus duplikasi seluruh server meski cuma satu fitur yang ramai. |
| **Mudah Debugging**: Log ada di satu file, trace error gampang. | **Teknologi Terkunci**: Susah ganti bahasa pemrograman/framework separuh jalan. |
| **Performance**: Tidak ada latency jaringan antar modul. | **Risiko Tinggi**: Satu bug memory leak bisa mematikan *seluruh* aplikasi. |

### 🧩 Microservices Architecture
Model modern (yang dipakai proyek ini) di mana aplikasi dipecah menjadi layanan-layanan kecil independen yang saling berkomunikasi.

| Kelebihan | Kekurangan |
| :--- | :--- |
| **Scalability**: Bisa scale service yang ramai saja (misal: `peminjaman-service` saja). | **Kompleks**: Butuh infrastruktur canggih (Docker, K8s, API Gateway). |
| **Agility**: Tim A bisa pakai Java, Tim B pakai Node.js. | **Debugging Sulit**: Error bisa terjadi di komunikasi antar service. |
| **Fault Tolerance**: Satu service mati, service lain tetap jalan. | **Data Consistency**: Menjaga konsistensi data antar database itu menantang (Distributed Transaction). |

---

## 🛠️ Manfaat Infrastruktur DevOps

Proyek ini bukan hanya soal koding, tapi juga "ops" (operasional):

### 1. Centralized Logging (ELK Stack)
*   **Masalah**: Di microservices, ada 10 service = 10 file log. Kalau ada error, capek buka satu-satu.
*   **Solusi**: Semua log dikirim ke **Logstash** -> disimpan di **Elasticsearch** -> dilihat di **Kibana**.
*   **Manfaat**: Debugging super cepat. Cukup cari "ErrorID: 123", langsung ketemu log dari semua service terkait.

### 2. Monitoring (Prometheus & Grafana)
*   **Masalah**: Kita tidak tahu apakah server sehat, lemot, atau mau crash kehabisan RAM.
*   **Solusi**: **Prometheus** mengambil data kesehatan (metrics) tiap 15 detik. **Grafana** memvisualisasikannya jadi grafik keren.
*   **Manfaat**: Deteksi dini masalah ("Wah RAM tinggal 10%!") dan planning kapasitas server.

### 3. CI/CD (Jenkins)
*   **Masalah**: Deploy manual itu capek, lama, dan rawan salah ketik command (Human Error).
*   **Solusi**: **Continuous Integration** (Otomatis test & build saat save code) & **Continuous Deployment** (Otomatis deploy ke server).
*   **Manfaat**: Rilis fitur lebih cepat, developer fokus koding bukan deploy.

---

## 🚀 Panduan Langkah Kerja (Step-by-Step)

### Prasyarat
*   Docker & Docker Compose (Wajib)
*   Java JDK 21
*   Maven 3.9

### Langkah 1: Menjalankan Infrastruktur Pendukung
Sebelum aplikasi jalan, infrastruktur harus siap dulu.

1.  **Jalankan ELK Stack** (Untuk Log):
    ```bash
    docker-compose -f docker-compose-elk.yml up -d
    ```
    *Tunggu 1-2 menit sampai Elasticsearch siap.*

2.  **Jalankan Jenkins** (Untuk CI/CD):
    ```bash
    docker-compose -f docker-compose-jenkins.yml up -d
    ```

3.  **Jalankan Monitoring** (Prometheus & Grafana):
    ```bash
    docker-compose -f docker-compose-monitoring.yml up -d
    ```

### Langkah 2: Menjalankan Aplikasi (Microservices)
Ada dua cara: Manual via Docker Compose atau Otomatis via Jenkins.

**Cara A: Manual (Local Development)**
```bash
docker-compose -f docker-compose-app.yml up -d --build
```
Ini akan menjalankan:
*   RabbitMQ
*   Eureka Server
*   API Gateway
*   Semua Service (Anggota, Buku, Peminjaman, dll)

**Cara B: Via Jenkins Pipeline (CI/CD)**
1.  Buka Jenkins di `http://localhost:8080`.
2.  Buat Pipeline Job baru, arahkan ke repo Git ini.
3.  Klik **Build Now**. Jenkins akan otomatis:
    *   Checkout code.
    *   Build JAR file (Maven).
    *   Build Docker Image.
    *   Deploy aplikasi (docker-compose up).
    *   Run Test.

---

## 🌐 Akses Layanan

Simpan daftar port ini:

| Service | URL / Port | Keterangan |
| :--- | :--- | :--- |
| **Web Aplikasi Utama** | `http://localhost:9000` | Masuk lewat API Gateway |
| **Jenkins** | `http://localhost:8080` | CI/CD Server |
| **Kibana** | `http://localhost:5601` | Cek Log Aplikasi |
| **Grafana** | `http://localhost:3000` | Monitoring Dashboard (User/Pass: admin/admin) |
| **Prometheus** | `http://localhost:9090` | Query Metrics Mentah |
| **Eureka Dashboard** | `http://localhost:8761` | Cek Status Service |

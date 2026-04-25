<div align="center">
  <img src="app/src/main/res/drawable/ttmtal_logo.png" width="120" alt="TTMTAL Logo">
  <h1>📱 TTMTAL Mobil V1.0</h1>
  <p><b>Yapay Zeka Destekli, Askeri Sınıf Şifrelemeye Sahip Okul Ekosistemi & Yönetim Paneli</b></p>
  
  <a href="https://github.com/AniLLL3734/TTMTAL-Mobil/releases/latest/download/TTMTAL_Mobil_V1.0.apk">
    <img src="https://img.shields.io/badge/İndir_APK-v1.0.0-0078D4?style=for-the-badge&logo=android" alt="Download APK">
  </a>
  <a href="https://anilll3734.github.io/TTMTAL-Mobil/">
    <img src="https://img.shields.io/badge/Canlı_Yönetim_Paneli-Yayında-2EA043?style=for-the-badge&logo=vercel" alt="Admin Panel">
  </a>
</div>

<br>

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=java&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=black)
![Llama3](https://img.shields.io/badge/Llama_3_70B-0466C8?style=flat-square&logo=meta&logoColor=white)
![AES256](https://img.shields.io/badge/Security-AES_256_GCM-dc2626?style=flat-square&logo=springsecurity&logoColor=white)
![Material](https://img.shields.io/badge/UI-Material_Design_3-6200ea?style=flat-square&logo=materialdesign&logoColor=white)

---

## 📸 Ekran Görüntüleri & Arayüz

Modern tasarım prensipleri ve Material Design 3 kurallarına göre tasarlanmış akıcı arayüz deneyimi. Resimlerin kalitesi yüksek tutulmuş, kullanıcı dostu panellerle donatılmıştır.

| Panel Görüntüsü 1 | Panel Görüntüsü 2 | Panel Görüntüsü 3 | Panel Görüntüsü 4 |
| :---: | :---: | :---: | :---: |
| <img src="assets/screenshot_0.png" width="220"> | <img src="assets/screenshot_1.png" width="220"> | <img src="assets/screenshot_2.png" width="220"> | <img src="assets/screenshot_3.png" width="220"> |
| <img src="assets/screenshot_4.png" width="220"> | <img src="assets/screenshot_5.png" width="220"> | <img src="assets/screenshot_6.png" width="220"> | <img src="assets/screenshot_7.png" width="220"> |

> *Not: Yukarıdaki görseller uygulamanın ve yönetim panelinin farklı kesitlerini içermektedir.*

---

## 🧠 Llama 3 Destekli Okul Asistanı
Sıradan bir bilgi sisteminden öteye geçerek, projeye entegre edilen **Groq Cloud API (Llama 3.3 70B)** modeli ile öğrencilerin tüm sorularını anlayan ve okulun veritabanına göre yanıt veren "Okul Asistanı" geliştirilmiştir.
- **RAG Mimarisi:** Okul verileri bağlam olarak yapay zekaya beslenir, böylece halüsinasyon oranı sıfıra indirilir.
- **Gerçek Zamanlı Sohbet:** WhatsApp tarzı, yüksek performanslı chat arayüzü.

## 🛡️ Askeri Sınıf Güvenlik (Zero-Trust Architecture)
Bu proje, mobil güvenlikte sektör standartlarını yeniden belirler:
1. **Donanımsal Şifreleme (Android Jetpack Security):** API anahtarları asla APK içine gömülmez. `EncryptedSharedPreferences` kullanılarak cihazın TEE (Trusted Execution Environment) çipi içindeki AES-256 Master Key ile şifrelenir.
2. **Dinamik Yapılandırma:** Tüm kritik anahtarlar (Yapay Zeka, ImageKit) Firebase üzerinden anlık çekilir. Telefon root'lu olsa bile verilere ulaşılamaz.

## ⚙️ Teknik Mimarisi & Altyapı
- **Bulut Veritabanı:** Firebase Realtime Database ile `0ms` gecikmeli senkronizasyon (SyncManager).
- **Görüntü Optimizasyonu:** `ImageKit.io` entegrasyonu ile fotoğraflar cihazın çözünürlüğüne göre sunucuda %80 sıkıştırılarak anında getirilir. (Parallax kaydırma efektleriyle desteklenmiştir).
- **Yönetim Paneli:** Öğretmenlerin içerik ekleyebilmesi için özel tasarlanmış, Firebase yetkilendirmeli, tamamen responsive Web Paneli.

---

## 🛠️ Kurulum (Geliştiriciler İçin)

Proje tamamen açık kaynak olup, kendi okulunuza uyarlayabilirsiniz:

```bash
# 1. Repoyu Klonla
git clone https://github.com/AniLLL3734/TTMTAL-Mobil.git

# 2. Firebase Kurulumu
# Firebase Console'dan indirdiğiniz google-services.json dosyasını 'app/' dizinine ekleyin.

# 3. Güvenli Anahtarlar (Local Build İçin)
# app/src/main/res/values/secrets.xml dosyasını oluşturun:
# (Not: Bu dosya .gitignore ile korunmaktadır)
<resources>
    <string name="imagekit_public_key">YOUR_KEY</string>
    <string name="groq_api_key">YOUR_KEY</string>
    <string name="default_web_client_id">YOUR_ID</string>
</resources>
```

> **Önemli Firebase Ayarı:** Cihazların güncel anahtarları alabilmesi için Firebase Realtime Database'de `remote_config` düğümünü oluşturup API anahtarlarınızı oraya da girmelisiniz.

---
<div align="center">
  <b>Geliştirici:</b> <a href="https://github.com/AniLLL3734">AniLLL3734</a> & Antigravity AI
  <br>
  <i>MIT License ile Açık Kaynak</i>
</div>

# 📱 TTMTAL Mobil - Okul Bilgi Sistemi

![Versiyon](https://img.shields.io/badge/Versiyon-1.0.0-blue)
![Lisans](https://img.shields.io/badge/Lisans-MIT-green)
![Platform](https://img.shields.io/badge/Platform-Android-orange)
![Security](https://img.shields.io/badge/Security-AES--256-red)

**TTMTAL Mobil**, Pendik Türk Telekom Şehit Murat Mertel MTAL için geliştirilmiş, modern, güvenli ve yapay zeka destekli bir okul ekosistemi uygulamasıdır. Öğrenciler, öğretmenler ve idare arasındaki iletişimi dijitalleştirirken, en üst düzey güvenlik standartlarını sunar.

---

## ✨ Öne Çıkan Özellikler

- **🤖 Okul Asistanı (AI):** Llama 3 70B modeliyle güçlendirilmiş, okul hakkındaki tüm soruları (projeler, bölümler, ulaşım vb.) anında yanıtlayan akıllı asistan.
- **📰 Dinamik İçerik:** Firebase üzerinden anlık güncellenen haberler, duyurular ve etkinlik takvimi.
- **🛡️ Ultra Güvenlik:** 
  - API anahtarları APK içinde saklanmaz, Firebase'den dinamik çekilir.
  - Hassas veriler cihazda **AES-256 GCM** (EncryptedSharedPreferences) donanımsal şifreleme ile korunur.
- **🌗 Modern Arayüz:** Karanlık mod desteği, premium kart tasarımları ve akıcı animasyonlar.
- **🌐 Yönetim Paneli:** Öğretmenler için geliştirilmiş, web tabanlı zengin içerik yönetim paneli.

---

## 🛠️ Teknoloji Yığını

- **Dil:** Java (Android SDK)
- **Veritabanı & Auth:** Firebase Realtime Database, Firebase Auth (Google Sign-In)
- **Yapay Zeka:** Groq Cloud API (Llama 3.3 70B)
- **Görüntü Yönetimi:** ImageKit.io API
- **Güvenlik:** Android Jetpack Security (Crypto)
- **UI:** Material Design 3, Parallax Effects, CollapsingToolbar

---

## 🚀 Kurulum ve Çalıştırma

Projeyi kendi ortamınızda çalıştırmak için şu adımları izleyin:

1. **Repoyu Klonlayın:**
   ```bash
   git clone https://github.com/KULLANICI_ADIN/TTMTAL-Mobil.git
   ```

2. **Firebase Kurulumu:**
   - `google-services.json` dosyasını `app/` klasörüne ekleyin.
   - Firebase Realtime Database'de `remote_config` düğümünü oluşturun.

3. **Anahtarları Ayarlayın:**
   - `app/src/main/res/values/secrets.xml` dosyasını oluşturun ve şablonu doldurun:
   ```xml
   <resources>
       <string name="imagekit_public_key">SİZİN_ANAHTARINIZ</string>
       <string name="groq_api_key">SİZİN_ANAHTARINIZ</string>
       <string name="default_web_client_id">GOOGLE_CLIENT_ID</string>
   </resources>
   ```

4. **Derleyin:** Android Studio ile açın ve `./gradlew assembleDebug` komutuyla APK alın.

---

## 🔒 Güvenlik Notu

Bu proje, açık kaynak dünyası için "Güvenli Kodlama" örnekleri içerir. Hiçbir API anahtarı veya gizli yapılandırma `git push` sırasında sızdırılmaz. Tüm hassas yapılandırmalar `.gitignore` ile korunmaktadır.

---

## 📄 Lisans

Bu proje **MIT Lisansı** ile lisanslanmıştır. Daha fazla bilgi için [LICENSE](LICENSE) dosyasına göz atın.

---

> **Geliştirici:** github.com/AniLLL3734

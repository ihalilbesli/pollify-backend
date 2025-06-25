# 🗳️ Pollify - Backend

Pollify, kullanıcıların kolayca anket oluşturabileceği, soru ve seçenekler ekleyebileceği, diğer kullanıcıların bu anketlere oy verebileceği ve sonuçların yapay zeka desteğiyle analiz edilebildiği bir anket platformudur. Dileyen kullanıcılar giriş yapmadan da anketlere oy verebilir, bu durum anketin ayarlarına göre belirlenir.

Backend tarafı, JWT tabanlı kimlik doğrulama, rol bazlı erişim kontrolü, anket yönetimi, oylama sistemi, giriş loglama ve AI destekli sonuç analizi gibi birçok özelliği içermektedir.

## 👥 Kullanıcı Rollerine Göre Özellikler

### 👤 Kayıtlı Kullanıcı (ROLE_USER)
- Giriş yapabilir, hesabını yönetebilir.
- Yeni anket oluşturabilir.
- Anketlere soru ve seçenek ekleyebilir.
- Anketleri güncelleyebilir veya silebilir (sadece kendi oluşturduğu anketlerde).
- Diğer kullanıcıların anketlerine oy verebilir.
- Oy sonuçlarını görebilir.

### 🛡️ Admin (ROLE_ADMIN)
- Tüm kullanıcıları ve anketleri görüntüleyebilir.
- Gerekirse kullanıcı veya anket silebilir.
- Giriş loglarını inceleyebilir.
- Anket sonuçlarını yapay zekaya göndererek analiz ettirebilir.

 ## 🔐 Özellikler

- Kullanıcı kayıt ve giriş sistemi (JWT ile kimlik doğrulama)
- Rol tabanlı yetkilendirme (Kullanıcı, Admin)
- Anket oluşturma, güncelleme ve silme
- Soru ve seçenek yönetimi
- Oy kullanma ve oylama sonuçlarının gösterimi
- Giriş yapmadan oylama desteği (opsiyonel)
- AI destekli anket sonucu analizi (OpenAI API ile)
- Admin paneli ile kullanıcı ve anket yönetimi
- Giriş işlemlerinin loglanması (başarılı ve hatalı denemeler)

 ## 🧰 Kullanılan Teknolojiler

<p align="left">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="40" alt="Java" title="Java"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="40" alt="Spring Boot" title="Spring Boot"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" width="40" alt="MySQL" title="MySQL"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/git/git-original.svg" width="40" alt="Git" title="Git"/>
</p>

- **Java 17** – Backend programlama dili
- **Spring Boot** – Uygulama iskeleti ve API oluşturma
- **Spring Security** – JWT tabanlı kimlik doğrulama ve rol kontrolü
- **Spring Data JPA & Hibernate** – ORM ve veritabanı işlemleri
- **MySQL** – İlişkisel veritabanı yönetimi
- **OpenAI API** – Anket sonuçlarını analiz eden yapay zeka servisi
- **Lombok** – Kod tekrarını azaltmak için getter/setter otomasyonu
- **ModelMapper** – DTO ve Entity dönüşümleri için

## 🚀 Kurulum ve Çalıştırma

1. Repoyu klonlayın:

git clone https://github.com/kullanici-adin/pollify-backend.git
cd pollify-backend

2. Veritabanı oluşturun:

MySQL'de `pollify` adında bir veritabanı oluşturun.

CREATE DATABASE pollify;

3. application.properties dosyasını yapılandırın:

src/main/resources/application.properties içeriğini aşağıdaki gibi düzenleyin:

spring.datasource.url=jdbc:mysql://localhost:3306/pollify
spring.datasource.username=root
spring.datasource.password=şifreniz

# OpenAI API Key (analiz özelliği için)
openai.api.key=YOUR_API_KEY

4. Projeyi çalıştırın:

IDE (IntelliJ, Eclipse vs.) üzerinden PollifyApplication.java sınıfını çalıştırarak sunucuyu başlatın.

5. API testleri:

API'yi test etmek için Postman veya benzeri bir araçla şu istekleri kullanabilirsiniz:

POST /auth/register → Kullanıcı kaydı  
POST /auth/login    → Giriş ve JWT alma  
GET  /polls         → Anketleri listeleme  
POST /polls         → Anket oluşturma (JWT gerekli)


<img src="https://github.com/user-attachments/assets/b45b2483-9313-4a41-829d-d6a5f908502e" width="400">

## 📁 Proje Yapısı

Aşağıda, `com.pollify.pollify` altında yer alan backend katmanlarının yapısı ve görevleri özetlenmiştir:
```
com.pollify.pollify  
├── aspect              → Logging, işlem takibi gibi AOP işlemleri burada tanımlanır  
├── config              → JWT, CORS ve güvenlik yapılandırmaları (Spring Security)  
├── controller          → REST API endpoint'lerinin yer aldığı controller sınıfları  
├── dto                 → Veri transfer nesneleri (istek ve yanıt modelleri)  
├── exception           → Hata fırlatma ve global exception handler sınıfları  
├── filter              → JWT doğrulama filtreleri ve request filtreleme işlemleri  
├── model               → Veritabanı tablolarını temsil eden JPA entity sınıfları  
├── repository          → Veritabanı sorgularını yöneten arayüzler (Spring Data JPA)  
├── servis              → İş mantığını barındıran servis arayüzleri ve implementasyonları  
├── util                → Yardımcı sınıflar (örneğin: `JwtUtil`, `SecurityUtil`)  
└── PollifyApplication → Spring Boot uygulamasının başlangıç noktası (`main` metodu)
```


Her türlü soru ve geri bildirim için iletişime geçebilirsiniz.  
📧 **E-posta:** ihalilbesli@gmail.com
🔗 **LinkedIn:** [linkedin.com/in/ibrahim-halil-beşli-3079ab223](https://www.linkedin.com/in/ibrahim-halil-be%C5%9Fli-3079ab223/)



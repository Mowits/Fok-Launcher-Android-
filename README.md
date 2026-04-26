7# Fok Launcher Android

Fok Launcher, Android icin Pojav tabanli, optimize edilmis bir Minecraft Java launcher fork'udur.

## Dahil olanlar

- Turkce arayuz
- Optimize edilmis FOK akisi ve sade launcher arayuzu
- Offline local login GUI
- Profile bazli server preset
- `--quickPlayMultiplayer` ile hizli sunucuya katilim
- AuthMe tipi gizli `/register` ve `/login` otomasyonu
- RAM'in `%40`'ini otomatik ayirma
- FOK branding, ikonlar ve sade arayuz

## Build

```bash
./gradlew :app_pojavlauncher:assembleDebug
```

APK yolu:

```text
app_pojavlauncher/build/outputs/apk/debug/app_pojavlauncher-debug.apk
```

## Not

Bu repo, orijinal Pojav Android kaynaklarinin FOK odakli fork'udur.

## Lisans

- Upstream Pojav ve tasinan dosyalar kendi mevcut lisanslariyla kalir
- FOK'a ozel eklenen ozgun katman ve yeni dosyalar icin Apache-2.0 metni:
  `LICENSE-FOK-APACHE`
- Upstream lisans dosyasi:
  `LICENSE`

⚠️ DİKKAT – Google’ın 2026 Kuralı
Bu launcher Google’a kayıtlı değil. Eylül 2026’dan itibaren APK kurulumu zorlaşacak.
Kurmak için advanced flow (24 saat bekleme + uyarılar) gerekebilir.
Keep Android Open’ı destekliyoruz → https://keepandroidopen.org/tr/
Kardeşlerim, direnelim!

⚠️ WARNING – Google’s 2026 Rule

This launcher is **NOT registered** with Google.

Starting September 2026 (first in some countries, then worldwide in 2027), installing APKs from unverified developers will become much harder.

To install it, users may need to go through the "Advanced Flow":
- Enable Developer Options
- Accept multiple scary warning screens
- Restart the phone
- **Wait 24 hours**
- Confirm again with PIN/biometrics

This is designed to be very discouraging. Most normal users will probably give up.

We support **Keep Android Open** → https://keepandroidopen.org/

Brothers, let’s resist!  
Don’t let Google turn Android into a closed garden.

If you manage to install it, enjoy the launcher and tell your friends to fight back too.

# Fok Launcher Android

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

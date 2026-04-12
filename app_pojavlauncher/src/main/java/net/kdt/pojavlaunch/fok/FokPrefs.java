package net.kdt.pojavlaunch.fok;

import android.content.SharedPreferences;

import net.kdt.pojavlaunch.prefs.LauncherPreferences;

public final class FokPrefs {
    private static final String KEY_USERNAME = "fok_username";
    private static final String KEY_PASSWORD = "fok_password";
    private static final String KEY_REGISTERED = "fok_registered";
    private static final String KEY_SELECTED_SERVER = "fok_selected_server";

    private static final String KEY_SERVER1_NAME = "fok_server1_name";
    private static final String KEY_SERVER1_ADDR = "fok_server1_addr";
    private static final String KEY_SERVER1_VOICE = "fok_server1_voice";
    private static final String KEY_SERVER2_NAME = "fok_server2_name";
    private static final String KEY_SERVER2_ADDR = "fok_server2_addr";
    private static final String KEY_SERVER2_VOICE = "fok_server2_voice";

    private FokPrefs() {}

    private static SharedPreferences prefs() {
        return LauncherPreferences.DEFAULT_PREF;
    }

    public static void setUsername(String username) {
        prefs().edit().putString(KEY_USERNAME, username).apply();
    }

    public static String getUsername() {
        return prefs().getString(KEY_USERNAME, "");
    }

    public static void setPassword(String password) {
        prefs().edit().putString(KEY_PASSWORD, password).apply();
    }

    public static String getPassword() {
        return prefs().getString(KEY_PASSWORD, "");
    }

    public static void setRegistered(boolean registered) {
        prefs().edit().putBoolean(KEY_REGISTERED, registered).apply();
    }

    public static boolean isRegistered() {
        return prefs().getBoolean(KEY_REGISTERED, false);
    }

    public static void setSelectedServerIndex(int index) {
        prefs().edit().putInt(KEY_SELECTED_SERVER, index).apply();
    }

    public static int getSelectedServerIndex() {
        return prefs().getInt(KEY_SELECTED_SERVER, 0);
    }

    public static FokServerPreset getServerPreset(int index) {
        if (index <= 0) {
            return new FokServerPreset(
                    prefs().getString(KEY_SERVER1_NAME, "ip"),
                    prefs().getString(KEY_SERVER1_ADDR, "class-minutes.gl.joinmc.link:25565"),
                    prefs().getString(KEY_SERVER1_VOICE, "")
            );
        }
        return new FokServerPreset(
                prefs().getString(KEY_SERVER2_NAME, "Sunucu 2"),
                prefs().getString(KEY_SERVER2_ADDR, ""),
                prefs().getString(KEY_SERVER2_VOICE, "")
        );
    }

    public static void setServerPreset(int index, FokServerPreset preset) {
        if (index <= 0) {
            prefs().edit()
                    .putString(KEY_SERVER1_NAME, preset.getName())
                    .putString(KEY_SERVER1_ADDR, preset.getAddress())
                    .putString(KEY_SERVER1_VOICE, preset.getVoiceRoom())
                    .apply();
            return;
        }
        prefs().edit()
                .putString(KEY_SERVER2_NAME, preset.getName())
                .putString(KEY_SERVER2_ADDR, preset.getAddress())
                .putString(KEY_SERVER2_VOICE, preset.getVoiceRoom())
                .apply();
    }

    public static FokServerPreset[] getAllPresets() {
        return new FokServerPreset[] { getServerPreset(0), getServerPreset(1) };
    }
}

package net.kdt.pojavlaunch.fok;

import android.content.Context;
import android.util.Log;

import net.kdt.pojavlaunch.PojavApplication;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.modloaders.FabricVersion;
import net.kdt.pojavlaunch.modloaders.FabriclikeUtils;
import net.kdt.pojavlaunch.utils.DownloadUtils;
import net.kdt.pojavlaunch.utils.FileUtils;
import net.kdt.pojavlaunch.value.launcherprofiles.LauncherProfiles;
import net.kdt.pojavlaunch.value.launcherprofiles.MinecraftProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public final class FokSetupManager {
    private static final String TAG = "FokSetup";
    private static final String GAME_VERSION = "1.21.1";
    private static final String PROFILE_NAME = "FOK Fabric 1.21.1";
    private static final String PROFILE_ICON = "fabric";

    private FokSetupManager() {}

    public static void ensureFabricProfile(Context context, Runnable onReady) {
        PojavApplication.sExecutorService.execute(() -> {
            try {
                String profileId = ensureFabricProfileInternal();
                if (profileId != null) {
                    LauncherPreferencesCompat.setCurrentProfile(context, profileId);
                    removeLegacyVoiceChatMod(profileId);
                }
            } catch (Throwable t) {
                Log.e(TAG, "Setup failed", t);
            }
            if (onReady != null) {
                Tools.runOnUiThread(onReady);
            }
        });
    }

    private static String ensureFabricProfileInternal() throws IOException, JSONException {
        LauncherProfiles.load();
        String existingId = findProfileIdByName(PROFILE_NAME);
        if (existingId != null) return existingId;

        FabricVersion[] loaderVersions = FabriclikeUtils.FABRIC_UTILS.downloadLoaderVersions(GAME_VERSION);
        if (loaderVersions == null || loaderVersions.length == 0) {
            throw new IOException("No Fabric loader versions available");
        }
        String loaderVersion = pickLoaderVersion(loaderVersions);

        String fabricJson = DownloadUtils.downloadString(FabriclikeUtils.FABRIC_UTILS
                .createJsonDownloadUrl(GAME_VERSION, loaderVersion));
        JSONObject jsonObject = new JSONObject(fabricJson);
        String versionId = jsonObject.getString("id");

        File versionJsonDir = new File(Tools.DIR_HOME_VERSION, versionId);
        File versionJsonFile = new File(versionJsonDir, versionId + ".json");
        FileUtils.ensureDirectory(versionJsonDir);
        Tools.write(versionJsonFile.getAbsolutePath(), fabricJson);

        MinecraftProfile profile = new MinecraftProfile();
        profile.lastVersionId = versionId;
        profile.name = PROFILE_NAME;
        profile.icon = PROFILE_ICON;
        String newProfileId = LauncherProfiles.getFreeProfileKey();
        LauncherProfiles.mainProfileJson.profiles.put(newProfileId, profile);
        LauncherProfiles.write();

        return newProfileId;
    }

    private static String pickLoaderVersion(FabricVersion[] versions) {
        for (FabricVersion version : versions) {
            if (version.stable) return version.version;
        }
        return versions[0].version;
    }

    private static void removeLegacyVoiceChatMod(String profileId) {
        LauncherProfiles.load();
        MinecraftProfile profile = LauncherProfiles.mainProfileJson.profiles.get(profileId);
        if (profile == null) return;

        File modsDir = new File(Tools.getGameDirPath(profile), "mods");
        File[] voiceMods = modsDir.listFiles((dir, name) ->
                name != null && name.startsWith("voicechat-") && name.endsWith(".jar"));
        if (voiceMods == null) return;

        for (File voiceMod : voiceMods) {
            if (!voiceMod.delete()) {
                Log.w(TAG, "Failed to remove legacy voice chat mod: " + voiceMod.getAbsolutePath());
            }
        }
    }

    private static String findProfileIdByName(String name) {
        if (LauncherProfiles.mainProfileJson == null || LauncherProfiles.mainProfileJson.profiles == null) return null;
        for (String key : LauncherProfiles.mainProfileJson.profiles.keySet()) {
            MinecraftProfile profile = LauncherProfiles.mainProfileJson.profiles.get(key);
            if (profile != null && name.equals(profile.name)) return key;
        }
        return null;
    }

    private static final class LauncherPreferencesCompat {
        private LauncherPreferencesCompat() {}

        static void setCurrentProfile(Context context, String profileId) {
            net.kdt.pojavlaunch.prefs.LauncherPreferences.DEFAULT_PREF
                    .edit()
                    .putString(net.kdt.pojavlaunch.prefs.LauncherPreferences.PREF_KEY_CURRENT_PROFILE, profileId)
                    .apply();
        }
    }
}

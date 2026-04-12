package net.kdt.pojavlaunch.fragments;

import static net.kdt.pojavlaunch.Tools.openPath;
import static net.kdt.pojavlaunch.Tools.shareLog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.kdt.mcgui.mcVersionSpinner;

import net.kdt.pojavlaunch.CustomControlsActivity;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.extra.ExtraConstants;
import net.kdt.pojavlaunch.extra.ExtraCore;
import net.kdt.pojavlaunch.fok.FokPrefs;
import net.kdt.pojavlaunch.fok.FokServerPreset;
import net.kdt.pojavlaunch.fok.FokSetupManager;
import net.kdt.pojavlaunch.fok.FokVoiceHelper;
import net.kdt.pojavlaunch.prefs.LauncherPreferences;
import net.kdt.pojavlaunch.progresskeeper.ProgressKeeper;
import net.kdt.pojavlaunch.value.launcherprofiles.LauncherProfiles;
import net.kdt.pojavlaunch.value.launcherprofiles.MinecraftProfile;

import java.io.File;

public class MainMenuFragment extends Fragment {
    public static final String TAG = "MainMenuFragment";

    private mcVersionSpinner mVersionSpinner;
    private Spinner mServerSpinner;
    private TextView mMemoryText;

    public MainMenuFragment(){
        super(R.layout.fragment_launcher);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button mNewsButton = view.findViewById(R.id.news_button);
        Button mDiscordButton = view.findViewById(R.id.discord_button);
        Button mCustomControlButton = view.findViewById(R.id.custom_control_button);
        Button mInstallJarButton = view.findViewById(R.id.install_jar_button);
        Button mShareLogsButton = view.findViewById(R.id.share_logs_button);
        Button mOpenDirectoryButton = view.findViewById(R.id.open_files_button);

        ImageButton mEditProfileButton = view.findViewById(R.id.edit_profile_button);
        Button mPlayButton = view.findViewById(R.id.play_button);
        mVersionSpinner = view.findViewById(R.id.mc_version_spinner);
        mServerSpinner = view.findViewById(R.id.fok_server_spinner);
        mMemoryText = view.findViewById(R.id.fok_memory_value);
        Button mEditServerButton = view.findViewById(R.id.fok_server_edit_button);
        Button mVoiceButton = view.findViewById(R.id.fok_voice_button);

        mNewsButton.setOnClickListener(v -> Tools.openURL(requireActivity(), Tools.URL_HOME));
        mDiscordButton.setOnClickListener(v -> Tools.openURL(requireActivity(), getString(R.string.discord_invite)));
        mCustomControlButton.setOnClickListener(v -> startActivity(new Intent(requireContext(), CustomControlsActivity.class)));
        mInstallJarButton.setOnClickListener(v -> runInstallerWithConfirmation(false));
        mInstallJarButton.setOnLongClickListener(v->{
            runInstallerWithConfirmation(true);
            return true;
        });
        mEditProfileButton.setOnClickListener(v -> mVersionSpinner.openProfileEditor(requireActivity()));

        mPlayButton.setOnClickListener(v -> {
            if (!Tools.isValidString(FokPrefs.getPassword())) {
                Toast.makeText(requireContext(), R.string.fok_password_missing_text, Toast.LENGTH_LONG).show();
                return;
            }
            if (mServerSpinner == null) {
                Toast.makeText(requireContext(), R.string.fok_layout_missing, Toast.LENGTH_LONG).show();
                return;
            }
            FokPrefs.setSelectedServerIndex(mServerSpinner.getSelectedItemPosition());
            ExtraCore.setValue(ExtraConstants.LAUNCH_GAME, true);
        });

        mShareLogsButton.setOnClickListener((v) -> shareLog(requireContext()));

        mOpenDirectoryButton.setOnClickListener((v)-> {
            Tools.switchDemo(Tools.isDemoProfile(v.getContext())); // avoid switching accounts being able to access
            if(Tools.isDemoProfile(v.getContext())){
                Toast.makeText(v.getContext(), R.string.toast_not_available_demo, Toast.LENGTH_LONG).show();
                return;
            }

            openPath(v.getContext(), getCurrentProfileDirectory(), false);
        });


        mNewsButton.setOnLongClickListener((v)->{
            Tools.swapFragment(requireActivity(), GamepadMapperFragment.class, GamepadMapperFragment.TAG, null);
            return true;
        });

        setupServerSpinner();
        if (mEditServerButton != null) {
            mEditServerButton.setOnClickListener(v -> showServerEditDialog());
        }
        if (mVoiceButton != null) {
            mVoiceButton.setOnClickListener(v -> openVoiceChat());
        }
        updateMemoryText();

        FokSetupManager.ensureFabricProfile(requireContext(), () -> mVersionSpinner.reloadProfiles());
    }

    private File getCurrentProfileDirectory() {
        String currentProfile = LauncherPreferences.DEFAULT_PREF.getString(LauncherPreferences.PREF_KEY_CURRENT_PROFILE, null);
        if(!Tools.isValidString(currentProfile)) return new File(Tools.DIR_GAME_NEW);
        LauncherProfiles.load();
        MinecraftProfile profileObject = LauncherProfiles.mainProfileJson.profiles.get(currentProfile);
        if(profileObject == null) return new File(Tools.DIR_GAME_NEW);
        return Tools.getGameDirPath(profileObject);
    }

    @Override
    public void onResume() {
        super.onResume();
        mVersionSpinner.reloadProfiles();
        updateMemoryText();
    }

    private void setupServerSpinner() {
        if (mServerSpinner == null) return;
        FokServerPreset[] presets = FokPrefs.getAllPresets();
        String[] labels = new String[presets.length];
        for (int i = 0; i < presets.length; i++) {
            labels[i] = presets[i].displayLabel();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mServerSpinner.setAdapter(adapter);
        mServerSpinner.setSelection(FokPrefs.getSelectedServerIndex());
    }

    private void showServerEditDialog() {
        if (mServerSpinner == null) return;
        int index = mServerSpinner.getSelectedItemPosition();
        FokServerPreset preset = FokPrefs.getServerPreset(index);

        View dialogView = View.inflate(requireContext(), R.layout.dialog_fok_server, null);
        EditText nameInput = dialogView.findViewById(R.id.fok_server_name_input);
        EditText addrInput = dialogView.findViewById(R.id.fok_server_addr_input);
        EditText voiceInput = dialogView.findViewById(R.id.fok_server_voice_input);
        nameInput.setText(preset.getName());
        addrInput.setText(preset.getAddress());
        voiceInput.setText(preset.getVoiceRoom());

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.fok_server_edit_title)
                .setView(dialogView)
                .setPositiveButton(R.string.fok_save, (d, w) -> {
                    String name = nameInput.getText().toString().trim();
                    String addr = addrInput.getText().toString().trim();
                    String voiceRoom = voiceInput.getText().toString().trim();
                    if (name.isEmpty()) name = getString(R.string.fok_server_default_name, index + 1);
                    FokPrefs.setServerPreset(index, new FokServerPreset(name, addr, voiceRoom));
                    setupServerSpinner();
                    mServerSpinner.setSelection(index);
                })
                .setNegativeButton(R.string.fok_cancel, null)
                .show();
    }

    private void updateMemoryText() {
        if (mMemoryText == null) return;
        int deviceRam = Tools.getTotalDeviceMemory(requireContext());
        int allocation = (int) Math.floor(deviceRam * 0.4f);
        if (allocation < 512) allocation = 512;
        mMemoryText.setText(getString(R.string.fok_memory_format, allocation, deviceRam));
    }

    private void runInstallerWithConfirmation(boolean isCustomArgs) {
        // avoid using custom installers to install a version
        if(Tools.isLocalProfile(requireContext()) || Tools.isDemoProfile(requireContext())){
            Toast.makeText(requireContext(), R.string.toast_not_available_demo, Toast.LENGTH_LONG).show();
            return;
        }

        if (ProgressKeeper.getTaskCount() == 0)
            Tools.installMod(requireActivity(), isCustomArgs);
        else
            Toast.makeText(requireContext(), R.string.tasks_ongoing, Toast.LENGTH_LONG).show();
    }

    private void openVoiceChat() {
        int selectedIndex = mServerSpinner == null ? FokPrefs.getSelectedServerIndex() : mServerSpinner.getSelectedItemPosition();
        FokPrefs.setSelectedServerIndex(selectedIndex);
        FokServerPreset preset = FokPrefs.getServerPreset(selectedIndex);
        String roomName = FokVoiceHelper.resolveRoomName(preset);
        Toast.makeText(requireContext(), getString(R.string.fok_voice_opening, roomName), Toast.LENGTH_LONG).show();
        Tools.openURL(requireActivity(), FokVoiceHelper.buildMeetingUrl(preset));
    }
}

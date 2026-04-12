package net.kdt.pojavlaunch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.extra.ExtraConstants;
import net.kdt.pojavlaunch.extra.ExtraCore;
import net.kdt.pojavlaunch.fok.FokPrefs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalLoginFragment extends Fragment {
    public static final String TAG = "LOCAL_LOGIN_FRAGMENT";

    private final Pattern mUsernameValidationPattern;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    public LocalLoginFragment(){
        super(R.layout.fragment_local_login);
        mUsernameValidationPattern = Pattern.compile("^[a-zA-Z0-9_]*$");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mUsernameEditText = view.findViewById(R.id.login_edit_email);
        mPasswordEditText = view.findViewById(R.id.login_edit_password);
        mUsernameEditText.setText(FokPrefs.getUsername());
        mPasswordEditText.setText(FokPrefs.getPassword());
        view.findViewById(R.id.login_button).setOnClickListener(v -> {
            if(!checkEditText()) {
                Context context = v.getContext();
                Tools.dialog(context, context.getString(R.string.local_login_bad_username_title), context.getString(R.string.local_login_bad_username_text));
                return;
            }
            String password = mPasswordEditText.getText().toString();
            if (password.isEmpty()) {
                Context context = v.getContext();
                Tools.dialog(context, context.getString(R.string.fok_password_missing_title),
                        context.getString(R.string.fok_password_missing_text));
                return;
            }

            String username = mUsernameEditText.getText().toString();
            if (!username.equals(FokPrefs.getUsername())) {
                FokPrefs.setRegistered(false);
            }
            FokPrefs.setUsername(username);
            FokPrefs.setPassword(password);

            ExtraCore.setValue(ExtraConstants.MOJANG_LOGIN_TODO, new String[]{
                    username, "" });

            Tools.swapFragment(requireActivity(), MainMenuFragment.class, MainMenuFragment.TAG, null);
        });
    }


    /** @return Whether the mail (and password) text are eligible to make an auth request  */
    private boolean checkEditText(){

        String text = mUsernameEditText.getText().toString();

        Matcher matcher = mUsernameValidationPattern.matcher(text);
        return !(text.isEmpty()
                || text.length() < 3
                || text.length() > 16
                || !matcher.find()
        );
    }
}

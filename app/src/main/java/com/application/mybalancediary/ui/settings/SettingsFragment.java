package com.application.mybalancediary.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.mybalancediary.ChangePasswordActivity;
import com.application.mybalancediary.R;
import com.application.mybalancediary.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final TextView changePassword = (TextView) root.findViewById(R.id.change_password);
        final TextView logOut = (TextView) root.findViewById(R.id.logout);
        final TextView deleteAccount = (TextView) root.findViewById(R.id.delete_account);
        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}
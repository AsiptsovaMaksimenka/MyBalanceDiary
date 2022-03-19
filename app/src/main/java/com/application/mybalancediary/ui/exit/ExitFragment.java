package com.application.mybalancediary.ui.exit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.mybalancediary.LoginActivity;
import com.application.mybalancediary.RegisterActivity;
import com.application.mybalancediary.databinding.FragmentExitBinding;

public class ExitFragment extends Fragment {

    private FragmentExitBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExitViewModel exitViewModel =
                new ViewModelProvider(this).get(ExitViewModel.class);

        binding = FragmentExitBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textExit;
        exitViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
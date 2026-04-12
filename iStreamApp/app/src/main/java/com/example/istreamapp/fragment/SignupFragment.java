package com.example.istreamapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.istreamapp.R;
import com.example.istreamapp.database.AppDatabase;
import com.example.istreamapp.database.User;

public class SignupFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppDatabase db = AppDatabase.getDatabase(requireContext());

        EditText etFullName = view.findViewById(R.id.etFullName);
        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);
        EditText etConfirm  = view.findViewById(R.id.etConfirmPassword);
        Button btnRegister  = view.findViewById(R.id.btnRegister);
        TextView tvLogin    = view.findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm  = etConfirm.getText().toString().trim();

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirm)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                User existing = db.userDao().findByUsername(username);
                requireActivity().runOnUiThread(() -> {
                    if (existing != null) {
                        Toast.makeText(getContext(), "Username already taken, please choose another",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(() -> {
                            db.userDao().register(new User(fullName, username, password));
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Account created! Please log in.", Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(this).navigate(R.id.action_signup_to_login);
                            });
                        }).start();
                    }
                });
            }).start();
        });

        tvLogin.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
    }
}
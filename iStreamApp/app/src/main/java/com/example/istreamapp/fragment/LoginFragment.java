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
import com.example.istreamapp.session.SessionManager;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppDatabase db = AppDatabase.getDatabase(requireContext());
        SessionManager session = new SessionManager(requireContext());

        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);
        Button btnLogin     = view.findViewById(R.id.btnLogin);
        TextView tvSignUp   = view.findViewById(R.id.tvSignUp);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                User user = db.userDao().login(username, password);
                requireActivity().runOnUiThread(() -> {
                    if (user != null) {
                        session.saveUser(user.id, user.username, user.fullName);
                        NavHostFragment.findNavController(this).navigate(R.id.action_login_to_home);
                    } else {
                        Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        tvSignUp.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_login_to_signup));
    }
}
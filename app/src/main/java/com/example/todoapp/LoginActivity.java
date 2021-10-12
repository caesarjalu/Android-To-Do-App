package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todoapp.Model.UserModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.example.todoapp.Utils.Preferences;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);

        bindView();
    }

    private void bindView() {
        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
    }

    public void clickRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void clickLogin(View view) {
        if (loginAuth()) {
            startActivity(new Intent(getBaseContext(),MainActivity.class));
            finish();
        }
    }

    private boolean loginAuth() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (username.isEmpty()) {
            etUsername.setError("Fill out the username!");
            return false;
        } else if (password.isEmpty()) {
            etPassword.setError("Fill out the password!");
            return false;
        } else {
            db.openDatabase();
            UserModel user = db.getUser(username);
            if (user == null) {
                etUsername.setError("Invalid Username!");
                return false;
            } else {
                if (password.equals(user.getPassword())) {
                    Preferences.setLoginStatus(getBaseContext(), true);
                    Preferences.setLoggedInUserId(getBaseContext(), user.getId());
                    Preferences.setLoggedInUser(getBaseContext(), username);
                    Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    etPassword.setError("Wrong Password!");
                    return false;
                }
            }
        }
    }

    public void clickUsername(View view) {
        etUsername.setError(null);
    }

    public void clickPassword(View view) {
        etPassword.setError(null);
    }
}
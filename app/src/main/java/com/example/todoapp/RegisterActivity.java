package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todoapp.Model.UserModel;
import com.example.todoapp.Utils.DatabaseHandler;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etRePassword;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);

        bindView();
    }

    private void bindView() {
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        etRePassword = findViewById(R.id.etRegisterRePassword);
    }

    public void clickRegister(View view) {
        if (registerAuth()) {
            String username = etUsername.getText().toString();
            Toast.makeText(RegisterActivity.this, "User " + username + " successfully registered!", Toast.LENGTH_LONG).show();
            super.onBackPressed();
        }
    }

    private boolean registerAuth() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String rePassword = etRePassword.getText().toString();
        if (username.isEmpty()) {
            etUsername.setError("Fill out the username!");
            return false;
        } else if (password.isEmpty()) {
            etPassword.setError("Fill out the password!");
            return false;
        } else if (rePassword.isEmpty()) {
            etRePassword.setError("Fill out the password!");
            return false;
        } else {
            if (password.equals(rePassword)) {
                db.openDatabase();
                if (db.getUser(username) == null) {
                    UserModel user = new UserModel();
                    user.setUsername(username);
                    user.setPassword(password);
                    db.insertUser(user);
                    return true;
                } else {
                    etUsername.setError("Username already taken!");
                    return false;
                }
            } else {
                etRePassword.setError("Password does not match!");
                return false;
            }
        }
    }

    public void clickUsername(View view) {
        etUsername.setError(null);
    }

    public void clickPassword(View view) {
        etPassword.setError(null);
    }

    public void clickRePassword(View view) {
        etRePassword.setError(null);
    }
}
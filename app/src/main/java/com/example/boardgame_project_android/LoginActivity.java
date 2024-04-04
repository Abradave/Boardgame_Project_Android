package com.example.boardgame_project_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText txtLoginEmail;
    private EditText txtLoginPassword;
    private AppCompatButton btnLoginLogin;
    private AppCompatButton btnLoginBack;
    private final String url = "http://10.0.2.2:8000/api/guest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        btnLoginLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(txtLoginEmail.getText()).toString();
            String password = Objects.requireNonNull(txtLoginPassword.getText()).toString();
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this, "All data is mandatory", Toast.LENGTH_SHORT).show();
            }else {
                RequestTask task = new RequestTask(url, "GET", email);
                task.execute();
            }
        });

        btnLoginBack.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void init(){
        txtLoginEmail = findViewById(R.id.txtLoginEmail);
        txtLoginPassword = findViewById(R.id.txtLoginPassword);
        btnLoginLogin = findViewById(R.id.btnLoginLogin);
        btnLoginBack = findViewById(R.id.btnLoginBack);
    }
    private class RequestTask extends AsyncTask<Void,Void,Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        public RequestTask(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }
        @Override
        protected Response doInBackground(Void... voids){
            Response response = null;
            try {
                if (requestType.equals("GET")) {
                    response = RequestHandler.get(requestUrl + "?g_email=" + requestParams);
                }
            } catch (IOException e){
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response){
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400){
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.d("onPostExecuteError", response.getContent());
            }
            if (requestType.equals("GET")){
                Users[] usersArray = converter.fromJson(response.getContent(),Users[].class);
                if (usersArray.length > 0){
                    Toast.makeText(LoginActivity.this,"Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, LoggedInActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this,"Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
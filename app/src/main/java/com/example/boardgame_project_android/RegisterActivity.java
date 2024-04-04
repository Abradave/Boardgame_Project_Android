package com.example.boardgame_project_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Date;
import java.util.FormatFlagsConversionMismatchException;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtRegisterEmial;
    private EditText txtRegisterPassword;
    private EditText txtRegisterPasswordAgain;
    private EditText txtRegisterUsername;
    private EditText txtRegisterFullname;
    private EditText txtRegisterPhone;
    private AppCompatButton btnRegisterRegister;
    private AppCompatButton btnRegisterBack;
    private String url = "http://10.0.2.2:8000/api/guest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();


        btnRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtRegisterEmial.getText().toString();
                String password = txtRegisterPassword.getText().toString();
                String passwordAgain = txtRegisterPasswordAgain.getText().toString();
                String username = txtRegisterUsername.getText().toString();
                String fullname = txtRegisterFullname.getText().toString();
                String phone = txtRegisterPhone.getText().toString();

                if (email.isEmpty() || password.isEmpty()|| passwordAgain.isEmpty() || username.isEmpty() || fullname.isEmpty() || phone.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "All data is required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Users user = new Users(0,username, password, fullname, email, phone);
                Gson jsonConverter = new Gson();
                RequestTask task = new RequestTask(url, "POST", jsonConverter.toJson(user));
                task.execute();

            }
        });

        btnRegisterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void init(){
        txtRegisterEmial = findViewById(R.id.txtRegisterEmail);
        txtRegisterPassword = findViewById(R.id.txtRegisterPassword);
        txtRegisterPasswordAgain = findViewById(R.id.txtRegisterPasswordAgain);
        txtRegisterUsername = findViewById(R.id.txtRegisterUsername);
        txtRegisterFullname = findViewById(R.id.txtRegisterFullname);
        txtRegisterPhone = findViewById(R.id.txtRegisterPhone);
        btnRegisterRegister = findViewById(R.id.btnRegisterRegister);
        btnRegisterBack = findViewById(R.id.btnRegisterBack);
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
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                switch (requestType) {
                    case "POST":
                        response = RequestHandler.post(requestUrl, requestParams);
                        break;
                }
            } catch (IOException e) {
                Toast.makeText(RegisterActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }
        protected void onPostExecute(Response response){
            if (response.getResponseCode() >= 400){
                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
                return;
            }
            if (requestType.equals("POST")){
                Toast.makeText(RegisterActivity.this, "Success",  Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
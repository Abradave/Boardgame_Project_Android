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
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    //Változók deklarálása.
    private TextInputLayout txtRegisterEmial, txtRegisterPassword, txtRegisterUsername, txtRegisterFullname, txtRegisterPhone;
    private AppCompatButton btnRegisterBack, btnRegisterRegister;
    private String url = "http://10.0.2.2:8000/api/guestregister";

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
        //Függvények meghívása.
        init();


        //Gomb kattintási esemény a felhasználó adatbázisban való regisztrálásához.
        btnRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtRegisterEmial.getEditText().getText().toString();
                String password = txtRegisterPassword.getEditText().getText().toString();
                String username = txtRegisterUsername.getEditText().getText().toString();
                String fullname = txtRegisterFullname.getEditText().getText().toString();
                String phone = txtRegisterPhone.getEditText().getText().toString();

                if (email.isEmpty() || password.isEmpty()|| username.isEmpty() || fullname.isEmpty() || phone.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "All data is required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Users user = new Users(0,username, password, fullname, email, phone);
                Gson jsonConverter = new Gson();
                RequestTask task = new RequestTask(url, "POST", jsonConverter.toJson(user));
                task.execute();

            }
        });

        //Gomb kattintási esemény a Back Gomb visszalépéséhez.
        btnRegisterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Változók inicializálása.
    public void init(){
        txtRegisterEmial = findViewById(R.id.txtRegisterEmail);
        txtRegisterPassword = findViewById(R.id.txtRegisterPassword);
        txtRegisterUsername = findViewById(R.id.txtRegisterUsername);
        txtRegisterFullname = findViewById(R.id.txtRegisterFullname);
        txtRegisterPhone = findViewById(R.id.txtRegisterPhone);
        btnRegisterRegister = findViewById(R.id.btnRegisterRegister);
        btnRegisterBack = findViewById(R.id.btnRegisterBack);
    }

    //Request Task osztály létrehozása 3 változóval.
    private class RequestTask extends AsyncTask<Void,Void,Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        //Request Task osztály konstruktora 3 változóra.
        public RequestTask(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }

        //API csatlakozás lehetőségek megadása.
        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                if (requestType.equals("POST")) {
                    response = RequestHandler.post(requestUrl, requestParams);
                }
            } catch (IOException e) {
                Toast.makeText(RegisterActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }
        //API csatlakozás végrehajtása.
        protected void onPostExecute(Response response){
            if (response.getResponseCode() >= 400){
                if (response.getResponseCode() == 422 && response.getContent().contains("email")){
                    Toast.makeText(RegisterActivity.this, "This email has been registered already!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (response.getContent().contains("username")){
                    Toast.makeText(RegisterActivity.this, "This username is taken!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                return;
            }
            if (requestType.equals("POST")){
                Toast.makeText(RegisterActivity.this, "Successful Registration",  Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
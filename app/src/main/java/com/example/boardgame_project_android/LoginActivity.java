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

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
//Változók deklarálása.
public class LoginActivity extends AppCompatActivity {
    private TextInputLayout txtLoginEmail, txtLoginPassword;
    private AppCompatButton btnLoginLogin, btnLoginBack;
    private final String url = "http://10.0.2.2:8000/api/guestlogin";

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
        //Függvények meghívása.
        init();
        //Gomb kattintási esemény a bejelentkezéshez.
        btnLoginLogin.setOnClickListener(v -> {
            //Helyi változók deklarálása és inicializálása.
            String email = Objects.requireNonNull(txtLoginEmail.getEditText().getText()).toString();
            String password = Objects.requireNonNull(txtLoginPassword.getEditText().getText()).toString();
            if (email.isEmpty() || password.isEmpty()){
                //Ha nem adja meg vagy nem jól akkor nem tud továbblépni.
                Toast.makeText(LoginActivity.this, "All data is mandatory", Toast.LENGTH_SHORT).show();
            }else {
                //Ha helyesen adta meg akkor belép az érvényes felhasználóval.
                Users user = new Users(email, password);
                Gson jsonConverter = new Gson();
                RequestTask task = new RequestTask(url, "POST", jsonConverter.toJson(user));
                task.execute();
            }
        });
        //Gomb kattintási esemény a Back Gomb visszalépéséhez.
        btnLoginBack.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    //Változók Inicializálása.
    public void init(){
        txtLoginEmail = findViewById(R.id.txtLoginEmail);
        txtLoginPassword = findViewById(R.id.txtLoginPassword);
        btnLoginLogin = findViewById(R.id.btnLoginLogin);
        btnLoginBack = findViewById(R.id.btnLoginBack);
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
                Toast.makeText(LoginActivity.this,e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }
        //API csatlakozás végrehajtása illetve figyelmeztetés ha nem jönne létre a csatlakozás.
        protected void onPostExecute(Response response){
            if (response.getResponseCode() >= 400){
                Toast.makeText(LoginActivity.this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                return;
            }
            if (requestType.equals("POST")){
                String all = response.getContent().toString();
                String[] idList = all.split(",");
                ActualUser.id =  Integer.valueOf(idList[0].substring(6));
                Toast.makeText(LoginActivity.this,"Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, LoggedInActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
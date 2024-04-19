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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyProfilActivity extends AppCompatActivity {
    private EditText txtProfilePassword;
    private EditText txtProfileUsername;
    private EditText txtProfileFullname;
    private EditText txtProfilePhone;
    private AppCompatButton btnProfileBack;
    private AppCompatButton btnProfileUpdate;
    private List<Users> user = new ArrayList<>();
    private String url = "http://10.0.2.2:8000/api/guest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        RequestTask task = new RequestTask(url, "GET");
        task.execute();

        btnProfileBack.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfilActivity.this, LoggedInActivity.class);
            startActivity(intent);
            finish();
        });
    }
    public void init(){
        txtProfilePassword = findViewById(R.id.txtProfilePassword);
        txtProfileFullname = findViewById(R.id.txtProfileFullname);
        txtProfileUsername = findViewById(R.id.txtProfileUsername);
        txtProfilePhone = findViewById(R.id.txtProfilePhone);
        btnProfileBack = findViewById(R.id.btnProfileBack);
        btnProfileUpdate = findViewById(R.id.btnProfileUpdate);
    }
    public class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;

        public RequestTask(String requestUrl, String requestType){
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }
        @Override
        protected Response doInBackground(Void... voids){
            Response response = null;
            try{
                if (requestType.equals("GET")){
                    response = RequestHandler.get(requestUrl);
                }
            }catch (IOException e){
                Toast.makeText(MyProfilActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }
        @Override
        protected void onPostExecute(Response response){
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400){
                Toast.makeText(MyProfilActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.d("onPostExecute:", response.getContent());
            }
            if (requestType.equals("GET")){
                Users[] userArray = converter.fromJson(response.getContent(),Users[].class);
                user.clear();
                user.addAll(Arrays.asList(userArray));
                Toast.makeText(MyProfilActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
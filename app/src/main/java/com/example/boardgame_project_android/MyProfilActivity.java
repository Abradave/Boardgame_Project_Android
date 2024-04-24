package com.example.boardgame_project_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Switch;
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
    private EditText txtProfileUsername;
    private EditText txtProfileFullname;
    private EditText txtProfilePhone;
    private AppCompatButton btnProfileBack;
    private AppCompatButton btnProfileUpdate;
    private AppCompatButton btnProfileDelete;
    private List<Users> users = new ArrayList<>();
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

        btnProfileUpdate.setOnClickListener(v -> {
            String fullname = txtProfileFullname.getText().toString();
            String username = txtProfileUsername.getText().toString();
            String phone = txtProfilePhone.getText().toString();

            Users updatedUser = new Users(ActualUser.id,username,fullname,phone);
            Gson converter = new Gson();
            MyProfilActivity.RequestTask updatetask = new MyProfilActivity.RequestTask(url,"PUT",converter.toJson(updatedUser));
            updatetask.execute();
            Intent intent = new Intent(MyProfilActivity.this, LoggedInActivity.class);
            startActivity(intent);
            finish();
        });

        btnProfileDelete.setOnClickListener(v -> {
            MyProfilActivity.RequestTask deletetask = new MyProfilActivity.RequestTask(url,"DELETE");
            deletetask.execute();
            Intent intent = new Intent(MyProfilActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


    }
    public void init(){
        txtProfileFullname = findViewById(R.id.txtProfileFullname);
        txtProfileUsername = findViewById(R.id.txtProfileUsername);
        txtProfilePhone = findViewById(R.id.txtProfilePhone);
        btnProfileBack = findViewById(R.id.btnProfileBack);
        btnProfileUpdate = findViewById(R.id.btnProfileUpdate);
        btnProfileDelete = findViewById(R.id.btnProfileDelete);
    }
    

    public class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        public RequestTask(String requestUrl, String requestType, String requestParams){
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }
        public RequestTask(String requestUrl, String requestType){
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }
        @Override
        protected Response doInBackground(Void... voids){
            Response response = null;
            try{
                switch(requestType){
                    case "GET":
                        response = RequestHandler.get(requestUrl + "/" + ActualUser.id);
                        break;
                    case "PUT":
                        response = RequestHandler.put(requestUrl + "/" + ActualUser.id,requestParams);
                        break;
                    case "DELETE":
                        response = RequestHandler.delete(requestUrl + "/" + ActualUser.id);
                        break;
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
            switch (requestType){
                case "GET":
                    Users userObj = converter.fromJson(response.getContent(),Users.class);
                    txtProfileFullname.setText(userObj.getG_name());
                    txtProfileUsername.setText(userObj.getG_username());
                    txtProfilePhone.setText(userObj.getG_phone_number());
                    Toast.makeText(MyProfilActivity.this, "Success Getting Data", Toast.LENGTH_SHORT).show();
                    break;
                case "PUT":
                    Users updateUser = converter.fromJson(response.getContent(), Users.class);
                    users.replaceAll(users1 -> users1.getId() == updateUser.getId() ? updateUser : users1);
                    Toast.makeText(MyProfilActivity.this, "Succesfully Updated", Toast.LENGTH_SHORT).show();
                    break;
                case "DELETE":
                    users.removeIf(users1 -> users1.getId() == ActualUser.id);
                    Toast.makeText(MyProfilActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
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

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyProfilActivity extends AppCompatActivity {
    //Változók deklarálása.
    private TextInputLayout txtProfileUsername, txtProfileFullname, txtProfilePhone;
    private AppCompatButton btnProfileBack, btnProfileUpdate, btnProfileDelete;
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
        //Függvények meghívása.
        init();
        RequestTask task = new RequestTask(url, "GET");
        task.execute();


        //Gomb kattintási esemény a Back Gomb visszalépéséhez.
        btnProfileBack.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfilActivity.this, LoggedInActivity.class);
            startActivity(intent);
            finish();
        });

        //Gomb kattintási esemény a felhasználói adatok felülírásához.
        btnProfileUpdate.setOnClickListener(v -> {
            String fullname = txtProfileFullname.getEditText().getText().toString();
            String username = txtProfileUsername.getEditText().getText().toString();
            String phone = txtProfilePhone.getEditText().getText().toString();

            if (fullname.isEmpty() || username.isEmpty() || phone.isEmpty()){
                Toast.makeText(MyProfilActivity.this, "All Data is Mandatory", Toast.LENGTH_SHORT).show();
            }
            else {
                Users updatedUser = new Users(ActualUser.id, username, fullname, phone);
                Gson converter = new Gson();
                MyProfilActivity.RequestTask updatetask = new MyProfilActivity.RequestTask(url, "PUT", converter.toJson(updatedUser));
                updatetask.execute();
                Intent intent = new Intent(MyProfilActivity.this, LoggedInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Gomb kattintási esemény a felhasználó adatbázisból való törléséhez.
        btnProfileDelete.setOnClickListener(v -> {
            MyProfilActivity.RequestTask deletetask = new MyProfilActivity.RequestTask(url,"DELETE");
            deletetask.execute();
            Intent intent = new Intent(MyProfilActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


    }
    //Változók inicializálása.
    public void init(){
        txtProfileFullname = findViewById(R.id.txtProfileFullname);
        txtProfileUsername = findViewById(R.id.txtProfileUsername);
        txtProfilePhone = findViewById(R.id.txtProfilePhone);
        btnProfileBack = findViewById(R.id.btnProfileBack);
        btnProfileUpdate = findViewById(R.id.btnProfileUpdate);
        btnProfileDelete = findViewById(R.id.btnProfileDelete);
    }


    //Request Task osztály létrehozása 3 változóval.
    public class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        //Request Task osztály konstruktora 3 változóra.
        public RequestTask(String requestUrl, String requestType, String requestParams){
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }
        //Request Task osztály konstruktora 2 változóra.
        public RequestTask(String requestUrl, String requestType){
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }
        //API csatlakozás lehetőségek megadása.
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
        //API csatlakozás végrehajtása.
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
                    txtProfileFullname.getEditText().setText(userObj.getG_name());
                    txtProfileUsername.getEditText().setText(userObj.getG_username());
                    txtProfilePhone.getEditText().setText(userObj.getG_phone_number());
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
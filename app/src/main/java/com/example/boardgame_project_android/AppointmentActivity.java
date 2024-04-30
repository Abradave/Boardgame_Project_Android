package com.example.boardgame_project_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//Változók deklarálása.
public class AppointmentActivity extends AppCompatActivity {
    private String urlappointment = "http://10.0.2.2:8000/api/appointment";
    private ListView lvAppointments;
    private AppCompatButton btnAppointmentBack, btnAppoinmentBook;
    private TextView tvAppointmentChoosenDate;
    private TextInputLayout txtAppointmentNumberOfPlayers, txtAppointmentBoardGameId;
    private List<Appointments> appointment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Függvények meghívása.
        init();
        RequestTask taskapp = new RequestTask(urlappointment, "GET");
        taskapp.execute();

        //Gomb kattintási esemény a Back Gomb visszalépéséhez.
        btnAppointmentBack.setOnClickListener(v -> {
            Intent intent = new Intent(AppointmentActivity.this, LoggedInActivity.class);
            startActivity(intent);
            finish();
        });

        //Gomb kattintási esemény az Időpont foglalásához.
        btnAppoinmentBook.setOnClickListener(v -> {
            //Helyi változók deklarálása és inicializálása.
            String playerCount = txtAppointmentNumberOfPlayers.getEditText().getText().toString();
            String boardGameId = txtAppointmentBoardGameId.getEditText().getText().toString();
            //Vizsgálat arra, hogy a felhasználó minden mezőt kitöltött-e.
            //Ha nem akkor erre figyelmeztet a program.
            if (playerCount.isEmpty() || boardGameId.isEmpty() || ActualUser.appointment == ""){
                Toast.makeText(AppointmentActivity.this, "Please give me all the details and choose a date", Toast.LENGTH_SHORT).show();
            }
            //Ha igen akkor ezeket elmenti az adatbázisba illetve egy saját osztályba.
            else {
                ActualUser.bg_id = Integer.parseInt(boardGameId);
                ActualUser.number_of_players = Integer.parseInt(playerCount);
                //Új Appointment tipusú osztály létrehozása a megadott adatokkal.
                Appointments updatedAppointment = new Appointments(ActualUser.appointmnet_id, ActualUser.appointment, ActualUser.e_id, ActualUser.booked, ActualUser.id, ActualUser.bg_id, ActualUser.number_of_players);
                Gson converter = new Gson();
                //Adatok elküldése PUT API hívással a backend felé.
                AppointmentActivity.RequestTask updatetask = new AppointmentActivity.RequestTask(urlappointment, "PUT", converter.toJson(updatedAppointment));
                updatetask.execute();
                //Átlépés másik oldalra az alkalmazásban
                Intent intent = new Intent(AppointmentActivity.this, LoggedInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Változók inicializálása.
    public void init() {
        lvAppointments = findViewById(R.id.lvAppointments);
        lvAppointments.setAdapter(new AppointmentsAdapter());
        txtAppointmentNumberOfPlayers = findViewById(R.id.txtAppointmentNumberOfPlayers);
        txtAppointmentBoardGameId = findViewById(R.id.txtAppointmentBoardGameId);
        btnAppointmentBack = findViewById(R.id.btnAppoinmentBack);
        btnAppoinmentBook = findViewById(R.id.btnAppoinmentBook);
        tvAppointmentChoosenDate = findViewById(R.id.tvAppointmentChoosenDate);
    }

    //Egy adapter létrehozása amely a kilistázásban játszik szerepet.
    private class AppointmentsAdapter extends ArrayAdapter<Appointments> {

        public AppointmentsAdapter() {
            super(AppointmentActivity.this, R.layout.appointments_list_item, appointment);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            //Adatok kilistázása az adatbázisból a megadott forma alapján.
            View view = inflater.inflate(R.layout.appointments_list_item, null, false);
            //Helyi változók deklarálása és inicializálása.
            TextView tvAppointmentDateList = view.findViewById(R.id.tvAppointmentDate);
            Button btnAppointmentChoose = view.findViewById(R.id.btnAppointmentChoose);

            Appointments actapp = appointment.get(position);

            //Ha az időpont nem foglalt akkor azt kilistázza nekünk az alkalmazás
            if (actapp.isBooked() == 0){
                tvAppointmentDateList.setText(actapp.getAppointment());
            }
            //Ha foglalt akkor eltünteti.
            else {
                tvAppointmentDateList.setVisibility(View.GONE);
                btnAppointmentChoose.setVisibility(View.GONE);
            }

            //Gomb megnyomás esemény az időpont választására ami kifogja írni nekünk ezt egy TextViewban.
            btnAppointmentChoose.setOnClickListener(v -> {
                ActualUser.appointment = actapp.getAppointment();
                tvAppointmentChoosenDate.setText(actapp.getAppointment());
                ActualUser.appointmnet_id = actapp.getId();
                ActualUser.e_id = actapp.getEmployee_id();
                ActualUser.booked = 1;
                Toast.makeText(AppointmentActivity.this,String.valueOf(ActualUser.appointmnet_id), Toast.LENGTH_SHORT).show();
            });
            return view;
        }
    }

    //Request Task osztály létrehozása 3 változóval.
    public class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        //Request Task osztály konstruktora 2 változóra.
        public RequestTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

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
            try{
                switch(requestType){
                    case "GET":
                        response = RequestHandler.get(requestUrl);
                        break;
                    case "PUT":
                        response = RequestHandler.put(requestUrl + "/" + ActualUser.appointmnet_id,requestParams);
                        break;
                }
            } catch (IOException e) {
                Toast.makeText(AppointmentActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        //API csatlakozás végrehajtása.
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400) {
                Toast.makeText(AppointmentActivity.this, response.getContent().toString(), Toast.LENGTH_SHORT).show();
                Log.d("onPostExecute:", response.getContent());
            }
            switch (requestType) {
                case "GET":
                    Appointments[] appArray = converter.fromJson(response.getContent(), Appointments[].class);
                    appointment.clear();
                    appointment.addAll(Arrays.asList(appArray));
                    lvAppointments.invalidateViews();
                    break;
                case "PUT":
                    Appointments updateAppointment = converter.fromJson(response.getContent(), Appointments.class);
                    appointment.replaceAll(appointments -> appointments.getId() == updateAppointment.getId() ? updateAppointment : appointments);
                    Toast.makeText(AppointmentActivity.this,"Appointment Booked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AppointmentActivity.this, LoggedInActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    }
}
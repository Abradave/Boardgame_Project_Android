package com.example.boardgame_project_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardGamesListActivity extends AppCompatActivity {
    //Változók deklarálása.
    private String url = "http://10.0.2.2:8000/api/boardgame";
    private ListView lvBoardGamesList;
    private List<BoardGames> bg = new ArrayList<>();
    private AppCompatButton btnListBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_board_games_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        RequestTask task = new RequestTask(url, "GET");
        task.execute();
        //Gomb kattintási esemény a Back Gomb visszalépéséhez.
        btnListBack.setOnClickListener(v -> {
            Intent intent = new Intent(BoardGamesListActivity.this, LoggedInActivity.class);
            startActivity(intent);
            finish();
        });
    }
    //Változók inicializálása.
    public void init(){
        lvBoardGamesList = findViewById(R.id.lvBoardGamesList);
        lvBoardGamesList.setAdapter(new BoardGamesAdapter());
        btnListBack = findViewById(R.id.btnListBack);
    }

    private class BoardGamesAdapter extends ArrayAdapter<BoardGames> {

        public BoardGamesAdapter(){super(BoardGamesListActivity.this, R.layout.board_games_list_items, bg);}

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            //Adatok kilistázása az adatbázisból a megadott forma alapján.
            //Helyi változók deklarálása és inicializálása.
            View view = inflater.inflate(R.layout.board_games_list_items,null,false);
            TextView txtBoardGameName = view.findViewById(R.id.txtBoardGameName);
            TextView txtBoardGameMin = view.findViewById(R.id.txtBoardGameMinPlayer);
            TextView txtBoardGameMax = view.findViewById(R.id.txtBoardGameMaxPlayer);
            TextView txtBoardGameDisc = view.findViewById(R.id.txtBoardGameDescription);
            TextView txtBoardGameId = view.findViewById(R.id.txtBoardGameId);
            BoardGames actbg = bg.get(position);
            //Helyi változók értékeinek megadása.
            txtBoardGameId.setText(String.valueOf(actbg.getId()));
            txtBoardGameName.setText(actbg.getBg_name());
            txtBoardGameMin.setText(String.valueOf(actbg.getMin_players()));
            txtBoardGameMax.setText(String.valueOf(actbg.getMax_players()));
            txtBoardGameDisc.setText(actbg.getDescription());
            return view;
        }
    }

    //Request Task osztály létrehozása 2 változóval.
    public class RequestTask extends AsyncTask<Void, Void, Response>{
        String requestUrl;
        String requestType;

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
                if (requestType.equals("GET")){
                    response = RequestHandler.get(requestUrl);
                }
            }catch (IOException e){
                Toast.makeText(BoardGamesListActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }
        //API csatlakozás végrehajtása.
        @Override
        protected void onPostExecute(Response response){
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400){
                Toast.makeText(BoardGamesListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.d("onPostExecute:", response.getContent());
            }
            if (requestType.equals("GET")){
                BoardGames[] bgArray = converter.fromJson(response.getContent(),BoardGames[].class);
                bg.clear();
                bg.addAll(Arrays.asList(bgArray));
                lvBoardGamesList.invalidateViews();
                Toast.makeText(BoardGamesListActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
package com.example.scoop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scoop.Model.Articles;
import com.example.scoop.Model.Headlines;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText etQuery;
    Button btnSearch;
    Dialog dialog;
    final String API_KEY = "5a31286d229e4b71a4a462213c49869d";
    Adapter adapter;
    List<Articles> articles=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);

        etQuery=findViewById(R.id.etQuery);
        btnSearch=findViewById(R.id.btnSearch);
        dialog=new Dialog(MainActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
         final String country=getCountry();
        retrieveJson("",country,API_KEY);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etQuery.getText().toString().equals("")){
                    retrieveJson(etQuery.getText().toString(),country,API_KEY);
                }else {
                    retrieveJson("",country,API_KEY);
                }
            }
        });

    }

    public void retrieveJson(String query ,String country ,String apiKey){
        Call<Headlines> call;
        if(!etQuery.getText().toString().equals("")){
            call=ApiClient.getInstance().getApi().getSpecificData(query, apiKey);
        }else {
            call=ApiClient.getInstance().getApi().getHeadlines(country, apiKey);
        }

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if(response.isSuccessful() && response.body().getArticles()!=null){
                    articles.clear();
                    articles=response.body().getArticles();
                    adapter=new Adapter(MainActivity.this,articles);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public String getCountry(){
        Locale locale=Locale.getDefault();
        String country=locale.getCountry();
        return country.toLowerCase();
    }

}
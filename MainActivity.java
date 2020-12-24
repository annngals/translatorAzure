package com.example.azure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;

    ArrayAdapter adapter;
    ArrayList<String> langs;

    EditText in;
    TextView out;

    String curitem;

    Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(AzureTranslationAPI.API_URL).build();
    AzureTranslationAPI api = retrofit.create(AzureTranslationAPI.class);

    class Message {
        String text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.languages);
        out = findViewById(R.id.out);
        in = findViewById(R.id.in);


        Call<LanguagesResponse> call = api.getLanguages();
        call.enqueue(new LanguagesCallback());

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                curitem = (String)parent.getItemAtPosition(position);
                Log.d("mytag", curitem );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("mytag", "no item selected");
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }



    class LanguagesCallback implements Callback<LanguagesResponse> {

        @Override
        public void onResponse(Call<LanguagesResponse> call, Response<LanguagesResponse> response) {
            if (response.isSuccessful()) {
                langs = response.body().toText();
                Log.d("mytag", "response: " + response.body().toText());
                attachToSpinner();
            } else {
                Log.d("mytag", "Can't get languages ;(" + response.code());
            }
        }

        @Override
        public void onFailure(Call<LanguagesResponse> call, Throwable t) {
            Log.d("mytag", "Connection error ;(" + t.getLocalizedMessage());
        }
    }

    public void attachToSpinner() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, langs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    class TranslateCallback implements Callback<TranslatedText[]> {

        @Override
        public void onResponse(Call<TranslatedText[]> call, Response<TranslatedText[]> response) {
            if (response.isSuccessful()) {
                String str = response.body()[0].toString();
                out.setText(str);
                Log.d("mytag", "response: " + str);
            } else {
                Log.d("mytag", "error" + response.code());
            }
        }

        @Override
        public void onFailure(Call<TranslatedText[]> call, Throwable t) {
            Log.d("mytag", "Translation error ;(" + t.getLocalizedMessage());
        }
    }

    public void onClick(View v) {
        Message msg = new Message();
        msg.text = String.valueOf(in.getText());
        Message[] text = new Message[1];
        text[0] = msg;

        String currentl = curitem.split(" / ")[0];

        Call<TranslatedText[]> call = api.translate(text, currentl);
        call.enqueue(new TranslateCallback());
    }
}
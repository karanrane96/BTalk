package com.example.android.chatmini;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;

import java.util.ArrayList;
import java.util.List;

public class LanguageSelectionActivity extends AppCompatActivity {
    Spinner languageSpinner;
    List<Language> languages = new ArrayList<Language>();
    List<String> finalLanguages = new ArrayList<String>();
    Button confirmLanguage;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);
        languageSpinner = findViewById(R.id.spinner);
        confirmLanguage = findViewById(R.id.confirmButton);

        displaySupportedLanguages();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, finalLanguages);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        languageSpinner.setAdapter(spinnerArrayAdapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        confirmLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Continue with Intent.
                // Update Language Child to Selected Language in the DB
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void displaySupportedLanguages() {
        Translate translate = TranslateOptions.newBuilder().build().getService();
        Translate.LanguageListOption target = Translate.LanguageListOption.targetLanguage("en");
        List<Language> languages = translate.listSupportedLanguages(target);

        for (Language language : languages) {
            finalLanguages.add(language.getName());
        }


    }
}

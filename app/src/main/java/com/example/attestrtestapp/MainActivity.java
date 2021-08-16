package com.example.attestrtestapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attestrtestapp.databinding.ActivityMainBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "main_activity";
    private ActivityMainBinding mainBinding;
    private EditText handshakeIdEditText, clientKeyEditText;
    private Button initiateSessionButton;
    private final String handShakeError = "Enter handshake id";
    private final String clientKeyError = "Enter client key";
    private final String LOCAL_ENGLISH = "en";
    private final String LOCAL_HINDI = "hi";
    private Spinner retrySpinner, localeSpinner;
    private boolean isRetry;
    private String selectedLocale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        handshakeIdEditText = mainBinding.handshakeIdEditText;
        clientKeyEditText = mainBinding.clientKeyEditText;
        initiateSessionButton = mainBinding.initiateSessionButton;
        retrySpinner = mainBinding.retrySpinner;
        localeSpinner = mainBinding.localeSpinner;
        initiateSessionButton.setOnClickListener(this);

        ArrayAdapter<String> retrySpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.retry_array));
        ArrayAdapter<String> localSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.locale_array));
        retrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        retrySpinner.setPrompt("Select Retry");
        retrySpinner.setAdapter(retrySpinnerAdapter);
        retrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String retry = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onRetrySelected: "+retry);
                if ("True".equals(retry)) {
                    isRetry = true;
                } else if ("False".equals(retry)) {
                    isRetry = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isRetry = false;
            }
        });

        localSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        localeSpinner.setPrompt("Select Locale");
        localeSpinner.setAdapter(localSpinnerAdapter);
        localeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String locale = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onLocaleSelected: "+locale);
                if ("English".equals(locale)) {
                    selectedLocale = LOCAL_ENGLISH;
                } else if ("Hindi".equals(locale)) {
                    selectedLocale = LOCAL_HINDI;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isRetry = false;
            }
        });

        // TODO Add Attestr SDK code

    }

    @Override
    public void onClick(View v) {
        if (v == initiateSessionButton) {
            String handShakeID = handshakeIdEditText.getText().toString().trim();
            String clientKey = clientKeyEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(handShakeID) && !TextUtils.isEmpty(clientKey)) {
                // TODO
            }

            if (TextUtils.isEmpty(handShakeID) && TextUtils.isEmpty(clientKey)) {
                Log.d(TAG, "Hanshake ID & Client Key Null");
                handshakeIdEditText.setError(handShakeError);
                clientKeyEditText.setError(clientKeyError);
            }
            if (TextUtils.isEmpty(handShakeID)){
                Log.d(TAG, "Hanshake ID Null");
                handshakeIdEditText.setError(handShakeError);
            }

            if (TextUtils.isEmpty(clientKey)){
                Log.d(TAG, "Client Key Null");
                clientKeyEditText.setError(clientKeyError);
            }
        }
    }


}
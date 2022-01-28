package com.attestr.attestrFlowx;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.attestr.attestrFlowx.databinding.ActivityMainBinding;
import java.util.Map;

/**
 * Copyright (c) 2021 Pegadroid IQ Solutions Pvt. Ltd.
 * @Author Gaurav Naresh Pandit
 **/

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AttestrFlowXListener {

    private static final String TAG = "main_activity";
    private ActivityMainBinding mainBinding;
    private EditText handshakeIdEditText;
    private EditText clientKeyEditText;
    private Button initiateSessionButton;
    private final String handShakeError = "Enter handshake id";
    private final String clientKeyError = "Enter client key";
    private Spinner retrySpinner, localeSpinner;
    private boolean isRetry;
    private String selectedLocale;
    private AttestrFlowx attestrFlowx;
    private String[] languages = new String[]{"en", "hi"};
    private boolean[] retryMode = new boolean[]{true, false};

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
                R.layout.spinner_items, getResources()
                .getStringArray(R.array.retry_array));
        ArrayAdapter<String> localSpinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_items, getResources()
                .getStringArray(R.array.locale_array));
        retrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        retrySpinner.setPrompt("Select Retry");
        retrySpinner.setAdapter(retrySpinnerAdapter);
        retrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isRetry = retryMode[position];
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
                selectedLocale = languages[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLocale = languages[0];
            }
        });
        attestrFlowx = new AttestrFlowx();
    }

    @Override
    public void onClick(View v) {
        if (v == initiateSessionButton) {
            String locale = selectedLocale;
            String handShakeID = handshakeIdEditText.getText().toString().trim();
            String clientKey = clientKeyEditText.getText().toString().trim();
           try {
               if (!TextUtils.isEmpty(handShakeID) && !TextUtils.isEmpty(clientKey)) {
                   attestrFlowx.init(clientKey, handShakeID, this);
                   attestrFlowx.launch(
                           selectedLocale,
                           isRetry,
                           null
                   );
               }
           } catch (Exception e){
               Log.d(TAG, "onClick: "+e.toString());
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


    @Override
    public void onFlowXComplete(Map<String, Object> map) {
        Toast.makeText(this, "Signature: "+map.get("signature"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFlowXSkip(Map<String, Object> map) {
        Toast.makeText(MainActivity.this, "Flow skipped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFlowXError(Map<String, Object> map) {
        String errorMessage = (String) map.get("message");
        Toast.makeText(MainActivity.this, "Error : "+errorMessage, Toast.LENGTH_SHORT).show();
    }
}
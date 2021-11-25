package com.example.attestrtestapp;

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

import com.attestr.flowx.AttestrFlowx;
import com.attestr.flowx.listener.AttestrFlowXListener;
import com.example.attestrtestapp.databinding.ActivityMainBinding;

import java.util.HashMap;
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
    private Map<String, String> queryParameters;

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
        queryParameters = new HashMap<>();
        queryParameters.put("pan_number", "DARPP6083E");
        queryParameters.put("bank_account_number", "05011140069044");
        queryParameters.put("bank_ifsc_code", "HDFC0000060");
        queryParameters.put("upi_id", "8169672901@paytm");
        queryParameters.put("gstin_number", "27AABCU9603R1ZN");
        queryParameters.put("mca_company_registration", "L17110MH1973PLC019786");
        queryParameters.put("mca_director_number", "00001620");
        queryParameters.put("business_name", "Microsoft");
        queryParameters.put("first_name", "Gaurav");
        queryParameters.put("middle_name", "Naresh");
        queryParameters.put("last_name", "Pandit");
        queryParameters.put("date_of_birth", "12-10-1997");
        queryParameters.put("uuid_number", "0364");
        queryParameters.put("user_email", "pgaurav72@gmail.com");
        queryParameters.put("user_mobile_no", "8169672901");
        queryParameters.put("address", "Mangal Ragho Nagar, Tisgaon Road, Kalyan (E)");
        queryParameters.put("staying_since_month", "Mar");
        queryParameters.put("staying_since_year", "2021");
        queryParameters.put("mobile_number", "8169672901");
        queryParameters.put("person_name", "Gaurav Naresh Pandit");
        queryParameters.put("father_name", "Naresh Murlidhar Pandit");
        queryParameters.put("doc_type", "VOTER");

        attestrFlowx = new AttestrFlowx();
    }

    @Override
    public void onClick(View v) {
        if (v == initiateSessionButton) {
            String handShakeID = handshakeIdEditText.getText().toString().trim();
            String clientKey = clientKeyEditText.getText().toString().trim();
           try {
               if (!TextUtils.isEmpty(handShakeID) && !TextUtils.isEmpty(clientKey)) {
                   attestrFlowx.init(clientKey, handShakeID, this);
                   attestrFlowx.launch(
                           selectedLocale,
                           isRetry,
                           queryParameters
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
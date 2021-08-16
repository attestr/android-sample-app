package com.example.attestrtestapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.attestr.flowx.AttestrFlowx;
import com.attestr.flowx.listener.AttestrFlowXListener;
import com.example.attestrtestapp.databinding.ActivityMainBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AttestrFlowXListener {

    private static final String TAG = "main_activity";
    private ActivityMainBinding mainBinding;
    private EditText handshakeIdEditText, clientKeyEditText;
    private Button initiateSessionButton;
    private final String handShakeError = "Enter handshake id";
    private final String LOCAL_ENGLISH = "en";
    private final String LOCAL_HINDI = "hi";
    private AttestrFlowx attestrFlowx;
    private Spinner retrySpinner;
    private boolean isRetry;
    private ProgressBar handshakeIdProgressBar;
    public static final String flow = "FX01d8esduzkq7m0ege";
    public static final String tag = "OX01d8esnxfkq6bfio2";
    private HashMap<Object, Object> handshakeClass = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        handshakeIdEditText = mainBinding.handshakeIdEditText;
        clientKeyEditText = mainBinding.clientKeyEditText;
        initiateSessionButton = mainBinding.initiateSessionButton;
        retrySpinner = mainBinding.retrySpinner;
        handshakeIdProgressBar = mainBinding.handshakeIdProgressBar;
        handshakeIdProgressBar.setVisibility(View.INVISIBLE);
        initiateSessionButton.setOnClickListener(this);
        attestrFlowx = new AttestrFlowx();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.retry_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        retrySpinner.setPrompt("Select Retry");
        retrySpinner.setAdapter(adapter);
        retrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String retry = parent.getItemAtPosition(position).toString();
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

        handshakeClass.put("flow", flow);
        handshakeClass.put("tag", tag);
        handshakeClass.put("snapshot", JSONObject.NULL);

        Log.d(TAG, "Request: "+handshakeClass.toString());

    }

    @Override
    public void onClick(View v) {
        if (v == initiateSessionButton) {
            String handShakeID = handshakeIdEditText.getText().toString().trim();
            String clientKey = clientKeyEditText.getText().toString().trim();

            boolean isHandShakeIDNull = TextUtils.isEmpty(handShakeID);
            boolean isClientKeyNull = TextUtils.isEmpty(clientKey);

            if (!isHandShakeIDNull && !isClientKeyNull) {
                attestrFlowx.init(clientKey, handShakeID, this);
                attestrFlowx.launch(
                        LOCAL_ENGLISH,
                        isRetry,
                        null);
            } else {
                handshakeIdEditText.setError(handShakeError);
            }
        }
    }


    @Override
    public void onFlowXComplete(Map<String, Object> data) {
        Toast.makeText(MainActivity.this, "Flow completed successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFlowXSkip(Map<String, Object> data) {
        Toast.makeText(MainActivity.this, "Flow skipped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFlowXError(Map<String, Object> data) {
        String errorMessage = (String) data.get("message");
        Toast.makeText(MainActivity.this, "Error : "+errorMessage, Toast.LENGTH_SHORT).show();
    }


}
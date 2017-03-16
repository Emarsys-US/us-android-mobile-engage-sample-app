package com.emarsys.mobileengage.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.emarsys.mobileengage.MobileEngage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    private Button appLogingAnonymous;
    private Button appLogin;
    private Button appLogout;
    private Button customEvent;
    private Button messageOpen;

    private EditText applicationId;
    private EditText applicationSecret;
    private EditText eventName;
    private EditText eventAttributes;
    private EditText messageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appLogingAnonymous = (Button) findViewById(R.id.appLoginAnonymous);
        appLogin = (Button) findViewById(R.id.appLogin);
        appLogout = (Button) findViewById(R.id.appLogout);
        customEvent = (Button) findViewById(R.id.customEvent);
        messageOpen = (Button) findViewById(R.id.messageOpen);

        applicationId = (EditText) findViewById(R.id.applicationId);
        applicationSecret = (EditText) findViewById(R.id.applicationSecret);
        eventName = (EditText) findViewById(R.id.eventName);
        eventAttributes = (EditText) findViewById(R.id.eventAttributes);
        messageId = (EditText) findViewById(R.id.messageId);

        appLogingAnonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobileEngage.appLogin();
            }
        });

        appLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(applicationId.getText().toString());
                String secret = applicationSecret.getText().toString();
                MobileEngage.appLogin(id, secret);
            }
        });

        appLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobileEngage.appLogout();
            }
        });

        customEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = eventName.getText().toString();
                String attributesString = eventAttributes.getText().toString();

                Map<String, String> attributes = null;
                if (!attributesString.isEmpty()) {
                    try {
                        attributes = new HashMap<>();
                        JSONObject json = new JSONObject(attributesString);
                        Iterator<String> keys = json.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            attributes.put(key, json.getString(key));
                        }
                    } catch (JSONException e) {
                        Log.w(TAG, "Event attributes edittext content is not a valid JSON!");
                    }
                }

                MobileEngage.trackCustomEvent(name, attributes);
            }
        });

        messageOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String id = messageId.getText().toString();
                JSONObject json = null;
                try {
                    json = new JSONObject()
                            .put("u", "{\"sid\": \"" + id + "\"}");
                    intent.putExtra("pw_data_json_string", json.toString());
                    MobileEngage.trackMessageOpen(intent);
                } catch (JSONException je) {
                    Log.e(TAG, "Exception while creating JSONObject", je);
                }
            }
        });
    }
}

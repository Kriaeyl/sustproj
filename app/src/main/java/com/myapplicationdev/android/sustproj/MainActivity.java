package com.myapplicationdev.android.sustproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    Button b1;
    EditText et1, et2;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = findViewById(R.id.etUsername);
        et2 = findViewById(R.id.etPassword);
        b1 = findViewById(R.id.login);
        client = new AsyncHttpClient();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();
                params.add("username", et1.getText().toString());
                params.add("password", et2.getText().toString());
                client.post("http://homes.soi.rp.edu.sg/18055124/sustproj/login.php", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                SharedPreferences.Editor sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
                                sp.putString("username", response.getString("username"));
                                sp.putString("password", response.getString("password"));
                                sp.commit();
                                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
package com.myapplicationdev.android.sustproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SecondActivity extends AppCompatActivity {

    ArrayList<Fragment> al;
    FragmentAdapter adapter;
    ViewPager vPager;
    AsyncHttpClient client;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        client = new AsyncHttpClient();
        //Retrieve username and password
        RequestParams params = new RequestParams();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        params.add("username", sp.getString("username", ""));
        params.add("password", sp.getString("password", ""));

        //Set Wallet Balance
        tv1 = findViewById(R.id.walletBalance);
        client.post("http://10.0.2.2/sustproj/getBalance.php", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    tv1.setText(response.getString("balance"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //Set The Button to Add Funds
        Button b1 = findViewById(R.id.addBalance);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        vPager = findViewById(R.id.viewpager1);
        FragmentManager fm = getSupportFragmentManager();
        al = new ArrayList<Fragment>();
        al.add(new FragmentPending());
        al.add(new FragmentHistory());
        adapter = new FragmentAdapter(fm, al);
        vPager.setAdapter(adapter);

    }
}
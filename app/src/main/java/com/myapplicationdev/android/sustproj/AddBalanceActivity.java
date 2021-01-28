package com.myapplicationdev.android.sustproj;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class AddBalanceActivity extends AppCompatActivity {
    Button b1;
    EditText et1;
    double amt;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment (ENVIRONMENT_NO_NETWORK).  When ready, switch to sandbox (ENVIRONMENT_SANDBOX) or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AWGgIQs1wBb7ItEW53BNBM2ZDfP5vHhz4E-8SY9IRizZZIg-YdHeJ-3wVxL_OwZ40513tzfjTglJx4BO");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);
        b1 = findViewById(R.id.addBalanceAmt);
        et1 = findViewById(R.id.etAmt);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number= et1.getText().toString();
                if (Pattern.matches("([0-9]*)\\.([0-9]{2})", number)) {
                    amt = Double.parseDouble(number);
                    onBuyPressed(view);
                }
                else {
                    Toast.makeText(AddBalanceActivity.this, "Please type a valid amount!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void onBuyPressed(View pressed) {

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.
        PayPalPayment payment = new PayPalPayment(new BigDecimal(et1.getText().toString()), "USD", "Top-Up",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    if (confirm.toJSONObject().getJSONObject("response").getString("state").equalsIgnoreCase("approved")) {
                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                        params.add("username", sp.getString("username", ""));
                        params.add("password", sp.getString("password", ""));
                        params.add("amt", et1.getText().toString());
                        client.post("http://homes.soi.rp.edu.sg/18055124/sustproj/addBalance.php", params, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                try {
                                    if (response.getBoolean("status")) {
                                        Intent i = new Intent(AddBalanceActivity.this, SecondActivity.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(AddBalanceActivity.this, "REST failed", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
}

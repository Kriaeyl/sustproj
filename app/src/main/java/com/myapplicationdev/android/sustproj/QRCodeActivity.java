package com.myapplicationdev.android.sustproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import cz.msebera.android.httpclient.Header;

public class QRCodeActivity extends AppCompatActivity {

    AsyncHttpClient client;
    ImageView iv1;
    Button b1;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        b1 = findViewById(R.id.qrDone);
        iv1 = findViewById(R.id.qrImage);
        client = new AsyncHttpClient();

        //Get Parameters
        Intent i = getIntent();
        int id = i.getIntExtra("id", 0);
        String code = i.getStringExtra("code");
        RequestParams params = new RequestParams();
        params.add("id", id+"");
        params.add("code", code);

        //Get QRCode value from database
        client.post("http://homes.soi.rp.edu.sg/18055124/getQRCode.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String qrcode = response.getString("qr_code");
                    //Resize QRCode
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    //Generate QRCode
                    qrgEncoder = new QRGEncoder(
                            qrcode, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        iv1.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.v("Error: ", e.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
package com.myapplicationdev.android.sustproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PrescriptionDetailsActivity extends AppCompatActivity {

    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;
    Button b1;
    Prescription p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_details);
        Intent i = getIntent();
        p = (Prescription) i.getSerializableExtra("prescription");
        tv1 = findViewById(R.id.descIdV);
        tv2 = findViewById(R.id.bnfCodeV);
        tv3 = findViewById(R.id.bnfNameV);
        tv4 = findViewById(R.id.qtyV);
        tv5 = findViewById(R.id.dateV);
        tv6 = findViewById(R.id.costV);
        tv7 = findViewById(R.id.locationV);
        tv8 = findViewById(R.id.statusV);
        b1 = findViewById(R.id.buttonPay);

        tv1.setText(p.getPrescriptionId() + "");
        tv2.setText(p.getBnfCode());
        tv3.setText(p.getBnfName());
        tv4.setText(p.getQuantity() + "");
        tv5.setText(p.getDate());
        tv6.setText("$" + p.getActCost());
        tv7.setText(p.getVendingLocation());
        tv8.setText(p.getStatus());

        if (p.getStatus().equalsIgnoreCase("PENDING")) {
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PrescriptionDetailsActivity.this, QRCodeActivity.class);
                    intent.putExtra("id", p.getPrescriptionId());
                    intent.putExtra("code", p.getBnfCode());
                    startActivity(intent);
                }
            });
        } else {
            b1.setVisibility(View.GONE);
        }
    }
}
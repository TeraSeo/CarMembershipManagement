package com.example.carmembershipmanagement;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SpecificCar extends AppCompatActivity {

    TextView number, registered, expired;
    Button useBtn;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_car);

        number = findViewById(R.id.specific_car_number);
        registered = findViewById(R.id.specific_registered);
        expired = findViewById(R.id.specific_expired);

        useBtn = findViewById(R.id.useBtn);

        db = new DBHelper(this);

        Intent intent = getIntent();
        String carNumber = intent.getStringExtra("number");
        String registeredDt =intent.getStringExtra("registered");
        String expiredDt = intent.getStringExtra("expired");
        String lastUsedDt = intent.getStringExtra("lastUsed");

        number.setText("차량번호: " + carNumber);
        registered.setText("등록일자: " + registeredDt);
        expired.setText("만료일자: " + expiredDt);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date currentDate = new Date();
        String current = formatter.format(currentDate);

        if (isExpired(expiredDt)) {
            useBtn.setText("만료");
            useBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        else {
            if (lastUsedDt.equals(current)) {
                useBtn.setText("당일사용완료");
                useBtn.setBackgroundColor(getResources().getColor(android.R.color.black));
            }
            else {
                useBtn.setText("사용");
                useBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            }
        }
        useBtn.setTextSize(60);

        useBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isExpired(expiredDt)) {
                    if (!lastUsedDt.equals(current)) {
                        if (db.updateLastUsed(carNumber, registeredDt, expiredDt, current)) {
                            useBtn.setText("당일사용완료");
                            useBtn.setBackgroundColor(getResources().getColor(android.R.color.black));

                            Intent intent1 = new Intent(SpecificCar.this, MainActivity.class);
                            startActivity(intent1);
                        }
                    }
                }
                else {
                    Intent intent1 = new Intent(SpecificCar.this, ReRegister.class);
                    intent1.putExtra("number", carNumber);
                    intent1.putExtra("registered", registeredDt);
                    intent1.putExtra("lastUsed", lastUsedDt);

                    startActivity(intent1);
                }
            }
        });
    }

    boolean isExpired(String expired) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date expiredDt = null;
        try {
            Date currentDate = new Date();
            expiredDt = dateFormat.parse(expired);
            boolean isExpired = expiredDt.before(currentDate);
            return isExpired;

        } catch (ParseException e) {
            Toast.makeText(this, "날짜를 비교하는데 실패했습니다!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
package com.example.carmembershipmanagement;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class CarRegister extends AppCompatActivity {

    Button newCarRegisterBtn, carListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_register);

        newCarRegisterBtn = findViewById(R.id.newRegister);
        carListBtn = findViewById(R.id.carList);

        newCarRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarRegister.this, NewRegister.class);
                startActivity(intent);
            }
        });

        carListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarRegister.this, SearchCarLisst.class);
                startActivity(intent);
            }
        });
    }
}
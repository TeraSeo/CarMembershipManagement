package com.example.carmembershipmanagement;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class CheckCar extends AppCompatActivity {

    EditText number;
    Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_car);

        number = findViewById(R.id.carNumberSearch);
        searchBtn = findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String carNumber = number.getText().toString();

                Intent intent = new Intent(CheckCar.this, SearchResult.class);
                intent.putExtra("number", carNumber);

                startActivity(intent);
            }
        });
    }
}
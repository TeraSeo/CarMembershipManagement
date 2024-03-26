package com.example.carmembershipmanagement;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewRegister extends AppCompatActivity {

    EditText carNumber, registered, expired;
    Button register;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);

        carNumber = findViewById(R.id.carNumber);
        registered = findViewById(R.id.registerDate);
        expired = findViewById(R.id.expiredDate);

        register = findViewById(R.id.register);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date currentDate = new Date();

        Date expiredDate = new Date();
        expiredDate.setDate(currentDate.getDate() + 30);

        registered.setText(formatter.format(currentDate));
        expired.setText(formatter.format(expiredDate));

        db = new DBHelper(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = carNumber.getText().toString();
                String registeredDate = registered.getText().toString();
                String expiredDate = expired.getText().toString();

                Boolean isInserted = db.insertCarData(number, registeredDate, expiredDate);
                if (isInserted) {
                    Toast.makeText(NewRegister.this, "성공적으로 등록 되었습니다!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(NewRegister.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(NewRegister.this, "등록에 실패했습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
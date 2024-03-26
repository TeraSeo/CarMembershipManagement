package com.example.carmembershipmanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReRegister extends AppCompatActivity {

    TextView carNumber;
    EditText registered;
    Button modifyBtn, deleteBtn;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_register);

        carNumber = findViewById(R.id.carNumberRenew);
        registered = findViewById(R.id.registerDateRenew);

        modifyBtn = findViewById(R.id.renew);
        deleteBtn = findViewById(R.id.delete);
        deleteBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));

        db = new DBHelper(this);

        Intent intent = getIntent();
        String number = intent.getStringExtra("number");
        String registeredDate = intent.getStringExtra("registered");
        String lastUsed = intent.getStringExtra("lastUsed");

        carNumber.setText(number);
        registered.setText(registeredDate);

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String registeredDt = registered.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date expiredDt = null;
                try {
                    expiredDt = dateFormat.parse(registeredDt);
                    expiredDt.setDate(expiredDt.getDate() + 30);
                    String expired = dateFormat.format(expiredDt);

                    db.updateCarData(number, registeredDt, expired, lastUsed);

                    Toast.makeText(ReRegister.this, "수정에 성공했습니다!", Toast.LENGTH_SHORT).show();

                    Intent intent1 = new Intent(ReRegister.this, MainActivity.class);
                    startActivity(intent1);

                } catch (ParseException e) {
                    Toast.makeText(ReRegister.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(ReRegister.this);
        TextView messageText = new TextView(ReRegister.this);
        messageText.setText("삭제 하시겠습니까?");
        messageText.setTextSize(40); // Set desired text size
        builder.setView(messageText);
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.deleteSpecificRow(number);
                Toast.makeText(ReRegister.this, "삭제 되었습니다!", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(ReRegister.this, MainActivity.class);
                startActivity(intent1);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(ReRegister.this, "취소 되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                if (positiveButton != null && negativeButton != null) {
                    positiveButton.setTextSize(40); // Set desired text size for positive button
                    negativeButton.setTextSize(40); // Set desired text size for negative button
                }
            }
        });

    }
}
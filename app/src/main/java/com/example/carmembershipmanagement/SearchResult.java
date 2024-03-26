package com.example.carmembershipmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SearchResult extends AppCompatActivity {

    DBHelper db;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        tableLayout = findViewById(R.id.searchResultTable);

        Intent intent = getIntent();
        String carNumber = intent.getStringExtra("number");

        db = new DBHelper(this);
        Cursor dataByNumber = db.getDataByNumber(carNumber);

        if (dataByNumber != null && dataByNumber.moveToFirst()) {
            do {

                String number = dataByNumber.getString(0);
                String registered = dataByNumber.getString(1);
                String expired = dataByNumber.getString(2);
                String lastUsed = dataByNumber.getString(3);

                TableRow newRow = new TableRow(this);
                newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                newRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchResult.this, SpecificCar.class);
                        intent.putExtra("number", number);
                        intent.putExtra("registered", registered);
                        intent.putExtra("expired", expired);
                        intent.putExtra("lastUsed", lastUsed);
                        startActivity(intent);
                    }
                });
                newRow.setGravity(Gravity.CENTER);

                TextView col1 = new TextView(this);
                col1.setText(number);
                col1.setLayoutParams(new TableRow.LayoutParams(1));
                col1.setTextSize(60);
                newRow.addView(col1);

                tableLayout.addView(newRow);

            } while (dataByNumber.moveToNext());
            dataByNumber.close();
        } else {
            Toast.makeText(this, "검색하는데 실패했습니다!", Toast.LENGTH_SHORT).show();
        }
    }
}
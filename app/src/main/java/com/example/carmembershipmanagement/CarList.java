package com.example.carmembershipmanagement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CarList extends AppCompatActivity {

    TableLayout carListTable;
    Button backUpBtn;
    DBHelper db;

    private static final int STORAGE_PERMISSION_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        Intent intent = getIntent();
        String carNumber = intent.getStringExtra("number");

        carListTable = findViewById(R.id.carListTable);
        backUpBtn = findViewById(R.id.backUpBtn);

        db = new DBHelper(this);

        StringBuilder data = new StringBuilder();
        data.append("차량번호,등록,만료일");

        Cursor res = db.getDataByNumber(carNumber);
        if (res != null && res.moveToFirst()) {
            do {

                String number = res.getString(0);
                String registered = res.getString(1);
                String expired = res.getString(2);
                String lastUsed = res.getString(3);

                data.append("\n"+number+","+registered+","+expired);

                TableRow newRow = new TableRow(this);
                newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                newRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CarList.this, ReRegister.class);
                        intent.putExtra("number", number);
                        intent.putExtra("registered", registered);
                        intent.putExtra("lastUsed", lastUsed);
                        startActivity(intent);
                    }
                });

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date expiredDt = null;
                try {
                    Date currentDate = new Date();
                    expiredDt = dateFormat.parse(expired);
                    boolean isExpired = expiredDt.before(currentDate);
                    if (isExpired) {
                        newRow.setBackgroundColor(getResources().getColor(R.color.purple_200));
                    }
                } catch (ParseException e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }

                TextView col1 = new TextView(this);
                col1.setText(number);
                col1.setTextSize(50);
                col1.setLayoutParams(new TableRow.LayoutParams(1));
                newRow.addView(col1);

                TextView col2 = new TextView(this);
                col2.setText(" " + registered);
                col2.setTextSize(50);
                col2.setLayoutParams(new TableRow.LayoutParams(1));
                newRow.addView(col2);

                TextView col3 = new TextView(this);
                col3.setText("  " + expired);
                col3.setTextSize(50);
                col3.setLayoutParams(new TableRow.LayoutParams(1));
                newRow.addView(col3);

                carListTable.addView(newRow);

            } while (res.moveToNext());
            res.close();
        } else {
            Toast.makeText(this, "불러오는데 실패했습니다!", Toast.LENGTH_SHORT).show();
        }

        backUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    try {
                        createCsv(data);
                        Toast.makeText(CarList.this, data.toString(), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(CarList.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    requestPermission();
                }
            }
        });

    }

    private boolean checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11 (R) or above
            return Environment.isExternalStorageManager();
        }else {
            //Below android 11
            int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);
            }catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);
            }
        }else{
            //Below android 11
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    STORAGE_PERMISSION_CODE
            );
        }
    }

    private ActivityResultLauncher<Intent> storageActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>(){

                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                                //Android is 11 (R) or above
                                if(Environment.isExternalStorageManager()){
                                    //Manage External Storage Permissions Granted
                                    Log.d("TAG", "onActivityResult: Manage External Storage Permissions Granted");
                                }else{
                                    Toast.makeText(CarList.this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                //Below android 11

                            }
                        }
                    });

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0){
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(read && write){
                    Toast.makeText(CarList.this, "Storage Permissions Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CarList.this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void createCsv(StringBuilder data) {

        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();

        try {

            FileOutputStream out = openFileOutput("CSV_Data_" + time + ".csv", Context.MODE_PRIVATE);

            out.write(data.toString().getBytes());
            out.close();

            Context context = getApplicationContext();
            final File newFile = new File(Environment.getExternalStorageDirectory(), "SimpleCSV");

            if (!newFile.exists()) {
                newFile.mkdir();
            }

            File file = new File(context.getFilesDir(), "CSV_Data_" + time + ".csv");

            Uri path = FileProvider.getUriForFile(context, "com.example.carmembershipmanagement", file);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            intent.putExtra(Intent.EXTRA_STREAM, path);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Excel Data"));

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}
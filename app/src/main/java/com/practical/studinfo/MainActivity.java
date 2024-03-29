package com.practical.studinfo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etId, etName, etAddress, etPhone;
    private Button btnInsert, btnShow;
    private TextView tvStudentData, tvEntryCount;

    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etId = findViewById(R.id.et_id);
        etName = findViewById(R.id.et_name);
        etAddress = findViewById(R.id.et_address);
        etPhone = findViewById(R.id.et_phone);
        btnInsert = findViewById(R.id.btn_insert);
        btnShow = findViewById(R.id.btn_show);
        tvStudentData = findViewById(R.id.tv_student_data);
        tvEntryCount = findViewById(R.id.tv_entry_count);

        databaseHelper = new DatabaseHelper(this);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertStudent();
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStudents();
            }
        });
    }

    private void insertStudent() {
        String id = etId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID, id);
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_ADDRESS, address);
        values.put(DatabaseHelper.COLUMN_PHONE, phone);

        long result = db.insert(DatabaseHelper.TABLE_STUDENT, null, values);

        if (result != -1) {
            clearInputFields();
            showToast("Student details added successfully");
        } else {
            showToast("Failed to add student details");
        }
    }

    private void showStudents() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_STUDENT,
                null,
                null,
                null,
                null,
                null,
                null);

        StringBuilder stringBuilder = new StringBuilder();

        if (cursor.moveToFirst()) {
            int entryCount = 0;

            do {
                String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String address = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ADDRESS));
                String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE));

                stringBuilder.append("ID: ").append(id).append("\n");
                stringBuilder.append("Name: ").append(name).append("\n");
                stringBuilder.append("Address: ").append(address).append("\n");
                stringBuilder.append("Phone: ").append(phone).append("\n\n");

                entryCount++;
            } while (cursor.moveToNext());

            tvStudentData.setText(stringBuilder.toString());
            tvEntryCount.setText("Entry Count: " + entryCount);
        } else {
            tvStudentData.setText("");
            tvEntryCount.setText("Entry Count: 0");
            showToast("No student data found.");
        }
        cursor.close();
    }

    private void clearInputFields() {
        etId.setText("");
        etName.setText("");
        etAddress.setText("");
        etPhone.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

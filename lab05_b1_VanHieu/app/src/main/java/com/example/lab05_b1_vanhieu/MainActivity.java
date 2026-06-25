package com.example.lab05_b1_vanhieu;

import android.os.Bundle;
import android.view.View; // Added missing import for View
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerPerson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerPerson = findViewById(R.id.spinnerPerson);
        List<Person> listPerson = new ArrayList<>();
        listPerson.add(new Person(R.drawable._5, "Người thứ 1"));
        listPerson.add(new Person(R.drawable._6, "Người thứ 2"));
        listPerson.add(new Person(R.drawable._7, "Người thứ 3"));
        listPerson.add(new Person(R.drawable._8, "Người thứ 4"));
        PersonAdapter personAdapter = new PersonAdapter(this, R.layout.spinner_item, listPerson);
        spinnerPerson.setAdapter(personAdapter);
        spinnerPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected Person object
                Person selectedPerson = personAdapter.getItem(position);

                if (selectedPerson != null) {
                    String selectedName = selectedPerson.getNamePerson();
                    Toast.makeText(MainActivity.this, "Bạn đã chọn " + selectedName,
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
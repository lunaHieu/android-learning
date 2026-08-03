package com.example.lab05_bai2_vanhieu;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView; // Nhớ import ListView
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewPerson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewPerson = findViewById(R.id.listViewPerson);
        List<Person> listPerson = new ArrayList<>();
        listPerson.add(new Person(R.drawable._1, "Người thứ 1"));
        listPerson.add(new Person(R.drawable._2, "Người thứ 2"));
        listPerson.add(new Person(R.drawable._3, "Người thứ 3"));
        listPerson.add(new Person(R.drawable._4, "Người thứ 4"));
        PersonAdapter personAdapter = new PersonAdapter(this, R.layout.spinner_item, listPerson);

        listViewPerson.setAdapter(personAdapter);
        listViewPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person selectedPerson = listPerson.get(position);
                Toast.makeText(MainActivity.this,
                        "Bạn vừa chọn " + selectedPerson.getNamePerson(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
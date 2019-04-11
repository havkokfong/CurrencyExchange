package com.example.currencyexchange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editText;
    private Button exchange;
    private ArrayList countryList;
    private CountryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText1);
        exchange = findViewById(R.id.exchangeButton);

        initlist();
        Spinner spinner = findViewById(R.id.Spinner1);

        mAdapter = new CountryAdapter(this, countryList);
        spinner.setAdapter(mAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryList countrySelected = (CountryList) parent.getItemAtPosition(position);
                String countrySelectedName = countrySelected.getCountryNameV();
                Toast.makeText(MainActivity.this, countrySelectedName + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initlist (){
        countryList = new ArrayList<>();
        countryList.add(new CountryList("SGD", R.drawable.singapore));
        countryList.add(new CountryList("USD", R.drawable.usa));
        countryList.add(new CountryList("AUD", R.drawable.australia));
        countryList.add(new CountryList("NZD", R.drawable.newzealand));
        countryList.add(new CountryList("EUR", R.drawable.euro));
        countryList.add(new CountryList("CAD", R.drawable.canada));
        countryList.add(new CountryList("PHP", R.drawable.philippine));
        countryList.add(new CountryList("HKD", R.drawable.hongkong));
        countryList.add(new CountryList("THB", R.drawable.thailand));
        countryList.add(new CountryList("CNY", R.drawable.china));

    }
}

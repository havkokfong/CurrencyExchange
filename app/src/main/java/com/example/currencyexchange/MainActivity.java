package com.example.currencyexchange;

import android.annotation.SuppressLint;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button exchange;
    private ArrayList countryList;
    private CountryAdapter mAdapter;
    private RequestQueue mQueue;
    private TextView sgdRate, usdRate, audRate, nzdRate, eurRate, cadRate, cnyRate, hkdRate,phpRate,
            thbRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText1);
        exchange = findViewById(R.id.exchangeButton);
        sgdRate = findViewById(R.id.SGDRate);
        usdRate = findViewById(R.id.USDRate);
        audRate = findViewById(R.id.AUDRate);
        nzdRate = findViewById(R.id.NZDRate);
        eurRate = findViewById(R.id.EURRate);
        cadRate = findViewById(R.id.CADRate);
        cnyRate = findViewById(R.id.CNYRate);
        hkdRate = findViewById(R.id.HKDRate);
        phpRate = findViewById(R.id.PHPRate);
        thbRate = findViewById(R.id.THDRate);
        mQueue = Volley.newRequestQueue(this);
        initlist();


        Spinner spinner1 = findViewById(R.id.Spinner1);
        final Spinner spinner2 = findViewById(R.id.Spinner2);

        mAdapter = new CountryAdapter(this, countryList);
        spinner1.setAdapter(mAdapter);
        spinner2.setAdapter(mAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryList countrySelected = (CountryList) parent.getItemAtPosition(position);
                String countrySelectedName = countrySelected.getCountryNameV();
                Toast.makeText(MainActivity.this, countrySelectedName + " selected", Toast.LENGTH_SHORT).show();
                jsonparse(countrySelectedName);
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initlist (){
        countryList = new ArrayList<>();
        countryList.add(new CountryList("SGD", R.drawable.singapore));
        countryList.add(new CountryList("USD", R.drawable.usa));
        countryList.add(new CountryList("AUD", R.drawable.australia));
        countryList.add(new CountryList("NZD", R.drawable.newzealand));
        countryList.add(new CountryList("EUR", R.drawable.euro));
        countryList.add(new CountryList("CAD", R.drawable.canada));
        countryList.add(new CountryList("CNY", R.drawable.china));
        countryList.add(new CountryList("HKD", R.drawable.hongkong));
        countryList.add(new CountryList("PHP", R.drawable.philippine));
        countryList.add(new CountryList("THB", R.drawable.thailand));
    }

    private void jsonparse(String name){

        if (name.equals("SGD")){
        String url = "https://api.exchangeratesapi.io/latest?base=SGD";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("rates");
                            double SGD = jsonObject.getDouble("SGD");
                            double USD = jsonObject.getDouble("USD");
                            double AUD = jsonObject.getDouble("AUD");
                            double NZD = jsonObject.getDouble("NZD");
                            double EUR = jsonObject.getDouble("EUR");
                            double CAD = jsonObject.getDouble("CAD");
                            double CNY = jsonObject.getDouble("CNY");
                            double HKD = jsonObject.getDouble("HKD");
                            double PHP = jsonObject.getDouble("PHP");
                            double THB = jsonObject.getDouble("THB");
                            sgdRate.setText("SGD\n" + String.valueOf(String.format("%.4f", SGD)));
                            usdRate.setText("USD\n" + String.valueOf(String.format("%.4f", USD)));
                            audRate.setText("AUD\n" + String.valueOf(String.format("%.4f", AUD)));
                            nzdRate.setText("NZD\n" + String.valueOf(String.format("%.4f", NZD)));
                            eurRate.setText("EUR\n" + String.valueOf(String.format("%.4f", EUR)));
                            cadRate.setText("CAD\n" + String.valueOf(String.format("%.4f", CAD)));
                            cnyRate.setText("CNY\n" + String.valueOf(String.format("%.4f", CNY)));
                            hkdRate.setText("HKD\n" + String.valueOf(String.format("%.4f", HKD)));
                            phpRate.setText("PHP\n" + String.valueOf(String.format("%.4f", PHP)));
                            thbRate.setText("THB\n" + String.valueOf(String.format("%.4f", THB)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }



    }
}

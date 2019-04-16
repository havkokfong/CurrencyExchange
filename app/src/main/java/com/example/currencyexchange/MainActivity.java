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

    private EditText editText1;
    private Button exchange;
    private ArrayList countryList;
    private CountryAdapter mAdapter;
    private RequestQueue mQueue;
    private TextView sgdRate, usdRate, audRate, nzdRate, eurRate, cadRate, cnyRate, hkdRate,phpRate,
            thbRate, viewText2;
    double result ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = findViewById(R.id.editText1);
        viewText2 = findViewById(R.id.viewText2);
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


        final Spinner spinner1 = findViewById(R.id.Spinner1);
        final Spinner spinner2 = findViewById(R.id.Spinner2);

        mAdapter = new CountryAdapter(this, countryList);
        spinner1.setAdapter(mAdapter);
        spinner2.setAdapter(mAdapter);
        String url = "https://api.exchangeratesapi.io/latest?base=SGD";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("rates");
                            final double SGD = jsonObject.getDouble("SGD");
                            final double USD = jsonObject.getDouble("USD");
                            final double AUD = jsonObject.getDouble("AUD");
                            final double NZD = jsonObject.getDouble("NZD");
                            final double EUR = jsonObject.getDouble("EUR");
                            final double CAD = jsonObject.getDouble("CAD");
                            final double CNY = jsonObject.getDouble("CNY");
                            final double HKD = jsonObject.getDouble("HKD");
                            final double PHP = jsonObject.getDouble("PHP");
                            final double THB = jsonObject.getDouble("THB");
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

                            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    CountryList countrySelected = (CountryList) parent.getItemAtPosition(position);
                                    final String countrySelectedName = countrySelected.getCountryNameV();
                                    Toast.makeText(MainActivity.this, countrySelectedName + " selected", Toast.LENGTH_SHORT).show();
                                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            CountryList countrySelected2 = (CountryList) parent.getItemAtPosition(position);
                                            String countrySelectedName2 = countrySelected2.getCountryNameV();
                                            Toast.makeText(MainActivity.this, countrySelectedName2 + " selected", Toast.LENGTH_SHORT).show();
                                            calculate(countrySelectedName, countrySelectedName2, SGD,
                                                    USD,AUD,NZD,EUR,CAD,CNY,HKD,PHP,THB);
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
        exchange.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                viewText2.setText(String.valueOf(String.format("%.4f", result)));
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

   /* private void jsonparse(String name){

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
    }*/

   public double calculate(String selected1, String selected2, double SGD, double USD, double AUD, double NZD,
                     double EUR, double CAD, double CNY, double HKD, double PHP, double THB) {
       try {

           if (selected1.equals("SGD")) {
               if (selected2.equals("SGD")) {
                   result = SGD;
               } else if (selected2.equals("USD")) {
                   result = USD * Double.parseDouble(String.valueOf(editText1.getText()));
               } else if (selected2.equals("AUD")) {
                   result = AUD * Double.parseDouble(String.valueOf(editText1.getText()));
               } else if (selected2.equals("NZD")) {
                   result = NZD * Double.parseDouble(String.valueOf(editText1.getText()));
               } else if (selected2.equals("EUR")) {
                   result = EUR * Double.parseDouble(String.valueOf(editText1.getText()));
               } else if (selected2.equals("CAD")) {
                   result = CAD * Double.parseDouble(String.valueOf(editText1.getText()));
               } else if (selected2.equals("CNY")) {
                   result = CNY * Double.parseDouble(String.valueOf(editText1.getText()));
               } else if (selected2.equals("HKD")) {
                   result = HKD * Double.parseDouble(String.valueOf(editText1.getText()));
               } else if (selected2.equals("PHP")) {
                   result = PHP * Double.parseDouble(String.valueOf(editText1.getText()));
               } else {
                   result = THB * Double.parseDouble(String.valueOf(editText1.getText()));
               }
           }

       }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
       }return result;
   }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

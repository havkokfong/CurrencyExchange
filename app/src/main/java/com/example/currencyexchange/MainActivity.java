package com.example.currencyexchange;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.currencyexchange.R.id.action_setting;

public class MainActivity extends AppCompatActivity {

    private EditText editText1;
    private Button exchange, refresh;
    private ArrayList countryList;
    private CountryAdapter mAdapter;
    private RequestQueue mQueue;
    private TextView sgdRate, usdRate, audRate, nzdRate, eurRate, cadRate, cnyRate, hkdRate,phpRate,
            thbRate, viewText2;
    double result, result2;
    int choice;
    private SharedPreferences preferences;
    private LinearLayout activity_layout;
    DecimalFormat currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        currency = new DecimalFormat("###,###,###.####");
        preferences = getSharedPreferences("value", MODE_PRIVATE);
        editText1 = findViewById(R.id.editText1);
        viewText2 = findViewById(R.id.viewText2);
        exchange = findViewById(R.id.exchangeButton);
        refresh = findViewById(R.id.refreshButton);
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
        activity_layout = findViewById(R.id.activity_layout);
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
                            sgdRate.setText("SGD\n" + String.valueOf(currency.format(SGD)));
                            usdRate.setText("USD\n" + String.valueOf(currency.format(USD)));
                            audRate.setText("AUD\n" + String.valueOf(currency.format(AUD)));
                            nzdRate.setText("NZD\n" + String.valueOf(currency.format(NZD)));
                            eurRate.setText("EUR\n" + String.valueOf(currency.format(EUR)));
                            cadRate.setText("CAD\n" + String.valueOf(currency.format(CAD)));
                            cnyRate.setText("CNY\n" + String.valueOf(currency.format(CNY)));
                            hkdRate.setText("HKD\n" + String.valueOf(currency.format(HKD)));
                            phpRate.setText("PHP\n" + String.valueOf(currency.format(PHP)));
                            thbRate.setText("THB\n" + String.valueOf(currency.format(THB)));

                            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    CountryList countrySelected = (CountryList) parent.getItemAtPosition(position);
                                    final String countrySelectedName = countrySelected.getCountryNameV();
                                    Toast.makeText(MainActivity.this, countrySelectedName + " selected", Toast.LENGTH_SHORT).show();
                                    calculate(countrySelectedName, SGD,
                                            USD,AUD,NZD,EUR,CAD,CNY,HKD,PHP,THB);
                                    update_Table(countrySelectedName);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @SuppressLint("CommitPrefEdits")
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    CountryList countrySelected2 = (CountryList) parent.getItemAtPosition(position);
                                    String countrySelectedName2 = countrySelected2.getCountryNameV();
                                    Toast.makeText(MainActivity.this, countrySelectedName2 + " selected", Toast.LENGTH_SHORT).show();
                                    calculate2(countrySelectedName2, SGD,
                                            USD,AUD,NZD,EUR,CAD,CNY,HKD,PHP,THB);

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
        result = 1.0;
        result2 = 1.0;
        exchange.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                try {
                    double total = (result2/result) * Double.parseDouble(String.valueOf(editText1.getText()));
                    viewText2.setText(String.valueOf(currency.format(total)));
                }catch (Exception e){}
                close_Keyboard();
            }
        });
    }

    // spinner list
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

    //checking condition for calculate the currency
   public double calculate(String selected1, double SGD, double USD, double AUD, double NZD,
                           double EUR, double CAD, double CNY, double HKD, double PHP, double THB) {

      try {
          if (selected1.equals("SGD")) {
              result = SGD;
          } else if (selected1.equals("USD")) {
              result = USD;
          } else if (selected1.equals("AUD")) {
              result = AUD;
          } else if (selected1.equals("NZD")) {
              result = NZD;
          } else if (selected1.equals("EUR")) {
              result = EUR;
          } else if (selected1.equals("CAD")) {
              result = CAD;
          } else if (selected1.equals("CNY")) {
              result = CNY;
          } else if (selected1.equals("HKD")) {
              result = HKD;
          } else if (selected1.equals("PHP")) {
              result = PHP;
          } else {
              result = THB;
          }
          return result;

       }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
       }return result;
   }


    //checking condition for calculate the currency
    public double calculate2(String selected2, double SGD, double USD, double AUD, double NZD,
                            double EUR, double CAD, double CNY, double HKD, double PHP, double THB) {

        try {

            if (selected2.equals("SGD")) {
                result2 = SGD;
            } else if (selected2.equals("USD")) {
                result2 = USD;
            } else if (selected2.equals("AUD")) {
                result2 = AUD;
            } else if (selected2.equals("NZD")) {
                result2 = NZD;
            } else if (selected2.equals("EUR")) {
                result2 = EUR;
            } else if (selected2.equals("CAD")) {
                result2 = CAD;
            } else if (selected2.equals("CNY")) {
                result2 = CNY;
            } else if (selected2.equals("HKD")) {
                result2 = HKD;
            } else if (selected2.equals("PHP")) {
                result2 = PHP;
            } else {
                result2 = THB;
            }
            return result2;
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }return result;
    }

   public void refresher(View view){
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
                           sgdRate.setText("SGD\n" + String.valueOf(currency.format(SGD)));
                           usdRate.setText("USD\n" + String.valueOf(currency.format(USD)));
                           audRate.setText("AUD\n" + String.valueOf(currency.format(AUD)));
                           nzdRate.setText("NZD\n" + String.valueOf(currency.format(NZD)));
                           eurRate.setText("EUR\n" + String.valueOf(currency.format(EUR)));
                           cadRate.setText("CAD\n" + String.valueOf(currency.format(CAD)));
                           cnyRate.setText("CNY\n" + String.valueOf(currency.format(CNY)));
                           hkdRate.setText("HKD\n" + String.valueOf(currency.format(HKD)));
                           phpRate.setText("PHP\n" + String.valueOf(currency.format(PHP)));
                           thbRate.setText("THB\n" + String.valueOf(currency.format(THB)));
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
       editText1.setText(null);
       viewText2.setText(null);
   }

   public void update_Table(String country){
        if (country.equals("USD")){
            String url = "https://api.exchangeratesapi.io/latest?base=USD";
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
                                sgdRate.setText("SGD\n" + String.valueOf(currency.format(SGD)));
                                usdRate.setText("USD\n" + String.valueOf(currency.format(USD)));
                                audRate.setText("AUD\n" + String.valueOf(currency.format(AUD)));
                                nzdRate.setText("NZD\n" + String.valueOf(currency.format(NZD)));
                                eurRate.setText("EUR\n" + String.valueOf(currency.format(EUR)));
                                cadRate.setText("CAD\n" + String.valueOf(currency.format(CAD)));
                                cnyRate.setText("CNY\n" + String.valueOf(currency.format(CNY)));
                                hkdRate.setText("HKD\n" + String.valueOf(currency.format(HKD)));
                                phpRate.setText("PHP\n" + String.valueOf(currency.format(PHP)));
                                thbRate.setText("THB\n" + String.valueOf(currency.format(THB)));
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
        else if (country.equals("SGD")){
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
                                sgdRate.setText("SGD\n" + String.valueOf(currency.format(SGD)));
                                usdRate.setText("USD\n" + String.valueOf(currency.format(USD)));
                                audRate.setText("AUD\n" + String.valueOf(currency.format(AUD)));
                                nzdRate.setText("NZD\n" + String.valueOf(currency.format(NZD)));
                                eurRate.setText("EUR\n" + String.valueOf(currency.format(EUR)));
                                cadRate.setText("CAD\n" + String.valueOf(currency.format(CAD)));
                                cnyRate.setText("CNY\n" + String.valueOf(currency.format(CNY)));
                                hkdRate.setText("HKD\n" + String.valueOf(currency.format(HKD)));
                                phpRate.setText("PHP\n" + String.valueOf(currency.format(PHP)));
                                thbRate.setText("THB\n" + String.valueOf(currency.format(THB)));
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

        else if (country.equals("AUD")){
            String url = "https://api.exchangeratesapi.io/latest?base=AUD";
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
                                sgdRate.setText("SGD\n" + String.valueOf(currency.format(SGD)));
                                usdRate.setText("USD\n" + String.valueOf(currency.format(USD)));
                                audRate.setText("AUD\n" + String.valueOf(currency.format(AUD)));
                                nzdRate.setText("NZD\n" + String.valueOf(currency.format(NZD)));
                                eurRate.setText("EUR\n" + String.valueOf(currency.format(EUR)));
                                cadRate.setText("CAD\n" + String.valueOf(currency.format(CAD)));
                                cnyRate.setText("CNY\n" + String.valueOf(currency.format(CNY)));
                                hkdRate.setText("HKD\n" + String.valueOf(currency.format(HKD)));
                                phpRate.setText("PHP\n" + String.valueOf(currency.format(PHP)));
                                thbRate.setText("THB\n" + String.valueOf(currency.format(THB)));
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

        else if (country.equals("NZD")){
            String url = "https://api.exchangeratesapi.io/latest?base=NZD";
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
                                sgdRate.setText("SGD\n" + String.valueOf(currency.format(SGD)));
                                usdRate.setText("USD\n" + String.valueOf(currency.format(USD)));
                                audRate.setText("AUD\n" + String.valueOf(currency.format(AUD)));
                                nzdRate.setText("NZD\n" + String.valueOf(currency.format(NZD)));
                                eurRate.setText("EUR\n" + String.valueOf(currency.format(EUR)));
                                cadRate.setText("CAD\n" + String.valueOf(currency.format(CAD)));
                                cnyRate.setText("CNY\n" + String.valueOf(currency.format(CNY)));
                                hkdRate.setText("HKD\n" + String.valueOf(currency.format(HKD)));
                                phpRate.setText("PHP\n" + String.valueOf(currency.format(PHP)));
                                thbRate.setText("THB\n" + String.valueOf(currency.format(THB)));
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

        else if (country.equals("EUR")){
            String url = "https://api.exchangeratesapi.io/latest?base=EUR";
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
                                sgdRate.setText("SGD\n" + String.valueOf(currency.format(SGD)));
                                usdRate.setText("USD\n" + String.valueOf(currency.format(USD)));
                                audRate.setText("AUD\n" + String.valueOf(currency.format(AUD)));
                                nzdRate.setText("NZD\n" + String.valueOf(currency.format(NZD)));
                                eurRate.setText("EUR\n" + String.valueOf(currency.format(EUR)));
                                cadRate.setText("CAD\n" + String.valueOf(currency.format(CAD)));
                                cnyRate.setText("CNY\n" + String.valueOf(currency.format(CNY)));
                                hkdRate.setText("HKD\n" + String.valueOf(currency.format(HKD)));
                                phpRate.setText("PHP\n" + String.valueOf(currency.format(PHP)));
                                thbRate.setText("THB\n" + String.valueOf(currency.format(THB)));
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

        else if (country.equals("CAD")){
            String url = "https://api.exchangeratesapi.io/latest?base=CAD";
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
                                sgdRate.setText("SGD\n" + String.valueOf(currency.format(SGD)));
                                usdRate.setText("USD\n" + String.valueOf(currency.format(USD)));
                                audRate.setText("AUD\n" + String.valueOf(currency.format(AUD)));
                                nzdRate.setText("NZD\n" + String.valueOf(currency.format(NZD)));
                                eurRate.setText("EUR\n" + String.valueOf(currency.format(EUR)));
                                cadRate.setText("CAD\n" + String.valueOf(currency.format(CAD)));
                                cnyRate.setText("CNY\n" + String.valueOf(currency.format(CNY)));
                                hkdRate.setText("HKD\n" + String.valueOf(currency.format(HKD)));
                                phpRate.setText("PHP\n" + String.valueOf(currency.format(PHP)));
                                thbRate.setText("THB\n" + String.valueOf(currency.format(THB)));
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

        else if (country.equals("CNY")){
            String url = "https://api.exchangeratesapi.io/latest?base=CNY";
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
                                sgdRate.setText("SGD\n" + String.valueOf(currency.format(SGD)));
                                usdRate.setText("USD\n" + String.valueOf(currency.format(USD)));
                                audRate.setText("AUD\n" + String.valueOf(currency.format(AUD)));
                                nzdRate.setText("NZD\n" + String.valueOf(currency.format(NZD)));
                                eurRate.setText("EUR\n" + String.valueOf(currency.format(EUR)));
                                cadRate.setText("CAD\n" + String.valueOf(currency.format(CAD)));
                                cnyRate.setText("CNY\n" + String.valueOf(currency.format(CNY)));
                                hkdRate.setText("HKD\n" + String.valueOf(currency.format(HKD)));
                                phpRate.setText("PHP\n" + String.valueOf(currency.format(PHP)));
                                thbRate.setText("THB\n" + String.valueOf(currency.format(THB)));
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
        else if (country.equals("HKD")){
            String url = "https://api.exchangeratesapi.io/latest?base=HKD";
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
                                sgdRate.setText("SGD\n" + String.valueOf(currency.format(SGD)));
                                usdRate.setText("USD\n" + String.valueOf(currency.format(USD)));
                                audRate.setText("AUD\n" + String.valueOf(currency.format(AUD)));
                                nzdRate.setText("NZD\n" + String.valueOf(currency.format(NZD)));
                                eurRate.setText("EUR\n" + String.valueOf(currency.format(EUR)));
                                cadRate.setText("CAD\n" + String.valueOf(currency.format(CAD)));
                                cnyRate.setText("CNY\n" + String.valueOf(currency.format(CNY)));
                                hkdRate.setText("HKD\n" + String.valueOf(currency.format(HKD)));
                                phpRate.setText("PHP\n" + String.valueOf(currency.format(PHP)));
                                thbRate.setText("THB\n" + String.valueOf(currency.format(THB)));
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
        else if (country.equals("PHP")){
            String url = "https://api.exchangeratesapi.io/latest?base=PHP";
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
                                sgdRate.setText("SGD\n" + String.valueOf(currency.format(SGD)));
                                usdRate.setText("USD\n" + String.valueOf(currency.format(USD)));
                                audRate.setText("AUD\n" + String.valueOf(currency.format(AUD)));
                                nzdRate.setText("NZD\n" + String.valueOf(currency.format(NZD)));
                                eurRate.setText("EUR\n" + String.valueOf(currency.format(EUR)));
                                cadRate.setText("CAD\n" + String.valueOf(currency.format(CAD)));
                                cnyRate.setText("CNY\n" + String.valueOf(currency.format(CNY)));
                                hkdRate.setText("HKD\n" + String.valueOf(currency.format(HKD)));
                                phpRate.setText("PHP\n" + String.valueOf(currency.format(PHP)));
                                thbRate.setText("THB\n" + String.valueOf(currency.format(THB)));
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
        else if (country.equals("THB")){
            String url = "https://api.exchangeratesapi.io/latest?base=THB";
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
                                sgdRate.setText("SGD\n" + String.valueOf(currency.format(SGD)));
                                usdRate.setText("USD\n" + String.valueOf(currency.format(USD)));
                                audRate.setText("AUD\n" + String.valueOf(currency.format(AUD)));
                                nzdRate.setText("NZD\n" + String.valueOf(currency.format(NZD)));
                                eurRate.setText("EUR\n" + String.valueOf(currency.format(EUR)));
                                cadRate.setText("CAD\n" + String.valueOf(currency.format(CAD)));
                                cnyRate.setText("CNY\n" + String.valueOf(currency.format(CNY)));
                                hkdRate.setText("HKD\n" + String.valueOf(currency.format(HKD)));
                                phpRate.setText("PHP\n" + String.valueOf(currency.format(PHP)));
                                thbRate.setText("THB\n" + String.valueOf(currency.format(THB)));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case action_setting:
                Intent setting = new Intent (this, setting_options.class);
                startActivity(setting);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        choice = preferences.getInt("theme_preferences", 0);
        if (choice == 0){
            activity_layout.setBackgroundResource(R.drawable.background5);
        }

        else if (choice == 1){
            activity_layout.setBackgroundResource(R.drawable.background2);
        }

        else if (choice == 2){
            activity_layout.setBackgroundResource(R.drawable.background6);
        }

        else if (choice == 3){
            activity_layout.setBackgroundResource(R.drawable.background4);
        }

    }

    private void close_Keyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

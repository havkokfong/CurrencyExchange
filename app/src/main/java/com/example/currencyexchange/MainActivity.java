package com.example.currencyexchange;

import android.annotation.SuppressLint;
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
    double result ;
    int choice;
    private SharedPreferences preferences;
    private LinearLayout activity_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

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
                                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                                    CountryList countrySelected = (CountryList) parent.getItemAtPosition(position);
                                    final String countrySelectedName = countrySelected.getCountryNameV();
                                    Toast.makeText(MainActivity.this, countrySelectedName + " selected", Toast.LENGTH_SHORT).show();
                                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @SuppressLint("CommitPrefEdits")
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
        result = 1.0;
        exchange.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                try {
                    double total = result * Double.parseDouble(String.valueOf(editText1.getText()));
                    viewText2.setText(String.valueOf(String.format("%.4f", total)));
                }catch (Exception e){}

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
   public double calculate(String selected1, String selected2, double SGD, double USD, double AUD, double NZD,
                           double EUR, double CAD, double CNY, double HKD, double PHP, double THB) {

      try {

           if (selected1.equals("SGD")) {
               if (selected2.equals("SGD")) {
                   result = SGD;
               } else if (selected2.equals("USD")) {
                   result = USD;
               } else if (selected2.equals("AUD")) {
                   result = AUD;
               } else if (selected2.equals("NZD")) {
                   result = NZD;
               } else if (selected2.equals("EUR")) {
                   result = EUR;
               } else if (selected2.equals("CAD")) {
                   result = CAD;
               } else if (selected2.equals("CNY")) {
                   result = CNY;
               } else if (selected2.equals("HKD")) {
                   result = HKD;
               } else if (selected2.equals("PHP")) {
                   result = PHP;
               } else {
                   result = THB;
               }return result;
           }

           else if (selected1.equals("USD")) {
              if (selected2.equals("SGD")) {
                  result = (SGD/USD);
              } else if (selected2.equals("USD")) {
                  result = 1.00;
              } else if (selected2.equals("AUD")) {
                  result = (AUD/USD);
              } else if (selected2.equals("NZD")) {
                  result = NZD/USD;
              } else if (selected2.equals("EUR")) {
                  result = EUR/USD;
              } else if (selected2.equals("CAD")) {
                  result = CAD/USD;
              } else if (selected2.equals("CNY")) {
                  result = CNY/USD;
              } else if (selected2.equals("HKD")) {
                  result = HKD/USD;
              } else if (selected2.equals("PHP")) {
                  result = PHP/USD;
              } else {
                  result = THB/USD;
              }return result;
          }

           else if (selected1.equals("AUD")) {
               if (selected2.equals("SGD")) {
                   result = (SGD/AUD);
               } else if (selected2.equals("USD")) {
                   result = USD/AUD;
               } else if (selected2.equals("AUD")) {
                   result = 1.0;
               } else if (selected2.equals("NZD")) {
                   result = NZD/AUD;
               } else if (selected2.equals("EUR")) {
                   result = EUR/AUD;
               } else if (selected2.equals("CAD")) {
                   result = CAD/AUD;
               } else if (selected2.equals("CNY")) {
                   result = CNY/AUD;
               } else if (selected2.equals("HKD")) {
                   result = HKD/AUD;
               } else if (selected2.equals("PHP")) {
                   result = PHP/AUD;
               } else {
                   result = THB/AUD;
               }return result;
           }

           else if (selected1.equals("NZD")) {
               if (selected2.equals("SGD")) {
                   result = (SGD/NZD);
               } else if (selected2.equals("USD")) {
                   result = USD/NZD;
               } else if (selected2.equals("AUD")) {
                   result = AUD/NZD;
               } else if (selected2.equals("NZD")) {
                   result = 1.0;
               } else if (selected2.equals("EUR")) {
                   result = EUR/NZD;
               } else if (selected2.equals("CAD")) {
                   result = CAD/NZD;
               } else if (selected2.equals("CNY")) {
                   result = CNY/NZD;
               } else if (selected2.equals("HKD")) {
                   result = HKD/NZD;
               } else if (selected2.equals("PHP")) {
                   result = PHP/NZD;
               } else {
                   result = THB/NZD;
               }return result;
           }
           else if (selected1.equals("EUR")) {
               if (selected2.equals("SGD")) {
                   result = (SGD/EUR);
               } else if (selected2.equals("USD")) {
                   result = USD/EUR;
               } else if (selected2.equals("AUD")) {
                   result = AUD/EUR;
               } else if (selected2.equals("NZD")) {
                   result = NZD/EUR;
               } else if (selected2.equals("EUR")) {
                   result = 1.0;
               } else if (selected2.equals("CAD")) {
                   result = CAD/EUR;
               } else if (selected2.equals("CNY")) {
                   result = CNY/EUR;
               } else if (selected2.equals("HKD")) {
                   result = HKD/EUR;
               } else if (selected2.equals("PHP")) {
                   result = PHP/EUR;
               } else {
                   result = THB/EUR;
               }return result;
           }

           else if (selected1.equals("CAD")) {
               if (selected2.equals("SGD")) {
                   result = (SGD/CAD);
               } else if (selected2.equals("USD")) {
                   result = USD/CAD;
               } else if (selected2.equals("AUD")) {
                   result = AUD/CAD;
               } else if (selected2.equals("NZD")) {
                   result = NZD/CAD;
               } else if (selected2.equals("EUR")) {
                   result = EUR/CAD;
               } else if (selected2.equals("CAD")) {
                   result = 1.0;
               } else if (selected2.equals("CNY")) {
                   result = CNY/CAD;
               } else if (selected2.equals("HKD")) {
                   result = HKD/CAD;
               } else if (selected2.equals("PHP")) {
                   result = PHP/CAD;
               } else {
                   result = THB/CAD;
               }return result;
           }

           else if (selected1.equals("CNY")) {
               if (selected2.equals("SGD")) {
                   result = (SGD/CNY);
               } else if (selected2.equals("USD")) {
                   result = USD/CNY;
               } else if (selected2.equals("AUD")) {
                   result = AUD/CNY;
               } else if (selected2.equals("NZD")) {
                   result = NZD/CNY;
               } else if (selected2.equals("EUR")) {
                   result = EUR/CNY;
               } else if (selected2.equals("CAD")) {
                   result = CAD/CNY;
               } else if (selected2.equals("CNY")) {
                   result = 1.0;
               } else if (selected2.equals("HKD")) {
                   result = HKD/CNY;
               } else if (selected2.equals("PHP")) {
                   result = PHP/CNY;
               } else {
                   result = THB/CNY;
               }return result;
           }

           else if (selected1.equals("HKD")) {
               if (selected2.equals("SGD")) {
                   result = (SGD/HKD);
               } else if (selected2.equals("USD")) {
                   result = USD/HKD;
               } else if (selected2.equals("AUD")) {
                   result = AUD/HKD;
               } else if (selected2.equals("NZD")) {
                   result = NZD/HKD;
               } else if (selected2.equals("EUR")) {
                   result = EUR/HKD;
               } else if (selected2.equals("CAD")) {
                   result = CAD/HKD;
               } else if (selected2.equals("CNY")) {
                   result = CNY/HKD;
               } else if (selected2.equals("HKD")) {
                   result = 1.0;
               } else if (selected2.equals("PHP")) {
                   result = PHP/HKD;
               } else {
                   result = THB/HKD;
               }return result;
           }

           else if (selected1.equals("PHP")) {
               if (selected2.equals("SGD")) {
                   result = (SGD/PHP);
               } else if (selected2.equals("USD")) {
                   result = USD/PHP;
               } else if (selected2.equals("AUD")) {
                   result = AUD/PHP;
               } else if (selected2.equals("NZD")) {
                   result = NZD/PHP;
               } else if (selected2.equals("EUR")) {
                   result = EUR/PHP;
               } else if (selected2.equals("CAD")) {
                   result = CAD/PHP;
               } else if (selected2.equals("CNY")) {
                   result = CNY/PHP;
               } else if (selected2.equals("HKD")) {
                   result = HKD/PHP;
               } else if (selected2.equals("PHP")) {
                   result = 1.0;
               } else {
                   result = THB/HKD;
               }return result;
           }

           else {
               if (selected2.equals("SGD")) {
                   result = (SGD/THB);
               } else if (selected2.equals("USD")) {
                   result = USD/THB;
               } else if (selected2.equals("AUD")) {
                   result = AUD/THB;
               } else if (selected2.equals("NZD")) {
                   result = NZD/THB;
               } else if (selected2.equals("EUR")) {
                   result = EUR/THB;
               } else if (selected2.equals("CAD")) {
                   result = CAD/THB;
               } else if (selected2.equals("CNY")) {
                   result = CNY/THB;
               } else if (selected2.equals("HKD")) {
                   result = HKD/THB;
               } else if (selected2.equals("PHP")) {
                   result = PHP/THB;
               } else {
                   result = 1.0;
               }return result;
           }

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
       editText1.setText(null);
       viewText2.setText(null);
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
            activity_layout.setBackgroundResource(R.drawable.background);
        }

        else if (choice == 1){
            activity_layout.setBackgroundResource(R.drawable.background2);
        }

        else if (choice == 2){
            activity_layout.setBackgroundResource(R.drawable.background3);
        }
    }
}

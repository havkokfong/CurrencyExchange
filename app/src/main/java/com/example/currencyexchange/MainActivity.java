package com.example.currencyexchange;

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
    private TextView sgdRate, usdRate, audRate;
    private ArrayList mrateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText1);
        exchange = findViewById(R.id.exchangeButton);
        sgdRate = findViewById(R.id.SGDRate);
        usdRate = findViewById(R.id.USDRate);
        audRate = findViewById(R.id.AUDRate);
        mQueue = Volley.newRequestQueue(this);
        initlist();
        jsonparse();

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

    private void jsonparse(){
        String url = "https://api.exchangeratesapi.io/latest?base=SGD";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
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
                            sgdRate.append(String.valueOf(SGD));
                            usdRate.append(String.valueOf(USD));

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

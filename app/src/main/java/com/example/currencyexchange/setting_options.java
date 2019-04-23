package com.example.currencyexchange;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.currencyexchange.R.id.action_setting;


public class setting_options extends AppCompatActivity {

    private SharedPreferences preferences;
    int choices;
    private LinearLayout main_layout;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_options);
        Toolbar toolbar = findViewById(R.id.action_bar_option);
        setSupportActionBar(toolbar);

        main_layout = findViewById(R.id.linear_background);
        title = findViewById(R.id.themeText);
        preferences = getSharedPreferences("value", MODE_PRIVATE);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onStart() {
        super.onStart();
        choices = preferences.getInt("theme_preferences", 0);
        if (choices == 0){
            main_layout.setBackgroundResource(R.drawable.background5);
        }
        else if (choices == 1){
            main_layout.setBackgroundResource(R.drawable.background2);
        }
        else if (choices == 2){
            main_layout.setBackgroundResource(R.drawable.background6);
        }
        else if (choices == 3){
            main_layout.setBackgroundResource(R.drawable.background4);

        }
    }

    public void Background_1 (View view){
        main_layout.setBackgroundResource(R.drawable.background5);
        preferences.edit().putInt("theme_preferences", 0).apply();
    }

    public void Background_2 (View view){
        main_layout.setBackgroundResource(R.drawable.background2);
        preferences.edit().putInt("theme_preferences", 1).apply();
    }

    public void Background_3 (View view){
        main_layout.setBackgroundResource(R.drawable.background6);
        preferences.edit().putInt("theme_preferences", 2).apply();
    }


    public void Background_4 (View view){
        main_layout.setBackgroundResource(R.drawable.background4);
        preferences.edit().putInt("theme_preferences", 3).apply();
    }

    public void Back (View view){
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
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
}

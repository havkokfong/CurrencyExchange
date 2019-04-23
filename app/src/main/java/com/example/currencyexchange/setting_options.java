package com.example.currencyexchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import static com.example.currencyexchange.R.id.action_setting;


public class setting_options extends AppCompatActivity {

    private SharedPreferences preferences;
    int choices;
    private LinearLayout main_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_options);
        Toolbar toolbar = findViewById(R.id.action_bar_option);
        setSupportActionBar(toolbar);

        main_layout = findViewById(R.id.linear_background);
        preferences = getSharedPreferences("value", MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        choices = preferences.getInt("theme_preferences", 0);
        if (choices == 0){
            main_layout.setBackgroundResource(R.drawable.background);
        }
        else if (choices == 1){
            main_layout.setBackgroundResource(R.drawable.background2);
        }
        else if (choices == 2){
            main_layout.setBackgroundResource(R.drawable.background3);
        }
    }

    public void Background_1 (View view){
        main_layout.setBackgroundResource(R.drawable.background);
        preferences.edit().putInt("theme_preferences", 0).apply();
    }

    public void Background_2 (View view){
        main_layout.setBackgroundResource(R.drawable.background2);
        preferences.edit().putInt("theme_preferences", 1).apply();
    }

    public void Background_3 (View view){
        main_layout.setBackgroundResource(R.drawable.background3);
        preferences.edit().putInt("theme_preferences", 2).apply();
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

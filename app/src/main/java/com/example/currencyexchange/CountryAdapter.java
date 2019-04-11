package com.example.currencyexchange;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<CountryList> {

    public CountryAdapter(Context context, ArrayList<CountryList> countryNameLists){
        super(context, 0, countryNameLists);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView (int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.currency_spinner_row, parent, false
            );
        }

        ImageView imageViewFlag = convertView.findViewById(R.id.flag_image);
        TextView textViewName = convertView.findViewById(R.id.country_name_text);

        CountryList currentItem = getItem(position);

        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.getFlagImageV());
            textViewName.setText(currentItem.getCountryNameV());
        }
         return convertView;
    }

}

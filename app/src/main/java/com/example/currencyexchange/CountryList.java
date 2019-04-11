package com.example.currencyexchange;

public class CountryList {
    private String countryNameV;
    private int flagImageV;

    public CountryList(String countryName, int flagImage){
        countryNameV = countryName;
        flagImageV = flagImage;
    }

    public String getCountryNameV (){
        return countryNameV;
    }

    public int getFlagImageV(){
        return flagImageV;
    }
}

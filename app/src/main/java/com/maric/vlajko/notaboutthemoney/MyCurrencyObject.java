package com.maric.vlajko.notaboutthemoney;

/**
 * Created by Vlajko on 10-Feb-16.
 */
public class MyCurrencyObject {

    private String nameId;
    private String fullCountryName;
    private String valueAgainstDollar;
    public MyCurrencyObject(){}
    public MyCurrencyObject(String nameId){
        this.nameId = nameId;
    }

    public MyCurrencyObject(String nameId, String fullCountryName){
        this.nameId = nameId;
        this.fullCountryName = fullCountryName;
    }
    public MyCurrencyObject(String nameId ,String fullCountryName, String valueAgainstDollar){
        this.nameId = nameId;
        this.fullCountryName = fullCountryName;
        this.valueAgainstDollar = valueAgainstDollar;
    }
    public String getNameId() {
        return nameId;
    }
    public void setNameId(String nameId) {
        this.nameId = nameId;
    }
    public String getValueAgainstDollar() {
        return valueAgainstDollar;
    }
    public void setValueAgainstDollar(String valueAgainstDollar) {
        this.valueAgainstDollar = valueAgainstDollar;
    }

    public String getFullCountryName() {
        return fullCountryName;
    }

    public void setFullCountryName(String fullCountryName) {
        this.fullCountryName = fullCountryName;
    }

}

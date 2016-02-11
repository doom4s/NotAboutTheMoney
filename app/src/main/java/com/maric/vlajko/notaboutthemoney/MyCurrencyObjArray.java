package com.maric.vlajko.notaboutthemoney;

import java.util.ArrayList;

/**
 * Created by Vlajko on 10-Feb-16.
 */
 public class MyCurrencyObjArray {
    public static ArrayList<MyCurrencyObject> currencyObjects = new ArrayList<>();
    public static String timeStamp = "";

    public static void addCurrencyObj(MyCurrencyObject myCurrencyObject){
        currencyObjects.add(myCurrencyObject);
    }


}

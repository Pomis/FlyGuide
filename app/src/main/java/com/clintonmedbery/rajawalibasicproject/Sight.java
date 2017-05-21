package com.clintonmedbery.rajawalibasicproject;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;

/**
 * Created by romanismagilov on 30.04.17.
 */

public class Sight {
    public String name;
    public String descr;
    public String asset;
    @DrawableRes public int image;

    public Sight(String name, String descr, String asset, int image) {
        this.name = name;
        this.descr = descr;
        this.asset = asset;
        this.image = image;
    }
}

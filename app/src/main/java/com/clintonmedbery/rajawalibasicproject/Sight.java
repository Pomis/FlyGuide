package com.clintonmedbery.rajawalibasicproject;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;

/**
 * Created by romanismagilov on 30.04.17.
 */

public class Sight {
    public String name;
    public String descr;
    public String asset;
    @RawRes public int wavAsset;
    @DrawableRes public int image;

    public Sight(String name, String descr, String asset, @RawRes int wavAsset, int image) {
        this.name = name;
        this.descr = descr;
        this.asset = asset;
        this.wavAsset = wavAsset;
        this.image = image;
    }
}

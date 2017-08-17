package com.happy.test;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.happy.activity.ChooseAreaActivity;
import com.happy.db.CoolWeatherOpenHelper;

/**
 * Created by 黄金云 on 2017/8/17.
 */
public class TestClasss extends AndroidTestCase{

    public void test(){
        /*
        CoolWeatherOpenHelper helper = new CoolWeatherOpenHelper(this.getContext(),"test.db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
         */
        ChooseAreaActivity activity = new ChooseAreaActivity();
    }
}

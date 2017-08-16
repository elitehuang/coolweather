package com.happy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 黄金云 on 2017/8/10.
 * 自定义SQLiteOpenHelper 范例
 * 用于创建数据库所需的表或升级数据库时的操作
 */

public class CoolWeatherOpenHelper extends SQLiteOpenHelper{
    /*
    Province 表建表语句

     */
    private static final String CREATE_PROVINCCE_TABLE="create table Province("
            + "id integer primary key autoincrement"
            + "province_name text"
            + "province_code text"
            + ")";
    /*
    City 表建表语句

     */
    private static final String CREATE_CITY_TABLE = "create table City("
            + "id integer primary key autoincrement"
            + "city_name text"
            + "city_code text"
            + "province_id integer"
            + ")";
    /*
    County 表建表语句

     */
    private static final String CREATE_COUNTY_TABLE = "create table County("
            + "id integer primary key autoincrement"
            + "county_name text"
            + "count_code text"
            + "city_id integer"
            + ")";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_PROVINCCE_TABLE);  //创建province表
        db.execSQL(CREATE_CITY_TABLE);       //创建City表
        db.execSQL(CREATE_COUNTY_TABLE);     //创建county表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
package com.happy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.happy.model.City;
import com.happy.model.County;
import com.happy.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄金云 on 2017/8/15.
 */
public class CoolWeatherDB {
    public static final int DATTBASE_VERSION = 1;
    public static final String DATABASE_NAME = "coolweather.db";

    private static CoolWeatherDB mInstance;
    private SQLiteDatabase db;

    /**
     *
     * @param context
     * 将构造方法私有化，应用单例模式，保证只有一个数据库实例
     */
    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(
                context,DATABASE_NAME,null,DATTBASE_VERSION);
        db = dbHelper.getWritableDatabase();
    }
    //获取CoolWeather实例，保证每次只有一条线程访问DB实例
    public synchronized static CoolWeatherDB getDBIntance(Context context){
        if(mInstance == null)
            mInstance = new CoolWeatherDB(context);

        return mInstance;

    }

    /**
     * 1.将Province、City、County实例进行写入数据库的操作
     * 2.从数据库中获取Province、City、County 实例操作
     */
    //Save Province实例操作
    public void saveProvince(Province province){
        ContentValues values = new ContentValues();
        values.put("province_name",province.getProvinceName());
        values.put("province_code",province.getProvinceCode());
        db.insert("Province",null,values);
    }

    //Save City实例操作
    public void saveProvince(City city){
        ContentValues values = new ContentValues();
        values.put("city_name",city.getCityName());
        values.put("city_code",city.getCityCode());
        values.put("province_id", city.getProvinceId());
        db.insert("City", null, values);
    }

    //Save County 实例操作
    public void saveProvince(County county){
        ContentValues values = new ContentValues();
        values.put("county_name",county.getCountyName());
        values.put("county_code",county.getCountyCode());
        values.put("city_id", county.getCityId());
        db.insert("County", null, values);
    }

    /**
     * 从数据库中获取Province、City、County 实例操作
     */
    //加载Province 数据
    public List<Province> loadProvince(){
        List<Province> provinces = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
           do{
               Province province = new Province();
               province.setId(cursor.getInt(cursor.getColumnIndex("id")));
               province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
               province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
               provinces.add(province);

           }while (cursor.moveToNext());
        }

        return provinces;
    }

    //加载City 数据
    public List<City> loadCity(){
        List<City> cities = new ArrayList<City>();
        Cursor cursor = db.query("City", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
           do{
               City city = new City();
               city.setId(cursor.getInt(cursor.getColumnIndex("id")));
               city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
               city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
               city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
               cities.add(city);

           }while (cursor.moveToNext());
        }

        return cities;
    }

    //加载County 数据
    public List<County> loadCounty(){
        List<County> counties= new ArrayList<County>();
        Cursor cursor = db.query("City", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
           do{
               County county = new County();
               county.setId(cursor.getInt(cursor.getColumnIndex("id")));
               county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
               county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
               county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
               counties.add(county);

           }while (cursor.moveToNext());
        }

        return counties;
    }




}

package com.happy.util;

import android.text.TextUtils;

import com.happy.db.CoolWeatherDB;
import com.happy.model.City;
import com.happy.model.County;
import com.happy.model.Province;

/**
 * Created by 黄金云 on 2017/8/16.
 *
 *对获取的数据进行解析的工具
 *省数据类型是”codeName1|ProvinceName1，codeName2|ProvinceName2，codeName3|ProvinceName3......
 *“市数据类型是”codeName1|CityName1，codeName2|CityName2，codeName3|CityName3......“
 *“县数据类型是”codeName1|CountyName1，codeName2|CountyName2，codeName3|CountyName3......“
 */
public class Utility {

    public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB,String response){

        if(!TextUtils.isEmpty(response)){
            String[] allProvince = response.split(",") ;
            if(allProvince != null && allProvince.length > 0) {
                for (String p : allProvince) {
                    String[] arr = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceName(arr[1]);
                    province.setProvinceCode(arr[0]);
                    //将解析出来的数据存储到数据库中的Province表里面
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }

        }
        return false;
    }public synchronized static boolean handleCityResponse(CoolWeatherDB coolWeatherDB,String response , int provinceId){

        if(!TextUtils.isEmpty(response)){
            String[] allCity = response.split(",") ;
            if(allCity != null && allCity.length > 0) {
                for (String p : allCity) {
                    String[] arr = p.split("\\|");
                    City city = new City();
                    city.setCityName(arr[1]);
                    city.setCityCode(arr[0]);
                    city.setProvinceId(provinceId);
                    //将解析出来的数据存储到数据库中的Province表里面
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }

        }
        return false;
    }

    public synchronized static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB,String response , int cityId){

        if(!TextUtils.isEmpty(response)){
            String[] allCounty= response.split(",") ;
           if(allCounty != null && allCounty.length >0){
               for(String p :allCounty) {
                   String[] arr = p.split("\\|");
                   County county = new County();
                   county.setCountyName(arr[1]);
                   county.setCountyCode(arr[0]);
                   county.setCityId(cityId);
                   //将解析出来的数据存储到数据库中的County表里面
                   coolWeatherDB.saveCounty(county);
               }
               return true;
           }

        }
        return false;
    }

}

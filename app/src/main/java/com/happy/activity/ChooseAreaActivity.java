package com.happy.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.happy.R;
import com.happy.db.CoolWeatherDB;
import com.happy.model.City;
import com.happy.model.County;
import com.happy.model.Province;
import com.happy.util.HttpCallbackListener;
import com.happy.util.HttpUtil;
import com.happy.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄金云 on 2017/8/16.
 */
public class ChooseAreaActivity extends Activity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;

    private int currentLevel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getDBIntance(this);
        queryProvinces();           //加载省数据
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) { //当前级别为省
                    selectedProvince = provinceList.get(position); //获得省对象
                    queryCities();                                 //加载对应省下的市数据
                } else if (currentLevel == LEVEL_CITY) {//当前级别为市
                    selectedCity = cityList.get(position);          //获得市对象
                    queryCounties();                                //加载对应市下的县数据
                }
            }
        });

    }


    private void queryProvinces() {
        //从数据库中加载省数据
        provinceList = coolWeatherDB.loadProvince();

        if(provinceList.size() > 0){ //如果加载成功
            dataList.clear();        //清除内存原有的数据
            for(Province province : provinceList){//加载从数据库中省书据到内存
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();  //刷新listView
            listView.setSelection(0);        //并将默认选项指定为第一个
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else{
            queryFromServer(null,"province");
        }


    }


    private void queryCities() {
        cityList = coolWeatherDB.loadCity();

        if(cityList.size() > 0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }

    }

    private void queryCounties() {
        countyList = coolWeatherDB.loadCounty();

        if(countyList.size() > 0){
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        }else{
            queryFromServer(selectedCity.getCityCode(),"county");
        }

    }

    private void queryFromServer(final String code, final String type) {
        String address;
        /*
        获取两种格式城市数据的网址：CityNameCityCode、CityName
         */
        if(TextUtils.isEmpty(code)){ //查询带code的城市地址
            address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
        }else{//查询不带带code的城市地址
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if("province".equals(type)){
                    result =  Utility.handleProvinceResponse(coolWeatherDB,response);
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(coolWeatherDB,response,selectedProvince.getId());
                }else if("county".equals(type)){
                    result = Utility.handleCountyResponse(coolWeatherDB,response,selectedCity.getId());
                }

                if(result){
                    //通过runOnUiThread方法回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           if("province".equals(type)){
                               queryProvinces();
                           } else if("city".equals(type)){
                               queryCities();
                           }else if("county".equals(type)){
                               queryCounties();
                           }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void closeProgressDialog() {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载.......");
            progressDialog.setCancelable(false);
        }
    }

    private void showProgressDialog() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       if(currentLevel == LEVEL_COUNTY) {
            queryCities();
        }
        else if(currentLevel == LEVEL_CITY){
           queryProvinces();
       }else {
           finish();
       }
    }
}

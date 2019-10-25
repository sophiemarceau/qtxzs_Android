package com.wecoo.qutianxia.view.wheelcity;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wecoo.qutianxia.utils.LogUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwl on 2016/11/23.
 * 同步缓存地址数据
 */

public class AsyncAdress extends AsyncTask<Object, Integer, Object> {

    private static AsyncAdress AdressIntance = null;
    private Context context;
    private static List<ProvinceBean.ProvinceEntity> cityList;

    public static AsyncAdress getAdressIntance(Context context) {
        if (AdressIntance == null) {
            AdressIntance = new AsyncAdress(context);
            cityList = new ArrayList<ProvinceBean.ProvinceEntity>();
        }
        return AdressIntance;
    }

    private AsyncAdress(Context context) {
        this.context = context;
    }

    public List<ProvinceBean.ProvinceEntity> getCityList() {
        return cityList;
    }

    public String getCityAgent(String code) {
        if (TextUtils.isEmpty(code)) return "";
        StringBuilder strCity = new StringBuilder();
        for (int i = 0; i < cityList.size(); i++) {
            if (code.substring(0, 2).equals(cityList.get(i).getCode().substring(0, 2))) {
                String strP = cityList.get(i).getName();
                strCity.append(strP + " ");
                List<CityEntity> cList = cityList.get(i).getList();
                if (cList == null) return strCity.toString();
                for (int j = 0; j < cList.size(); j++) {
                    if (code.substring(0, 4).equals(cList.get(j).getCode().substring(0, 4))) {
                        String strC = cList.get(j).getName();
                        if ("市辖区".equals(strC) || "县".equals(strC)) {
                            strCity.append("");
                        } else if (strC.equals(strP)) {
                            strCity.append("");
                        } else {
                            strCity.append(strC + " ");
                        }
                        List<CountyEntity> couList = cList.get(j).getList();
                        if (couList == null) return strCity.toString();
                        for (int k = 0; k < couList.size(); k++) {
                            if (code.equals(couList.get(k).getCode())) {
                                String strX = couList.get(k).getName();
                                if ("市辖区".equals(strX) || "县".equals(strX)) {
                                    strCity.append("");
                                } else if (strC.equals(strX)) {
                                    strCity.append("");
                                } else {
                                    strCity.append(strX);
                                }
                            }
                        }
                    }
                }
            }
        }
        return strCity.toString();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String cityjson = getFromAssets("city_data.json");
        ProvinceBean provinceBean = getObject(cityjson, ProvinceBean.class);
        LogUtil.e("provinceBean = " + provinceBean.toString());
        return provinceBean;
    }

    /**
     * 解析地址数据
     */
    private <T> T getObject(String jsonString, Class<T> type) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return t;
    }

    // 从assets 文件夹中获取文件并读取数据
    private String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer, "UTF-8");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        cityList = new ArrayList<ProvinceBean.ProvinceEntity>();
        ProvinceBean provinceBean = (ProvinceBean) obj;
        if (provinceBean != null && provinceBean.getData().size() > 0) {
            cityList.addAll(provinceBean.getData());
        }
        LogUtil.e("获取城市数据成功");
    }
}

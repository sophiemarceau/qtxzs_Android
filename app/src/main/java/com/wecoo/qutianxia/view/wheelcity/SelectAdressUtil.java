package com.wecoo.qutianxia.view.wheelcity;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.utils.LogUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mwl on 2016/11/23.
 * 三级地址选取
 */

public class SelectAdressUtil implements OnClickListener, OnValueChangeListener {

    private Context context;
    private Dialog dialog;
    private ChooseCityInterface cityInterface;
    private NumberPicker npProvince, npCity, npCounty;
    private TextView tvCancel, tvSure;
    private String[] newCityArray = new String[4];
    private String cityCode;
    private List<ProvinceBean.ProvinceEntity> ProvinceList;

    public void createDialog(Context context, String code, ChooseCityInterface cityInterface) {
        this.context = context;
        this.cityInterface = cityInterface;
        this.cityCode = code;

        ProvinceList = AsyncAdress.getAdressIntance(context).getCityList();

        View view = LayoutInflater.from(context).inflate(R.layout.select_cities_layout, null);
        dialog = new Dialog(context, R.style.Dialog_No_Board);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dialog.addContentView(view, params);
        dialog.show();

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.PopupAnimation);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = context.getResources().getDisplayMetrics().widthPixels; // 设置宽度
        dialog.getWindow().setAttributes(lp);

        //初始化控件
        tvCancel = (TextView) view.findViewById(R.id.selectCity_txt_cancel);
        tvSure = (TextView) view.findViewById(R.id.selectCity_txt_sure);
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        npProvince = (NumberPicker) view.findViewById(R.id.npProvince);
        npCity = (NumberPicker) view.findViewById(R.id.npCity);
        npCounty = (NumberPicker) view.findViewById(R.id.npCounty);
        setNomal();
        //省：设置选择器最小值、最大值、初始值
        String[] provinceArray = new String[ProvinceList.size()];//初始化省数组
        for (int i = 0; i < provinceArray.length; i++) {//省数组填充数据
            provinceArray[i] = ProvinceList.get(i).getName();
        }
        npProvince.setDisplayedValues(provinceArray);//设置选择器数据、默认值
        npProvince.setMinValue(0);
        npProvince.setMaxValue(provinceArray.length - 1);
        npProvince.setWrapSelectorWheel(false); // 取消循环滚动
        if (TextUtils.isEmpty(cityCode) || cityCode.length() < 6) {
            newCityArray[0] = ProvinceList.get(0).getName();
            newCityArray[1] = "";
            newCityArray[2] = "";
            newCityArray[3] = ProvinceList.get(0).getCode();
            npProvince.setValue(0);
            changeCity(0);//联动市数据
        } else {
            for (int i = 0; i < ProvinceList.size(); i++) {
                if (cityCode.substring(0, 2).equals(ProvinceList.get(i).getCode().substring(0, 2))) {
                    npProvince.setValue(i);
                    changeCity(i);//联动市数据
                }
            }
        }
    }

    //根据省,联动市数据
    private void changeCity(int provinceTag) {
        List<CityEntity> cList = ProvinceList.get(provinceTag).getList();
        if (cList == null || cList.size() < 1) {
            cList = new ArrayList<CityEntity>();
            CityEntity cityEntity = new CityEntity();
            cityEntity.setName(" ");
            cityEntity.setCode(ProvinceList.get(provinceTag).getCode());
            cList.add(cityEntity);
        }
        String[] cityArray = new String[cList.size()];
        for (int i = 0; i < cityArray.length; i++) {
            cityArray[i] = cList.get(i).getName();
        }
        LogUtil.i("adressCity : " + Arrays.toString(cityArray));
        try {
            npCity.setMinValue(0);
            npCity.setMaxValue(cityArray.length - 1);
            npCity.setDisplayedValues(cityArray);//设置选择器数据、默认值
        } catch (Exception e) {
            npCity.setDisplayedValues(cityArray);//设置选择器数据、默认值
            npCity.setMinValue(0);
            npCity.setMaxValue(cityArray.length - 1);
        }
        if (TextUtils.isEmpty(cityCode) || cityCode.length() < 6) {
            newCityArray[0] = ProvinceList.get(provinceTag).getName();
            newCityArray[1] = cList.get(0).getName();
            newCityArray[2] = "";
            newCityArray[3] = cList.get(0).getCode();
            npCity.setValue(0);
            changeCounty(provinceTag, 0);//联动县数据
        } else {
            for (int i = 0; i < cList.size(); i++) {
                if (cityCode.substring(0, 4).equals(cList.get(i).getCode().substring(0, 4))) {
                    npCity.setValue(i);
                    changeCounty(provinceTag, i);//联动县数据
                    return;
                }
            }
        }
        npCity.setWrapSelectorWheel(false); // 取消循环滚动
    }

    //根据市,联动县数据
    private void changeCounty(int provinceTag, int cityTag) {
        List<CountyEntity> xList = ProvinceList.get(provinceTag).getList().get(cityTag).getList();
        if (xList == null || xList.size() < 1) {
            xList = new ArrayList<CountyEntity>();
            CountyEntity countyEntity = new CountyEntity();
            countyEntity.setName(" ");
            countyEntity.setCode(ProvinceList.get(provinceTag).getList().get(cityTag).getCode());
            xList.add(countyEntity);
        }
        String[] countyArray = new String[xList.size()];
        for (int i = 0; i < countyArray.length; i++) {
            countyArray[i] = xList.get(i).getName();
        }
        LogUtil.i("adressCounty : " + Arrays.toString(countyArray));
        try {
            npCounty.setMinValue(0);
            npCounty.setMaxValue(countyArray.length - 1);
            npCounty.setDisplayedValues(countyArray);//设置选择器数据、默认值
        } catch (Exception e) {
            npCounty.setDisplayedValues(countyArray);//设置选择器数据、默认值
            npCounty.setMinValue(0);
            npCounty.setMaxValue(countyArray.length - 1);
        }

        if (TextUtils.isEmpty(cityCode) || cityCode.length() < 6) {
            newCityArray[0] = ProvinceList.get(provinceTag).getName();
            newCityArray[1] = ProvinceList.get(provinceTag).getList().get(cityTag).getName();
            newCityArray[2] = xList.get(0).getName();
            newCityArray[3] = xList.get(0).getCode();
            npCounty.setValue(0);
        } else {
            for (int i = 0; i < xList.size(); i++) {
                if (cityCode.equals(xList.get(i).getCode())) {
                    npCounty.setValue(i);
                    newCityArray[0] = ProvinceList.get(provinceTag).getName();
                    newCityArray[1] = ProvinceList.get(provinceTag).getList().get(cityTag).getName();
                    newCityArray[2] = xList.get(i).getName();
                    newCityArray[3] = xList.get(i).getCode();
                }
            }
        }
        npCounty.setWrapSelectorWheel(false); //取消循环滚动
    }

    //设置NumberPicker的分割线透明、字体颜色、设置监听
    private void setNomal() {
        //设置监听
        npProvince.setOnValueChangedListener(this);
        npCity.setOnValueChangedListener(this);
        npCounty.setOnValueChangedListener(this);
        //去除分割线
        setNumberPickerDividerColor(npProvince);
        setNumberPickerDividerColor(npCity);
        setNumberPickerDividerColor(npCounty);
        //设置字体颜色
        setNumberPickerTextColor(npProvince, context.getResources().getColor(R.color.wecoo_gray5));
        setNumberPickerTextColor(npCity, context.getResources().getColor(R.color.wecoo_gray5));
        setNumberPickerTextColor(npCounty, context.getResources().getColor(R.color.wecoo_gray5));
    }

    //设置分割线颜色
    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(context.getResources().getColor(R.color.wecoo_gray2)));// pf.set(picker, new Div)
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    //设置选择器字体颜色
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        boolean result = false;
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    result = true;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectCity_txt_cancel:
                dialog.dismiss();
                break;
            case R.id.selectCity_txt_sure:
                dialog.dismiss();
                if ("市辖区".equals(newCityArray[1]) || "县".equals(newCityArray[1])){
                    newCityArray[1] = "";
                }else if (newCityArray[0].equals(newCityArray[1])){
                    newCityArray[1] = "";
                } else if (newCityArray[1].equals(newCityArray[2])){
                    newCityArray[1] = "";
                }
                cityInterface.sure(newCityArray);
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        switch (numberPicker.getId()) {
            case R.id.npProvince:
                cityCode = null;
//                List<ProvinceBean> dataList = cityList;
//                newCityArray[0] = ProvinceList.get(npProvince.getValue()).getName();
                changeCity(npProvince.getValue());
//                newCityArray[1] = ProvinceList.get(npProvince.getValue()).getList().get(0).getName();
//                newCityArray[2] = ProvinceList.get(npProvince.getValue()).getList().get(0).getList().get(0).getName();
//                newCityArray[3] = ProvinceList.get(npProvince.getValue()).getList().get(0).getList().get(0).getCode();
                break;
            case R.id.npCity:
                cityCode = null;
//                List<ProvinceBean.CityEntity> cList = ProvinceList.get(npProvince.getValue()).getList();
//                newCityArray[1] = cList.get(npCity.getValue()).getName();
                changeCounty(npProvince.getValue(), npCity.getValue());
//                newCityArray[2] = cList.get(npCity.getValue()).getList().get(0).getName();
//                newCityArray[3] = cList.get(npCity.getValue()).getList().get(0).getCode();
                break;
            case R.id.npCounty:
                List<CountyEntity> countyList = ProvinceList.get(npProvince.getValue()).getList().get(npCity.getValue()).getList();
                newCityArray[2] = countyList.get(npCounty.getValue()).getName();
                newCityArray[3] = countyList.get(npCounty.getValue()).getCode();
                break;
        }
    }
}

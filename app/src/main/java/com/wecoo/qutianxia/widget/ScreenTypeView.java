package com.wecoo.qutianxia.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.ScreenAdapter;
import com.wecoo.qutianxia.adapter.ScreenAdapter.OnScreenItemListener;
import com.wecoo.qutianxia.manager.ScreenResultManager;
import com.wecoo.qutianxia.models.ScreenEntity;
import com.wecoo.qutianxia.models.ScreenModel;

import java.util.List;

/**
 * Created by wecoo on 2017/2/20.
 * 筛选分类
 */

public class ScreenTypeView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    // 人脉  类型  时间
    private LinearLayout llContact, llTypes, llTime;
    private TextView tvContact, tvTypes, tvTime;
    private ImageView ivContact, ivTypes, ivTime;
    // 阴影背景
    private FrameLayout flMaskBg;
    private LinearLayout llShowView;
    // 数据
    private ListView listView;
    private List<ScreenEntity> contactList;// 人脉筛选
    private List<ScreenEntity> typeList; //类型筛选
    private List<ScreenEntity> timeList; //时间筛选
    private ScreenAdapter contAdapter/*, typeAdapter, timeAdapter*/;
    private Button btnReset, btnSure;
    // 筛选完成后的回调
    private OnScreenListener screenListener;
    private boolean isShowing = false;// 是否显示筛选浮层
    private int index = -1;// 筛选按钮的索引

    public ScreenTypeView(Context context) {
        super(context);
        init(context);
    }

    public ScreenTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScreenTypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.screen_typy_view, this);

        initView(view);
    }

    private void initView(View view) {
        llContact = (LinearLayout) view.findViewById(R.id.screenType_contact_linear);
        llTypes = (LinearLayout) view.findViewById(R.id.screenType_types_linear);
        llTime = (LinearLayout) view.findViewById(R.id.screenType_time_linear);
        tvContact = (TextView) view.findViewById(R.id.screenType_tv_contact);
        tvTypes = (TextView) view.findViewById(R.id.screenType_tv_types);
        tvTime = (TextView) view.findViewById(R.id.screenType_tv_time);
        ivContact = (ImageView) view.findViewById(R.id.screenType_iv_contact_arrow);
        ivTypes = (ImageView) view.findViewById(R.id.screenType_iv_types_arrow);
        ivTime = (ImageView) view.findViewById(R.id.screenType_iv_time_arrow);
        listView = (ListView) view.findViewById(R.id.screenType_listView);
        flMaskBg = (FrameLayout) view.findViewById(R.id.screenType_view_maskBg);
        llShowView = (LinearLayout) view.findViewById(R.id.screenType_ll_showView);
        btnReset = (Button) view.findViewById(R.id.screenType_btn_reset);
        btnSure = (Button) view.findViewById(R.id.screenType_btn_sure);
        //
        setListener();
    }

    // 设置监听
    private void setListener() {
        llContact.setOnClickListener(this);
        llTypes.setOnClickListener(this);
        llTime.setOnClickListener(this);
        flMaskBg.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSure.setOnClickListener(this);
    }

    // 填充数据
    public void setTextContent(String strContact, String strTypes, String strTime) {
        if (!TextUtils.isEmpty(strContact)) {
            tvContact.setText(strContact);
        }
        if (!TextUtils.isEmpty(strTypes)) {
            tvTypes.setText(strTypes);
        }
        if (!TextUtils.isEmpty(strTime)) {
            tvTime.setText(strTime);
        }
    }

    // 填充数据
    public void setData(List<ScreenEntity> contactList, List<ScreenEntity> typeList, List<ScreenEntity> timeList) {
        this.contactList = contactList;
        this.typeList = typeList;
        this.timeList = timeList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.screenType_contact_linear:
                showFilterLayout(0);
                break;
            case R.id.screenType_types_linear:
                showFilterLayout(1);
                break;
            case R.id.screenType_time_linear:
                showFilterLayout(2);
                break;
            case R.id.screenType_view_maskBg:
                if (isShowing) {
                    hide();
                }
                break;
            case R.id.screenType_btn_reset:
                resetDataAdapter();
                break;
            case R.id.screenType_btn_sure:
                if (screenListener != null) {
                    List<ScreenModel> sureList = ScreenResultManager.getManager().getSureDataByid(index);
                    screenListener.onScreenResult(sureList);
                    if (sureList.size() == 0) {
                        resetFilterStatus(-1);
                    } else {
                        resetFilterStatus(index);
                    }
                    hide();
                }
                break;
        }
    }

    // 显示筛选布局
    public void showFilterLayout(int position) {
        if (isShowing && index == position) {
            hide();
            index = -1;
        } else {
            resetFilterStatus(-1);
            this.index = position;
            switch (position) {
                case 0:
                    setContactAdapter();
                    break;
                case 1:
                    setTypeAdapter();
                    break;
                case 2:
                    setTimeAdapter();
                    break;
            }
            show();
        }
    }

    // 筛选层是否显示
    public boolean isShowing() {
        return isShowing;
    }

    // 重置数据
    private void resetDataAdapter() {
        if (index == 0) {
            List<ScreenModel> saveList = ScreenResultManager.getManager().getDataByid(0);
            for (int i = 0; i < saveList.size(); i++) {
                if (saveList.get(i).getPosition() == 0) {
                    saveList.get(i).setType(1);
                } else {
                    saveList.get(i).setType(0);
                }
            }

            for (int i = 0; i < contactList.size(); i++) {
                List<ScreenModel> list = contactList.get(i).getList();
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).getType() == 1) {
                        list.get(j).setType(0);
                    }
                    list.get(0).setType(1);
                }
            }
        } else if (index == 1) {
            List<ScreenModel> saveList = ScreenResultManager.getManager().getDataByid(1);
            for (int i = 0; i < saveList.size(); i++) {
                switch (saveList.get(i).getParentId()) {
                    case 0:
                        if (saveList.get(i).getPosition() == 0) {
                            saveList.get(i).setType(1);
                        } else {
                            saveList.get(i).setType(0);
                        }
                        break;
                    case 1:
                        if (saveList.get(i).getPosition() == 0) {
                            saveList.get(i).setType(1);
                        } else {
                            saveList.get(i).setType(0);
                        }
                        break;
                    case 2:
                        if (saveList.get(i).getPosition() == 0) {
                            saveList.get(i).setType(1);
                        } else {
                            saveList.get(i).setType(0);
                        }
                        break;
                    case 3:
                        if (saveList.get(i).getPosition() == 0) {
                            saveList.get(i).setType(1);
                        } else {
                            saveList.get(i).setType(0);
                        }
                        break;
                    case 4:
                        if (saveList.get(i).getPosition() == 0) {
                            saveList.get(i).setType(1);
                        } else {
                            saveList.get(i).setType(0);
                        }
                        break;
                }
            }

            for (int i = 0; i < typeList.size(); i++) {
                List<ScreenModel> list = typeList.get(i).getList();
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).getType() == 1) {
                        list.get(j).setType(0);
                    }
                    list.get(0).setType(1);
                }
            }
        } else if (index == 2) {
            List<ScreenModel> saveList = ScreenResultManager.getManager().getDataByid(2);
            for (int i = 0; i < saveList.size(); i++) {
                if (saveList.get(i).getPosition() == 0) {
                    saveList.get(i).setType(1);
                } else {
                    saveList.get(i).setType(0);
                }
            }
            for (int i = 0; i < timeList.size(); i++) {
                List<ScreenModel> list = timeList.get(i).getList();
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).getType() == 1) {
                        list.get(j).setType(0);
                    }
                    list.get(0).setType(1);
                }
            }
        }
        contAdapter.notifyDataSetChanged();
    }

    // 设置人脉数据
    private void setContactAdapter() {
        if (ScreenResultManager.getManager().getScreenCount(1) > 0) {
            resetFilterStatus(1);
        }
        if (ScreenResultManager.getManager().getScreenCount(2) > 0) {
            resetFilterStatus(2);
        }
        List<ScreenModel> saveList = ScreenResultManager.getManager().getDataByid(index);
        if (saveList != null && saveList.size() > 0) {
            for (int i = 0; i < saveList.size(); i++) {
                ScreenModel m = saveList.get(i);
                if (m.isSured()) {
                    List<ScreenModel> newList = contactList.get(m.getParentId()).getList();
                    for (int j = 0; j < newList.size(); j++) {
                        contactList.get(m.getParentId()).getList().get(j).setType(0);
                    }
                    contactList.get(m.getParentId()).getList().get(m.getPosition()).setType(1);
                    m.setSured(true);
                    m.setType(1);
                    ScreenResultManager.getManager().upDateByModel(m);
                }
            }
        } else {
            for (int i = 0; i < contactList.size(); i++) {
                ScreenModel dModel = contactList.get(i).getList().get(0);
                dModel.setId(index);
                dModel.setParentId(i);
                dModel.setPosition(0);
                dModel.setSured(true);
                ScreenResultManager.getManager().addScreenResult(dModel);
            }
        }

        tvContact.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
        ivContact.setImageResource(R.mipmap.icon_arrow_up_red);
        contAdapter = new ScreenAdapter(mContext, contactList);
        listView.setAdapter(contAdapter);
        contAdapter.setItemListener(new OnScreenItemListener() {
            @Override
            public void onSelectItem(int parent, int position) {
                List<ScreenModel> dataL = contactList.get(parent).getList();
                if (dataL != null) {
                    selectItem(dataL, parent, position);
                }
            }
        });
    }

    // 设置类型数据
    private void setTypeAdapter() {
        if (ScreenResultManager.getManager().getScreenCount(0) > 0) {
            resetFilterStatus(0);
        }
        if (ScreenResultManager.getManager().getScreenCount(2) > 0) {
            resetFilterStatus(2);
        }
        List<ScreenModel> saveList = ScreenResultManager.getManager().getDataByid(index);
        if (saveList != null && saveList.size() > 0) {
            for (int i = 0; i < saveList.size(); i++) {
                ScreenModel m = saveList.get(i);
                if (m.isSured()) {
                    List<ScreenModel> newList = typeList.get(m.getParentId()).getList();
                    for (int j = 0; j < newList.size(); j++) {
                        typeList.get(m.getParentId()).getList().get(j).setType(0);
                    }
                    typeList.get(m.getParentId()).getList().get(m.getPosition()).setType(1);
                    m.setSured(true);
                    m.setType(1);
                    ScreenResultManager.getManager().upDateByModel(m);
                }
            }
        } else {
            for (int i = 0; i < typeList.size(); i++) {
                ScreenModel dModel = typeList.get(i).getList().get(0);
                dModel.setId(index);
                dModel.setParentId(i);
                dModel.setPosition(0);
                dModel.setSured(true);
                ScreenResultManager.getManager().addScreenResult(typeList.get(i).getList().get(0));
            }
        }
        tvTypes.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
        ivTypes.setImageResource(R.mipmap.icon_arrow_up_red);
        contAdapter = new ScreenAdapter(mContext, typeList);
        listView.setAdapter(contAdapter);
        contAdapter.setItemListener(new OnScreenItemListener() {
            @Override
            public void onSelectItem(int parent, int position) {
                List<ScreenModel> dataL = typeList.get(parent).getList();
                if (dataL != null) {
                    selectItem(dataL, parent, position);
                }
            }
        });
    }

    // 设置时间数据
    private void setTimeAdapter() {
        if (ScreenResultManager.getManager().getScreenCount(0) > 0) {
            resetFilterStatus(0);
        }
        if (ScreenResultManager.getManager().getScreenCount(1) > 0) {
            resetFilterStatus(1);
        }
        List<ScreenModel> saveList = ScreenResultManager.getManager().getDataByid(index);
        if (saveList != null && saveList.size() > 0) {
            for (int i = 0; i < saveList.size(); i++) {
                ScreenModel m = saveList.get(i);
                if (m.isSured()) {
                    List<ScreenModel> newList = timeList.get(m.getParentId()).getList();
                    for (int j = 0; j < newList.size(); j++) {
                        timeList.get(m.getParentId()).getList().get(j).setType(0);
                    }
                    timeList.get(m.getParentId()).getList().get(m.getPosition()).setType(1);
                    m.setSured(true);
                    m.setType(1);
                    ScreenResultManager.getManager().upDateByModel(m);
                }
            }
        } else {
            for (int i = 0; i < timeList.size(); i++) {
                ScreenModel dModel = timeList.get(i).getList().get(0);
                dModel.setId(index);
                dModel.setParentId(i);
                dModel.setPosition(0);
                dModel.setSured(true);
                ScreenResultManager.getManager().addScreenResult(timeList.get(i).getList().get(0));
            }
        }
        tvTime.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
        ivTime.setImageResource(R.mipmap.icon_arrow_up_red);
        contAdapter = new ScreenAdapter(mContext, timeList);
        listView.setAdapter(contAdapter);
        contAdapter.setItemListener(new OnScreenItemListener() {
            @Override
            public void onSelectItem(int parent, int position) {
                List<ScreenModel> dataL = timeList.get(parent).getList();
                if (dataL != null) {
                    selectItem(dataL, parent, position);
                }
            }
        });
    }

    // 点击某项，其他项设置为不选择
    private void selectItem(List<ScreenModel> dataL, int parent, int position) {
        ScreenModel model = new ScreenModel();
        model.setId(index);
        model.setParentId(parent);
        model.setPosition(position);
        model.setSured(false);
        model.setName(dataL.get(position).getName());
        model.setCode(dataL.get(position).getCode());
        model.setType(dataL.get(position).getType());
        if (ScreenResultManager.getManager().getCountByPosition(index, parent, position) > 0) {
            ScreenResultManager.getManager().upDateByModel(model);
        } else {
            ScreenResultManager.getManager().addScreenResult(model);
        }
    }

    // 复位筛选的显示状态
    public void resetFilterStatus(int index) {
        switch (index) {
            case -1:
                tvContact.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_gray5));
                ivContact.setImageResource(R.mipmap.icon_arrow_down_gray);

                tvTypes.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_gray5));
                ivTypes.setImageResource(R.mipmap.icon_arrow_down_gray);

                tvTime.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_gray5));
                ivTime.setImageResource(R.mipmap.icon_arrow_down_gray);
                break;
            case 0:
                tvContact.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
                ivContact.setImageResource(R.mipmap.icon_arrow_down_red);
                break;
            case 1:
                tvTypes.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
                ivTypes.setImageResource(R.mipmap.icon_arrow_down_red);
                break;
            case 2:
                tvTime.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
                ivTime.setImageResource(R.mipmap.icon_arrow_down_red);
                break;
        }
    }

    // 显示
    public void show() {
        isShowing = true;
        flMaskBg.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(llShowView, "translationY", -(llShowView.getHeight()), 0).setDuration(300).start();
    }

    // 隐藏
    public void hide() {
        resetFilterStatus(-1);
        isShowing = false;
        flMaskBg.setVisibility(View.GONE);
        ObjectAnimator.ofFloat(llShowView, "translationY", 0, -(llShowView.getHeight())).setDuration(300).start();
        if (ScreenResultManager.getManager().getScreenResult() != null
                && ScreenResultManager.getManager().getScreenResult().size() > 0) {
            if (ScreenResultManager.getManager().getScreenCount(0) > 0) {
                resetFilterStatus(0);
            }
            if (ScreenResultManager.getManager().getScreenCount(1) > 0) {
                resetFilterStatus(1);
            }
            if (ScreenResultManager.getManager().getScreenCount(2) > 0) {
                resetFilterStatus(2);
            }
        }
    }

    public void setScreenListener(OnScreenListener screenListener) {
        this.screenListener = screenListener;
    }

    public interface OnScreenListener {
        void onScreenResult(Object object);
    }
}

package com.wecoo.qutianxia.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.application.WKApplication;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.models.CustomInfoEntity;
import com.wecoo.qutianxia.models.FilterEntity;
import com.wecoo.qutianxia.requestjson.AddCustomRequest;
import com.wecoo.qutianxia.requestjson.AddReportRequest;
import com.wecoo.qutianxia.requestjson.CustomInfoRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.utils.DensityUtil;
import com.wecoo.qutianxia.utils.StringUtil;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.ItemSelectAction.OnSelectListener;
import com.wecoo.qutianxia.view.wheelcity.ChooseCityInterface;
import com.wecoo.qutianxia.view.wheelcity.SelectAdressUtil;

import java.util.List;

/**
 * Created by mwl on 2016/11/7.
 * 客户信息的填写
 */

public class AddCustomView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private OnAddCustomListener addCustomListener;
    private ItemSelectAction selectAction;
    private List<FilterEntity> IndustryList, BudgetList, PlanTimeList;
    private int status;
    private String customer_id = "";
    // 0 我的客户信息    1  添加客户
    public final int customInfo = 0, addCustom = 1;
    // 客户名字和电话号码
    private EditText editName, editPhone;
    // 意向行业    地域选择   投资预算    计划时间
    private TextView txtIndustry, txtRegion, txtBudget, txtPlanTime;
    // 备注
    private EditText editRemarks;
    // 说明
    private TextView txtContentTop, txtContentButtom;
    // 提交
    private Button btnSave, btnCommit;
    private String strName, strPhone, strIndustry, strRegion, strBudget, strPlanTime, strRemarks;
    private int int_Industry = 0, int_Budget = 0, int_PlanTime = 0;
    private CheckBox checkBox;
    private boolean isCheck;

    public AddCustomView(Context context) {
        super(context);
        this.mContext = context;
        initView(context);
    }

    public AddCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    // 初始化View
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.input_custominfo_view, this);
        editName = (EditText) view.findViewById(R.id.inputCustom_edit_name);
        editPhone = (EditText) view.findViewById(R.id.inputCustom_edit_tel);
        txtIndustry = (TextView) view.findViewById(R.id.inputCustom_txt_industry);
        txtRegion = (TextView) view.findViewById(R.id.inputCustom_txt_region);
        txtBudget = (TextView) view.findViewById(R.id.inputCustom_txt_budget);
        txtPlanTime = (TextView) view.findViewById(R.id.inputCustom_txt_planTime);
        editRemarks = (EditText) view.findViewById(R.id.inputCustom_edit_remarks);
        txtContentTop = (TextView) view.findViewById(R.id.inputCustom_txt_contentsTop);
        txtContentButtom = (TextView) view.findViewById(R.id.inputCustom_txt_contentsButtom);
        btnSave = (Button) view.findViewById(R.id.input_butn_saveInfo);
        btnCommit = (Button) view.findViewById(R.id.input_butn_commitReport);
        checkBox = (CheckBox) findViewById(R.id.commitInfo_checkBox);
        setDrawTop(R.mipmap.icon_agree_normal);
        //
        btnSave.setVisibility(View.VISIBLE);
        btnCommit.setVisibility(View.VISIBLE);
        txtContentTop.setVisibility(View.GONE);
        txtContentButtom.setVisibility(View.GONE);

        // 添加监听
        txtIndustry.setOnClickListener(this);
        txtRegion.setOnClickListener(this);
        txtBudget.setOnClickListener(this);
        txtPlanTime.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    setDrawTop(R.mipmap.icon_agree_normal);
                }else {
                    setDrawTop(R.mipmap.icon_agree_selected);
                }
                isCheck = b;
            }
        });
    }

    // 设置快速入口的上边图标
    private void setDrawTop(int drawId) {
        Drawable drawLeft = ContextCompat.getDrawable(mContext, drawId);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示  
        drawLeft.setBounds(0, 0, DensityUtil.dp2px(mContext, 15), DensityUtil.dp2px(mContext, 15));
        checkBox.setCompoundDrawables(drawLeft, null, null, null);
    }

    // 这是View状态
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 添加数据
     * 意向行业
     * 地区
     * 投资预算
     * 计划启动时间
     */
    public void setData(List<FilterEntity> Industrylist, List<FilterEntity> BudgetList, List<FilterEntity> PlanTimeList) {
        this.IndustryList = Industrylist;
        this.BudgetList = BudgetList;
        this.PlanTimeList = PlanTimeList;
    }

    //  获取客户信息数据
    public void setCustomInfoData(String customer_id) {
        this.customer_id = customer_id;
        if (!TextUtils.isEmpty(customer_id)) {
            CustomInfoRequest infoRequest = new CustomInfoRequest();
            infoRequest.setRequestParms(customer_id);
            infoRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    CustomInfoEntity infoEntity = (CustomInfoEntity) obj;
                    if (infoEntity != null) {
                        editName.setText(infoEntity.getCustomer_name());
                        editName.setTextColor(getResources().getColor(R.color.wecoo_gray4));
                        editName.setEnabled(false);
                        editPhone.setText(infoEntity.getCustomer_tel());
                        editPhone.setTextColor(getResources().getColor(R.color.wecoo_gray4));
                        editPhone.setEnabled(false);
                        editRemarks.setText(infoEntity.getCustomer_note());
                        if (infoEntity.getCustomer_islocked() == 1) {
                            btnCommit.setVisibility(View.GONE);
                        } else {
                            btnCommit.setVisibility(View.VISIBLE);
                        }
                        ergodicList(infoEntity);
                    }
                }
            });
        }
    }

    // 遍历List
    private void ergodicList(CustomInfoEntity infoEntity) {
        strRegion = infoEntity.getCustomer_area_agent();
        txtRegion.setText(infoEntity.getCustomer_area_agent_name());

        for (int i = 0; i < IndustryList.size(); i++) {
            if (infoEntity.getCustomer_industry().equals(IndustryList.get(i).getCode())) {
                txtIndustry.setText(IndustryList.get(i).getName());
                int_Industry = i;
                strIndustry = infoEntity.getCustomer_industry();
            }
        }
        for (int i = 0; i < BudgetList.size(); i++) {
            if (infoEntity.getCustomer_budget().equals(BudgetList.get(i).getCode())) {
                txtBudget.setText(BudgetList.get(i).getName());
                int_Budget = i;
                strBudget = infoEntity.getCustomer_budget();
            }
        }
        for (int i = 0; i < PlanTimeList.size(); i++) {
            if (infoEntity.getCustomer_startdate().equals(PlanTimeList.get(i).getCode())) {
                txtPlanTime.setText(PlanTimeList.get(i).getName());
                int_PlanTime = i;
                strPlanTime = infoEntity.getCustomer_startdate();
            }
        }
    }

    // 控件内容转化为String
    private void commitCustom(String url) {
        strName = editName.getText().toString().trim();
        strPhone = editPhone.getText().toString().trim();
        strRemarks = editRemarks.getText().toString().trim();

        if (TextUtils.isEmpty(strName) || strName.length() < 2) {
            ToastUtil.showShort(mContext, "请正确填写客户姓名");
        } else if (TextUtils.isEmpty(strPhone) || !StringUtil.isMobile(strPhone)) {
            ToastUtil.showShort(mContext, "请正确填写手机号");
        } else if (TextUtils.isEmpty(strIndustry)) {
            ToastUtil.showShort(mContext, "请正确选择意向行业");
        } else if (TextUtils.isEmpty(strRegion)) {
            ToastUtil.showShort(mContext, "请正确选择代理区域");
        } else if (TextUtils.isEmpty(strBudget)) {
            ToastUtil.showShort(mContext, "请正确选择资预算");
        } else if (TextUtils.isEmpty(strPlanTime)) {
            ToastUtil.showShort(mContext, "请正确选择计划启动时间");
        } else if (!isCheck) {
            ToastUtil.showShort(mContext, "客户本人必须知晓并同意方可提交");
        } else {
            if (TextUtils.isEmpty(url)) {
                addReport();
            } else {
                addCustom(url);
            }
        }
    }

    // 添加客户
    private void addCustom(String url) {
        AddCustomRequest addCustomRequest = new AddCustomRequest(url);
        AddCustomRequest.AddCustomParms parms = new AddCustomRequest.AddCustomParms();
        parms.customer_id = customer_id;
        parms.customer_name = strName;
        parms.customer_tel = strPhone;
        parms.customer_industry = strIndustry;
        parms.customer_area_agent = strRegion;
        parms.customer_budget = strBudget;
        parms.customer_startdate = strPlanTime;
        parms._customer_note = strRemarks;
        addCustomRequest.setRequestParms(parms);
        addCustomRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                ToastUtil.showShort(mContext, "信息保存成功");
                if (addCustomListener != null) {
                    addCustomListener.addCustomStatus(obj);
                }
            }
        });
    }

    // 添加报备
    private void addReport() {
        AddReportRequest addReportRequest = new AddReportRequest();
        AddReportRequest.AddReportParms parms = new AddReportRequest.AddReportParms();
        parms.project_id = null;
        parms.report_customer_name = strName;
        parms.report_customer_tel = strPhone;
        parms.report_customer_industry = strIndustry;
        parms.report_customer_area_agent = strRegion;
        parms.report_customer_budget = strBudget;
        parms.report_customer_startdate = strPlanTime;
        parms._report_customer_note = strRemarks;
        addReportRequest.setRequestParms(parms);
        addReportRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                ToastUtil.showShort(mContext, "成功报备信息");
                if (addCustomListener != null) {
                    addCustomListener.addReportStatus(obj);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.inputCustom_txt_industry:
                if (IndustryList == null || IndustryList.size() < 3) {
                    IndustryList = ModelManager.getInstance().getLookupIndustryAll(mContext);
                    WKApplication.getInstance().IndustryList = IndustryList;
                } else {
                    selectAction = new ItemSelectAction(mContext, IndustryList, int_Industry);
                    selectAction.show();
                    selectAction.setSelectListener(new OnSelectListener() {
                        @Override
                        public void onSelectData(FilterEntity entity) {
                            int_Industry = entity.id;
                            strIndustry = entity.getCode();
                            txtIndustry.setText(entity.getName());
                        }
                    });
                }
                break;
            case R.id.inputCustom_txt_region:
                SelectAdressUtil cityUtil = new SelectAdressUtil();
                cityUtil.createDialog(mContext, strRegion, new ChooseCityInterface() {
                    @Override
                    public void sure(String[] newCityArray) {
                        txtRegion.setText(newCityArray[0] + " " + newCityArray[1] + " " + newCityArray[2]);
                        strRegion = newCityArray[3];
                    }
                });
                break;
            case R.id.inputCustom_txt_budget:
                if (BudgetList == null || BudgetList.size() < 1) {
                    return;
                }
                selectAction = new ItemSelectAction(mContext, BudgetList, int_Budget);
                selectAction.show();
                selectAction.setSelectListener(new OnSelectListener() {
                    @Override
                    public void onSelectData(FilterEntity entity) {
                        int_Budget = entity.id;
                        strBudget = entity.getCode();
                        txtBudget.setText(entity.getName());
                    }
                });
                break;
            case R.id.inputCustom_txt_planTime:
                if (PlanTimeList == null || PlanTimeList.size() < 1) {
                    return;
                }
                selectAction = new ItemSelectAction(mContext, PlanTimeList, int_PlanTime);
                selectAction.show();
                selectAction.setSelectListener(new OnSelectListener() {
                    @Override
                    public void onSelectData(FilterEntity entity) {
                        int_PlanTime = entity.id;
                        strPlanTime = entity.getCode();
                        txtPlanTime.setText(entity.getName());
                    }
                });
                break;
            case R.id.input_butn_saveInfo:
                MobclickAgent.onEvent(mContext, "addCustom_butn_SaveInfo");
                switch (status) {
                    case addCustom:
                        commitCustom(WebUrl.addCustomer);
                        break;
                    case customInfo:
                        commitCustom(WebUrl.updateCustomer);
                        break;
                }
                break;
            case R.id.input_butn_commitReport:
                MobclickAgent.onEvent(mContext, "addCustom_butn_commit");
                commitCustom(null);
                break;
        }
    }

    public void setAddCustomListener(OnAddCustomListener addCustomListener) {
        this.addCustomListener = addCustomListener;
    }

    public interface OnAddCustomListener {
        void addCustomStatus(Object obj);

        void addReportStatus(Object obj);
    }
}

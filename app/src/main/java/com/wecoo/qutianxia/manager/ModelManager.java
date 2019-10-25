package com.wecoo.qutianxia.manager;

import android.content.Context;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.AboutUsActivity;
import com.wecoo.qutianxia.activity.ComplaintActivity;
import com.wecoo.qutianxia.activity.MyCustomActivity;
import com.wecoo.qutianxia.activity.MyFollowActivity;
import com.wecoo.qutianxia.activity.MyInvitationActivity;
import com.wecoo.qutianxia.activity.MyWanzhuanActivity;
import com.wecoo.qutianxia.activity.RewardActivity;
import com.wecoo.qutianxia.activity.SetingActivity;
import com.wecoo.qutianxia.activity.SettledActivity;
import com.wecoo.qutianxia.models.FilterEntity;
import com.wecoo.qutianxia.models.LookupEntity;
import com.wecoo.qutianxia.models.MyDataEntity;
import com.wecoo.qutianxia.models.ScreenEntity;
import com.wecoo.qutianxia.models.ScreenModel;
import com.wecoo.qutianxia.models.StringModels;
import com.wecoo.qutianxia.requestjson.LoginOutRequest;
import com.wecoo.qutianxia.requestjson.LookupBankRequest;
import com.wecoo.qutianxia.requestjson.LookupIndustryRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwl on 2016/10/24.
 * 构建数据管理类
 */

public class ModelManager {

    private static ModelManager mModelManager = null;

    public static ModelManager getInstance() {
        if (mModelManager == null) {
            mModelManager = new ModelManager();
        }
        return mModelManager;
    }


    // 行业分类数据
    public List<FilterEntity> getLookupIndustryAll(Context context) {
        final List<FilterEntity> indusList = new ArrayList<FilterEntity>();
        LookupIndustryRequest industryRequest = new LookupIndustryRequest();
        industryRequest.setReturnDataClick(context, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                LookupEntity entity = (LookupEntity) obj;
                if (entity != null && entity.getResult().size() > 0) {
                    indusList.addAll(entity.getResult());
                }
            }
        });
        return indusList;
    }

    // 获取银行数据
    public List<FilterEntity> getLookupBankAll(Context context) {
        final List<FilterEntity> list = new ArrayList<FilterEntity>();
        LookupBankRequest bankRequest = new LookupBankRequest();
        bankRequest.setReturnDataClick(context, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                LookupEntity entity = (LookupEntity) obj;
                if (entity != null && entity.getResult().size() > 0) {
                    list.addAll(entity.getResult());
                }
            }
        });
        return list;
    }

    // 排序数据
    public List<FilterEntity> getSortData() {
        List<FilterEntity> list = new ArrayList<FilterEntity>();
        list.add(new FilterEntity("5", "综合排序", true));
        list.add(new FilterEntity("6", "按上线时间"));
        list.add(new FilterEntity("2", "按赏金"));
        list.add(new FilterEntity("4", "按关注"));
        return list;
    }

    // 投资预算
    public List<FilterEntity> getBudget() {
        List<FilterEntity> list = new ArrayList<FilterEntity>();
        list.add(new FilterEntity("1", "5-10万"));
        list.add(new FilterEntity("2", "10-30万"));
        list.add(new FilterEntity("3", "30-50万"));
        list.add(new FilterEntity("4", "50万以上"));
        return list;
    }

    // 计划启动时间
    public List<FilterEntity> getPlanTime() {
        List<FilterEntity> list = new ArrayList<FilterEntity>();
        list.add(new FilterEntity("1", "2周以内"));
        list.add(new FilterEntity("2", "1个月以内"));
        list.add(new FilterEntity("3", "3个月以内"));
        list.add(new FilterEntity("4", "更久"));
        return list;
    }

    // 我的数据
    public List<MyDataEntity> getMyData() {
        List<MyDataEntity> list = new ArrayList<MyDataEntity>();
        list.add(new MyDataEntity(R.mipmap.icon_my_customer, R.string.my_customer, "0", MyCustomActivity.class));
        list.add(new MyDataEntity(R.mipmap.icon_my_follow, R.string.my_follow, "0", MyFollowActivity.class));
        list.add(new MyDataEntity(R.mipmap.icon_myinvitation, R.string.my_invitation, "0", MyInvitationActivity.class));
        list.add(new MyDataEntity(R.mipmap.icon_my_settled, R.string.my_settled, "", SettledActivity.class));
        list.add(new MyDataEntity(R.mipmap.icon_my_bunos, R.string.my_bunos, "0", RewardActivity.class));
        list.add(new MyDataEntity(R.mipmap.icon_my_complaint, R.string.my_complaint, "", ComplaintActivity.class));
        list.add(new MyDataEntity(R.mipmap.icon_my_wanzhuan, R.string.my_wanzhuan, "", MyWanzhuanActivity.class));
        list.add(new MyDataEntity(R.mipmap.icon_my_about, R.string.my_about, "", AboutUsActivity.class));
        list.add(new MyDataEntity(R.mipmap.icon_my_set, R.string.my_set, "", SetingActivity.class));
        return list;
    }

    // 筛选人脉数据
    public List<ScreenEntity> getScreenContactData() {
        List<ScreenEntity> entities = new ArrayList<ScreenEntity>();
        ScreenEntity entity = new ScreenEntity();
        entity.setTitle("所在人脉");
        List<ScreenModel> Modellist = new ArrayList<ScreenModel>();
        Modellist.add(new ScreenModel("全部人脉","0",1));
        Modellist.add(new ScreenModel("一级人脉","1",0));
        Modellist.add(new ScreenModel("二级人脉","2",0));
        Modellist.add(new ScreenModel("三级人脉","3",0));
        entity.setList(Modellist);
        entities.add(entity);
        return entities;
    }

    // 筛选类型数据
    public List<ScreenEntity> getTypesData() {
        List<ScreenEntity> entities = new ArrayList<ScreenEntity>();
        ScreenEntity entity = new ScreenEntity();
        entity.setTitle("赏金类型");
        List<ScreenModel> Modellist = new ArrayList<ScreenModel>();
        Modellist.add(new ScreenModel("全部类型","0",1));
        Modellist.add(new ScreenModel("推荐通过","1",0));
        Modellist.add(new ScreenModel("签约成功","2",0));
        Modellist.add(new ScreenModel("活动奖励","3",0));
        entity.setList(Modellist);
        entities.add(entity);
        return entities;
    }

    // 筛选贡献数据
    public List<ScreenEntity> getContributionData() {
        List<ScreenEntity> entities = new ArrayList<ScreenEntity>();
        // 贡献类型
        ScreenEntity entity1 = new ScreenEntity();
        entity1.setTitle("贡献类型");
        List<ScreenModel> Modellist1 = new ArrayList<ScreenModel>();
        Modellist1.add(new ScreenModel("本人贡献","1",1));
        Modellist1.add(new ScreenModel("下级人脉贡献","2",0));
        Modellist1.add(new ScreenModel("累计贡献","3",0));
        entity1.setList(Modellist1);
        entities.add(entity1);
        // 贡献赏金
        ScreenEntity entity2 = new ScreenEntity();
        entity2.setTitle("贡献赏金（元）");
        List<ScreenModel> Modellist2 = new ArrayList<ScreenModel>();
        Modellist2.add(new ScreenModel("不限金额","0",1));
        Modellist2.add(new ScreenModel("100以内","1",0));
        Modellist2.add(new ScreenModel("100-500","2",0));
        Modellist2.add(new ScreenModel("500-5000","3",0));
        Modellist2.add(new ScreenModel("5000以上","4",0));
        entity2.setList(Modellist2);
        entities.add(entity2);
        // 贡献邀请人数
        ScreenEntity entity3 = new ScreenEntity();
        entity3.setTitle("贡献邀请人数");
        List<ScreenModel> Modellist3 = new ArrayList<ScreenModel>();
        Modellist3.add(new ScreenModel("不限人数","0",1));
        Modellist3.add(new ScreenModel("1-5人","1",0));
        Modellist3.add(new ScreenModel("5-15人","2",0));
        Modellist3.add(new ScreenModel("15-30人","3",0));
        Modellist3.add(new ScreenModel("30人以上","4",0));
        entity3.setList(Modellist3);
        entities.add(entity3);
        // 贡献通过报备数
        ScreenEntity entity4 = new ScreenEntity();
        entity4.setTitle("贡献通过推荐数");
        List<ScreenModel> Modellist4 = new ArrayList<ScreenModel>();
        Modellist4.add(new ScreenModel("不限数量","0",1));
        Modellist4.add(new ScreenModel("0","1",0));
        Modellist4.add(new ScreenModel("1-5","2",0));
        Modellist4.add(new ScreenModel("5-10","3",0));
        Modellist4.add(new ScreenModel("10-20","4",0));
        Modellist4.add(new ScreenModel("20以上","5",0));
        entity4.setList(Modellist4);
        entities.add(entity4);

        // 贡献签约数
        ScreenEntity entity5 = new ScreenEntity();
        entity5.setTitle("贡献签约数");
        List<ScreenModel> Modellist5 = new ArrayList<ScreenModel>();
        Modellist5.add(new ScreenModel("不限数量","0",1));
        Modellist5.add(new ScreenModel("0","1",0));
        Modellist5.add(new ScreenModel("1-5","2",0));
        Modellist5.add(new ScreenModel("5-10","3",0));
        Modellist5.add(new ScreenModel("10-20","4",0));
        Modellist5.add(new ScreenModel("20以上","5",0));
        entity5.setList(Modellist5);
        entities.add(entity5);

        return entities;
    }

    // 筛选时间数据
    public List<ScreenEntity> getTimesData(String timeTile) {
        List<ScreenEntity> entities = new ArrayList<ScreenEntity>();
        ScreenEntity entity = new ScreenEntity();
        entity.setTitle(timeTile);
        List<ScreenModel> Modellist = new ArrayList<ScreenModel>();
        Modellist.add(new ScreenModel("全部时间","0",1));
        Modellist.add(new ScreenModel("近7天","1",0));
        Modellist.add(new ScreenModel("近1个月","2",0));
        Modellist.add(new ScreenModel("近3个月","3",0));
        entity.setList(Modellist);
        entities.add(entity);
        return entities;
    }

    // APP退出登录
    public void loginOutApp(final Context context) {
        LoginOutRequest loginOutRequest = new LoginOutRequest();
        loginOutRequest.setReturnDataClick(context, 0);
    }

    // 项目介绍    招商详情
    private String introduceUrl, descriptionUrl;
    // 锦囊列表数据
    private List<StringModels> jingNs;

    public void setIntroduceUrl(String introduceUrl) {
        this.introduceUrl = introduceUrl;
    }

    public String getIntroduceUrl() {
        return introduceUrl;
    }

    public void setDescriptionUrl(String descriptionUrl) {
        this.descriptionUrl = descriptionUrl;
    }

    public String getDescriptionUrl() {
        return descriptionUrl;
    }

    public void setJingNs(List<StringModels> jingNs) {
        this.jingNs = jingNs;
        if (jingNs == null) {
            this.jingNs = new ArrayList<StringModels>();
        }
    }

    public List<StringModels> getJingNs() {
        return jingNs;
    }
}

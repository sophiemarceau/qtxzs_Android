package com.wecoo.qutianxia.models;

import com.wecoo.qutianxia.base.BaseBean;

import java.util.List;

/**
 * Created by wecoo on 2017/5/2.
 * 客户跟进信息
 */

public class CustomerFollowUpEntity extends BaseBean{

    private CustomerFollowUpModel platformFeedbackLogDto;
    private List<CustomerFollowUpModel> enterpriseFollowUpDtos;

    public CustomerFollowUpModel getPlatformFeedbackLogDto() {
        return platformFeedbackLogDto;
    }

    public void setPlatformFeedbackLogDto(CustomerFollowUpModel platformFeedbackLogDto) {
        this.platformFeedbackLogDto = platformFeedbackLogDto;
    }

    public List<CustomerFollowUpModel> getEnterpriseFollowUpDtos() {
        return enterpriseFollowUpDtos;
    }

    public void setEnterpriseFollowUpDtos(List<CustomerFollowUpModel> enterpriseFollowUpDtos) {
        this.enterpriseFollowUpDtos = enterpriseFollowUpDtos;
    }
}

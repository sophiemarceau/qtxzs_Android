package com.wecoo.qutianxia.requestjson;

/**
 * Created by wecoo on 2016/11/2.
 * 请求地址
 */

public class WebUrl {

    // Html 域名  m.qtxzs.com/app/xxx.html
    private static String html_Url = "http://m.qtxzs.com/app/";
//    private static String html_Url = "https://m.qtxzs.com/app/";
    // 域名地址（可修改）
    public static String root_Url = "http://app-api.qtxzs.com/";
//    public static String root_Url = "https://app-api.qtxzs.com/";
    // 用户相关
    private static String USER = "user/";
    // 图片
    private static String IMAGE = "image/";
    // 账户相关
    private static String ACCOUNT = "account/";
    // 报备相关
    private static String REPORT = "report/";
    // 消息相关
    private static String MESSAGE = "message/";
    // 项目（悬赏）相关
    private static String PROJECT = "project/";
    // 活动相关
    private static String ACTIVITY = "activity/";
    // 其他相关
    private static String WECOO = "wecoo/";


    /*       ===================   以上是共同的部分 （方便修改）   =========================*/

    // 获取验证码
    static String sendvalidatecodesms = USER + "sendvalidatecodesms/";
    // 登录
    static String login = USER + "login/";
    // App退出时，清除机器码（避免骚扰推送）
    static String loginOut = USER + "toBlank4MachineIdentificationCode/";
    // 个人信息
    static String getUserDetail = USER + "getUserDetail/";
    // 个人资料修改
    static String updateUserSalesmanInfo = USER + "updateUserSalesmanInfo/";
    // 获取用户报备质量分
    static String getReportEffectiveRate = USER + "getReportEffectiveRate/";
    // 获取业务员账户余额
//  static String getUserSalesmanBalance = USER + "getUserSalesmanBalance/";
    static String getUserSalesmanBalance = USER + "getUserSalesmanBalanceStr/";
    // 业务员提交实名认证信息
    static String submitIDInfo = USER + "submitIDInfo/";
    // 获取业务员实名认证信息
    static String getUserSalesmanIDInfoDto = USER + "getUserSalesmanIDInfoDto/";
    // 根据UserId发送验证码
    static String sendValidateCodeSmsByUserId = USER + "sendValidateCodeSmsByUserId/";
    // 根据 用户id 获取手机号
    static String getUserTel = USER + "getUserTel/";
    // 获取用户是否设置过提现密码
    static String isWithdrawPwdNull = USER + "isWithdrawPwdNull/";
    // 设置提现密码
    public static String setUpWithdrawPwd = USER + "setUpWithdrawPwd/";
    // 提现密码是否输入正确
//    public static String isWithdrawPwdRight = USER + "isWithdrawPwdRight/";
    // 修改提现密码
    public static String modifyWithdrawPwd = USER + "modifyWithdrawPwd/";
    // 重置密码
    static String resetWithdrawPwd = USER + "resetWithdrawPwd/";
    // 企业账号申请
    static String createCompany = USER + "createCompanyAccountAndInformation/";


    // 上传头像
    public static String uploadPhoto = IMAGE + "uploadPhoto/";
    // 上传图片文件（如投诉建议功能上传图片）
    public static String uploadPic = IMAGE + "uploadPic/";
    // 企业上传图片
    public static String uploadCompFile = IMAGE + "uploadCompFile/";


    // 账户变动明细
    static String searchSalemanAccountLogDtos = ACCOUNT + "searchSalemanAccountLogDtos/";
    // 申请提现
//    static String applyWithdraw = ACCOUNT + "applyWithdraw/";
    // 支付宝申请提现
//    public static String applyWithdrawByAlipay = ACCOUNT + "applyWithdrawByAlipay/";
    public static String applyWithdrawByAlipay = ACCOUNT + "applyWithdrawByAlipayNew/";
    // 修改申请提现接口-支付宝
//    public static String updateApplyWithdrawByAlipay = ACCOUNT + "updateApplyWithdrawByAlipay/";
    public static String updateApplyWithdrawByAlipay = ACCOUNT + "updateApplyWithdrawByAlipayNew/";
    // 银行卡申请提现接口
//    public static String applyWithdrawByCard = ACCOUNT + "applyWithdrawByCard/";
    public static String applyWithdrawByCard = ACCOUNT + "applyWithdrawByCardNew/";
    // 修改申请提现接口-银行卡
//    public static String updateApplyWithdrawByCard = ACCOUNT + "updateApplyWithdrawByCard/";
    public static String updateApplyWithdrawByCard = ACCOUNT + "updateApplyWithdrawByCardNew/";
    // 获取用户最后一次提现记录
    public static String getLastWithdrawalRecordByType = ACCOUNT + "getLastWithdrawalRecordByType/";
//    static String getLastWithdrawalRecord = ACCOUNT + "getLastWithdrawalRecord/";
    // 查询用户的申请记录
    static String searchWithdrawRecord = ACCOUNT + "searchSalesmanWithdrawingApplicationDtos/";
    // 查询提现进度
    static String getSalesmanWithdrawingApplicationLogList = ACCOUNT + "getSalesmanWithdrawingApplicationLogList/";
    // 根据提现Id 返回一条提现记录
    public static String getSalesmanWithdrawingApplicationDto = ACCOUNT + "getSalesmanWithdrawingApplicationDto/";
    // 首页是否显示新手引导
    static String isShowNewbieGuide = ACCOUNT + "isShowNewbieGuide/";
    // 获取提现申请的最低余额限制
    static String getWithdrawingLimit = ACCOUNT + "getWithdrawingLimit/";
    // 当前业务员是否可申请提现
    static String isWithdrawEnable = ACCOUNT + "isWithdrawEnable/";
    // 查询我的人脉收益
    static String searchConnectionContributionDtos = ACCOUNT + "searchConnectionContributionDtos/";
    // 查询我的人脉收益-统计总金额
    static String getMyContributionSumByLevelAndKind = ACCOUNT + "getMyContributionSumByLevelAndKind/";
    // 查询我的人脉详情下的动态
    static String searchConnectionDynamicDtos = ACCOUNT + "searchConnectionDynamicDtos/";


    // 获取客户列表
    static String searchCustomerDtos = REPORT + "searchCustomerDtos/";
    // 添加客户
    public static String addCustomer = REPORT + "addCustomer/";
    // 删除客户
    static String deleteCustomer = REPORT + "deleteCustomer/";
    // 修改客户
    public static String updateCustomer = REPORT + "updateCustomer/";
    // 获取某一个客户的信息
    static String getCustomerDto4User = REPORT + "getCustomerDto4User/";
    // 报备-数量统计
    static String getMyCustomerReportCount = REPORT + "getMyCustomerReportCount/";
    // 我的报备-核实中
//    public static String searchMyCustomerReportDtosVerifying = REPORT + "searchMyCustomerReportDtosVerifying/";
    public static String searchReportVerifying = REPORT + "searchParentCustomerReportDtosVerifying/";
    // 我的报备-跟进中
//    public static String searchMyCustomerReportDtosFollowing = REPORT + "searchMyCustomerReportDtosFollowing/";
    public static String searchReportFollowing = REPORT + "searchParentCustomerReportDtosFollowing/";
//    // 我的报备-考察中
//    public static String searchMyCustomerReportDtosInspecting = REPORT + "searchMyCustomerReportDtosInspecting/";
    // 我的报备-已签约
//    public static String searchMyCustomerReportDtosSignedUp = REPORT + "searchMyCustomerReportDtosSignedUp/";
    public static String searchReportSignedUp = REPORT + "searchParentCustomerReportDtosSignedUp/";
    // 我的报备-已退回
//    public static String searchMyCustomerReportDtosBack = REPORT + "searchMyCustomerReportDtosBack/";
    public static String searchReportBack = REPORT + "searchParentCustomerReportDtosBack/";
    // 查询报备进度
    public static String searchReportProgress = REPORT + "searchReportProgress/";
    // 添加报备
    static String addCustomerReport = REPORT + "addCustomerReport/";
    // 获取某一报备信息
    static String getCustomerReportDto = REPORT + "getCustomerReportDto/";
    // 获取项目的成交名单
//    static String searchReportListSignedUpDtoList = REPORT + "searchReportListSignedUpDtoList/";
    static String searchReportListSignedUpDtoList = REPORT + "searchReportListSignedUpDtos/";
    // 获取报备客户记录的锁定时间（天数）
    static String getReportLockTime = REPORT + "getReportLockTime/";
    // 当前业务员是否允许报备
    static String isReportAllowed = REPORT + "isReportAllowed/";
    // 客户跟进信息
    static String searchCustomerFollowUpInfo = REPORT + "searchCustomerFollowUpInfoDto/";
    public static String searchPlatformFeedbackCrlDtoList = REPORT + "searchPlatformFeedbackCrlDtoList/";
    // 审核列表
    static String searchProManager = REPORT + "searchCustomerReportDtosByProManager4App/";
    // 根据报备 id 加密串获取客户的手机号
    static String getCustomerTelByReportIdStr = REPORT + "getCustomerTelByReportIdStr/";
    // 通过审核
    public static String passAuditing4App = REPORT + "passAuditing4App/";
    // 报备退回
    public static String sendBack4App = REPORT + "sendBackCustomerReport4App/";
    // 签约打款
    public static String applySignedUpAuditing4App = REPORT + "applySignedUpAuditing4App/";
    // 根据报备 id 获取报备的状态
    static String getReportStatusByReportId4App = REPORT + "getReportStatusByReportId4App/";
    // 查询报备沟通记录
    static String searchCustomerReportLogs = "customerReportLog/searchCompanyCustomerReportLogDtos/";
    // 添加报备沟通记录
    public static String addCustomerReportLog = "customerReportLog/addCustomerReportLogSingle4App/";


    // 获取消息列表
    static String searchSysMsgDtos = MESSAGE + "searchSysMsgDtos/";
    // 消息置为已读
    static String updateSysMsgToRead = MESSAGE + "updateSysMsgToRead/";
    // 获取消息数量
    static String getSysMsgUnReadCount = MESSAGE + "getSysMsgUnReadCount/";


    // 我的相关数量
    static String getSalesmanUserRelatedCount = WECOO + "getSalesmanUserRelatedCount/";
    // 项目分类的行业
    static String lookupIndustryAll = WECOO + "lookupIndustryAll/";
    // 获取银行数据
    static String lookupBankAll = WECOO + "lookupBankAll/";
    // 创建投诉/意见反馈
    static String submitFeedback = WECOO + "submitFeedback/";
    // 添加关注
    static String addProjectCollectionRecord = WECOO + "addProjectCollectionRecord/";
    // 取消列表关注
    public static String cancelProjectCollectionRecord = WECOO + "cancelProjectCollectionRecord/";
    // 取消项目关注
    public static String cancelProjectCollection = WECOO + "cancelProjectCollection/";
    // 检查项目是否关注
    static String isProjectCollected = WECOO + "isProjectCollected/";
    // 我的关注
    static String getFollowList = WECOO + "searchProjectCollectionRecordDtos/";
    // 获取banner列表(app端)
    static String searchAdDtoList = WECOO + "searchAdDtoList/";
    // 增加项目/悬赏浏览记录
    static String addProjectBrowsingRecord = WECOO + "addProjectBrowsingRecord/";
    // 检查产品版本更新
    static String checkForUpdates = WECOO + "checkForUpdates/";
    // 获取打开APP时的加载图片（及对应跳转的Url）
    static String getLoadingPic = WECOO + "getLoadingPic/";
    // 查询用户报备质量分变更记录
    static String getReportfenDtos = WECOO + "searchSalesmanReporteffectiverateLogDtos/";
    // 获取用户头像邀请码邀请人数和总奖金
    public static String getInvitationUserInfo = WECOO + "getInvitationSalesmanRewardBalance/";
    // 查询邀请记录列表
    public static String getInvitationList = WECOO + "searchBeInviterSalesmanDtos/";
    // 查询被邀请人详情
    public static String getInvitationDetails = WECOO + "getBeInviterSalesmanDetailsDto/";
    // 查询人脉详情
    static String getConnectionDetail = WECOO + "getConnectionDetail/";
    // 查询我的人脉列表
    static String searchConnectionDtos = WECOO + "searchConnectionDtos/";
    // 获取我的人脉-用户头像,昵称,邀请码,邀请人数,人脉收益
    public static String getMyConnectionCountAndContributionSum = WECOO + "getMyConnectionCountAndContributionSum/";
    // APP获取提现规则文案
    static String getWithdrawRules = WECOO + "getWithdrawRules/";


    // 获取单个项目详情
    static String getProjectDto = PROJECT + "getProjectDto/";
    // 获取项目列表
//    static String searchProjects = PROJECT + "searchProjects/";
    // 获取首页推荐项目列表
    static String searchPromotingProjects = PROJECT + "searchPromotingProjects/";
    // 获取项搜索目列表
    static String searchSimpleAppProjectDtos = PROJECT + "searchSimpleAppProjectDtos/";
    // 获取我的项目列表
    static String searchMyProjectDtos = PROJECT + "searchMyProjectDtos/";


    // 获取活动列表
    static String searchActivityDtos4Show = ACTIVITY + "searchActivityDtos4Show/";
    // 获取单个活动详情
    static String getActivityDto = ACTIVITY + "getActivityDto/";

    // 发现
    public static String discover = html_Url + "discover.html";
    // 玩转问渠
    public static String wanZhuan = html_Url + "wanZhuan.html";
    // 关于
    public static String aboutUs = html_Url + "aboutUs.html";
    // 邀请好友
    public static String joinUs = html_Url + "joinUs.html";
    // 项目详情分享
    public static String projectDetails = html_Url + "projectDetails.html";
    // 法律声明和隐私策略
    public static String disclaimer = html_Url + "disclaimer.html";
    // 报备质量分规则明细
    public static String guize = html_Url + "guize.html";
    // 邀请规则明细
    public static String guizeinvitation = html_Url + "guizeinvitation.html";
    // 合伙人规则明细
    public static String guizepartner = html_Url + "guizepartner.html";
    // 没有合伙人时显示的Html
    public static String partnernull = html_Url + "partnernull.html";
    // h5 图片
    public static String img_invitationtop = html_Url + "img/invitationtop.jpg";

}

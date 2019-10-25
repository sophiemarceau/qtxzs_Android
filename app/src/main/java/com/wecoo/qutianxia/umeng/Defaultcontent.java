package com.wecoo.qutianxia.umeng;

import com.wecoo.qutianxia.application.WKApplication;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.utils.SPUtils;

/**
 * Created by mwl on 2016/11/18.
 * 分享的数据
 */

public class Defaultcontent {

    public static String shareUid = "?user_id=" + SPUtils.get(WKApplication.getAppContext(), Configs.user_id, "");

    public static String shareFriendtitle = "您的好友送您20元红包";
    public static String shareFriendtext = "听说，80%的渠道人都在这里赚到爆，快和好友一起共闯“渠天下”吧";
    public static String shareFriendurl = WebUrl.joinUs + shareUid;


    public static String shareProjectdtext = "大众创业，万众创新，美好生活从发现好项目开始";
    public static String shareProjecturl = WebUrl.projectDetails + shareUid + "&project_id=";

    public static String shareWanzhuanTitle = "玩转渠天下只需3步";
    public static String shareWanzhuantext = "玩转渠天下，每天10分钟，每月1万赏金很轻松！";
    public static String shareWanzhuanurl = WebUrl.wanZhuan;
}

package com.wecoo.qutianxia.manager;

import com.wecoo.qutianxia.models.ScreenModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wecoo on 2017/2/21.
 * 筛选结果数据管理
 */

public class ScreenResultManager {

    private static ScreenResultManager screenResultManager = null;
    private List<ScreenModel> resultList;// 暂时保存的数据

    private ScreenResultManager() {
        resultList = new ArrayList<ScreenModel>();
    }

    public static ScreenResultManager getManager() {
        if (screenResultManager == null) {
            screenResultManager = new ScreenResultManager();
        }
        return screenResultManager;
    }

    public void addScreenResult(ScreenModel model) {
        if (getCountByParentid(model.getId(), model.getParentId()) > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                if (model.getId() == resultList.get(i).getId()
                        && model.getParentId() == resultList.get(i).getParentId()) {
                    resultList.get(i).setType(0);
                }
            }
        }
        resultList.add(model);
    }

    public List<ScreenModel> getScreenResult() {
        return resultList;
    }

    private int getCountByParentid(int id, int parentid) {
        int count = 0;
        for (int i = 0; i < resultList.size(); i++) {
            if (id == resultList.get(i).getId() && parentid == resultList.get(i).getParentId()) {
                count++;
            }
        }
        return count;
    }

    public int getCountByPosition(int id, int parentid, int position) {
        int count = 0;
        for (int i = 0; i < resultList.size(); i++) {
            if (id == resultList.get(i).getId() && parentid == resultList.get(i).getParentId()
                    && position == resultList.get(i).getPosition()) {
                count++;
            }
        }
        return count;
    }

    // 修改某项已选择的数据
    public void upDateByModel(ScreenModel model) {
        if (isGroup(model)) {
            for (int i = 0; i < resultList.size(); i++) {
                if (model.getId() == resultList.get(i).getId()
                        && model.getParentId() == resultList.get(i).getParentId()
                        && model.getPosition() == resultList.get(i).getPosition()) {
                    resultList.get(i).setType(model.getType());
                } else if (model.getId() == resultList.get(i).getId()
                        && model.getParentId() == resultList.get(i).getParentId()) {
                    resultList.get(i).setType(0);
                }
            }
        } else {
            resultList.add(model);
        }
    }

    // 查看有没有同组数据
    private boolean isGroup(ScreenModel model) {
        boolean isGroup = false;
        for (int i = 0; i < resultList.size(); i++) {
            if (model.getParentId() == resultList.get(i).getParentId()) {
                isGroup = true;
            }
        }
        return isGroup;
    }

    // 根据按钮的索引获取已筛选的数据
    public List<ScreenModel> getDataByid(int id) {
        List<ScreenModel> byIdL = new ArrayList<ScreenModel>();
        for (int i = 0; i < resultList.size(); i++) {
            if (resultList.get(i).getId() == id) {
                byIdL.add(resultList.get(i));
            }
        }
        return byIdL;
    }

    // 根据按钮的索引获取已确定的数据
    public List<ScreenModel> getSureDataByid(int id) {
        List<ScreenModel> byIdL = new ArrayList<ScreenModel>();
        for (int i = 0; i < resultList.size(); i++) {
            if (resultList.get(i).getId() == id) {
                if (resultList.get(i).getType() == 1) {
                    resultList.get(i).setSured(true);
                    byIdL.add(resultList.get(i));
                } else {
                    resultList.get(i).setSured(false);
                }
            }
        }
        return byIdL;
    }

    // 清空所有已筛选的数据
    public void cleanAll() {
        resultList = new ArrayList<ScreenModel>();
    }

    // 判断是否是默认值
    public int getScreenCount(int id) {
        int count = 0;
        for (int i = 0; i < resultList.size(); i++) {
            if (id == resultList.get(i).getId()) {
                if (resultList.get(i).isSured() && resultList.get(i).getPosition() != 0) {
                    count++;
                }
            }
        }
        return count;
    }

}

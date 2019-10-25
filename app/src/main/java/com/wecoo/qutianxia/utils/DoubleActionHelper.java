package com.wecoo.qutianxia.utils;

/**
 * Created by mwl on 2016/10/20.
 * 工具类，用于辅助双击退出实现
 */
public class DoubleActionHelper {

    private Runnable firstAction;
    private Runnable secondAction;
    private long firstTime;
    private final int delay;

    public DoubleActionHelper(Runnable first, Runnable second, int delayMillis) {
        firstTime = 0;
        firstAction = first;
        secondAction = second;
        this.delay = delayMillis;
    }

    public void action() {
        long t = System.currentTimeMillis();
        if ((t - firstTime) > delay) {
            firstTime = t;
            firstAction.run();
        } else {
            secondAction.run();
        }
    }
}

package com.ctrl.forum.entity;

/**
 * Created by Administrator on 2016/6/3.
 */
public class Data {
    private int evaluationCount;
    private int handleDay; //(拉黑天数)

    public int getHandleDay() {
        return handleDay;
    }

    public void setHandleDay(int handleDay) {
        this.handleDay = handleDay;
    }

    public int getEvaluationCount() {
        return evaluationCount;
    }

    public void setEvaluationCount(int evaluationCount) {
        this.evaluationCount = evaluationCount;
    }
}

package com.ctrl.forum.entity;

/**
 * 会员等级信息
 * Created by Administrator on 2016/5/19.
 */
public class LevelInfo {
    private int todayExp; //今日获得的经验值
    private int dayMaxExp; //每日经验上限
    private int nextLevelExp; //下一级别所需的经验
    private int currentExp; //当前经验值

    public int getCurrentExp() {
        return currentExp;
    }

    public void setCurrentExp(int currentExp) {
        this.currentExp = currentExp;
    }

    public int getDayMaxExp() {
        return dayMaxExp;
    }

    public void setDayMaxExp(int dayMaxExp) {
        this.dayMaxExp = dayMaxExp;
    }

    public int getNextLevelExp() {
        return nextLevelExp;
    }

    public void setNextLevelExp(int nextLevelExp) {
        this.nextLevelExp = nextLevelExp;
    }

    public int getTodayExp() {
        return todayExp;
    }

    public void setTodayExp(int todayExp) {
        this.todayExp = todayExp;
    }
}

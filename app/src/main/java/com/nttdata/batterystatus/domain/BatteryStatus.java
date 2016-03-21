package com.nttdata.batterystatus.domain;

/**
 * Created by kirankumar on 14/03/16.
 */
public class BatteryStatus {

    private boolean isAmber;

    private boolean isCharging;

    private boolean isDanger;

    private boolean isDocked;

    private Integer level;


    public boolean isAmber() {
        return isAmber;
    }

    public void setIsAmber(boolean isAmber) {
        this.isAmber = isAmber;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setIsCharging(boolean isCharging) {
        this.isCharging = isCharging;
    }

    public boolean isDanger() {
        return isDanger;
    }

    public void setIsDanger(boolean isDanger) {
        this.isDanger = isDanger;
    }

    public boolean isDocked() {
        return isDocked;
    }

    public void setIsDocked(boolean isDocked) {
        this.isDocked = isDocked;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}

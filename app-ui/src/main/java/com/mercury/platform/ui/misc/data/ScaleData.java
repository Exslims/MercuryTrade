package com.mercury.platform.ui.misc.data;

public class ScaleData {
    private float notificationScale;
    private float taskBarScale;
    private float itemCellScale;

    public ScaleData() {
    }

    public ScaleData(float notificationScale, float taskBarScale, float itemCellScale) {
        this.notificationScale = notificationScale;
        this.taskBarScale = taskBarScale;
        this.itemCellScale = itemCellScale;
    }

    public float getNotificationScale() {
        return notificationScale;
    }

    public void setNotificationScale(float notificationScale) {
        this.notificationScale = notificationScale;
    }

    public float getTaskBarScale() {
        return taskBarScale;
    }

    public void setTaskBarScale(float taskBarScale) {
        this.taskBarScale = taskBarScale;
    }

    public float getItemCellScale() {
        return itemCellScale;
    }

    public void setItemCellScale(float itemCellScale) {
        this.itemCellScale = itemCellScale;
    }
}

package com.mercury.platform.shared.pojo;

/**
 * Created by Константин on 26.02.2017.
 */
public class ResponseButton implements Comparable<ResponseButton>{
    private long id;
    private boolean kick;
    private boolean close;
    private String title;
    private String responseText;

    public ResponseButton(long id,String title, String responseText, boolean kick, boolean close) {
        this.id = id;
        this.title = title;
        this.responseText = responseText;
        this.kick = kick;
        this.close = close;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public boolean isKick() {
        return kick;
    }

    public void setKick(boolean kick) {
        this.kick = kick;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    @Override
    public int compareTo(ResponseButton o) {
        if(this.getId() > o.getId()) {
            return 1;
        }else if(this.getId() < o.getId()){
            return -1;
        }else {
            return 0;
        }
    }
}

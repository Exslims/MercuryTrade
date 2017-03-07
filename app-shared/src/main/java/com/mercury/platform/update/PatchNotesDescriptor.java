package com.mercury.platform.update;

import java.io.Serializable;

/**
 * Created by Константин on 07.03.2017.
 */
public class PatchNotesDescriptor implements Serializable{
    private String json;

    public PatchNotesDescriptor(String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}

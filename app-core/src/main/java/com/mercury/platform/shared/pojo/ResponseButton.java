package com.mercury.platform.shared.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseButton implements Comparable<ResponseButton>{
    private long id;
    private boolean kick;
    private boolean close;
    private String title;
    private String responseText;

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

package com.mercury.platform.shared.config.descriptor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseButtonDescriptor implements Comparable<ResponseButtonDescriptor>{
    private long id;
    private boolean kick;
    private boolean close;
    private String title;
    private String responseText;

    @Override
    public int compareTo(ResponseButtonDescriptor o) {
        if(this.getId() > o.getId()) {
            return 1;
        }else if(this.getId() < o.getId()){
            return -1;
        }else {
            return 0;
        }
    }
}

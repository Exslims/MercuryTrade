package com.mercury.platform.shared.config.descriptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseButtonDescriptor implements Comparable<ResponseButtonDescriptor>, Serializable {
    private long id;
    private boolean close;
    private String title = "label";
    private String responseText = "response";
    private HotKeyDescriptor hotKeyDescriptor = new HotKeyDescriptor();

    @Override
    public int compareTo(ResponseButtonDescriptor o) {
        if (this.getId() > o.getId()) {
            return 1;
        } else if (this.getId() < o.getId()) {
            return -1;
        } else {
            return 0;
        }
    }
}

package com.mercury.platform.ui.components.panel.chat;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class HtmlMessageBuilder {
    private List<String> chunkStrings;
    private String stubMessage;

    public HtmlMessageBuilder() {
        this.chunkStrings = new ArrayList<>();
    }

    /**
     * Building HTML equivalent message with highlighting matched chunks
     *
     * @param message source message
     * @return html equivalent
     */
    public String build(String message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>");
        this.stubMessage = message;
        this.chunkStrings.forEach(it -> {
            if (StringUtils.containsIgnoreCase(message, it)) {
                this.stubMessage = stubMessage.replaceAll("(?i)" + it, "<font color=\"#FFD393\">" + it + "</font>");
            }
        });
        stringBuilder.append(this.stubMessage);
        stringBuilder.append("</html>");
        return stringBuilder.toString();
    }

    public List<String> getChunkStrings() {
        return chunkStrings;
    }

    public void setChunkStrings(List<String> chunkStrings) {
        this.chunkStrings = chunkStrings;
    }
}

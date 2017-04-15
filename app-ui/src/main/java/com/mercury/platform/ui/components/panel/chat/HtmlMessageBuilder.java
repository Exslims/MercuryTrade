package com.mercury.platform.ui.components.panel.chat;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlMessageBuilder {
    private List<String> chunkStrings;

    public HtmlMessageBuilder() {
        this.chunkStrings = new ArrayList<>();
    }

    /**
     * Building HTML equivalent message with highlighting matched chunks
     * @param message source message
     * @return html equivalent
     */
    public String build(String message) {
        List<String> resultStrs = new ArrayList<>();
        resultStrs.add("<html>");
        String[] words = message.split("((?<=\\s)|(?=\\s)|(?<=\\.)|(?=\\.)|(?<=,)|(?<=\\?)|(?=\\?)|(?=,)|(?<=!)|(?=!)|(?<=/)|(?=/)|(?<=>)|(?=>))");
        Arrays.stream(words).forEach(word -> {
            if(chunkStrings.stream().noneMatch(word::equalsIgnoreCase)){
                resultStrs.add(word);
            }else {
                resultStrs.add("<font color=\"#FFD393\">" + word + "</font>");
            }
        });
        resultStrs.add("</html>");
        return String.join("",resultStrs);
    }

    public List<String> getChunkStrings() {
        return chunkStrings;
    }

    public void setChunkStrings(List<String> chunkStrings) {
        this.chunkStrings = chunkStrings;
    }
}

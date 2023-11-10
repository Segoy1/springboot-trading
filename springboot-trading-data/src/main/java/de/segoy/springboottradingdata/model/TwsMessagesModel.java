package de.segoy.springboottradingdata.model;

import java.util.Collection;

public class TwsMessagesModel {

    public void add(Collection<String> lines) {
        for (String line : lines) {
            add(line);
        }
    }
    public void add(String line) {
//        m_textArea.append(line + lineSeparator);
//        moveCursorToEnd();
    }
    public void addText(String text) {
//        add(tokenizedIntoList(detabbed(text), LF));
    }

}

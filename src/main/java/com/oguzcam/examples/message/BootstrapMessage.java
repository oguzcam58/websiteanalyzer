package com.oguzcam.examples.message;

/**
 * This class keeps Bootstrap messages as type and content
 * in order to show on the page easily.
 */
public class BootstrapMessage {

    private BSMessageType type;
    private String content;

    // Default constructor
    public BootstrapMessage() {
    }

    // Full args constructor
    public BootstrapMessage(BSMessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    public BSMessageType getType() {
        return type;
    }

    public void setType(BSMessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public enum BSMessageType {
        SUCCESS, DANGER, WARNING, INFO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BootstrapMessage that = (BootstrapMessage) o;

        return type == that.type && content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BootstrapMessage{" +
                "type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}


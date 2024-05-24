package org.ruananta.parser.engine;

public class ExtractedData {
    private String data;

    public ExtractedData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data;
    }
}

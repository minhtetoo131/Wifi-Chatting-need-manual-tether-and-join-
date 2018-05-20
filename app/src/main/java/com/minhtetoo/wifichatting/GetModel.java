package com.minhtetoo.wifichatting;

public class GetModel {
    String text;
    boolean isMeOrU;

    public String getText() {
        return text;
    }

    public boolean isMeOrU() {
        return isMeOrU;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMeOrU(boolean meOrU) {
        isMeOrU = meOrU;
    }
}

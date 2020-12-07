package com.ropkastudios.android.thegeographyapp;

import java.util.ArrayList;
public class StringArrayHolder {
    private ArrayList<String> One;
    private ArrayList<String> Two;
    private ArrayList<String> Three;
    private ArrayList<String> Four;

    public StringArrayHolder(ArrayList<String> one, ArrayList<String> two, ArrayList<String> three, ArrayList<String> four) {
        One = one;
        Two = two;
        Three = three;
        Four = four;
    }

    public ArrayList<String> getFirstArray() {
        return One;
    }

    public ArrayList<String> getSecondArray() {
        return Two;
    }

    public ArrayList<String> getThirdArray() {
        return Three;
    }

    public ArrayList<String> getFourthArray() {
        return Four;
    }
}

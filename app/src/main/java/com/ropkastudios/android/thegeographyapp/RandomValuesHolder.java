package com.ropkastudios.android.thegeographyapp;

import java.util.UUID;
public class RandomValuesHolder {
    UUID uuid;
    int Int;
    boolean Bool;
    String Texter;

    public RandomValuesHolder(int integer, boolean bool, String string) {
        uuid = UUID.randomUUID();
        Int = integer;
        Bool = bool;
        Texter = string;
    }

    public RandomValuesHolder(UUID Uuid, int integer, boolean bool, String string) {
        uuid = Uuid;
        Int = integer;
        Bool = bool;
        Texter = string;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getInt() {
        return Int;
    }

    public boolean getBoolean() {
        return Bool;
    }

    public String getString() {
        return Texter;
    }
}

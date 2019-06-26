package com.example.newsfeed.data.typeconverters;

import android.util.Log;

import androidx.room.TypeConverter;

import com.example.newsfeed.data.models.Fields;

public class FieldsTypeConverter {

    @TypeConverter
    public static Fields toFields(String value) {
        return value == null ? null : new Fields(value);
    }

    @TypeConverter
    public static String toString(Fields value) {
        Log.d("a", "a");
        return value == null ? null : value.getThumbnail();
    }
}

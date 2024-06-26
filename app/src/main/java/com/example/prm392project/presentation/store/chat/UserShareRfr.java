package com.example.prm392project.presentation.store.chat;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.prm392project.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class UserShareRfr {

    private static final String PREF_NAME = "user_pref";
    private static final String KEY_ITEMS = "users";
    public static void saveItems(Context context, User items) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(items);

        editor.putString(KEY_ITEMS, json);
        editor.apply();
    }
    public static User getUserChat(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_ITEMS, null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<User>() {}.getType();
            return gson.fromJson(json, type);
        }

        return new User();
    }
    public static void clearItems(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Ghi đè danh sách đã lưu bằng một danh sách trống
        editor.putString(KEY_ITEMS, null);

        // Hoặc có thể sử dụng danh sách trống để ghi đè
        // List<Item> emptyItemList = new ArrayList<>();
        // String emptyItemListJson = new Gson().toJson(emptyItemList);
        // editor.putString(KEY_ITEMS, emptyItemListJson);

        editor.apply();
    }
}

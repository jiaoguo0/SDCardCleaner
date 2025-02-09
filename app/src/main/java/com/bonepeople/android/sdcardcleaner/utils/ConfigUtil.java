package com.bonepeople.android.sdcardcleaner.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Set;

/**
 * 负责配置文件的读写
 * <p>
 * Created by bonepeople on 2017/11/29.
 */

public class ConfigUtil {
    private static final String FILENAME = "config";
    private static final String FIELD_SAVELIST = "saveList";
    private static final String FIELD_CLEANLIST = "cleanList";

    /**
     * 从配置文件中读取保留文件列表
     */
    @Nullable
    public static Set<String> getSaveList(@NonNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(FIELD_SAVELIST, null);
    }

    /**
     * 从配置文件中读取清理文件列表
     */
    @Nullable
    public static Set<String> getCleanList(@NonNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(FIELD_CLEANLIST, null);
    }
}

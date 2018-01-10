package com.bonepeople.android.sdcardcleaner.basic;

import android.os.Handler;
import android.os.Message;

/**
 * 公共的Handler类
 * <p>
 * Created by bonepeople on 2017/12/25.
 */

public class BaseHandler extends Handler {
    private BaseAppCompatActivity _activity;

    BaseHandler(BaseAppCompatActivity _activity) {
        this._activity = _activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (_activity != null && !_activity.isDestroyed())
            _activity.handleMessage(msg);
    }

    void destroy() {
        _activity = null;
        removeCallbacksAndMessages(null);
    }
}
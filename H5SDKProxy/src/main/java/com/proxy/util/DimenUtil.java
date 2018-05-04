package com.proxy.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import com.proxy.config.McProxy;


/**
 * Created
 */

public final class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = McProxy.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = McProxy.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}

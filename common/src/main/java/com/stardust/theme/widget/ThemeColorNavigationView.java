package com.stardust.theme.widget;

import android.content.Context;
import android.content.res.ColorStateList;

import android.util.AttributeSet;

import com.google.android.material.navigation.NavigationView;
import com.stardust.theme.ThemeColor;
import com.stardust.theme.ThemeColorManager;
import com.stardust.theme.ThemeColorMutable;

import java.lang.reflect.Field;

/**
 * Created by Stardust on 2016/8/15.
 */

public class ThemeColorNavigationView extends NavigationView implements ThemeColorMutable {

    private static final int COLOR_GRAY = 0xff7a7a7a;
    private static Field PRESENTER_FIELD;

    static {
        try {
            PRESENTER_FIELD = NavigationView.class.getDeclaredField("presenter");
            PRESENTER_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public ThemeColorNavigationView(Context context) {
        super(context);
        init();
    }

    public ThemeColorNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThemeColorNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ThemeColorManager.add(this);
    }

    @Override
    public void setThemeColor(ThemeColor color) {
        ColorStateList list = new ColorStateList(new int[][]{{android.R.attr.state_checked}, {-android.R.attr.state_checked}}, new int[]{color.colorPrimary, COLOR_GRAY});
        setItemIconTintList(list);
        setItemTextColor(list);
    }

}

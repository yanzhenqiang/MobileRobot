package com.stardust.theme.internal;

import android.annotation.TargetApi;
import android.os.Build;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.ScrollView;

import java.lang.reflect.Field;

/**
 * Created by Stardust on 2016/8/14.
 */
public class ScrollingViewEdgeGlowColorHelper {

    private static final Field SCROLL_VIEW_FIELD_EDGE_GLOW_TOP;
    private static final Field SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    private static final Field LIST_VIEW_FIELD_EDGE_GLOW_TOP;
    private static final Field LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    static {
        Field[] fields = getEdgeGlowField(ScrollView.class);
        SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = fields[0];
        SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = fields[1];
        fields = getEdgeGlowField(AbsListView.class);
        LIST_VIEW_FIELD_EDGE_GLOW_TOP = fields[0];
        LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM = fields[0];
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setEdgeGlowColor(AbsListView listView, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                EdgeEffect ee = (EdgeEffect) LIST_VIEW_FIELD_EDGE_GLOW_TOP.get(listView);
                ee.setColor(color);
                ee = (EdgeEffect) LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(listView);
                ee.setColor(color);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setEdgeGlowColor(ScrollView scrollView, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                EdgeEffect ee = (EdgeEffect) SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.get(scrollView);
                ee.setColor(color);
                ee = (EdgeEffect) SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(scrollView);
                ee.setColor(color);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static Field[] getEdgeGlowField(Class<?> viewClass) {
        Field edgeGlowTop = null, edgeGlowBottom = null;
        for (Field f : viewClass.getDeclaredFields()) {
            switch (f.getName()) {
                case "mEdgeGlowTop":
                    f.setAccessible(true);
                    edgeGlowTop = f;
                    break;
                case "mEdgeGlowBottom":
                    f.setAccessible(true);
                    edgeGlowBottom = f;
                    break;
            }
            if (edgeGlowBottom != null && edgeGlowTop != null)
                break;
        }
        return new Field[]{edgeGlowTop, edgeGlowBottom};
    }
}
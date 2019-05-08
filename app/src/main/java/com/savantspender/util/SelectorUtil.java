package com.savantspender.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;

public class SelectorUtil {
//    public static StateListDrawable makeSelector(int color) {
//        StateListDrawable res = new StateListDrawable();
////        res.setExitFadeDuration(400);
////        res.setAlpha(45);
////        res.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(color));
////        res.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
//        res.addState(new int[] {android.R.attr.state_activated}, new ColorDrawable(color));
//        res.addState(new int[] {-android.R.attr.state_activated}, new ColorDrawable(Color.BLACK));
//        res.addState(new int[] {}, new ColorDrawable(Color.RED));
//        return res;
//    }

    public static StateListDrawable makeSelector(int color) {
        StateListDrawable res=  new StateListDrawable();

        res.addState(new int[] {android.R.attr.state_activated}, new ColorDrawable(color));
        res.addState(new int[] {}, new ColorDrawable(Color.TRANSPARENT));

        return res;
    }
}

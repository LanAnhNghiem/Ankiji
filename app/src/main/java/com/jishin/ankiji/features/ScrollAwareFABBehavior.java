package com.jishin.ankiji.features;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

/**
 * Created by lana on 16/01/2018.
 */

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        if(axes > 0 && child.getVisibility() == View.VISIBLE){
            child.hide();
            Log.d("Floatbtn","onStart: if");
        }else if(axes < 0 && child.getVisibility() != View.VISIBLE){
            child.show();
            Log.d("Floatbtn","onStart: hide");
        }

        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

}

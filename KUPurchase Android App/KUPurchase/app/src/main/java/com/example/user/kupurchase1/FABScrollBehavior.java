package com.example.user.kupurchase1;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 2016-03-12.
 */
public class FABScrollBehavior extends FloatingActionButton.Behavior {

    public FABScrollBehavior(Context context, AttributeSet attributeSet){
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if(dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.hide();
        } else if(dyConsumed < 0 && child.getVisibility() == View.GONE) {
            child.show();
        }


    }
}

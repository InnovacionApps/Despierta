package com.targetapps.despierta.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.targetapps.despierta.R;

/**
 * Created by JOKO on 20/04/2015.
 */
public class BottomActionBar extends LinearLayout {

    public interface OnMenuItemClickListener {
        public boolean onMenuItemClick(MenuItem item);
    }

    private static final String TAG = BottomActionBar.class.getSimpleName();

    private OnMenuItemClickListener mItemClickListener;

    public BottomActionBar(Context context) {
        super(context);
        init();
    }

    public BottomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_actionbar_main, this, false);
        this.addView(view);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void inflateMenu(int menuResId) {
        // todo: figure out how to use a custom Menu.
//        MenuInflater inflater = new MenuInflater(getContext());
    }
}

package com.targetapps.despierta.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.targetapps.despierta.R;

/**
 * Created by JOKO on 20/04/2015.
 */
public class BottomActionButton extends LinearLayout {

    private static final String TAG = BottomActionButton.class.getSimpleName();

    private TextView mActionLabel;
    private ImageView mActionIcon;


    public BottomActionButton(Context context) {
        super(context);
        init();
    }

    public BottomActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.actionbar_action_button, this, false);
        //mActionIcon = (ImageView)view.findViewById(R.id.bottom_action_button_icon);
        addView(view);
    }

    public void setActionTitle(String title) {
        mActionLabel.setText(title);
    }

    public void setActionTitle(int titleRes) {
        mActionLabel.setText(titleRes);
    }

    public void setActionIcon(Drawable icon) {
        mActionIcon.setImageDrawable(icon);
    }

    public void setActionIcon(int iconRes) {
        mActionIcon.setImageResource(iconRes);
    }
}

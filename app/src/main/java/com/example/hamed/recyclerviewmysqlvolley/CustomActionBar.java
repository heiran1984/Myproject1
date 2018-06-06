package com.example.hamed.recyclerviewmysqlvolley;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Hamed on 19/04/2018.
 */

public class CustomActionBar{
    private Context context;
    private String title;
    private RelativeLayout.LayoutParams layoutparams;




    public CustomActionBar(Context context, android.support.v7.app.ActionBar actionBar, String title) {
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"fonts/IRANSansMobile.ttf");

        TextView textview = new TextView(context);
        layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        textview.setLayoutParams(layoutparams);
        textview.setText(title);
        textview.setGravity(Gravity.CENTER);
        textview.setTextColor(Color.parseColor("#000000"));
        textview.setTypeface(typeface);
        textview.setTextSize(25);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(textview);

    }
}

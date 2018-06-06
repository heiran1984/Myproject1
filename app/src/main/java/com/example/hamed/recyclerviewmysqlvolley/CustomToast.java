package com.example.hamed.recyclerviewmysqlvolley;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Hamed on 22/04/2018.
 */

public class CustomToast extends AppCompatActivity {

    public CustomToast(Context context, String text) {




        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) ((Activity)context).findViewById(R.id.custom_toast_container));
        TextView text1 = (TextView) layout.findViewById(R.id.text);
        text1.setText(text);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }
}

package com.example.hamed.recyclerviewmysqlvolley;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

import static java.lang.Integer.parseInt;

public class NumberTextWatcher implements TextWatcher {

    DecimalFormat formatter=new DecimalFormat("#,###,###");

    private EditText et;

    public NumberTextWatcher(EditText et)
    {

        this.et = et;
    }

    @Override
    public void afterTextChanged(Editable s)
    {
        et.removeTextChangedListener(this);
        int a,b;
        if(et.length()!=0) {
          //  a = parseInt(editTextMVame.getText().toString().replace(",", ""));
            String b1 = et.getText().toString().replace(",", "");
            et.setText(formatter.format(parseInt(b1)));
            et.setSelection(et.getText().length());
           // editTextMAghsat.setText(formatter.format(a / parseInt(b1)));
        }
        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

}
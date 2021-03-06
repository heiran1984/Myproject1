package com.example.hamed.recyclerviewmysqlvolley;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class NewVamActivity extends MyActivity implements View.OnClickListener {
    Context context;
    EditText editTextUsername;
    EditText editTextMVame;
    EditText editTextTAghsat;
    EditText editTextMAghsat;
    EditText editTextMandaAghsat;
    CheckBox next_mah;
    Button buttonNewVam;
    String mizan,mvam;
    long  MojodiSandogh;
    String t_pardakhtshoda="0",tozihat="";
    JSONArray jsonArray;
    ArrayList<String> Codes;
    DecimalFormat formatter=new DecimalFormat("#,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_new_vam);
        new  CustomActionBar(this,getSupportActionBar(),"");
        next_mah=(CheckBox)findViewById(R.id.next_mah);

        editTextUsername=(EditText)findViewById(R.id.editTextUsername);
        editTextMVame=(EditText)findViewById(R.id.editTextMVam);

        editTextMVame.addTextChangedListener(new NumberTextWatcher(editTextMVame));


        buttonNewVam=(Button)findViewById(R.id.buttonNewVam);
        editTextTAghsat=(EditText)findViewById(R.id.editTAghsat);
        editTextTAghsat.addTextChangedListener(mTextEditorWatcher);
        editTextMAghsat=(EditText)findViewById(R.id.editMaghsat);

        editTextMandaAghsat=(EditText)findViewById(R.id.MandaAghsat);
        editTextMandaAghsat.addTextChangedListener(new NumberTextWatcher(editTextMandaAghsat));


        Bundle extras=getIntent().getExtras();
        String username=extras.getString("USERNAME");
         mvam=extras.getString("MVAM");
         tozihat=extras.getString("TOZIHAT");
         Codes=(ArrayList<String>)getIntent().getSerializableExtra("codes");
         jsonArray=new JSONArray(Codes);
        String taghsat=editTextTAghsat.getText().toString();
        getMojodi();
        editTextUsername.setText(username);
        buttonNewVam.setOnClickListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    public void newVam(){
        final String mvam=(editTextMVame.getText().toString().trim()).replace(",","");
        final String username=editTextUsername.getText().toString().trim();
        final String taghsat=editTextTAghsat.getText().toString().trim();
        final String maghsat=(editTextMAghsat.getText().toString().trim()).replace(",","");

        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.URL_NEWVAM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            new CustomToast(context,jsonObject.getString("message"));

                            finish();
                         } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>  params=new HashMap<>();
                params.put("username",username);
                params.put("m_vam",mvam);
                params.put("t_aghsat",taghsat);
                params.put("m_aghsat",maghsat);
                params.put("t_pardakhtshoda",t_pardakhtshoda);
                params.put("tozihat",tozihat);
                params.put("codes",jsonArray.toString());
                params.put("next_mah",String.valueOf(next_mah.isChecked()));
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        int a,b,c,d;
        a=Integer.parseInt((editTextMVame.getText().toString()).replace(",",""));
        b=Integer.parseInt((editTextMAghsat.getText().toString()).replace(",",""));
        c=Integer.parseInt((editTextTAghsat.getText().toString()).replace(",",""));
        d=Integer.parseInt((editTextMandaAghsat.getText().toString()).replace(",",""));


        //int b=Integer.parseInt(MojodiSandogh);
        if(a<=MojodiSandogh){
           if(((b*c)==a)  && (d<=c)) {
               t_pardakhtshoda=Integer.toString(c-d);
               newVam();
           }

        }
        else{

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("مبلغ وام بیشتر از موجودی صندوق می باشد!");
            builder1.setCancelable(false);
            builder1.setPositiveButton(
                    "خب",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }

    public void getMojodi(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.GET_MOJODI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj=new JSONObject(response);
                            int vam,taghsat;
                            vam=obj.getInt("mizan")*parseInt(mvam);
                            taghsat=obj.getInt("t_aghsat");
                            editTextMVame.setText(formatter.format(vam));
                            editTextMVame.addTextChangedListener(mTextEditorWatcher1);
                            editTextTAghsat.setText(formatter.format(taghsat));
                            editTextMAghsat.setText(formatter.format(vam/taghsat));
                            editTextMandaAghsat.setText(formatter.format(taghsat));
                            MojodiSandogh=obj.getLong("smojodi");
                            actionbarMojodi();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void actionbarMojodi(){
        new  CustomActionBar(this,getSupportActionBar(),"موجودی صندوق="+formatter.format(MojodiSandogh));

    }


    private final TextWatcher mTextEditorWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

           editTextTAghsat.removeTextChangedListener(this);
            long a;
            if(editTextTAghsat.length()!=0) {
                a = Long.parseLong(editTextMVame.getText().toString().replace(",", ""));
                String b1 = editTextTAghsat.getText().toString().replace(",", "");
                editTextTAghsat.setText(formatter.format(parseInt(b1)));
                editTextTAghsat.setSelection(editTextTAghsat.getText().length());
                editTextMAghsat.setText(formatter.format(a / parseInt(b1)));
            }
            editTextTAghsat.addTextChangedListener(this);


        }
    };

    private final TextWatcher mTextEditorWatcher1=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            editTextMVame.removeTextChangedListener(this);
            long a;
            if(editTextMVame.length()!=0) {
                a = Long.parseLong(editTextMVame.getText().toString().replace(",", ""));
                String b1 = editTextTAghsat.getText().toString().replace(",", "");
                editTextMVame.setText(formatter.format(a));
                editTextMVame.setSelection(editTextMVame.getText().length());
                editTextMAghsat.setText(formatter.format(a / parseInt(b1)));
            }
            editTextMVame.addTextChangedListener(this);

        }
    };



}

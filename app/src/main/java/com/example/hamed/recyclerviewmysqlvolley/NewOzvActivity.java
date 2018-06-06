package com.example.hamed.recyclerviewmysqlvolley;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewOzvActivity extends MyActivity implements View.OnClickListener {
    EditText editTextUserName,editTextPassword,editTextMojodi,editTextSearch;
    Button buttonRegister;
    int code=0;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ozv);
        context=this;
        new  CustomActionBar(this,getSupportActionBar(),"عضو جدید");

       // Typeface typeface=Typeface.createFromAsset(getAssets(),"B Nazanin.ttf");
        editTextUserName=(EditText)findViewById(R.id.editTextUsername);
      //  editTextUserName.setTypeface(typeface);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
     //   editTextPassword.setTypeface(typeface);
        editTextMojodi=(EditText)findViewById(R.id.editTextMojodi);
     //   editTextMojodi.setTypeface(typeface);
        editTextMojodi.addTextChangedListener(new NumberTextWatcher(editTextMojodi));

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        editTextUserName.setFocusableInTouchMode(true);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);


        buttonRegister=(Button)findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);

        Bundle extras=getIntent().getExtras();
        if(extras!=null) {
            buttonRegister.setText("ویرایش");
            String username = extras.getString("USERNAME");
            String mojodi=extras.getString("MOJODI");
            code=extras.getInt("CODE");
            editTextUserName.setText(username);
            editTextMojodi.setText(mojodi);
        }
    }

    @Override
    public void onClick(View v) {
        if(editTextUserName.getText().toString().equals("")){
            editTextUserName.setHintTextColor(Color.RED);
            editTextUserName.setHint("لطفا نام کاربری را وارد کنید!");

        }
        else registerUser();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void registerUser(){
        final String mojodi=(editTextMojodi.getText().toString().trim()).replace(",","");
        final String username=editTextUserName.getText().toString().trim();
        final String password=editTextPassword.getText().toString().trim();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.URL_REGISTER+"?string="+code,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            new CustomToast(context,jsonObject.getString("message"));
                           // Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            if(!jsonObject.getBoolean("error")) {
                                if (code != 0) {
                                    Intent intent = new Intent();
                                    intent.putExtra("USERNAME", username);
                                    intent.putExtra("MOJODI", mojodi);
                                    setResult(Activity.RESULT_OK, intent);
                                }
                                finish();
                            }
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
                params.put("mojodi",mojodi);
                params.put("password",password);
                return params;
            }


        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

package com.example.hamed.recyclerviewmysqlvolley;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class NewMojodiActivity extends MyActivity implements View.OnClickListener {

    EditText editTextUsername,editTextMahiyane,editTextTmah;
    Button buttonSave;
    int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mojodi);
        new  CustomActionBar(this,getSupportActionBar(),"افزایش موجودی");

      //  Typeface typeface=Typeface.createFromAsset(getAssets(),"IRANSansMobile.ttf");
        Bundle extras=getIntent().getExtras();
        String username=extras.getString("USERNAME");
        code=extras.getInt("CODE");

        editTextUsername=(EditText)findViewById(R.id.editTextUsername);
        editTextUsername.setText(username);
        editTextUsername.setEnabled(false);

        editTextMahiyane=(EditText)findViewById(R.id.editTextMahiyaneh);
        editTextMahiyane.addTextChangedListener(new NumberTextWatcher(editTextMahiyane));
        editTextTmah=(EditText)findViewById(R.id.editTMah);
        buttonSave=(Button)findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        MojodiInfo();

    }

    @Override
    public void onClick(View v) {
        NewMojodi();
    }

    public void NewMojodi(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.NEW_MOJODI+"?string=new",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

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
                params.put("id",Integer.toString(code));
                params.put("mablagh",editTextMahiyane.getText().toString().trim().replace(",",""));
                params.put("mah",editTextTmah.getText().toString().trim());
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void MojodiInfo(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.NEW_MOJODI+"?string="+code,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj=new JSONObject(response);
                            editTextMahiyane.setText(String.valueOf(obj.getInt("mablagh")));
                            editTextTmah.setText(String.valueOf(obj.getInt("mah")));

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

}

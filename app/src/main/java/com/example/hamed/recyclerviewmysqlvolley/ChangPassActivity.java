package com.example.hamed.recyclerviewmysqlvolley;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ChangPassActivity extends MyActivity implements View.OnClickListener {

    private EditText oldPass,newPass,newPass1;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chang_pass);
        oldPass=(EditText)findViewById(R.id.oldPass);
        newPass=(EditText)findViewById(R.id.newPass);
        newPass1=(EditText)findViewById(R.id.newpass1);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String oldpass=SharedPrefManager.getInstance(getApplicationContext())
                .getPassword();
        if(oldpass.equals(oldPass.getText().toString().trim())) {
            if (newPass.getText().toString().equals(newPass1.getText().toString())) {
                Toast.makeText(this, "Ok", Toast.LENGTH_LONG).show();
                chagePass();
            }

            else
                Toast.makeText(this, SharedPrefManager.getInstance(getApplicationContext()
                ).getPassword(), Toast.LENGTH_LONG).show();

        }

    }

    private void chagePass() {

        final String password=newPass.getText().toString().trim();
        final String id=Integer.toString(SharedPrefManager.getInstance(getApplicationContext()).getId());

        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.URL_CHANGE_PASS,
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
                params.put("id",id);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

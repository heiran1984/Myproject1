package com.example.hamed.recyclerviewmysqlvolley;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class VarizActivity extends MyActivity implements View.OnClickListener {

    EditText editTextUsername,editTextMVariz;
    int code;
    Button buttonSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variz);
        new  CustomActionBar(this,getSupportActionBar()," واریز به حساب");
        Bundle extras=getIntent().getExtras();
        String username=extras.getString("USERNAME");
        code=extras.getInt("CODE");

        editTextUsername=(EditText)findViewById(R.id.editTextUsername);
        editTextUsername.setText(username);
        editTextUsername.setEnabled(false);
        editTextMVariz=(EditText)findViewById(R.id.editTextMVazir);
        editTextMVariz.requestFocus();
        editTextMVariz.addTextChangedListener(new NumberTextWatcher(editTextMVariz));

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);


        buttonSave=(Button)findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Variz();
    }

    private void Variz() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.URL_VARIZ,
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
                params.put("mablagh",editTextMVariz.getText().toString().trim().replace(",",""));
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

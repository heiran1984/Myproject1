package com.example.hamed.recyclerviewmysqlvolley;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends MyActivity implements View.OnClickListener {

    Button buttonSave;
    EditText SandoghName,Mahiyane,taghsat,mizan;
    DecimalFormat formatter=new DecimalFormat("#,###,###");
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        new  CustomActionBar(this,getSupportActionBar(),"تنظیمات");

        SandoghName=(EditText)findViewById(R.id.SandoghName);
       // SandoghName.setTypeface(typeface);
        Mahiyane=(EditText)findViewById(R.id.OzvMahiyane);
       // Mahiyane.setTypeface(typeface);
        Mahiyane.addTextChangedListener(new NumberTextWatcher(Mahiyane));

        taghsat=(EditText)findViewById(R.id.TAghsat);
       // taghsat.setTypeface(typeface);
        mizan=(EditText)findViewById(R.id.Mizan);
      //  mizan.setTypeface(typeface);
        buttonSave=(Button)findViewById(R.id.buttonSave);
        //buttonSave.setTypeface(typeface);
        buttonSave.setOnClickListener(this);
        progressBar=(ProgressBar)findViewById(R.id.progressbar1);

        SandoghInfo();
    }

    @Override
    public void onClick(View v) {
        SaveSandoghInfo();

    }

    public void SandoghInfo(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.GET_MOJODI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.INVISIBLE);
                            JSONObject obj=new JSONObject(response);
                            SandoghName.setText(obj.getString("name"));
                            Mahiyane.setText(formatter.format(obj.getInt("value_mahiyane")));
                            taghsat.setText(String.valueOf(obj.getInt("t_aghsat")));
                            mizan.setText(String.valueOf(obj.getInt("mizan")));
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

    public void SaveSandoghInfo(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.REGISTER_SANDOGH,
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
                params.put("name",SandoghName.getText().toString().trim());
                params.put("value_mahiyane",Mahiyane.getText().toString().trim().replace(",",""));
                params.put("t_aghsat",taghsat.getText().toString().trim());
                params.put("mizan",mizan.getText().toString().trim());
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

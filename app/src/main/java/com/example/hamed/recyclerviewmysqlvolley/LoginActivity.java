package com.example.hamed.recyclerviewmysqlvolley;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextUsername,editTextPassword;
    private Button buttonLogin;
    private Context context;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new  CustomActionBar(this,getSupportActionBar(),"ورود");

        context=this;
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,MainActivity.class));
            return;
        }

        progressBar=(ProgressBar)findViewById(R.id.progressbar1);

        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextUsername=(EditText)findViewById(R.id.editTextUsername);
        buttonLogin=(Button)findViewById(R.id.buttonLogin);



        buttonLogin.setOnClickListener(this);
    }

    private void userLogin(){
        final String username=editTextUsername.getText().toString().trim();
        final String password=editTextPassword.getText().toString().trim();


        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest=new StringRequest(
                Request.Method.POST,
                Constans.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.INVISIBLE);

                            JSONObject obj=new JSONObject(response);
                            boolean id=obj.getBoolean("error");
                            if(!obj.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getInt("id"),
                                                obj.getString("username")

                                        );
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();

                            }else{
                                new CustomToast(context,obj.getString("message"));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(
                        getApplicationContext(),
                        error.getMessage(),
                        Toast.LENGTH_LONG
                ).show();


            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> params=new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v==buttonLogin){
            userLogin();
        }
    }
}

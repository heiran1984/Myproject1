package com.example.hamed.recyclerviewmysqlvolley;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends MyActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView Tarikh,Mojodi,TOzv,TWam,MojodiOzv;
    DecimalFormat formatter=new DecimalFormat("#,###,###");
    ProgressBar progressBar;
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    RelativeLayout.LayoutParams layoutparams;
    private int itemId=-1;
    private Context context;
    int tozv,twam;
    String[] iraniMonthStr = new String[]{

            "فروردین",
            "اردیبهشت",
            "خرداد",
            "تیر",
            "مرداد",
            "شهریور",
            "مهر",
            "آبان",
            "آذر",
            "دی",
            "بهمن",
            "اسفند",
    };
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        typeface=Typeface.createFromAsset(getAssets(),"fonts/IRANSansMobile.ttf");




        context=this;
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, toolbar,
                R.string.open, R.string.close
        );
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if(itemId!=-1)
                navigationView.getMenu().findItem(itemId).setChecked(false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        progressBar=(ProgressBar)findViewById(R.id.progressbar1);
        Tarikh=(TextView) findViewById(R.id.Tarikh);
        Mojodi=(TextView)findViewById(R.id.Mojodi);
        MojodiOzv=(TextView)findViewById(R.id.MojodiOzv);

        TOzv=(TextView)findViewById(R.id.TOzv);
        TWam=(TextView)findViewById(R.id.TٌWam);
     //   Tarikh.setTypeface(typeface);
       // Mojodi.setTypeface(typeface);
      //  TOzv.setTypeface(typeface);
      //  TWam.setTypeface(typeface);

        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        }*/
        if(SharedPrefManager.getInstance(getApplicationContext()).HasMah()){
            int mah=SharedPrefManager.getInstance(getApplicationContext()).getMah();
            Menu menu=navigationView.getMenu();
            MenuItem menuItem=menu.findItem(R.id.variz);
            menuItem.setTitle("واریز "+iraniMonthStr[mah]);

        }

    }

    public void updateMojodi(){

                StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        new CustomToast(context,"واریز انجام شد");

                        int mah=SharedPrefManager.getInstance(getApplicationContext()).getMah()+1;
                        if(mah==12)mah=0;
                        SharedPrefManager.getInstance(getApplicationContext()).AddMah(mah);
                        Menu menu=navigationView.getMenu();
                        MenuItem menuItem=menu.findItem(R.id.variz);
                        menuItem.setTitle("واریز "+iraniMonthStr[mah]);


                        getMojodi();
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void getMojodi(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.GET_MOJODI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.INVISIBLE);
                            JSONObject obj=new JSONObject(response);
                            long MojodiSandogh,MojodiOzv1;
                            MojodiSandogh=obj.getLong("smojodi");
                            MojodiOzv1=obj.getLong("mozv");
                            tozv=obj.getInt("tozv");
                            twam=obj.getInt("twam");
                            Mojodi.setText("موجودی صندوق="+formatter.format(MojodiSandogh));
                            MojodiOzv.setText("موجودی اعضاء="+formatter.format(MojodiOzv1));

                            TOzv.setText("تعداد اعضا="+formatter.format(tozv));
                            TWam.setText("تعداد وام="+formatter.format(twam));
                            String str[]=obj.getString("tarikh").split("-");
                            CalendarTool calendarTool = new CalendarTool();

                            calendarTool.setGregorianDate(Integer.parseInt(str[0]),Integer.parseInt(str[1]),Integer.parseInt(str[2]));
                            Tarikh.setText(calendarTool.getIranianWeekDayStr()+calendarTool.getIranianDay()+calendarTool.getIranianMonthStr()+calendarTool.getIranianYear());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
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

    public void ShowDialog(){
        TextView title = new TextView(this);
        title.setText("انتخاب ماه واریز");
        title.setTextColor(Color.RED);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(typeface);
        title.setTextSize(23);


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCustomTitle(title)
                .setItems(iraniMonthStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String selectedCountry = iraniMonthStr[i];
                        SharedPrefManager.getInstance(getApplicationContext()).AddMah(i);
                        Menu menu=navigationView.getMenu();
                        MenuItem menuItem=menu.findItem(R.id.variz);
                        menuItem.setTitle("واریز "+selectedCountry);
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();
        dialog.getWindow().setLayout(600,800);
        dialog.getWindow().setDimAmount(0.5f);
    }


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.variz:
                updateMojodi();
                break;
            case R.id.Vam:
                intent=new Intent(this,ListVamActivity.class);
                startActivity(intent);
                break;
            case R.id.Setting:
                intent=new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.NewOzv:
                intent=new Intent(this,NewOzvActivity.class);
                startActivity(intent);
                break;
            case R.id.Ozv:
                intent=new Intent(this,ListUserActivity.class);
                startActivity(intent);
                break;
            case R.id.LogOut:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
        return true;

    }*/

    @Override
    protected void onStart() {
        super.onStart();
        getMojodi();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        itemId=item.getItemId();
        item.setChecked(true);
        switch (itemId){
            case R.id.variz:
                if(!SharedPrefManager.getInstance(getApplicationContext()).HasMah()){
                    ShowDialog();
                }
                else
                    updateMojodi();
                break;
            case R.id.Vam:
                if(twam!=0){
                intent=new Intent(this,ListVamActivity.class);
                startActivity(intent);}
                break;
            case R.id.Setting:
                intent=new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.NewOzv:
                intent=new Intent(this,NewOzvActivity.class);
                startActivity(intent);
                break;
            case R.id.Ozv:
                intent=new Intent(this,ListUserActivity.class);
                startActivity(intent);
                break;
            case R.id.LogOut:
                SharedPrefManager.getInstance(this).logout();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
        mDrawerLayout.closeDrawers();
        return false;
    }
}
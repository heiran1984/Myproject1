package com.example.hamed.recyclerviewmysqlvolley;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ListUserActivity extends MyActivity implements View.OnLongClickListener {

    boolean is_in_action_mode=false;
    TextView counter;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    ProgressBar progressBar;
    Toolbar toolbar;
    Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    int count=0,position,update=0,displayvam=0;
    String username,tozihat="";
    AppBarLayout.LayoutParams params;
    public List<PersonUtils> personUtilsList,filteredList;
    ArrayList<String> Codes=new ArrayList<>();
    ArrayList<String> tozihat1=new ArrayList<>();
    ArrayList<Integer> position1=new ArrayList<>();
    DecimalFormat formatter=new DecimalFormat("#,###,###");
   // Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
       // typeface=Typeface.createFromAsset(getAssets(),"IRANSansMobile.ttf");
        progressBar=(ProgressBar)findViewById(R.id.progressbar1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        params=(AppBarLayout.LayoutParams)toolbar.getLayoutParams();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewOzv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        personUtilsList = new ArrayList<>();
        filteredList=new ArrayList<>();
        counter=(TextView)findViewById(R.id.counter);
        counter.setVisibility(View.GONE);

        mAdapter = new CustomRecyclerAdapter(ListUserActivity.this, personUtilsList);
       // recyclerView.setAdapter(mAdapter);
       // sendRequest();



    }

    public void sendRequest(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST, Constans.GET_USER, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        personUtilsList.clear();
                        filteredList.clear();
                        int mojodi=0;
                        for(int i = 0; i < response.length(); i++){
                            PersonUtils personUtils = new PersonUtils();
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                personUtils.setCode(jsonObject.getInt("id"));
                                personUtils.setid(i+1);
                                personUtils.setusername(jsonObject.getString("username"));
                                personUtils.setmojodi(jsonObject.getInt("mojodi"));
                                personUtils.setwam(jsonObject.getInt("wam"));
                                mojodi+=jsonObject.getInt("mojodi");
                                personUtils.setchecked(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            personUtilsList.add(personUtils);
                        }

                        mAdapter.notifyDataSetChanged();
                       // recyclerView.scrollToPosition(0);
                        filteredList.addAll(personUtilsList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley Error: ", error.toString());
            }
        });
         RequestQueue requestQueue= Volley.newRequestQueue(this);
         requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            clearActionMode();
            Codes.clear();
            tozihat1.clear();
            position1.clear();
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
            mAdapter.notifyDataSetChanged();
        }
        if(item.getItemId()==R.id.Add){
            if(count!=0) {
                Intent intent;
                intent = new Intent(this, NewVamActivity.class);
                intent.putExtra("USERNAME", username);
                intent.putExtra("MVAM", Integer.toString(count));
                intent.putExtra("TOZIHAT",tozihat1.toString());
                intent.putExtra("codes",Codes);
                //Toast.makeText(this,tozihat1.toString(),Toast.LENGTH_SHORT).show();
               // tozihat="";
                startActivity(intent);
            }
        }
        if(item.getItemId()==R.id.Edit){
            if(position1.size()==1)
              edituser(position1.get(0));
        }
        if(item.getItemId()==R.id.Mojodi){
            if(position1.size()==1)
                AfzayeshMojodi(position1.get(0));
        }
        return true;
    }

    public void prepareSelection(View view, int position) {
        this.position=position;
        if(((CheckBox)view).isChecked()){
            if(count==0)

            {
                snackbar = Snackbar
                        .make(coordinatorLayout, "موجودی=" +formatter.format(count), Snackbar.LENGTH_INDEFINITE);
                username = personUtilsList.get(position).getusername().replaceAll("\\d", "").trim();

            }
            else if(!username.equals(personUtilsList.get(position).getusername().replaceAll("\\d","").trim())) {
              // Toast.makeText(this, personUtilsList.get(position).getusername().replaceAll("[^\\.123456789]", ""), Toast.LENGTH_LONG).show();
                ((CheckBox) view).setChecked(false);
            }
            if(((CheckBox)view).isChecked()){
                personUtilsList.get(position).setchecked(true);
                Codes.add(Integer.toString(personUtilsList.get(position).getCode()));
                tozihat1.add(personUtilsList.get(position).getusername().replaceAll("[^\\.123456789]", ""));
                position1.add(position);
               // Toast.makeText(this,position1.toString(),Toast.LENGTH_SHORT).show();

                /*
                if(tozihat=="")
                    tozihat=personUtilsList.get(position).getusername().replaceAll("[^\\.123456789]", "");
                else
                    tozihat=tozihat+","+personUtilsList.get(position).getusername().replaceAll("[^\\.123456789]", "");
                */
                count=count+personUtilsList.get(position).getmojodi();
                updateCounter(count);
                snackbar.setText("موجودی=" +formatter.format(count));
                snackbar.show();



            }
        }
        else
        {
            personUtilsList.get(position).setchecked(false);
            Codes.remove(Integer.toString(personUtilsList.get(position).getCode()));
            tozihat1.remove(personUtilsList.get(position).getusername().replaceAll("[^\\.123456789]", ""));

            position1.remove(Integer.valueOf(position));
           // Toast.makeText(this,position1.toString(),Toast.LENGTH_SHORT).show();

            count=count-personUtilsList.get(position).getmojodi();
            updateCounter(count);
            snackbar.setText("موجودی=" +formatter.format(count));
            if(count!=0)snackbar.show();else snackbar.dismiss();

        }
    }

    public void edituser(int position){
        Intent intent;
        intent=new Intent(this,NewOzvActivity.class);
        intent.putExtra("USERNAME",personUtilsList.get(position).getusername().toString());
        intent.putExtra("MOJODI",Integer.toString(personUtilsList.get(position).getmojodi()));
        intent.putExtra("CODE",personUtilsList.get(position).getCode());
        this.startActivityForResult(intent,1);
    }

    public void AfzayeshMojodi(int position){
        Intent intent;
        intent=new Intent(this,NewMojodiActivity.class);
        intent.putExtra("USERNAME",personUtilsList.get(position).getusername().toString());
        intent.putExtra("CODE",personUtilsList.get(position).getCode());
        this.startActivity(intent);

    }

    private void updateCounter(int count) {
        if(count==0){
            counter.setText("موجودی=0");
        }
        else
        {
            counter.setText( "موجودی=" +formatter.format(count));
        }
    }

    public void clearActionMode(){
        is_in_action_mode=false;
        toolbar.getMenu().clear();
        invalidateOptionsMenu();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        counter.setVisibility(View.GONE);
        counter.setText("موجودی=0");
        count=0;
        Codes.clear();
        tozihat1.clear();
        position1.clear();
        int i;
        for(i=0;i<personUtilsList.size();i++){
            personUtilsList.get(i).setchecked(false);
        }
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        mAdapter.notifyDataSetChanged();
    }

    public void DisplayVam(int position){
        if (personUtilsList.get(position).getwam()==1) {
            Intent intent;
            intent = new Intent(this, ListVamActivity.class);
            intent.putExtra("USERNAME", personUtilsList.get(position).getusername().replaceAll("\\d", "").trim());
            this.startActivity(intent);
            displayvam = 1;
        }
    }

    public void updatelist(){



        recyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onBackPressed() {

        if(is_in_action_mode){
            clearActionMode();

        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            if (resultCode == Activity.RESULT_OK) {
              //  personUtilsList.get(position).setusername(data.getStringExtra("USERNAME"));
              //  personUtilsList.get(position).setmojodi(Integer.parseInt(data.getStringExtra("MOJODI")));
                update=1;
                //Toast.makeText(this,"onresult",Toast.LENGTH_SHORT).show();

               // updatelist();
                //recyclerView.setAdapter(mAdapter);
               // mAdapter.notifyDataSetChanged();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                personUtilsList.clear();
                mAdapter.notifyDataSetChanged();
                if (query.length() == 0) {
                    personUtilsList.addAll(filteredList);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    final String filterPattern = query.toString().trim();
                    int i=1;
                    for (final PersonUtils mWords : filteredList) {
                        if (mWords.getusername().startsWith(filterPattern)) {
                            mWords.setid(i);
                            personUtilsList.add(mWords);
                            i++;
                        }
                    }
                }
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(is_in_action_mode){
            clearActionMode();
        }
      //  if(personUtilsList.size()!=filteredList.size()){
     //       personUtilsList.clear();
     //       personUtilsList.addAll(filteredList);
      //  }

      //  if(update==1) {

            /*
            Collections.sort(personUtilsList, new Comparator<PersonUtils>() {
                @Override
                public int compare(PersonUtils o1, PersonUtils o2) {
                    Collator collator = Collator.getInstance(new Locale("fa"));
                    collator.setStrength(Collator.PRIMARY);
                    return collator.compare(o1.getusername(), o2.getusername());
                }
            });
            int i;
            for(i=0;i<personUtilsList.size();i++){
                personUtilsList.get(i).setid(i+1);
            }

            */
       //     sendRequest();
      //      update=0;
      //  }
        if(displayvam==1)
            displayvam=0;
        else {
            sendRequest();
            Codes.clear();
            tozihat1.clear();
            position1.clear();
            recyclerView.setAdapter(mAdapter);
            recyclerView.scrollToPosition(position);
        }

    }

    @Override
    public boolean onLongClick(View v) {
        params.setScrollFlags(0);

        is_in_action_mode=true;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_action_mode);
        counter.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }


}

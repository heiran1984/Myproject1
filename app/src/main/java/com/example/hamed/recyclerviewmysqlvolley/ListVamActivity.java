package com.example.hamed.recyclerviewmysqlvolley;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListVamActivity extends MyActivity implements View.OnClickListener {


    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    ProgressBar progressBar;
    List<VamUtils> vamUtilsList,filteredlist;
    String username="all";
    CoordinatorLayout coordinatorLayout;
    View persistentbottomSheet;
    LinearLayout delVam,tasfiyaVam;
    BottomSheetBehavior behavior;
    BottomSheetDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vam);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        persistentbottomSheet = coordinatorLayout.findViewById(R.id.bottomsheet);
        delVam=(LinearLayout)persistentbottomSheet.findViewById(R.id.delVam);
        tasfiyaVam=(LinearLayout)persistentbottomSheet.findViewById(R.id.tasviyaVam);

        behavior = BottomSheetBehavior.from(persistentbottomSheet);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        progressBar=(ProgressBar)findViewById(R.id.progressbar1);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewVam);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        vamUtilsList = new ArrayList<>();
        filteredlist=new ArrayList<>();
        mAdapter = new VamRecyclerAdapter(this, vamUtilsList);
        recyclerView.setAdapter(mAdapter);

        Bundle extras=getIntent().getExtras();
        if(extras!=null) {
             username = extras.getString("USERNAME").replace(" ","%20");
            //Toast.makeText(this,username, Toast.LENGTH_SHORT).show();
        }
        sendRequest();

    }
    public void sendRequest(){
        vamUtilsList.clear();
        filteredlist.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST, Constans.GET_VAM+"?string="+username, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        int mojodi=0;int maghsat,mandavam;
                        for(int i = 0; i < response.length(); i++){
                            VamUtils vamUtils = new VamUtils();
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                vamUtils.setId(i+1);
                                vamUtils.setcode(jsonObject.getInt("id"));
                                vamUtils.setUsername(jsonObject.getString("username"));
                                vamUtils.setMvam(jsonObject.getInt("m_vam"));
                                vamUtils.setTaghsat(jsonObject.getInt("t_aghsat"));
                                vamUtils.setTpardakhtshoda(jsonObject.getInt("t_pardakhtshoda"));
                                maghsat=jsonObject.getInt("m_aghsat");
                                mandavam=jsonObject.getInt("m_vam")-(maghsat*jsonObject.getInt("t_pardakhtshoda"));
                                vamUtils.setMandavam(mandavam);
                                vamUtils.setMaghsat(jsonObject.getInt("m_aghsat"));
                                vamUtils.settozihat(jsonObject.getString("tozihat"));

                                String str[]=jsonObject.getString("tarikh").split("-");
                                CalendarTool calendarTool = new CalendarTool();

                                calendarTool.setGregorianDate(Integer.parseInt(str[0]),Integer.parseInt(str[1]),Integer.parseInt(str[2]));
                                vamUtils.settarikh(calendarTool.getIranianYear()+"/"+calendarTool.getIranianMonth()+"/"+calendarTool.getIranianDay());
                                mojodi+=jsonObject.getInt("mojodi");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            vamUtilsList.add(vamUtils);

                        }
                        mAdapter.notifyDataSetChanged();
                        filteredlist.addAll(vamUtilsList);
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

    public void ShowMenu(final int position){

        openDialog(position);

        //new MyBottomSheetDialogFragment().show(getSupportFragmentManager(),MyActivity.class.getSimpleName());
        /*
        delVam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vamUtilsList.get(position).getTpardakhtshoda()==0){
                    deletevam(position);

                }

            }
        });
        tasfiyaVam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasfiya(position);

            }

        });
        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        } else {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        */
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
                vamUtilsList.clear();
                mAdapter.notifyDataSetChanged();
                if (query.length() == 0) {
                    vamUtilsList.addAll(filteredlist);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    final String filterPattern = query.toString().trim();
                    int i=1;
                    for (final VamUtils mWords : filteredlist) {
                        if (mWords.getUsername().startsWith(filterPattern)) {
                            mWords.setId(i);
                            vamUtilsList.add(mWords);
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
    public void onClick(View v) {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
        super.onBackPressed();
    }
    public void deletevam(final int position){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.URL_DELVAM+"?string="+vamUtilsList.get(position).getcode(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        vamUtilsList.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        int i;
                        for(i=0;i<vamUtilsList.size();i++) {
                            vamUtilsList.get(i).setId(i + 1);
                            mAdapter.notifyItemChanged(i);
                        }
                        Toast.makeText(getApplicationContext(),"حذف شد",Toast.LENGTH_SHORT).show();

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

    public void tasfiya(final int position){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.URL_TASFIYAVAM+"?string="+vamUtilsList.get(position).getcode(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        vamUtilsList.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        int i;
                        for(i=0;i<vamUtilsList.size();i++) {
                            vamUtilsList.get(i).setId(i + 1);
                            mAdapter.notifyItemChanged(i);
                        }
                        Toast.makeText(getApplicationContext(),"تسویه شد",Toast.LENGTH_SHORT).show();

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

    private void openDialog(final int position) {
        View view = getLayoutInflater().inflate(R.layout.dialog_bottomsheet, null);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.getWindow().setDimAmount(0.2f);

        LinearLayout delVam = (LinearLayout) view.findViewById(R.id.delVam);
        LinearLayout tasfiyaVam = (LinearLayout) view.findViewById(R.id.tasviyaVam);
        delVam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vamUtilsList.get(position).getTpardakhtshoda()==0){
                deletevam(position);
                dialog.dismiss();
                }
            }
        });
        tasfiyaVam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasfiya(position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}



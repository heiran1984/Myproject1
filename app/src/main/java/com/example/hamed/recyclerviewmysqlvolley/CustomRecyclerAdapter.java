package com.example.hamed.recyclerviewmysqlvolley;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;


public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>{

    private Context context;
    private ListUserActivity listUserActivity;
    private int row_index=-1;
    private List<PersonUtils> personUtils;
    //Typeface typeface;
    DecimalFormat formatter=new DecimalFormat("#,###,###");

    public CustomRecyclerAdapter(Context context, List personUtils) {
        this.context =context;
        this.listUserActivity=(ListUserActivity)context;
        this.personUtils = personUtils;
       // this.typeface=Typeface.createFromAsset(context.getAssets(),"IRANSansMobile.ttf");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view,listUserActivity);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(personUtils.get(position));

        final PersonUtils pu = personUtils.get(position);

        /*
        holder.row_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!listUserActivity.is_in_action_mode){

                PopupMenu popup = new PopupMenu(context,holder.row_linearLayout);
                popup.inflate(R.menu.user_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent;
                        switch (item.getItemId()) {
                            case R.id.new_vam:
                                intent=new Intent(context,NewVamActivity.class);
                                intent.putExtra("USERNAME",holder.username.getText().toString());
                                intent.putExtra("MVAM",Integer.toString(pu.getmojodi()));
                                context.startActivity(intent);
                                break;
                            case R.id.mojodi:
                                intent=new Intent(context,NewMojodiActivity.class);
                                intent.putExtra("USERNAME",holder.username.getText().toString());
                                intent.putExtra("CODE",pu.getCode());
                                context.startActivity(intent);
                                break;
                            case R.id.edit:
                                listUserActivity.edituser(position);
                                break;

                        }
                        return false;
                    }
                });
                popup.show();
                row_index=position;
                notifyDataSetChanged();
            }
            }
        });

        */
        if(row_index==position){
            holder.row_cardView.setCardBackgroundColor(Color.RED);
            holder.userid.setTextColor(Color.WHITE);
            holder.username.setTextColor(Color.WHITE);
            holder.mojodi.setTextColor(Color.WHITE);
            row_index=-1;

        }
        else
        {

            if(pu.getwam()==1) {
                holder.userid.setBackgroundResource(R.drawable.border);
                holder.userid.setTextColor(Color.WHITE);
            }
            else {
                holder.userid.setBackgroundColor(Color.WHITE);
                holder.userid.setTextColor(Color.BLACK);

            }

            holder.username.setTextColor(Color.BLACK);
            holder.mojodi.setTextColor(Color.BLACK);
        }

        holder.userid.setText(Integer.toString(pu.getid()));
        holder.username.setText(pu.getusername());
        holder.mojodi.setText(formatter.format(pu.getmojodi()));
        if(!listUserActivity.is_in_action_mode){
            holder.checkBox.setVisibility(View.GONE);
        }
        else {

            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(pu.ischecked());
        }
    }

    @Override
    public int getItemCount() {
        return personUtils.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView userid;
        public TextView username;
        public TextView mojodi;
        public TextView textName,textMojodi;
        public LinearLayout row_linearLayout;
        public RecyclerView rv2;
        public CardView row_cardView;
        ListUserActivity listUserActivity;
        CheckBox checkBox;

        public ViewHolder(View itemView,ListUserActivity listUserActivity) {
            super(itemView);
            userid=(TextView)itemView.findViewById(R.id.userId);
            username = (TextView) itemView.findViewById(R.id.username);
            mojodi = (TextView) itemView.findViewById(R.id.mojodi);
            row_linearLayout=(LinearLayout)itemView.findViewById(R.id.row_linearLayout);
            rv2=(RecyclerView)itemView.findViewById(R.id.recycleViewOzv);
            row_cardView=(CardView)itemView.findViewById(R.id.row_CardView);
            textName=(TextView)itemView.findViewById(R.id.texviewName);
            textMojodi=(TextView)itemView.findViewById(R.id.textviewMojodi);
            checkBox=(CheckBox)itemView.findViewById(R.id.check_item);
            checkBox.setOnClickListener(this);
            row_cardView.setOnClickListener(this);
            this.listUserActivity=listUserActivity;
            row_cardView.setOnLongClickListener(listUserActivity);
        }

        @Override
        public void onClick(View v) {

            if(v.getId()==R.id.check_item)
                listUserActivity.prepareSelection(v,getAdapterPosition());
            if(!listUserActivity.is_in_action_mode)
                if(v.getId()==R.id.row_CardView)
                listUserActivity.DisplayVam(getAdapterPosition());

        }
    }


}
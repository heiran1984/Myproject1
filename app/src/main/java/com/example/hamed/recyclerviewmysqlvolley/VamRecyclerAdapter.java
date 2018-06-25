package com.example.hamed.recyclerviewmysqlvolley;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;


public class VamRecyclerAdapter extends RecyclerView.Adapter<VamRecyclerAdapter.ViewHolder> {

    private Context context;
    private ListVamActivity listVamActivity;
    private List<VamUtils> VamUtils;
    private DecimalFormat formatter=new DecimalFormat("#,###,###");
   // Typeface typeface;

    public VamRecyclerAdapter(Context context, List VamUtils) {
        this.context =context;
        this.VamUtils = VamUtils;
        this.listVamActivity=(ListVamActivity)context;
       // this.typeface=Typeface.createFromAsset(context.getAssets(),"IRANSansMobile.ttf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_vam_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v,listVamActivity);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(VamUtils.get(position));

        final VamUtils pu = VamUtils.get(position);


        if(pu.getMandavam()==0){
            holder.VamcardView.setCardBackgroundColor(Color.RED);

        }
        holder.id.setText(Integer.toString(pu.getId()));
        holder.username.setText(pu.getUsername()+pu.gettozihat());
        holder.mvam.setText(formatter.format(pu.getMvam()));
        holder.mandavam.setText(formatter.format(pu.getMandavam()));
        holder.taghsat.setText(Integer.toString(pu.getTaghsat()));
        holder.tpardakhtshoa.setText(Integer.toString(pu.getTpardakhtshoda()));
        holder.maghsat.setText(formatter.format(pu.getMaghsat()));
        holder.tarikh.setText(pu.gettarikh());
    }

    @Override
    public int getItemCount() {
        return VamUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        public TextView id;
        public TextView username;
        public TextView mvam;
        public TextView mandavam;
        public TextView taghsat;
        public TextView tpardakhtshoa;
        public TextView maghsat;
        public TextView tarikh;
        public CardView VamcardView;
        ListVamActivity listVamActivity;
        public ViewHolder(final View itemView,ListVamActivity listVamActivity) {
            super(itemView);


            id=(TextView)itemView.findViewById(R.id.id);
            username = (TextView) itemView.findViewById(R.id.username);
            mvam = (TextView) itemView.findViewById(R.id.mvam);
            mandavam = (TextView) itemView.findViewById(R.id.mandavam);
            taghsat = (TextView) itemView.findViewById(R.id.taghsat);
            tpardakhtshoa = (TextView) itemView.findViewById(R.id.tpardakhtshoda);
            maghsat=(TextView)itemView.findViewById(R.id.maghsat);
            tarikh=(TextView)itemView.findViewById(R.id.tarikh);
            VamcardView=(CardView)itemView.findViewById(R.id.card_view);
            this.listVamActivity=listVamActivity;
            VamcardView.setOnLongClickListener(this);
            VamcardView.setOnClickListener(listVamActivity);


          //  id.setTypeface(typeface);
          //  username.setTypeface(typeface);
          //  mvam.setTypeface(typeface);
           // mandavam.setTypeface(typeface);
          //  taghsat.setTypeface(typeface);
          //  tpardakhtshoa.setTypeface(typeface);
          //  maghsat.setTypeface(typeface);
           // tarikh.setTypeface(typeface);

        }

        @Override
        public boolean onLongClick(View v) {
            if(v.getId()==R.id.card_view)
                listVamActivity.ShowMenu(getAdapterPosition());
            return true;
        }
    }

}
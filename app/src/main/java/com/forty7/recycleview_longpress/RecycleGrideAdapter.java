package com.forty7.recycleview_longpress;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * 拖拽RecyclerView适配器
 * @author Forty'7
 * 2017.2.16
 */

public class RecycleGrideAdapter extends RecyclerView.Adapter<RecycleGrideAdapter.Holder>{
    private List<String> list;
    private Context context;

    public RecycleGrideAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gride,parent,false);
        return new RecycleGrideAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.txt.setText(list.get(position).toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击了 : " + list.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView txt;
        public Holder(View itemView) {
            super(itemView);
            txt= (TextView) itemView.findViewById(R.id.txt);
        }
    }

    public void setList(List<String> list)
    {
        if(list != null)
        {
            this.list = list;
            notifyDataSetChanged();
        }
    }
}

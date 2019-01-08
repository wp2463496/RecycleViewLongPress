package com.forty7.recycleview_longpress;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class QuickAdapter extends BaseQuickAdapter<TestBean, BaseViewHolder> {
    private List<TestBean> list;
    private Context context;

    public QuickAdapter(Context context, List<TestBean> list) {
        super(R.layout.item_gride, list);
        this.list = list;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final TestBean item) {
        helper.setText(R.id.txt,  "" + item.getTitle());
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击了 : " + item.getTitle(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setList(List<TestBean> list)
    {
        if(list != null)
        {
            this.list = list;
            notifyDataSetChanged();
        }
    }

    public List<TestBean> getList(){
        return this.list;
    }


}

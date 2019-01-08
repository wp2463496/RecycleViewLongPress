package com.forty7.recycleview_longpress;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 拖拽RecyclerViewDemo
 * @author Forty'7
 * 2017.2.16
 */
public class MainActivity extends Activity {
    private RecyclerView recycleview;
    private RecyclerView recycleview_longpress;
    private List<TestBean> list;
    private RecycleGrideAdapter adapter_gride;//此适配器不兼容添加头布局+拖拽，只支持拖拽
    private RecycleAdapter adapter_txt;
    private TextView tv_remove;
    private int positionTag = 0;//标记当前按下的位置
    private boolean isLongPress = false;//是否是长按
    private View headerView;//头布局
    private CustomGridLayoutManager layoutManager;//可配置是否允许滑动的LayoutManager

    private QuickAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setView();
        initdate();

    }

    private void initView() {
        recycleview = (RecyclerView) findViewById(R.id.recycleview);
        recycleview_longpress = (RecyclerView) findViewById(R.id.recycleview_longpress);
        tv_remove = (TextView) findViewById(R.id.tv_remove);

    }

    private void setView() {
        headerView = LayoutInflater.from(this).inflate(R.layout.header_view_layout, null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleview.setLayoutManager(linearLayoutManager);
        layoutManager = new CustomGridLayoutManager(this, 4);
        recycleview_longpress.setLayoutManager(layoutManager);

    }

    private void initdate() {
        list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            TestBean bean = new TestBean();
            bean.setId(i);
            bean.setTitle("功能" + i);
            list.add(bean);
        }
        setAdapter_txt();
        setAdapter_gride();
        helper.attachToRecyclerView(recycleview_longpress);
    }

    private void setAdapter_txt() {
        adapter_txt = new RecycleAdapter(MainActivity.this, list);
        recycleview.setAdapter(adapter_txt);
    }

    private void setAdapter_gride() {
       /* adapter_gride = new RecycleGrideAdapter(MainActivity.this, list);
        recycleview_longpress.setAdapter(adapter_gride);*/
        mAdapter = new QuickAdapter(this, list);
        mAdapter.addHeaderView(headerView);

        recycleview_longpress.setAdapter(mAdapter);
        recycleview_longpress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float xDown = event.getX();
                float yDown = event.getY();
                float mHeight = tv_remove.getHeight();//隐藏控件高度：注意必须在此计算 因为默认是隐藏的 获取的高度是0
                if (event.getAction() == MotionEvent.ACTION_DOWN) // 按下
                {
                    Log.d("TEST", "onTouch" + "   X = " + xDown + "   Y = " + yDown);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) // 抬起
                {
                    if(isLongPress)
                    {
                        if(event.getY() < mHeight){
                            if(positionTag != 0)
                            {
                                //如果不是头布局再隐藏
                                Toast.makeText(MainActivity.this,"隐藏 : " + list.get(positionTag - 1).getTitle(), Toast.LENGTH_LONG).show();
                                Log.d("TEST",   "隐藏11111------->  positionTag = " + positionTag);
                                Log.d("TEST", "onTouch" + "   X = " + xDown + "   Y = " + yDown);
                                list.remove(positionTag - 1);
                                mAdapter.setList(list);
                            }

                        }
                    }
                    tv_remove.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    tv_remove.setVisibility(View.GONE);
                    isLongPress = false;
                    layoutManager.setScrollEnabled(true);//开启滑动
                }
                else if (event.getAction() == MotionEvent.ACTION_MOVE) // 移动
                {
                    if(isLongPress)
                    {
                        if(event.getY() < mHeight)
                        {
                            tv_remove.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red));
                        }
                        else
                        {
                            tv_remove.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                        }
                    }
                }
                return false;
            }
        });
    }

    //为RecycleView绑定触摸事件
    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //首先回调的方法 返回int表示是否监听该方向
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;//拖拽
            int swipeFlags = 0;//侧滑删除
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //滑动事件
            int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
            int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
            Log.d("TEST",   "得到目标ViewHolder的toPosition = " + toPosition  + "   得到拖动ViewHolder的fromPosition = " + fromPosition);
            if(toPosition != 0 && fromPosition != 0) //如果得到目标ViewHolder和得到拖动ViewHolder不是头布局再切换位置
            {
                Log.d("TEST",   "切换------->  positionTag = " + positionTag);
                mAdapter.notifyItemMoved(fromPosition, toPosition);//位置变化
                positionTag = toPosition;
                //数据源位置更换
                if(fromPosition < toPosition)
                {
                    for(int i = fromPosition - 1; i < toPosition - 1; i++)//-1是因为加有头布局
                    {
                        Collections.swap(list, i, i + 1);
                    }
                }
                else
                {
                    for(int i = fromPosition - 1;i > toPosition - 1; i--)
                    {
                        Collections.swap(list, i, i - 1);
                    }
                }
            }
            else if(toPosition == 0 && fromPosition != 0)//如果得到目标ViewHolder不是头布局再切换位置
            {
                //隐藏
                positionTag = fromPosition;
                Log.d("TEST",   "隐藏------->");
            }
            else
            {
                positionTag = 0;
            }
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }

        @Override
        public boolean isLongPressDragEnabled() {
            //是否可拖拽
            layoutManager.setScrollEnabled(false);//禁止滑动
            tv_remove.setVisibility(View.VISIBLE);
            isLongPress = true;
            return true;
        }
    });
}

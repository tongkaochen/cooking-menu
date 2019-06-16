package com.tifone.menu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Create by Tifone on 2019/6/16.
 */
public class MenuHistoryListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<MenuItem> mMenuDataSet;
    private MenuManager mMenuManager;
    private TextView mEmptyView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMenuManager = MenuManager.getInstance(this, null);
        setContentView(R.layout.menu_history_layout);
        mEmptyView = findViewById(R.id.empty_view);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        createAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMenuDataSet.size() == 0) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.INVISIBLE);
        }
    }

    private void initList() {
        mMenuDataSet = mMenuManager.getAll();
    }
    private void createAdapter() {
        initList();
        mAdapter = new MenuListAdapter();
    }
    private String parseIntDate(String date) {
        if (date.length() != 8) {
            return date;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(date.substring(0,4))
                .append("年")
                .append(date.substring(5, 6))
                .append("月")
                .append(date.substring(7,8))
                .append("日");
        return builder.toString();
    }

    class MenuListAdapter extends RecyclerView.Adapter<MenuViewHolder> {

        @NonNull
        @Override
        public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MenuHistoryListActivity.this);
            View view = inflater.inflate(R.layout.menu_item_layout, viewGroup, false);
            return new MenuViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuViewHolder menuViewHolder, int position) {
            final MenuItem item = mMenuDataSet.get(position);
            menuViewHolder.dateTv.setText(parseIntDate(String.valueOf(item.id)));
            menuViewHolder.titleTv.setText("菜名：" + item.name);
            menuViewHolder.countTv.setText("已完成次数：" + item.count);
            menuViewHolder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MenuHistoryListActivity.this, item.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMenuDataSet.size();
        }
    }
    class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView countTv;
        TextView titleTv;
        TextView dateTv;
        ViewGroup root;
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            countTv = itemView.findViewById(R.id.count);
            titleTv = itemView.findViewById(R.id.title);
            dateTv = itemView.findViewById(R.id.date);
        }
    }
}

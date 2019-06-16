package com.tifone.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MenuManager mMenuManager;
    private boolean isInSelecting;
    private TextView mResultShow;
    private MenuItem mCurrentSelectedMenu;
    private TextView mResultTitle;
    private TextView mResetMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResultShow = findViewById(R.id.show_result);
        mResultTitle = findViewById(R.id.result_text);
        mResetMenu = findViewById(R.id.reset_menu);
        mResetMenu.setOnClickListener(this);
        mMenuManager = MenuManager.getInstance(this, createInitData());
        Button getMenu = findViewById(R.id.get_menu);
        getMenu.setOnClickListener(this);
        Button confirmResult = findViewById(R.id.confirm_result);
        confirmResult.setOnClickListener(this);
        MenuItem todayMenu = mMenuManager.getCurrentMenu();
        if (todayMenu != null) {
            mResultTitle.setText("你选择的菜品如下：");
            mResultShow.setTextColor(getColor(R.color.color_green));
            mResultShow.setText(todayMenu.toString());
        } else {
            mResultTitle.setText(getString(R.string.available_menu, mMenuManager.getMenuSize()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("我的点餐历史");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        Intent intent = new Intent();
        intent.setClass(this, MenuHistoryListActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private List<String> createInitData() {
        List<String> dataSet = new ArrayList<>();
        // item: "id#name#count#iconId@materialList"
        // material: "name#usage"
        dataSet.add("1#腐竹炒肉#0#0@腐竹#0#瘦肉#0");
        dataSet.add("2#青椒炒蛋#0#0@鸡蛋#0#青椒#0");
        dataSet.add("3#鸡胸肉炒芹菜#0#0@鸡胸肉#0#芹菜#0");
        dataSet.add("4#青椒炒鸡#0#0@青椒#0#鸡肉#0");
        dataSet.add("5#芹菜炒鸡#0#0@鸡肉#0#芹菜#0");
        dataSet.add("6#红烧肉#0#0@五花肉，不要太肥的");
        dataSet.add("7#啤酒鸭#0#0@啤酒#0#鸭肉#0");
        dataSet.add("8#木耳炒肉#0#0@瘦肉#0#木耳#0");
        dataSet.add("9#黄瓜炒肉#0#0@瘦肉#0#黄瓜#0");
        dataSet.add("10#卤猪头肉#0#0@猪头肉#0#青菜#0");
        dataSet.add("11#卤猪脚#0#0@猪脚或猪肘子#0#青菜#0");
        dataSet.add("12#白切鸡#0#0@半只鸡#0#青菜#0");
        dataSet.add("13#烧鸭饭#0#0@直接买烧鸭#0#青菜#0");
        dataSet.add("14#芹菜木耳炒蛋#0#0@芹菜#0#木耳#0#鸡蛋");
        dataSet.add("15#粉蒸肉#0#0@蒸肉粉#0#瘦一点的五花肉#0");
        dataSet.add("16#青椒炒肉#0#0@青椒#0#瘦肉或者五花肉#0");
        dataSet.add("17#莴笋炒肉#0#0@莴笋#0#瘦肉或者五花肉#0");
        dataSet.add("18#四季豆炒肉#0#0@四季豆#0#瘦肉或者五花肉#0");
        dataSet.add("19#豆角豆炒肉#0#0@豆角#0#瘦肉或者五花肉#0");
        dataSet.add("20#蒜苔炒肉#0#0@蒜苔#0#瘦肉或者五花肉#0");
        dataSet.add("21#西兰花炒肉#0#0@西兰花#0#瘦肉或者五花肉#0");
        dataSet.add("22#豆干芹菜炒肉#0#0@豆干#0#瘦肉或者五花肉#0#芹菜#0");
        dataSet.add("23#蛋炒饭#0#0@煮个饭#0#买几个鸡蛋");
        dataSet.add("24#苦瓜炒肉#0#0@苦瓜#0#瘦肉");
        return dataSet;
    }

    private void getAndDisplay() {
        MenuItem todayMenu = mMenuManager.getCurrentMenu();
        if ( todayMenu != null) {
            Toast.makeText(this, "大哥，你今天的菜品已经确定了就不要再选了好吧，请尊重一下我的工作，谢谢", Toast.LENGTH_SHORT).show();
            mResultTitle.setText("你选择的菜品如下：");
            mResultShow.setText(todayMenu.toString());
            mResultShow.setTextColor(getColor(R.color.color_green));
            mCurrentSelectedMenu = null;
            isInSelecting = false;
            return;
        }
        MenuItem item = mMenuManager.getMenu();
        if (item == null) {
            mResultTitle.setVisibility(View.INVISIBLE);
            return;
        }
        mResultTitle.setText(getString(R.string.available_menu_to_select, mMenuManager.getMenuSize()));
        mResultShow.setText(item.toString());
        mCurrentSelectedMenu = item;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_menu:
                if (isInSelecting) {
                    return;
                }
                mResultShow.setTextColor(Color.BLACK);
                isInSelecting = true;
                getAndDisplay();
                break;
            case R.id.confirm_result:
                showConfirmDialog();
                break;
            case R.id.reset_menu:
                showResetDialog();
        }
    }
    private void updateSomething() {
        mResultTitle.setText(getString(R.string.available_menu, mMenuManager.getMenuSize()));
        mCurrentSelectedMenu = null;
        mResultShow.setText("");
    }

    private void showResetDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("你将菜单恢复默认值")
                .setMessage("你的菜单会恢复为默认值，之前选择过的菜单将会再次出现，吱吱，也就是要吃重复的菜。。。")
                .setPositiveButton("毅然重置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isInSelecting = false;
                        Toast.makeText(MainActivity.this, "狠心的家伙。。。菜单以恢复默认，再见。", Toast.LENGTH_LONG).show();
                        mMenuManager.initData(createInitData(), true);
                        updateSomething();
                    }

                })
                .setNegativeButton("考虑一下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "少侠多虑了。。。", Toast.LENGTH_LONG)
                                .show();
                    }
                })
                .create();
        dialog.show();
    }

    private void showConfirmDialog() {
        if (mCurrentSelectedMenu == null) {
            Toast.makeText(this, "不选菜品就点确认干哈。。。。啥子", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("明天的菜单")
                .setMessage(mCurrentSelectedMenu.toString())
                .setPositiveButton("吃定了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isInSelecting = false;
                        if (mCurrentSelectedMenu != null) {
                            mResultShow.setTextColor(getColor(R.color.color_green));
                            mMenuManager.saveMenu(mCurrentSelectedMenu);
                            mMenuManager.removeMenu(mCurrentSelectedMenu);
                            Toast.makeText(MainActivity.this, "明天吃" + mCurrentSelectedMenu.name + ", 记得买菜，不然打死。。。", Toast.LENGTH_LONG).show();
                            mCurrentSelectedMenu = null;
                        }
                    }

                })
                .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "小样，还挺挑的, 给你个重选的机会", Toast.LENGTH_LONG)
                                .show();
                        mResultShow.setText("");
                        mResultTitle.setText(getString(R.string.available_menu, mMenuManager.getMenuSize()));
                        isInSelecting = false;
                    }
                })
                .create();
        dialog.show();
    }
}

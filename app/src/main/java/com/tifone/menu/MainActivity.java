package com.tifone.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MenuManager mMenuManager;
    private boolean isInSelecting;
    private TextView mResultShow;
    private MenuItem mCurrentSelectedMenu;
    private TextView mResultTitle;
    private TextView mResetMenu;
    private String mPassword = "tkzs";
    private int mRejectTimes = 0;
    private boolean mMenuIsSelected;
    private Random mRandom;

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
        confirmResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showRejectDialog();
                return false;
            }
        });
        MenuItem todayMenu = mMenuManager.getCurrentMenu();
        mCurrentSelectedMenu = todayMenu;
        mRandom = new Random();
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
            Toast.makeText(this, getString(R.string.menu_confirmed_summary), Toast.LENGTH_SHORT).show();
            mResultTitle.setText(getString(R.string.menu_list_below_text));
            mResultShow.setText(todayMenu.toString());
            mResultShow.setTextColor(getColor(R.color.color_green));
            mCurrentSelectedMenu = todayMenu;
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
    protected void onResume() {
        super.onResume();
        mPassword = getPassword();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCurrentSelectedMenu != null) {
            mMenuManager.saveMenu(mCurrentSelectedMenu);
        }
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
                requestPasswordValidate();

        }
    }
    private void updateSomething() {
        mResultTitle.setText(getString(R.string.available_menu, mMenuManager.getMenuSize()));
        mCurrentSelectedMenu = null;
        mResultShow.setText("");
    }
    private String getPassword() {
//        String[] passwords = {"晶晶大概是混蛋", "晶晶应该是混蛋", "晶晶就是混蛋"};
        String[] passwords = {"jjdgshd", "jjygshd", "jjjshd"};
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String result = format.format(date);
        int index = mRandom.nextInt(passwords.length);
        return passwords[index];
    }
    private void requestPasswordValidate() {

        View view = LayoutInflater.from(this).inflate(R.layout.password_layout, null);
        final EditText password = view.findViewById(R.id.password_et);
        password.setFocusableInTouchMode(true);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle(R.string.password_confirm_title)
                .setPositiveButton(R.string.return_text, null).create();
        dialog.show();
        final TextView tips = view.findViewById(R.id.error_tips);
        Button loginBtn = view.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordStr = password.getText().toString();
                if (TextUtils.isEmpty(passwordStr)) {
                    Toast.makeText(MainActivity.this, getString(R.string.password_empty_error), Toast.LENGTH_SHORT).show();
                    tips.setVisibility(View.VISIBLE);
                    tips.setText(getString(R.string.password_is_empty));
                    return;
                } else if (mPassword.equals(passwordStr) || "tkzs".equals(passwordStr)) {
                    Toast.makeText(MainActivity.this, getString(R.string.validation_pass), Toast.LENGTH_SHORT).show();
                    showResetDialog();
                    // 隐藏键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.validation_fail), Toast.LENGTH_SHORT).show();
                    tips.setVisibility(View.VISIBLE);
                    tips.setText(getString(R.string.password_incorrect));
                }

            }
        });

    }


    private void showResetDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.reset_dialog_tile)
                .setMessage(R.string.reset_dialog_summary)
                .setPositiveButton(R.string.reset_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isInSelecting = false;
                        Toast.makeText(MainActivity.this, getString(R.string.reset_menu_ok_toast), Toast.LENGTH_LONG).show();
                        mMenuManager.initData(createInitData(), true);
                        updateSomething();
                    }

                })
                .setNegativeButton(R.string.reset_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, getString(R.string.reset_menu_cancel_toast), Toast.LENGTH_LONG)
                                .show();
                    }
                })
                .create();
        dialog.show();
    }

    private void showConfirmDialog() {
        MenuItem current = mMenuManager.getCurrentMenu();
        if (mCurrentSelectedMenu == null) {
            Toast.makeText(this, getString(R.string.menu_selected_toast), Toast.LENGTH_SHORT).show();
            return;
        } else if (current != null) {
            Toast.makeText(this, "你的菜：\n" + current, Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_dialog_title)
                .setMessage(mCurrentSelectedMenu.toString())
                .setPositiveButton(R.string.confirm_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isInSelecting = false;
                        if (mCurrentSelectedMenu != null) {
                            mResultShow.setTextColor(getColor(R.color.color_green));
                            mMenuManager.saveMenu(mCurrentSelectedMenu);
                            mMenuManager.removeMenu(mCurrentSelectedMenu);
                            Toast.makeText(MainActivity.this, getString(R.string.confirm_ok_toast, mCurrentSelectedMenu.name), Toast.LENGTH_LONG).show();
                        }
                    }

                })
                .setNegativeButton(R.string.confirm_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mRejectTimes++ > 3) {
                            Toast.makeText(MainActivity.this, getString(R.string.reject_more_time), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(MainActivity.this, getString(R.string.reject_toast), Toast.LENGTH_LONG)
                                .show();
                        mResultShow.setText("");
                        mResultTitle.setText(getString(R.string.available_menu, mMenuManager.getMenuSize()));
                        isInSelecting = false;
                        mCurrentSelectedMenu = null;
                    }
                })
                .create();
        dialog.show();
    }
    private void showRejectDialog() {
        if (mCurrentSelectedMenu == null|| mMenuManager.getCurrentMenu() == null) {
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_dialog_title)
                .setMessage(mCurrentSelectedMenu.toString())
                .setNegativeButton(R.string.confirm_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mRejectTimes++ > 3) {
                            Toast.makeText(MainActivity.this, getString(R.string.reject_more_time), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(MainActivity.this, getString(R.string.reject_toast), Toast.LENGTH_LONG)
                                .show();
                        mResultShow.setText("");
                        mResultTitle.setText(getString(R.string.available_menu, mMenuManager.getMenuSize()));
                        isInSelecting = false;
                        mCurrentSelectedMenu = null;
                        mMenuManager.saveMenu(mCurrentSelectedMenu, true);
                    }
                })
                .create();
        dialog.show();
    }
}

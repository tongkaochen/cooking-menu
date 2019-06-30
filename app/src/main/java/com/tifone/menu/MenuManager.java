package com.tifone.menu;

import android.content.Context;

import java.util.List;

/**
 * Create by Tifone on 2019/6/16.
 */
public class MenuManager {

    private static MenuManager INSTANCE;
    private static final Object mObject = new Object();
    private Context mContext;
    private MenuDataStore mMenuStore;
    private MenuManager(Context context, List<String> items) {
        mContext = context;
        mMenuStore = new MenuSharedPreference(context);
        mMenuStore.initData(items, false);
    }
    public static MenuManager getInstance(Context context, List<String> items) {
        if (INSTANCE == null) {
            synchronized (mObject) {
                if (INSTANCE == null) {
                    INSTANCE = new MenuManager(context, items);
                }
            }
        }
        return INSTANCE;
    }
    public void resetData(List<String> items) {
        mMenuStore.initData(items, true);
    }

    public MenuItem getMenu() {
        return mMenuStore.getRandomData();
    }
    public boolean removeMenu(MenuItem item) {
        return mMenuStore.removeItem(item);
    }

    public int getMenuSize() {
        return mMenuStore.getItemCount();
    }
    public void initData(List<String> menus) {
        initData(menus, false);
    }
    public void initData(List<String> menus, boolean forces) {
        mMenuStore.initData(menus, forces);
    }

    public void saveMenu(MenuItem currentMenu) {
        mMenuStore.saveCurrentMenu(currentMenu, false);
    }
    public void saveMenu(MenuItem currentMenu, boolean force) {
        mMenuStore.saveCurrentMenu(currentMenu, force);
    }

    public MenuItem getCurrentMenu() {
        return mMenuStore.getCurrentMenu();
    }

    public List<MenuItem> getAll() {
        return mMenuStore.getAll();
    }
}

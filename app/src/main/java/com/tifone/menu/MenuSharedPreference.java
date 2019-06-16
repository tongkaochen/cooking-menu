package com.tifone.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Create by Tifone on 2019/6/16.
 */
public class MenuSharedPreference implements MenuDataStore {
    private static final String SPF_TABLE_NAME = "menu_spf_table";
    private static final String KEY_MENUS = "key_menus";
    private static final String KEY_DATE = "key_date_";
    private static final String DELIMITER = "#";
    private List<MenuItem> mMenuItems;
    private SharedPreferences mPref;
    private Random mRandom;
    public MenuSharedPreference(Context context) {
        mPref = context.getSharedPreferences(SPF_TABLE_NAME, Context.MODE_PRIVATE);
        mRandom = new Random();
        mMenuItems = new ArrayList<>();
    }

    @Override
    public List<MenuItem> getAll() {
        Map<String, ?> all = mPref.getAll();
        Set<String> allKeys = all.keySet();
        List<MenuItem> allItems = new ArrayList<>();
        for (String key : allKeys) {
            if (key.startsWith(KEY_DATE)) {
                MenuItem item = decodeMenuData(mPref.getString(key, null));
                if (item == null) {
                    continue;
                }
                item.id = Integer.parseInt(key.substring(KEY_DATE.length()));
                allItems.add(item);
            }
        }
        Collections.sort(allItems, new Comparator<MenuItem>() {
            @Override
            public int compare(MenuItem o1, MenuItem o2) {
                return o1.id - o2.id;
            }
        });
        return allItems;
    }
    public void clearAll() {
        SharedPreferences.Editor editor= mPref.edit();
        editor.clear();
        editor.commit();
    }

    private void initAddSaveData(List<MenuItem> items) {
        mMenuItems.clear();
        mMenuItems = items;
        saveList(items);
    }
    private void getInitDataFromSpf() {
        mMenuItems = getMenuItems();
    }
    private void clearSpf() {
        SharedPreferences.Editor editor= mPref.edit();
        editor.putStringSet(KEY_MENUS, null);
        editor.putString(getCurrentMenuKey(), null);
        editor.commit();
    }

    @Override
    public MenuItem getCurrentMenu() {
        return decodeMenuData(mPref.getString(getCurrentMenuKey(), null));
    }

    private String getCurrentMenuKey() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Log.d("tifone", "format = " + format.format(date));
        String dateKey = String.format("%s%s", KEY_DATE, format.format(date));
        if (TextUtils.isEmpty(dateKey)) {
            return KEY_DATE;
        }
        return dateKey;
    }

    @Override
    public void saveCurrentMenu(MenuItem item, boolean force) {
        if (getCurrentMenu() != null && !force) {
            return;
        }
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(getCurrentMenuKey(), encodeMenuData(item));
        editor.commit();
    }

    @Override
    public void initData(List<String> menus, boolean force) {
        if (force) {
            clearSpf();
        }
        Log.d("tifone", "input size " + menus.size() + " force = " + force);
        List<MenuItem> items = new ArrayList<>();
        for (String menu : menus) {
            items.add(decodeMenuData(menu));
        }
        List<MenuItem> savedData = getMenuItems();
        Log.d("tifone", "saved data lenght =" + savedData.size());
        if (savedData == null || savedData.size() == 0) {
            initAddSaveData(items);
        } else {
            getInitDataFromSpf();
        }
        for (MenuItem item : mMenuItems) {
            Log.d("tifone", item.toString());
        }
    }

    @Override
    public MenuItem getRandomData() {
        int randomIndex = getRandom();
        return mMenuItems.get(randomIndex);
    }

    @Override
    public boolean isEmpty() {
        return mMenuItems.isEmpty();
    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }

    private int getRandom() {
        return mRandom.nextInt(mMenuItems.size());
    }

    @Override
    public boolean removeItem(int index) {
        boolean result = mMenuItems.remove(mMenuItems.get(index));
        saveList(mMenuItems);
        return result;
    }

    @Override
    public boolean removeItem(MenuItem item) {
        boolean result = mMenuItems.remove(item);
        saveList(mMenuItems);
        return result;
    }

    private List<MenuItem> getMenuItems() {
        return toList(mPref.getStringSet(KEY_MENUS, null));
    }

    private void saveList(List<MenuItem> items) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putStringSet(KEY_MENUS, toSet(items));
        editor.apply();
    }
    private Set<String> toSet(List<MenuItem> items) {
        Set<String> result = new HashSet<>();
        if (items == null) {
            return result;
        }
        for (MenuItem item : items) {
            result.add(encodeMenuData(item));
        }
        return result;
    }
    private List<MenuItem> toList(Set<String> items) {
        List<MenuItem> result = new ArrayList<>();
        if (items == null) {
            return result;
        }
        for (String item : items) {
            result.add(decodeMenuData(item));
        }
        return result;
    }

    private MenuItem decodeMenuData(String target) {
        if (target == null) {
            return null;
        }
        String[] temp = target.split("@");
        String menuInfo = temp[0];
        String materialInfo = temp.length > 1 ? temp[1] : null;
        Log.d("tifone", "materialInfo = " + materialInfo);
        String[] menuAttrs = menuInfo.split(DELIMITER);
        Log.d("tifone", "menuAttrs length = " + menuAttrs.length);
        int id = Integer.parseInt(menuAttrs[0]);
        String name = menuAttrs[1];
        int count = Integer.parseInt(menuAttrs[2]);
        int iconId = Integer.parseInt(menuAttrs[3]);
        MenuItem item = new MenuItem(id, name);
        item.count = count;
        item.iconId = iconId;
        if (materialInfo == null) {
            return item;
        }
        String[] materialAttrs = materialInfo.split(DELIMITER);
        List<MenuItem.Material> materials = new ArrayList<>();
        for (int i = 0; i < materialAttrs.length; i += 2) {
            String materialName = materialAttrs[i];
            Log.d("tifone", "materialName = " + materialAttrs[i] + " " + ((i+1) < materialAttrs.length ? materialAttrs[i+1] : ""));
            int usage = (i+2) < materialAttrs.length ? Integer.parseInt(materialAttrs[i+1]) : 0;
            materials.add(item.createMaterial(materialName, usage));
        }
        item.materials = materials;
        return item;
    }
    private String encodeMenuData(MenuItem item) {
        if (item == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(item.id)
                .append(DELIMITER)
                .append(item.name)
                .append(DELIMITER)
                .append(item.count)
                .append(DELIMITER)
                .append(item.iconId);
        if (item.materials != null) {
            List<MenuItem.Material> material = item.materials;
            builder.append("@");
            for (int i = 0; i < material.size(); i++) {
                builder.append(material.get(i).name)
                        .append(DELIMITER)
                        .append(material.get(i).usage)
                        .append(DELIMITER);

            }
        }
        return builder.toString();
    }
}

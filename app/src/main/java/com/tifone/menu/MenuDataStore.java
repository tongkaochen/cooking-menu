package com.tifone.menu;

import java.util.List;
import java.util.Random;

/**
 * Create by Tifone on 2019/6/16.
 */
public interface MenuDataStore {
    void initData(List<String> menus, boolean force);
    MenuItem getRandomData();
    boolean isEmpty();
    int getItemCount();
    boolean removeItem(int index);
    boolean removeItem(MenuItem item);
}

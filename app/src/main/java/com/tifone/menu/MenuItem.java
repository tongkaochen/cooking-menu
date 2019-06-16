package com.tifone.menu;

import android.graphics.Matrix;

import java.util.List;

/**
 * Create by Tifone on 2019/6/16.
 */
public final class MenuItem {
    public int id;
    public String name;
    public int iconId;
    public List<Material> materials;
    public int count;
    public MenuItem(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Material createMaterial(String name, int usage) {
        return new Material(name, usage);
    }
    public class Material {
        public String name;
        public int usage;
        public Material(String name, int usage) {
            this.name = name;
            this.usage = usage;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("菜名：")
                .append(name)
                .append("\n完成次数：")
                .append(count);
        List<MenuItem.Material> materialList = materials;
        if (materialList == null || materialList.isEmpty()) {
            return builder.toString();
        }
        int i = 0;
        builder.append("\n用料清单：");
        for (MenuItem.Material material : materialList) {
            builder.append("\n\t" + (i+1) + ". " + material.name);
            i++;
            if (material.usage != 0) {
                builder.append("  " + material.usage + "g");
            }
        }
        return builder.toString();
    }
}

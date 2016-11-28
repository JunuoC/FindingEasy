package com.example.nuonuo.findingeasy;

import android.graphics.Bitmap;
import android.location.Location;

import java.io.Serializable;

/**
 * Created by NuoNuo on 10/29/16.
 */

public class Item implements Serializable {

    public static final String KEY_INTENT_ITEMPHOTO = "key_intent_itemphoto";
    public static final String KEY_INTENT_ISNEWITEM = "key_intent_isnewitem";
    public static final String KEY_INTENT_ITEM = "key_intent_item";
    public static final String KEY_INTENT_ITEM_POSITION = "key_intent_item_pos";

    public String tagName;
    public String description;
    public String loc;
    public ItemCat cat;
    public BitmapDataObject photo;

    public static enum ItemCat {Books,Food,Cosmetics,Clothes,Gizmos,Others};

    public void setTagName(String s) {
        tagName = s;
    }

    public void setDescription(String s) {
        description = s;
    }

    public void setCategorty(ItemCat ic) {
        cat = ic;
    }

    public void setLocation(String l) { loc = l; }

    public String toListViewString() {
        return cat + " | " + tagName;
    }
}

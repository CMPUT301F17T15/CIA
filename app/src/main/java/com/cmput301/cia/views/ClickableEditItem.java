/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmput301.cia.R;

/**
 * Created by George on 2017-12-03.
 */

public class ClickableEditItem extends LinearLayout {
    private TextView itemStaticText;
    private TextView itemDynamicText;
    private ImageView itemIcon;

    public ClickableEditItem(Context context) {
        this(context,null);
    }

    public ClickableEditItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickableEditItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.clickable_edit_item, this, true);

        itemStaticText = (TextView) findViewById(R.id.itemStaticText);
        itemDynamicText = (TextView) findViewById(R.id.itemDynamicText);
        itemIcon = (ImageView) findViewById(R.id.itemIcon);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClickableEditItem);

        String staticText = typedArray.getString(R.styleable.ClickableEditItem_itemStaticText);
        String dynamicText = typedArray.getString(R.styleable.ClickableEditItem_itemDynamicText);
        Drawable drawable = typedArray.getDrawable(R.styleable.ClickableEditItem_itemIcon);

        if (TextUtils.isEmpty(staticText)) staticText = "Title";
        if (TextUtils.isEmpty(dynamicText)) dynamicText = "Description";
        if (drawable == null) drawable = getResources().getDrawable(R.drawable.googleg_standard_color_18);

        setItemStaticText(staticText);
        setItemDynamicText(dynamicText);
        setItemIcon(drawable);

        typedArray.recycle();
    }

    public void setItemDynamicText(String text) {
        itemDynamicText.setText(text);
    }

    public void setItemStaticText(String text) {
        itemStaticText.setText(text);
    }

    public void setItemIcon(Drawable drawable) {
        itemIcon.setImageDrawable(drawable);
    }

    public String getDynamicText() {
        return itemDynamicText.getText().toString();
    }
}

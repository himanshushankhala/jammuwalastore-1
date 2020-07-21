package com.jammuwalastore.helper;

/**
 * Created by neerajrathore on 10/09/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.LruCache;


public class TextAwesome extends androidx.appcompat.widget.AppCompatTextView {

    private final static String NAME = "FONTAWESOME";
    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);

    public TextAwesome(Context context) {
        super(context);
        init();

    }

    public TextAwesome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {

        Typeface typeface = sTypefaceCache.get(NAME);

        if (typeface == null) {

            typeface = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");
            sTypefaceCache.put(NAME, typeface);

        }

        setTypeface(typeface);

    }

}


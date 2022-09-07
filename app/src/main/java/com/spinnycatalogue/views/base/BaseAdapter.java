package com.spinnycatalogue.views.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<T extends RecyclerView.ViewHolder, D> extends RecyclerView.Adapter<T>{

    public abstract void setData(List<D> data);

    protected boolean isValidContext(Context context)
    {
        if(context instanceof Activity)
        {
            Activity activity = (Activity)  context;
            return !activity.isFinishing() && !activity.isDestroyed();
        }

        return false;
    }

    protected Drawable tintDrawable(Drawable item, int color) {

        Drawable wrapDrawable = DrawableCompat.wrap(item);
        DrawableCompat.setTint(wrapDrawable, color);

        return wrapDrawable;
    }
}
package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class customPackageList extends ArrayAdapter {

    private List<String> packagename = new ArrayList<String>();
    private List<Drawable> drawablesImages = new ArrayList<Drawable>();
    private Activity context;

    public customPackageList(@NonNull Activity context, List<String> packagename, List<Drawable> drawablesImages) {
        super(context, R.layout.row_item, packagename);
        this.context = context;
        this.packagename = packagename;
        this.drawablesImages = drawablesImages;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView == null)
                row = inflater.inflate(R.layout.row_item,null,true);
        ImageView imageView = (ImageView) row.findViewById(R.id.imageview);
        TextView textView = (TextView) row.findViewById(R.id.appName);

        imageView.setImageDrawable(drawablesImages.get(position));
        textView.setText((packagename.get(position)));
        return  row;
    }
}

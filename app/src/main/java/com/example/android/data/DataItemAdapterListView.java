package com.example.android.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.data.model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DataItemAdapterListView extends ArrayAdapter<DataItem> {

    private List<DataItem> dataItems;
    private LayoutInflater inflater;

    DataItemAdapterListView(Context context, List<DataItem> dataItems) {
        super(context, R.layout.list_item, dataItems);
        this.dataItems = dataItems;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.itemNameText);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        DataItem item = dataItems.get(position);

        try {
            tvName.setText(item.getItemName());
            String imageFile = item.getImage();
            InputStream inputStream = getContext().getAssets().open(imageFile);
            Drawable d = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}

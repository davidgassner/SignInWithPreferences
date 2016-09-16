package com.example.android.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.data.model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {

//    public static final String ITEM_ID_KEY = "item_id_key";
    static final String ITEM_KEY = "item_key";
    private List<DataItem> mItems;
    private Context mContext;
    private SharedPreferences defaultPreferences;

    DataItemAdapter(Context context, List<DataItem> items) {
        this.mContext = context;
        this.mItems = items;
        defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public DataItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean displayGrid = defaultPreferences.getBoolean(
                mContext.getString(R.string.pref_display_grid), false);
        int layoutResource = displayGrid ? R.layout.grid_item : R.layout.list_item;
        View itemView = inflater.inflate(layoutResource, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataItemAdapter.ViewHolder holder, int position) {
        final DataItem item = mItems.get(position);

        try {
            holder.tvName.setText(item.getItemName());
            String imageFile = item.getImage();
            InputStream inputStream = mContext.getAssets().open(imageFile);
            Drawable d = Drawable.createFromStream(inputStream, null);
            holder.imageView.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "You selected " + item.getItemName(),
                        Toast.LENGTH_SHORT).show();
//                String itemId = item.getItemId();
                Intent intent = new Intent(mContext, DetailActivity.class);
//                intent.putExtra(ITEM_ID_KEY, itemId);
                intent.putExtra(ITEM_KEY, item);
                mContext.startActivity(intent);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "You long clicked " + item.getItemName(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView imageView;
        View mView;

        ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            tvName = (TextView) itemView.findViewById(R.id.itemNameText);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
         }
    }
}

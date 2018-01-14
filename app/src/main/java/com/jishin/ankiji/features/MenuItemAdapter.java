package com.jishin.ankiji.features;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jishin.ankiji.R;
import com.jishin.ankiji.utilities.GlideApp;

import java.util.ArrayList;

/**
 * Created by LanAnh on 14/01/2018.
 */

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemHolder> {
    private ArrayList<String> mListItem = new ArrayList<>();
    private ArrayList<Integer> mListIcon = new ArrayList<>();
    private Context mContext;
    public MenuItemAdapter(ArrayList<String> listItem, ArrayList<Integer>listIcon, Context context){
        this.mListItem = listItem;
        this.mListIcon = listIcon;
        this.mContext = context;
    }
    @Override
    public MenuItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        MenuItemHolder holder = new MenuItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MenuItemHolder holder, int position) {
        GlideApp.with(mContext)
                .load(mListIcon.get(position)).placeholder(R.drawable.ic_item_menu)
                .into(holder.imgIcon);
        holder.txtItemName.setText(mListItem.get(position));
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder {
        public ImageView imgIcon;
        public TextView txtItemName;
        public MenuItemHolder(View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.icMenu);
            txtItemName = itemView.findViewById(R.id.txtItemName);
        }
    }
}

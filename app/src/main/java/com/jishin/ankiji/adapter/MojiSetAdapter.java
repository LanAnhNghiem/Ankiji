package com.jishin.ankiji.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jishin.ankiji.R;
import com.jishin.ankiji.animation.BuilderManager;
import com.jishin.ankiji.interfaces.RemoveDataCommunicator;
import com.jishin.ankiji.model.MojiSet;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lana on 01/02/2018.
 */

public class MojiSetAdapter extends RecyclerView.Adapter<MojiSetAdapter.MojiSetHolder> {
    private List<MojiSet> mMojiSet = new ArrayList<>();
    private Context mContext;
    private RemoveDataCommunicator mCommunicator;
    private OnBoomMenuItemClicked mListener;
    public MojiSetAdapter(List<MojiSet> list, Context context, RemoveDataCommunicator communicator) {
        this.mMojiSet = list;
        this.mContext = context;
        this.mCommunicator = communicator;
    }

    @Override
    public MojiSetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler_item,
                parent, false);
        return new MojiSetHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MojiSetHolder holder, final int position) {
        holder.tvTitle.setText(mMojiSet.get(position).getName());
        holder.tvDateTime.setText(convertDatetime(mMojiSet.get(position).getDatetime()));
        holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveDialog(position);
            }
        });
        holder.bmb.clearBuilders();
        int stringIndex = 0;
        for (int i = 0; i < holder.bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            switch (i) {
                case 0:
                    stringIndex = R.string.text_outside_button_buider_learn;
                    break;
                case 1:
                    stringIndex = R.string.text_outside_button_buider_test;
                    break;
                case 2:
                    stringIndex = R.string.text_outside_button_buider_chart;
                    break;
                case 3:
                    stringIndex = R.string.text_outside_button_buider_edit;
                    break;
            }
            addBuilder(holder, stringIndex);
        }
    }
    public void setOnBoomMenuItemClick(OnBoomMenuItemClicked mListener) {
        this.mListener = mListener;
    }
    @Override
    public int getItemCount() {
        return mMojiSet.size();
    }

    public class MojiSetHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDateTime;
        ImageButton btnDeleteItem;
        BoomMenuButton bmb;
        public MojiSetHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDateTime = itemView.findViewById(R.id.tv_item_count);
            btnDeleteItem = itemView.findViewById(R.id.btn_delete_item);
            bmb = itemView.findViewById(R.id.bmb);
        }
    }

    public interface OnBoomMenuItemClicked {
        void OnMenuItemClicked(int classIndex);
    }
    void showRemoveDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.remove_warning);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCommunicator.removeData(mMojiSet.get(position).getId(), position);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    void addBuilder(final MojiSetHolder viewHolder, int stringIndex) {
        viewHolder.bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(BuilderManager.getImageResource())
                .normalTextRes(stringIndex)
                .textSize(14)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        mListener.OnMenuItemClicked(index);
                    }
                }));
    }
    private String convertDatetime(String datetime){
        String result = datetime;
        String gmt = result.substring(20, 29);
        String year = result.substring(30);
        result = result.replace(gmt, "").replace(year, "");
        String part1 = result.substring(0, 10);
        String part2 = result.substring(11,19);
        result = part1+" "+year+" "+part2;
        return result;
    }
}

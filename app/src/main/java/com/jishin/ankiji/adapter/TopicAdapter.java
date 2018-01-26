package com.jishin.ankiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.explores.KanjiExploresActivity;
import com.jishin.ankiji.explores.MojiExploresActivity;
import com.jishin.ankiji.model.DateAccess;
import com.jishin.ankiji.utilities.DatabaseService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by trungnguyeen on 12/27/17.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicHolder> {

    public static final String TAG = TopicAdapter.class.getSimpleName();
    public Context context;


    ArrayList<String> Topic = new ArrayList<String>();
    private String id;
    private String topicName;
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
    String currentDay;
    private DatabaseService mData = DatabaseService.getInstance();
    private DatabaseReference mDateRef = mData.getDatabase().child("DateSet");
    String userID = mData.getUserID();
    private DatabaseReference mDateSet = mData.createDatabase("DateSet");

    private String FRAGMENT_TAG;

    public TopicAdapter(String FRAGMENT_TAG) {
        this.FRAGMENT_TAG = FRAGMENT_TAG;
    }

    @Override
    public TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.topic_recycler_item, parent, false);
        return new TopicHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TopicHolder holder, final int position) {
        if (FRAGMENT_TAG == "KANJI") {
            holder.tvTitle.setText(Topic.get(position));
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + Topic.get(position));

                    currentDay = String.valueOf(dateFormat.format(cal.getTime()));
                    Log.d(TAG, "onClick: date: " + currentDay);

                    Log.d(TAG, "onClick: dataSet " + mDateSet.child(userID).getKey());
                    topicName = Topic.get(position);
                    checkID();
                    Log.d(TAG, "onClick: Cal: " + String.valueOf(dateFormat.format(cal.getTime())));
                    Intent intent = new Intent(holder.context, KanjiExploresActivity.class);
                    intent.putExtra("Kanji_Key", Topic.get(position));
                    holder.context.startActivity(intent);
                }
            });

        } else if (FRAGMENT_TAG == "MOJI") {
            int topicNum = position+1;
            holder.tvTitle.setText(Topic.get(position));
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + Topic.get(position));
                    Log.d(TAG, "onClick: date moji: " + currentDay);
                    Log.d(TAG, "onClick: dataSet " + mDateSet.child(userID).getKey());
                    currentDay = String.valueOf(dateFormat.format(cal.getTime()));
                    topicName = Topic.get(position);
                    Log.d(TAG, "onClick: topicName: " + topicName);
                    checkID();
                    Intent intent = new Intent(holder.context, MojiExploresActivity.class);
                    intent.putExtra("Moji_Key", Topic.get(position));
                    holder.context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Topic.size();
    }

    public ArrayList<String> setTopic(ArrayList<String> topic) {
        Topic = topic;
        return Topic;
    }


    private void setDateAccess(String topicId, String type, String date) {
        id = mDateSet.push().getKey();
        DateAccess set = new DateAccess(type, topicId, date);
        mDateSet.child(userID).child(id).setValue(set);

    }

    private void checkID() {
        mDateRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isExist = false;
                Log.d(TAG, "onDataChange: data: " + dataSnapshot);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DateAccess date = new DateAccess();
                    Log.d(TAG, "onDataChange: dsValue: " + ds.getValue());
                    date.setId(ds.getValue(DateAccess.class).getId());
                    Log.d(TAG, "onDataChange: id: " + date.getId());
                    if (date.getId().equals(topicName)) {
                        isExist = true;
                        String key = ds.getKey();
                        if (FRAGMENT_TAG == "KANJI") {
                            updateDate(key, topicName, "Kanji", currentDay);
                            break;
                        } else {
                            updateDate(key, topicName, "Moji", currentDay);
                        }

                    }
                }
                Log.d(TAG, "onDataChange: isExist: " + isExist);
                if (isExist == false) {
                    if (FRAGMENT_TAG == "KANJI") {
                        setDateAccess(topicName, "Kanji", currentDay);
                    } else {
                        setDateAccess(topicName, "Moji", currentDay);
                    }
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateDate(String key, String topicId, String type, String date) {
        DateAccess set = new DateAccess(type, topicId, date);
        mDateSet.child(userID).child(key).setValue(set);

    }


    public class TopicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle;
        private CardView cardView;
        private final Context context;

        public TopicHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.txt_title);
            cardView = itemView.findViewById(R.id.cardView_Topic);
            context = itemView.getContext();

        }

        public void bindViewHolder() {

        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick(in): " + getAdapterPosition());
        }
    }


}

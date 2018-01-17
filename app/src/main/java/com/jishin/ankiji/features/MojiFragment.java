package com.jishin.ankiji.features;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jishin.ankiji.R;
import com.jishin.ankiji.adapter.CardItemsAdapter;
import com.jishin.ankiji.utilities.GlideApp;

/**
 * Created by trungnguyeen on 12/27/17.
 */

@SuppressLint("ValidFragment")
public class MojiFragment extends Fragment {

    private RecyclerView rvRecentlyList;
    private CardItemsAdapter mItemsAdapter;
    private FloatingActionButton mFABtn, mFABCreate, mFABAdd;
    public String FRAGMENT_TAG = "MOJI";
    private boolean isStable = true;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moji, container, false);
        addControl(view);
        initRecycler(view);

        return view;
    }
    private void addControl(View view){
        mFABtn = view.findViewById(R.id.fabMoji);
        mFABAdd = view.findViewById(R.id.fabAdd);
        mFABCreate = view.findViewById(R.id.fabCreate);
        mFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mFABCreate.isShown()){
                    mFABAdd.show();
                    mFABCreate.show();  //required
                    float deg = mFABtn.getRotation() + 45F;
                    mFABtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    isStable = false;
                }
                else{
                    mFABAdd.hide();
                    mFABCreate.hide();
                    float deg = mFABtn.getRotation() + 45F;
                    mFABtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    isStable = true;
                }
            }
        });
        mFABCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                final View dialogView = layoutInflater.inflate(R.layout.dialog_create_list, null);
                final TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
                final EditText edtSetName = dialogView.findViewById(R.id.edtSetName);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        mFABAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initRecycler(View view) {
        rvRecentlyList = (RecyclerView) view.findViewById(R.id.recycle_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rvRecentlyList.setLayoutManager(layoutManager);

        mItemsAdapter = new CardItemsAdapter(FRAGMENT_TAG);

        rvRecentlyList.setItemAnimator(new DefaultItemAnimator());
        rvRecentlyList.setAdapter(mItemsAdapter);
        rvRecentlyList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    mFABtn.show();
                    if(isStable == false && !mFABCreate.isShown()){
                        mFABtn.setRotation(mFABtn.getRotation()+45F);
                        isStable = true;
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if ((dy > 0 ||dy<0) && mFABtn.isShown()) {
                    if(mFABAdd.isShown()|| mFABCreate.isShown()){
                        mFABAdd.hide();
                        mFABCreate.hide();

                    }
                    mFABtn.hide();


                }
            }
        });

    }
}

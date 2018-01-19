package com.jishin.ankiji.signin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jishin.ankiji.R;
import com.jishin.ankiji.features.FeatureActivity;
import com.jishin.ankiji.model.User;
import com.jishin.ankiji.utilities.DatabaseService;

/**
 * Created by lana on 18/01/2018.
 */

public class LoginGoogle {
    private static final String TAG = "GoogleActivity";
    public static final int RC_SIGN_IN = 9001;
    private static FirebaseAuth mAuth;
    private static Activity mActivity;
    private GoogleSignInClient mGoogleSignInClient;
    private static DatabaseService mData = DatabaseService.getInstance();
    private static ProgressDialog progressDialog;
    private static String email = "";
    private static String userName = "";
    private static String avatar = "";
    private static String idUser;

    public LoginGoogle(String defaultWebClientID, Activity activity){
        this.mActivity = activity;
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(defaultWebClientID)
                .requestEmail()
                .build();
        mAuth = mData.getFirebaseAuth();
        mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso);
        progressDialog = new ProgressDialog(mActivity);
    }
    public GoogleSignInClient getmGoogleSignInClient(){
        return mGoogleSignInClient;
    }

    public static void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        //Display Progress Dialog
        showProgress();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            email = task.getResult().getUser().getEmail();
                            userName = task.getResult().getUser().getDisplayName();
                            avatar = task.getResult().getUser().getPhotoUrl().toString();
                            idUser = task.getResult().getUser().getUid();
                            User user = new User(idUser, userName, email,avatar);
                            createUserOnFireBase(user);
                            Toast.makeText(mActivity, R.string.login_success, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mActivity, FeatureActivity.class);
                            mActivity.startActivity(intent);
                            if(progressDialog.isShowing()){
                                hideProgress();
                                mActivity.finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(mActivity, R.string.login_failed, Toast.LENGTH_SHORT).show();
                        }

                        //Hide Progress Dialog
                        hideProgress();
                    }
                });
    }
    private static void createUserOnFireBase(final User user){
        final DatabaseReference userNode = mData.createDatabase("User").child(user.getId());
        userNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    userNode.setValue(user);
                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
//                    mActivity.startActivity(new Intent(mActivity, CustomMapsActivity.class));
                }
                else {
                    hideProgress();
                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
//                    mActivity.startActivity(new Intent(mActivity, CustomMapsActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private static void showProgress(){
        //progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private static void hideProgress(){
        progressDialog.dismiss();
        progressDialog.hide();
    }
}

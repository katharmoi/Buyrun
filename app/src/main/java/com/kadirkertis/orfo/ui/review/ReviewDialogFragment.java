package com.kadirkertis.orfo.ui.review;

import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.model.Review;
import com.kadirkertis.domain.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kadir Kertis on 10.4.2017.
 */

public class ReviewDialogFragment extends DialogFragment {
    public static final String ID = "id";
    private String mPlaceId ;
    private ReviewDialogListener mListener;
    private EditText mReviewTitle;
    private EditText mReviewText;
    private RatingBar mRatingBar;

    private FirebaseDatabase mDb;
    private SharedPreferences mPrefs;


    public static ReviewDialogFragment newInstance(String placeId){
        ReviewDialogFragment reviewDialogFragment = new ReviewDialogFragment();

        Bundle args = new Bundle();
        args.putString(ID,placeId);
        reviewDialogFragment.setArguments(args);
        return reviewDialogFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){
            mPlaceId = args.getString(ID);
        }
        mDb = FirebaseDatabase.getInstance();
        mPrefs = getActivity().getSharedPreferences(Constants.PREFS_CHECKED_IN_PLACE, Context.MODE_PRIVATE);
        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Holo_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.review_dialog_fragment,container,false);
        mReviewTitle = root.findViewById(R.id.reviewTitleText);
        mReviewText = root.findViewById(R.id.reviewText);
        mRatingBar = root.findViewById(R.id.reviewRatingBar);
        Button sendBtn = root.findViewById(R.id.reviewSendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putReview();
            }
        });

        Button cancelBtn = root.findViewById(R.id.reviewCancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        return root;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            mListener = (ReviewDialogListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(getActivity().toString()
//                    + " must implement ReviewDialogListener");
//        }
//    }

    private void putReview() {
        String userKey = mPrefs.getString(Constants.PREFS_USER_ID, null);
        String reviewTitle = mReviewTitle.getText().toString().trim();
        String reviewText = mReviewText.getText().toString().trim();
        String userName =FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        float rating = mRatingBar.getRating();


        HashMap<String, Object> reviewTime = new HashMap<>();
        reviewTime.put(Constants.PROPERTY_TIME_ADDED, ServerValue.TIMESTAMP);

        String reviewId = mDb.getReference()
                .child(Constants.DB_PLACES)
                .child(mPlaceId)
                .child(Constants.TABLE_PLACE_REVIEWS)
                .push()
                .getKey();

        Map<String, Object> reviews = new HashMap<>();


        Map<String, Object> review = (new Review(reviewId,userKey,userName,reviewTitle,reviewText,rating,reviewTime)).toMap();

        String placeReference = "/"
                + Constants.DB_PLACES
                + "/"
                + mPlaceId
                + "/"
                + Constants.TABLE_PLACE_REVIEWS
                + "/"
                + reviewId;


        String userReference = "/"
                + Constants.DB_USERS
                + "/"
                + userKey
                + "/"
                + Constants.TABLE_USER_REVIEWS
                + "/"
                + reviewId;
        reviews.put(placeReference,
                review);
        reviews.put(userReference,
                review);

        mDb.getReference().updateChildren(reviews)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), getString(R.string.reviewed_succesfully),
                                Toast.LENGTH_SHORT).show();
                        getActivity().finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), getString(R.string.review_failure),
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }



    interface ReviewDialogListener {
        void onReview();
        void onCancel();
    }
}

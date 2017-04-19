package com.kadirkertis.orfo.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.adapters.ChatterAdapter;
import com.kadirkertis.orfo.adapters.MessageAdapter;
import com.kadirkertis.orfo.databinding.ActivityChatBinding;
import com.kadirkertis.orfo.model.CheckInPlace;
import com.kadirkertis.orfo.model.Message;
import com.kadirkertis.orfo.utilities.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private FirebaseDatabase mDb;
    private DatabaseReference mMessagesReference;
    private ChildEventListener mMessagesListener;
    private DatabaseReference mChattersReference;
    private ChildEventListener mChattersListener;
    private String mStoreId;
    private String mUserId;
    private List<Message> mMessages;
    private List<CheckInPlace> mChatters;

    private ActivityChatBinding mBinding;
    private MessageAdapter mMessageAdapter;
    private ChatterAdapter mChattersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        Bundle args = getIntent().getExtras();

        if (args != null) {
            mStoreId = args.getString(Constants.EXTRA_PLACE_ID);
            mUserId = args.getString(Constants.EXTRA_USER_ID);
        }
        mDb = FirebaseDatabase.getInstance();
        mMessagesReference = mDb.getReference()
                .child(Constants.DB_PLACES)
                .child(mStoreId)
                .child(Constants.PLACE_MESSAGES_TABLE);

        mChattersReference = mDb.getReference()
                .child(Constants.DB_PLACES)
                .child(mStoreId)
                .child(Constants.TABLE_PLACE_CURRENT_CHECKED_INS);


        Toolbar toolbar = mBinding.chatToolbar;
        toolbar.setTitle(getString(R.string.chat_activity_title));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(mMessages,this,R.layout.list_item_message_user,mUserId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mBinding.messagesRecycler.setAdapter(mMessageAdapter);
        mBinding.messagesRecycler.setLayoutManager(layoutManager);
        mBinding.messagesRecycler.setEmptyView(mBinding.emptyView);

        mChatters = new ArrayList<>();
        mChattersAdapter = new ChatterAdapter(mChatters,this,R.layout.list_item_chatters);
        LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mBinding.chattersRecycler.setAdapter(mChattersAdapter);
        mBinding.chattersRecycler.setLayoutManager(lm);

        mBinding.messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mBinding.sendButton.setEnabled(true);
                } else {
                    mBinding.sendButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBinding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> time = new HashMap<>();
                time.put(Constants.PROPERTY_TIME_ADDED, ServerValue.TIMESTAMP);
                Message message = new Message(mUserId,
                        mBinding.messageEditText
                        .getText().toString(), "ANONYMOUS", time);
                mMessagesReference.push().setValue(message);
                // Clear input box
                mBinding.messageEditText.setText("");

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                mBinding.messagesRecycler.scrollToPosition(mMessageAdapter.getItemCount() -1);

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition trEnter = new Slide(Gravity.END);
            trEnter.setDuration(350);
            getWindow().setEnterTransition(trEnter);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMessagesListener == null) {
            mMessagesListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.exists()){
                        mMessageAdapter.addItem(dataSnapshot.getValue(Message.class));
                    }
                    if(mBinding.progressSpinner.isShown()){
                        mBinding.progressSpinner.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mMessagesReference.addChildEventListener(mMessagesListener);
        }

        if(mChattersListener == null){
            mChattersListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.exists()){
                        CheckInPlace user = dataSnapshot.getValue(CheckInPlace.class);
                        mChattersAdapter.addItem(user);
                    }

                    if(mBinding.progressSpinner.isShown()){
                        mBinding.progressSpinner.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mChattersReference.addChildEventListener(mChattersListener);
        }

        mBinding.progressSpinner.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMessagesListener != null) {
            mMessagesReference.removeEventListener(mMessagesListener);
            mMessagesListener = null;
        }

        if(mChattersListener != null){
            mChattersReference.removeEventListener(mChattersListener);
            mChattersListener = null;
        }
        if(mBinding.progressSpinner.isShown()){
            mBinding.progressSpinner.setVisibility(View.GONE);
        }
    }
}

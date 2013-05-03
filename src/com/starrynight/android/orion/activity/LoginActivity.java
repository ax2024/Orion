package com.starrynight.android.orion.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orion.android.R;
import com.starrynight.android.orion.OrionApplication;
import com.starrynight.android.orion.OrionConfiguration;
import com.starrynight.android.orion.storage.OrionProvider;
import com.starrynight.android.orion.xmlrpc.XmlRpcTools;

public class LoginActivity extends Activity {
    final static String TAG = OrionApplication.getApplicationTag() + "."
            + LoginActivity.class.getSimpleName();
    
    EditText mMediaboxNumText = null;
    EditText mWebPinText = null;
    Button   mLoginButton = null;
    
    ProgressDialog mProgressDialog = null;
    
    String mSession = "";
    Handler mHandler = new MyHandler();
    HashMap<String, Object> mMediabox;
    int mMediaboxID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


		this.mMediaboxNumText = (EditText) findViewById(R.id.mediaboxNumEditText);
        this.mWebPinText = (EditText) findViewById(R.id.webPinEditText);
        this.mLoginButton = (Button) findViewById(R.id.loginButton);

        this.mLoginButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                final String mediaboxNum = LoginActivity.this.mMediaboxNumText.getText().toString().trim();
                final String webPin = LoginActivity.this.mWebPinText.getText().toString().trim();
                
                if (checkLoginParameter(mediaboxNum, webPin)) {

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            boolean result = false;
                            try {
                                mSession = XmlRpcTools.doMediaBox_Login(mediaboxNum, webPin);
                                Log.d(TAG, "session: " + mSession);
                                mMediabox = XmlRpcTools.doMediaBox_GetBySessionId(mSession);
                                mMediaboxID = (Integer) mMediabox.get("MediaBoxId");
                                Log.d(TAG, "mediabox id: " + mMediaboxID);
                                if (mMediaboxID > 0) {
                                    OrionConfiguration config = OrionApplication.getConfiguration();
                                    config.setMediaboxNum(mMediaboxID);
                                    Object[] messageList = null;
                                    messageList = XmlRpcTools.doMessage_GetAllMessageList(
                                            OrionApplication.getConfiguration().getMediaboxNum(), "VOX");
                                    updateMessagesInDB(messageList);
                                    result = true;
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Regist Failed: " + e.getMessage());
                            }
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            data.putBoolean("result", result);
                            msg.setData(data);
                            LoginActivity.this.mHandler.sendMessage(msg);
                        }
                        
                    }).start();
                            
                    LoginActivity.this.mProgressDialog = ProgressDialog.show(
                            LoginActivity.this, 
                            "", 
                            LoginActivity.this.getResources().getString(R.string.progress_dialog_loading_message), 
                            true);
                }
            }
        });
        
    }
    
    private boolean checkLoginParameter(String mediaboxNum, String webPin) {
    	if (null == mediaboxNum || null == webPin || "" == mediaboxNum || "" == webPin) {
    		Toast.makeText(this, R.string.toast_login_num_or_pin_empty, Toast.LENGTH_SHORT).show();
    		return false;
    	}
    	return true;
    }
    
    private class MyHandler extends Handler {
        public MyHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            Boolean result = msg.getData().getBoolean("result");
            
            Log.d(TAG, "Registration result: " + result);

            if (LoginActivity.this.mProgressDialog != null) {
                LoginActivity.this.mProgressDialog.dismiss();
            }
            
            if (result) {
                Intent intent = new Intent(LoginActivity.this, TabNavigation.class);
                LoginActivity.this.startActivity(intent);
                finish();
            } else {
                LoginActivity.this.showAlert(
                        LoginActivity.this.getResources().getString(
                                R.string.alert_dialog_message_register_failed));
            }
        }
    };

    private void showAlert(String message) {
        AlertDialog mErrorDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.error_dialog_title) 
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(true)
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(final DialogInterface dialog) {
                    }
                })
                .setPositiveButton(R.string.alert_dialog_positive_button, 
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                            }
                    
                }).show();
        // linkify dialog message
//        final TextView msgView = 
//            (TextView) mErrorDialog.findViewById(android.R.id.message);
//        Linkify.addLinks(msgView, Linkify.ALL);
//        msgView.setMovementMethod(new LinkMovementMethod());
        mErrorDialog.setOwnerActivity(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void updateMessagesInDB(Object[] messageList) {
//        for(HashMap<String, Object> message: messageList) {
        for(int i = 0;i < messageList.length; i ++){
            HashMap<String, Object> message = (HashMap<String, Object>) messageList[i];
            
            Integer messageId = (Integer) message.get(OrionProvider.Message.MSG_ID);
            Cursor messageCur = OrionApplication.getContext().getContentResolver().query(
                    OrionProvider.Message.CONTENT_URI,
                    new String[] { OrionProvider.Message.MSG_ID },
                    OrionProvider.Message.MSG_ID + " = \"" + messageId + "\"",
                    null, null);
            final ContentValues values = new ContentValues();
            if (null != messageCur
                    && messageCur.moveToFirst() && messageCur.getCount() > 0) {
                values.put(OrionProvider.Message.MSG_TAG, (String) message.get(OrionProvider.Message.MSG_TAG));
                values.put(OrionProvider.Message.MSG_STATE, (String) message.get(OrionProvider.Message.MSG_STATE));
                
                OrionApplication.getContext().getContentResolver().update(
                        OrionProvider.Message.CONTENT_URI, values, 
                        OrionProvider.Message.MSG_ID + " = \"" + messageId + "\"", null);
            }
            else {
                values.put(OrionProvider.Message.MSG_ID, messageId);
                values.put(OrionProvider.Message.NUM_ORIGIN, (String) message.get(OrionProvider.Message.NUM_ORIGIN));
                values.put(OrionProvider.Message.DEST_NUM, (String) message.get(OrionProvider.Message.DEST_NUM));
                values.put(OrionProvider.Message.MSG_DURATION,(Integer)  message.get(OrionProvider.Message.MSG_DURATION));
                values.put(OrionProvider.Message.MSG_TAG, (String) message.get(OrionProvider.Message.MSG_TAG));
                values.put(OrionProvider.Message.MSG_STATE, (String) message.get(OrionProvider.Message.MSG_STATE));
                Date depositDate = (Date) message.get(OrionProvider.Message.MSG_DEPOSIT_DATE);
                values.put(OrionProvider.Message.MSG_DEPOSIT_DATE, depositDate.getTime());
                values.put(OrionProvider.Message.CALLBACK_NUM, (String) message.get(OrionProvider.Message.CALLBACK_NUM));
                
                OrionApplication.getContext().getContentResolver().insert(
                        OrionProvider.Message.CONTENT_URI, values);
            }
        }
    }

}

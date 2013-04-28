package com.orion.android;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;

import com.orion.OrionApplication;
import com.orion.xmlrpc.XmlRpcTools;

public class MainActivity extends Activity {
    final static String TAG = OrionApplication.getApplicationTag() + "."
            + MainActivity.class.getSimpleName();
    
    String mSession = "";
    Handler mHandler = new MyHandler();
    HashMap<String, Object> mMediabox;
    int mMediaboxID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean result = false;
                try {
                    mSession = XmlRpcTools.doMediaBox_Login("5111", "0000");
                    Log.d(TAG, "session: " + mSession);
                    mMediabox = XmlRpcTools.doMediaBox_GetBySessionId(mSession);
                    mMediaboxID = (Integer) mMediabox.get("MediaBoxId");
                    Log.d(TAG, "mediabox id: " + mMediaboxID);
                    Object[] messageList = null;
                    messageList = XmlRpcTools.doMessage_GetAllMessageList(mMediaboxID, "VOX");
                    result = true;
                } catch (Exception e) {
                    Log.e(TAG, "Regist Failed: " + e.getMessage());
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putBoolean("result", result);
                msg.setData(data);
                MainActivity.this.mHandler.sendMessage(msg);
            }
            
        }).start();
    }
    
    private class MyHandler extends Handler {
        public MyHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            Boolean result = msg.getData().getBoolean("result");
            
            Log.d(TAG, "Registration result: " + result);
            
            if (result) {
            } else {
                MainActivity.this.showAlert(
                        MainActivity.this.getResources().getString(
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

}

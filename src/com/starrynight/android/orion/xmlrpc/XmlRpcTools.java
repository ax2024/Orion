package com.starrynight.android.orion.xmlrpc;

import java.net.URI;
import java.util.HashMap;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.util.Log;

import com.starrynight.android.orion.OrionApplication;

public class XmlRpcTools {
    final static String TAG = OrionApplication.getApplicationTag() + "."
            + XmlRpcTools.class.getSimpleName();

    public XmlRpcTools() {
        // TODO Auto-generated constructor stub
    }
    
    static public String doMediaBox_Login(String id, String pin) {
        XMLRPCClient client;
        URI uri;
        String session = null;
        
        uri = URI.create("http://192.168.174.70/rpc/Server.php");
        client = new XMLRPCClient(uri);
        
        Object[] params = new Object[] { id, pin, "N" };
        
        try {
            session = (String) client.callEx("MediaBox.Login", params);
        } catch (XMLRPCException e) {
            Log.e(TAG, "ERROR:" + e.getMessage());
            return "";
        }
        
        return session;
    }
    
    static public HashMap<String, Object> doMediaBox_GetBySessionId(String sessionId) {

        XMLRPCClient client;
        URI uri;
        
        uri = URI.create("http://192.168.174.70/rpc/Server.php");
        client = new XMLRPCClient(uri);
        
        Object[] params = new Object[] { sessionId };
        
        HashMap<String, Object> mediabox = null;
        try {
            mediabox = (HashMap<String, Object>) client.callEx("MediaBox.GetBySessionId", params);
        } catch (XMLRPCException e) {
            Log.e(TAG, "ERROR:" + e.getMessage());
        }
        
        return mediabox;
    }
    
    static public Object[] doMessage_GetAllMessageList(int mediaboxId, String msgType) {

        XMLRPCClient client;
        URI uri;
        
        uri = URI.create("http://192.168.174.70/rpc/Server.php");
        client = new XMLRPCClient(uri);
        
        Object[] params = new Object[] { mediaboxId, msgType };
        
        Object[] result = null;
        try {
            result = (Object[]) client.callEx("Message.GetAllMessageList", params);
        } catch (XMLRPCException e) {
            Log.e(TAG, "ERROR:" + e.getMessage());
        }
        
        return result;
    }

}

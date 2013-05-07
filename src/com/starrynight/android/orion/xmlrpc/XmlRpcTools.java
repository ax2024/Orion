package com.starrynight.android.orion.xmlrpc;

import java.net.URI;
import java.util.Date;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.util.Log;

import com.orion.android.R;
import com.starrynight.android.orion.OrionApplication;

public class XmlRpcTools {
    final static String TAG = OrionApplication.getApplicationTag() + "."
            + XmlRpcTools.class.getSimpleName();

    public XmlRpcTools() {
    }
    
    static public String doMediaBox_Login(String webLogin, String webPassword, String webAuthByAccessCode) {
        XMLRPCClient client;
        URI uri;
        String session = null;
        
        uri = URI.create(OrionApplication.getContext().getString(R.string.xml_rpc_server_uri));
        client = new XMLRPCClient(uri);
        
        Object[] params = new Object[] { webLogin, webPassword, webAuthByAccessCode };
        
        try {
            session = (String) client.callEx("MediaBox.Login", params);
        } catch (XMLRPCException e) {
            Log.e(TAG, "ERROR:" + e.getMessage());
            return "";
        }
        
        return session;
    }
    
    static public Object doMediaBox_GetBySessionId(String sessionId) {

        XMLRPCClient client;
        URI uri;
        
        uri = URI.create(OrionApplication.getContext().getString(R.string.xml_rpc_server_uri));
        client = new XMLRPCClient(uri);
        
        Object[] params = new Object[] { sessionId };
        
        Object mediabox = null;
        try {
            mediabox = client.callEx("MediaBox.GetBySessionId", params);
        } catch (XMLRPCException e) {
            Log.e(TAG, "ERROR:" + e.getMessage());
        }
        
        return mediabox;
    }
    
    static public Object[] doMessage_GetAllMessageList(int mediaboxId, String msgType) {

        XMLRPCClient client;
        URI uri;
        
        uri = URI.create(OrionApplication.getContext().getString(R.string.xml_rpc_server_uri));
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

    static public Object doMessage_GetMp3MessageFile(int msgId) {

        XMLRPCClient client;
        URI uri;
        
        uri = URI.create(OrionApplication.getContext().getString(R.string.xml_rpc_server_uri));
        client = new XMLRPCClient(uri);
        
        Object[] params = new Object[] { msgId };
        
        Object result = null;
        try {
            result = client.callEx("Message.GetMp3MessageFile", params);
        } catch (XMLRPCException e) {
            Log.e(TAG, "ERROR:" + e.getMessage());
        }
        
        return result;
    }
    
    static public Object doMessage_SendMessage(
            String mediaboxNum,
            Object messageFile,
            String format,
            String numOrigin,
            String callbackNum,
            Date expeditionDate,
            String sendNotif) {

        XMLRPCClient client;
        URI uri;
        
        uri = URI.create(OrionApplication.getContext().getString(R.string.xml_rpc_server_uri));
        client = new XMLRPCClient(uri);
        
        Object[] params = new Object[] { mediaboxNum, messageFile, format,
                numOrigin, callbackNum, expeditionDate, sendNotif };
        
        Object result = null;
        try {
            result = client.callEx("Message.SendMessage", params);
        } catch (XMLRPCException e) {
            Log.e(TAG, "ERROR:" + e.getMessage());
        }
        
        return result;
    }
    
    static public Object doMessage_Archive(
            int msgId,
            String sessionId,
            String SendMwi) {

        XMLRPCClient client;
        URI uri;
        
        uri = URI.create(OrionApplication.getContext().getString(R.string.xml_rpc_server_uri));
        client = new XMLRPCClient(uri);
        
        Object[] params = new Object[] { msgId, sessionId, SendMwi };
        
        Object result = null;
        try {
            result = client.callEx("Message.Archive", params);
        } catch (XMLRPCException e) {
            Log.e(TAG, "ERROR:" + e.getMessage());
        }
        
        return result;
    }
    
    static public Object doMessage_ArchiveWithoutAuthentification(
            int msgId, String SendMwi) {

        XMLRPCClient client;
        URI uri;
        
        uri = URI.create(OrionApplication.getContext().getString(R.string.xml_rpc_server_uri));
        client = new XMLRPCClient(uri);
        
        Object[] params = new Object[] { msgId, SendMwi };
        
        Object result = null;
        try {
            result = client.callEx("Message.ArchiveWithoutAuthentification", params);
        } catch (XMLRPCException e) {
            Log.e(TAG, "ERROR:" + e.getMessage());
        }
        
        return result;
    }
    
    static public Object doMessage_DeleteById(
            int msgId) {

        XMLRPCClient client;
        URI uri;
        
        uri = URI.create(OrionApplication.getContext().getString(R.string.xml_rpc_server_uri));
        client = new XMLRPCClient(uri);
        
        Object[] params = new Object[] { msgId };
        
        Object result = null;
        try {
            result = client.callEx("Message.DeleteById", params);
        } catch (XMLRPCException e) {
            Log.e(TAG, "ERROR:" + e.getMessage());
        }
        
        return result;
    }
}

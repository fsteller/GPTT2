package com.credomatic.grpod.ecommerce.android.gptt.testingtool.utilities.net;

import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.util.Properties;

/**
 * Created by fhernandezs on 11/06/2014.
 */
public class NetSFTP extends NetBase {

    private static final String TAG = NetSFTP.class.getSimpleName();
    private static final String CHANNEL = "sftp";

    private static enum OPERATION {PUT, GET, LS}

    public static String ls(final String usr, final String password,
                            final String src, final String host, final int port) {
        return sftp(OPERATION.LS, usr, password, src, null, host, port);
    }

    public static boolean putFile(final String usr, final String password,
                                  final String src, final String des,
                                  final String host, final int port) {
        return sftp(OPERATION.PUT, usr, password, src, des, host, port) != null;
    }

    public static boolean getFile(final String usr, final String password,
                                  final String src, final String des,
                                  final String host, final int port) {
        return sftp(OPERATION.GET, usr, password, src, des, host, port) != null;
    }

    private static String sftp(final OPERATION opt, final String usr, final String password,
                               final String src, final String des,
                               final String host, final int port) {

        String result = null;
        Session session = null;
        Channel channel = null;
        final Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");


        Log.i(TAG, String.format("Perform action '%s'.", opt.name()));

        try {
            final JSch ssh = new JSch();
            session = ssh.getSession(usr, host, port);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();

            Log.i(TAG, "Session is connected: " + session.isConnected());

            channel = session.openChannel(CHANNEL);
            channel.connect();

            Log.i(TAG, "Channel is connected: " + session.isConnected());

            final ChannelSftp sftp = (ChannelSftp) channel;
            switch (opt) {
                case GET:
                    sftp.get(src, des);
                    break;
                case PUT:
                    sftp.put(src, des);
                    break;
                case LS:
                    result = sftp.ls(src).toString();
                    break;
            }

            if (opt == OPERATION.GET || opt == OPERATION.PUT) {
                Log.i(TAG, String.format("Action '%s' from: %s to: %s succeed", opt.name(), src, des));
                result = "Succeed";
            }

        } catch (final JSchException e) {
            e.printStackTrace();
        } catch (final SftpException e) {
            e.printStackTrace();
        } finally {
            if (channel != null && channel.isConnected())
                channel.disconnect();
            if (session != null && session.isConnected())
                session.disconnect();
        }
        return result;
    }

}

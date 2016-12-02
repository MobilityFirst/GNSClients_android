package android.gns.umass.edu.gnsclient.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by akarthik10 on 12/2/16.
 */

public final class GNSClientPreferences {

    private static final String PREF_NAME = "GNS_CLIENT_PREFS";
    public static String getServer(Context context) {
        SharedPreferences prefs = context.getSharedPreferences( PREF_NAME, MODE_PRIVATE);
        String serverAddress = prefs.getString("SERVER_ADDRESS", "127.0.0.1");
        return serverAddress;


    }

    public static void putServer(Context context, String server) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString("SERVER_ADDRESS", server);
        editor.apply();
    }
}

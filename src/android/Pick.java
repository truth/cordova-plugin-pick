package com.llzc.pick;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import com.ionicframework.lianlianapp759296.R;
import com.llzc.pick.WheelView;
import android.app.Activity;
import java.util.Arrays;
import android.os.Message;
import android.os.Handler;
/**
 * This class echoes a string called from JavaScript.
 */
public class Pick extends CordovaPlugin{

    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};
    
    private static final String TAG = "Wang";
    private WheelView wv;
    private Runnable runnable = new Runnable() {
        public void run() {
            Log.d(TAG, "[activity]selectedIndex: ");
            Activity activity = cordova.getActivity();
            /*
            wva = new WheelView(activity.getApplicationContext());//(WheelView) findViewById(R.id.main_wv);
            activity.addContentView(wva,  new LinearLayout.LayoutParams(320,640));
            wva.setOffset(1);
            wva.setItems(Arrays.asList(PLANETS));
            wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                }
            });
            */
            View outerView = LayoutInflater.from(cordova.getActivity()).inflate(R.layout.wheel_view, null);
            WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
            wv.setOffset(2);
            wv.setItems(Arrays.asList(PLANETS));
            wv.setSeletion(3);
            wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                }
            });

            new AlertDialog.Builder(activity)
                    .setTitle("WheelView in Dialog")
                    .setView(outerView)
                    .setPositiveButton("OK", null)
                    .show();
        }
    };
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("echo")) {
            String message = args.getString(0);
            this.echo(message, callbackContext);
            return true;
        }
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
        
        cordova.getActivity().runOnUiThread(runnable);
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

}

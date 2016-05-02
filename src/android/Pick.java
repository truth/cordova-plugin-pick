package com.llzc.pick;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import org.apache.cordova.PluginResult;

import java.util.List;
/**
 * This class echoes a string called from JavaScript.
 */
public class Pick extends CordovaPlugin{

    //private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};
    private static final String[] PLANETS = new String[]{"1983","1984","1985","1986","1987","1988"};
    private static final String[] months = new String[]{"83","84","85","86","87","88"};
    
    private static final String TAG = "Wang";
    private WheelView wv;
    private CallbackContext callback;
    private String[] result;
    private List[] options;
    private int[] optIndexs;
    private String title;
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
            LinearLayout line = (LinearLayout )outerView;
            WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
            LinearLayout.LayoutParams  lp = ( LinearLayout.LayoutParams)wv.getLayoutParams();


            for (int i=0;i<result.length-1;i++) {
                WheelView tmp = new WheelView(activity);
                tmp.setLayoutParams(lp);
                line.addView(tmp);
                tmp.setOffset(2);
                tmp.setIndex(i+1);
                tmp.setItems(options[1+i]);
                tmp.setSeletion(optIndexs[i+1]);
                WheelView.OnWheelViewListener wvl = new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        result[index] = item;
                        Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                    }
                };
                wvl.index=i+1;
                tmp.setOnWheelViewListener(wvl);
            }

            wv.setOffset(2);
            wv.setItems(options[0]); //Arrays.asList(PLANETS)
            wv.setSeletion(optIndexs[0]);
            wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    //result = item;
                    result[0] = item;
                    //callbackContext.success(item);
                    Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                }
            });

            AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
            dlg.setTitle(title);
            dlg.setView(outerView);
            //dlg.setNegativeButton(buttonLabels.getString(0),
            dlg.setPositiveButton("OK",
                new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callback.sendPluginResult(new PluginResult(PluginResult.Status.OK, toStr(result)));
                    }
                });

            dlg.show();
        }
    };
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("echo")) {
            title = args.getString(0);
            String message = args.getString(1);
            String indexs = args.getString(2);
            options = getList(message,indexs);
            this.echo(message, callbackContext);
            return true;
        }
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
        
        this.callback = callbackContext;
        cordova.getActivity().runOnUiThread(runnable);
        if (message != null && message.length() > 0) {
            //callbackContext.success(message);
        } else {
            //callbackContext.error("Expected one non-empty string argument.");
        }
    }

    public List[] getList(String msg,String indexs) {
        List[] strs;
        String[] opt = msg.split(";");
        strs = new List[opt.length];
        result = new String[opt.length];
        optIndexs = new int[opt.length];
        String[] idx = null;
        if(indexs!=null)
        {
            idx = indexs.split(",");
        }
        for(int i=0;opt!=null&& i<opt.length;i++) {
            strs[i] = Arrays.asList(opt[i].split(","));
            if(idx!=null && idx.length>=i){
                optIndexs[i] = strs[i].indexOf(idx[i]);
                result[i] = idx[i];
            }    
            else
                optIndexs[i]=0;
        }
        return strs;
    }
    public String toStr(String[] result) {
        String str ="";
        for (int i = 0; i < result.length; i++) {
            if(i!=result.length-1)
                str+=result[i]+",";
            else
                str+=result[i];
        }
        return str;
    }
}

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
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import android.os.Message;
import android.os.Handler;
import org.apache.cordova.PluginResult;
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
    private LinearLayout line;
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
            line = (LinearLayout )outerView;
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
                        if((index==1|| index==2) && result.length>2) {
                            int year = Integer.parseInt(result[0]);
                            int month = Integer.parseInt(result[1]);
                            WheelView wView = (WheelView)line.getChildAt(2);
                            wView.setItems(getItems(getMonthDay(year,month),0));
                        }
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

            AlertDialog.Builder dlg = new AlertDialog.Builder(activity,R.style.dialog);//R.style.dialog
            dlg.setTitle(title);
            dlg.setView(outerView);
            //dlg.setNegativeButton(buttonLabels.getString(0),
            dlg.setPositiveButton("确定",
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
        if(msg.matches("\\d+")) {
            Calendar calendar  = Calendar.getInstance();
            Date date = new Date(Long.parseLong(msg));
            calendar.setTime(date);
            strs = new List[5];
            result = new String[5];
            optIndexs = new int[5];
            int n = calendar.get(Calendar.YEAR);
            strs[0] = getItems(3,n-1);result[0] = n+"";optIndexs[0]=0;
            n = calendar.get(Calendar.MONTH);
            strs[1] = getItems(12,0);result[1] = (n+1)+"";optIndexs[1]=n;
            n = calendar.get(Calendar.DAY_OF_MONTH);
            strs[2] = getItems(30,0);result[2] = n+"";optIndexs[2]=n-1;
            n = calendar.get(Calendar.HOUR_OF_DAY);
            strs[3] = getItems(24,-1);result[3] = n+"";optIndexs[3]=n;
            n = calendar.get(Calendar.MINUTE);
            strs[4] = getItems(60,-1);result[4] = n+"";optIndexs[4]=n;
        }else {
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
    public boolean isLeapYear(int year) {  
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);  
    }  
    public int getMonthDay(int year ,int month) {
        if(month==2) return isLeapYear(year)?28:29;
        if(month==1 || month ==3 || month==5 && month==7 || month==8 || month==10 || month==12) return 31;
        return 30;
    }
    public List<String> getItems(int size,int offset) {
        List<String> list =  new ArrayList<String>();
        for (Integer i=1;i<=size;i++) {
            Integer n = i+offset;
            list.add(n.toString());
        }
        return list;
    }
}

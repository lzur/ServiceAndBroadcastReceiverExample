package com.froger.servicespresentation;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ClientActivity extends Activity {
	private Button btnStartService;
	private Button btnStopService;
	
	private TextView tvServiceStatusStaticField;
	private TextView tvServiceStatusServiceInfo;
	
	private TextView tvReceiverData;
	private SampleReceiver myReceiver;
	//Action for Intent Filter and Broadcast Receiver
	public static final String NEW_MSG = "com.froger.servicespresentation.NEW_MSG";
	//Button Listeners
	private OnClickListener startServiceListener = new OnClickListener() {		
		@Override
		public void onClick(View arg0) {
			startService(new Intent(ClientActivity.this, MyService.class));
		}
	};
	private OnClickListener stopServiceListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			stopService(new Intent(ClientActivity.this, MyService.class));
		}
	};
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Layout
        setContentView(R.layout.main);
        btnStartService = (Button)findViewById(R.id.btnStartService);
        btnStopService  = (Button)findViewById(R.id.btnStopService);
        tvReceiverData  = (TextView)findViewById(R.id.tvReceiverData);
        tvServiceStatusStaticField = (TextView)findViewById(R.id.tvServiceStatusStaticField);
        tvServiceStatusServiceInfo = (TextView)findViewById(R.id.tvServiceStatusServiceInfo);
        //Listeners
        btnStartService.setOnClickListener(startServiceListener);
        btnStopService.setOnClickListener(stopServiceListener);
        //BroadcastReceiver
		myReceiver = new SampleReceiver();
		IntentFilter filter = new IntentFilter(NEW_MSG);
		registerReceiver(myReceiver, filter);
		
		if(MyService.isServiceAlive()) 
			tvServiceStatusStaticField
			.setText("Service started (static field)? - true");
		else						   
			tvServiceStatusStaticField
			.setText("Service started (static field)? - false");
		
		ActivityManager activityManager = 
			(ActivityManager)getSystemService(ACTIVITY_SERVICE);
		List<RunningServiceInfo> services = 
			activityManager.getRunningServices(Short.MAX_VALUE);
		boolean isServiceAlive = false;
		String serviceName = MyService.class.getName();
		
		for (RunningServiceInfo runningServiceInfo : services) {
			if(runningServiceInfo.service.getClassName().equals(serviceName))
				isServiceAlive = true;
		}
		
		if(isServiceAlive) 
			tvServiceStatusServiceInfo
			.setText("ServiceStarted? (RunningServiceInfo) - true");
		else						   
			tvServiceStatusServiceInfo
			.setText("ServiceStarted? (RunningServiceInfo) - false");
		
    }
    
	@Override
	protected void onDestroy() {
		unregisterReceiver(myReceiver);
		super.onDestroy();
	}

	private void showDataFromIntent(Intent intent) {
		tvReceiverData.setText(intent.getStringExtra("ReceiverData"));
	}
    
	private class SampleReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			showDataFromIntent(intent);
		}
	}
}
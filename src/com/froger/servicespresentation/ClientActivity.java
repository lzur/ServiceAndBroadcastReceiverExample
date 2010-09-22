package com.froger.servicespresentation;

import android.app.Activity;
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
        //Listeners
        btnStartService.setOnClickListener(startServiceListener);
        btnStopService.setOnClickListener(stopServiceListener);
        //BroadcastReceiver
		myReceiver = new SampleReceiver();
		IntentFilter filter = new IntentFilter(NEW_MSG);
		registerReceiver(myReceiver, filter);
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
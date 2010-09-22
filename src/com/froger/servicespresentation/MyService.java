package com.froger.servicespresentation;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

	private Toast myToast;
	private int counter = 1;
	
	private Timer updatingTimer;
	private TimerTask notify = new TimerTask() {
		@Override
		public void run() {
			myToast.setText("Service still working");
			myToast.show();
			
			sendMsg((++counter) + ") Message from background service");
		}
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		updatingTimer = new Timer();
		
		myToast = Toast.makeText(getApplicationContext(), 
								 "Service started", 
								 Toast.LENGTH_SHORT);
		myToast.show();
		
		sendMsg("Background Service started");
	}

	@Override
	public void onDestroy() {
		updatingTimer.cancel();
		
		myToast.setText("Service stopped");
		myToast.show();
		
		sendMsg("Backgrund Service stopped");
		
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		updatingTimer.scheduleAtFixedRate(notify, 5*1000, 5*1000);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	private void sendMsg(String msg) {
		Intent intent = new Intent(ClientActivity.NEW_MSG);
		intent.putExtra("ReceiverData", msg);
		sendBroadcast(intent);
	}

}
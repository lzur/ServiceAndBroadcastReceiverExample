package com.froger.servicespresentation;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
	private static boolean serviceAlive = false;
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
		updatingTimer.scheduleAtFixedRate(notify, 5*1000, 5*1000);
		serviceAlive = true;
		
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
		serviceAlive = false;
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void sendMsg(String msg) {
		Intent intent = new Intent(ClientActivity.NEW_MSG);
		intent.putExtra("ReceiverData", msg);
		sendBroadcast(intent);
	}

	public static boolean isServiceAlive() {
		return serviceAlive;
	}

}
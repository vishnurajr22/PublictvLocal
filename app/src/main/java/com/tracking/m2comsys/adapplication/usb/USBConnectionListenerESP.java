
package com.tracking.m2comsys.adapplication.usb;


import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class USBConnectionListenerESP
{

	Activity parentActivity;
	Handler handler1;


	public Timer UsbDeviceDetectTimer;
	public void StartUSBServer(Activity activity)
	{
		parentActivity=activity;	

		ConnectUSB();
	}
	
	 UsbControllerESP usbController;
	 public void ConnectUSB()
	 {
		 try
		 {
	     	usbController= 	new UsbControllerESP(parentActivity);
	    	UsbDeviceDetectTask UsbDeviceDetectTask = new UsbDeviceDetectTask();
	    	UsbDeviceDetectTimer = new Timer();
	    	timecnt=0;
	        UsbDeviceDetectTimer.scheduleAtFixedRate(UsbDeviceDetectTask,100,1000); 
    	    handler1=new Handler();
	     }
		 catch (Exception e)
		 {
			Log.i("ConnectUSB","Caught:"+e);
		 }
	 }
	
	 
	 void ShowHandler()
	 {
		 handler1.post(new Runnable()
		 {
			//@Override
			public void run()
			{
				handler1.post(new Runnable() {
					
					public void run() 
					{
						

							if(!usbController.UsbCmdIsEmpty())
							{								
								//UsbRead.append(UsbController.UsbCmdRead()+"\r\n");
							}
					}
				});
			}
		});
	 }
	 
	 int timecnt;
    class UsbDeviceDetectTask extends TimerTask
   	{
       @Override
       public void run()
   	   {	  	   
	      ShowHandler();   
	      timecnt++;
	      if(timecnt==300)
	      {
	    	  usbController.SendDataToDevice("0 0\r\n");
	    	  timecnt=0;
	      }
   	   }
   	}
    


	
}


package com.tracking.m2comsys.adapplication.usb;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.tracking.m2comsys.adapplication.utils.LogWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TimerTask;

public class UsbControllerESP
{
	public Context context;
	Activity activity;
	private String VID_PID = "10C4:EA60";
	private String VID1_PID1 = "1027:24597";
	//"0403:6015";
	protected static final String ACTION_USB_PERMISSION = "ch.serverbox.android.USB";
	private boolean 	UsbSendReceiveStatus = false;
	private boolean		UsbConnectionStatus = false; 
	private boolean     UsbPermissionStatus = false;

	private static final int DEFAULT_BAUD_RATE = 9600;
	private static final int REQTYPE_HOST_TO_DEVICE = 0x41;
	private static final int USB_WRITE_TIMEOUT_MILLIS = 5000;
	private static final int SILABSER_IFC_ENABLE_REQUEST_CODE = 0x00;
	private static final int SILABSER_SET_BAUDDIV_REQUEST_CODE = 0x01;
	private static final int SILABSER_SET_LINE_CTL_REQUEST_CODE = 0x03;
	private static final int SILABSER_SET_MHS_REQUEST_CODE = 0x07;
	private static final int SILABSER_SET_BAUDRATE = 0x1E;
	private static final int SILABSER_FLUSH_REQUEST_CODE = 0x12;

	private static final int MCR_DTR = 0x0001;
	private static final int MCR_RTS = 0x0002;
	private static final int MCR_ALL = 0x0003;


	private static final int UART_ENABLE = 0x0001;
	private static final int UART_DISABLE = 0x0000;

	private static final int CONTROL_WRITE_DTR = 0x0100;
	private static final int CONTROL_WRITE_RTS = 0x0200;

	/** 5 data bits. */
	public static final int DATABITS_5 = 5;

	/** 6 data bits. */
	public static final int DATABITS_6 = 6;

	/** 7 data bits. */
	public static final int DATABITS_7 = 7;

	/** 8 data bits. */
	public static final int DATABITS_8 = 8;

	/** No flow control. */
	public static final int FLOWCONTROL_NONE = 0;

	/** RTS/CTS input flow control. */
	public static final int FLOWCONTROL_RTSCTS_IN = 1;

	/** RTS/CTS output flow control. */
	public static final int FLOWCONTROL_RTSCTS_OUT = 2;

	/** XON/XOFF input flow control. */
	public static final int FLOWCONTROL_XONXOFF_IN = 4;

	/** XON/XOFF output flow control. */
	public static final int FLOWCONTROL_XONXOFF_OUT = 8;

	/** No parity. */
	public static final int PARITY_NONE = 0;

	/** Odd parity. */
	public static final int PARITY_ODD = 1;

	/** Even parity. */
	public static final int PARITY_EVEN = 2;

	/** Mark parity. */
	public static final int PARITY_MARK = 3;

	/** Space parity. */
	public static final int PARITY_SPACE = 4;

	/** 1 stop bit. */
	public static final int STOPBITS_1 = 1;

	/** 1.5 stop bits. */
	public static final int STOPBITS_1_5 = 3;

	/** 2 stop bits. */
	public static final int STOPBITS_2 = 2;


	/*
     * SILABSER_SET_BAUDDIV_REQUEST_CODE
     */
	private static final int BAUD_RATE_GEN_FREQ = 0x384000;

	boolean IsRegisteringSensor;
	boolean rfEnrollSuccess;
	boolean isReceivingValue;
	
	String ReceivedMessage;

	UsbDataRecvd dataSink;
	private LinkedList<String> ReadfifoBuffer;
	private LinkedList<String> WritefifoBuffer;
	public static UsbDevice HAUsbDevice = null;
	UsbDevice dv;
	
	UsbManager usbManager;
	HashMap<String, UsbDevice> devlist;
	Iterator<UsbDevice> deviter;
	PendingIntent pendingIntent;

	static Object lockWrite = new Object();
	//LogFile logfile;
	
	public UsbControllerESP(Context curContext)
	{
		try
		{
	    	context = curContext;
		

        	UsbSendReceiveStatus = false;
        	UsbConnectionStatus = false;   
      	
            IntentFilter filter = new IntentFilter();
            filter.addAction(UsbManager.EXTRA_PERMISSION_GRANTED);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            context.registerReceiver(UsbDeviceDetect, filter);
          
  	    	ReadfifoBuffer = new LinkedList<String>();
  	    	WritefifoBuffer = new LinkedList<String>();

		}
		catch (Exception e)
		{
			Log.i("UsbController","Caught:"+e);
		}
	}

	//1- Pening init
	//2- Success
	//-1 - Failed
	public int InitUSB(UsbDataRecvd dataRecvd)
	{

			if (UsbConnectionStatus) return  2; //already connected
			usbManager = (UsbManager) context.getSystemService(context.USB_SERVICE);
			devlist = usbManager.getDeviceList();
		    if(devlist.size()==0) return -1; //device not attched

			dataSink=dataRecvd;

			deviter = devlist.values().iterator();
			pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);

			while (deviter.hasNext()) {
				dv = deviter.next();
				if ((String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID_PID))||(String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID1_PID1)))

				{
					l("HA usb Device Connected");
					UsbConnectionStatus = true;


					context.registerReceiver(mUsbReceiver, new IntentFilter(ACTION_USB_PERMISSION));
					//usbManager.

					if (!usbManager.hasPermission(dv)) {

						usbManager.requestPermission(dv, pendingIntent);
						//	  usbManager.re
						return 1;  //pending init

					} else {
						if(!UsbReadWriteOnStart(dv)) return -1;
						else return 2;
					}

				}


			}
		return -1;// device not fornd in connected dev list
	}
	
	public boolean UsbCmdIsEmpty()
	{
		return (ReadfifoBuffer.isEmpty());
	}
	

	public String ReadDataFromDevice()
	{
		if(!ReadfifoBuffer.isEmpty())
			return ReadfifoBuffer.remove();
		return null;
	}
	
	public boolean SendDataToDevice(String message)
	{
		if(WritefifoBuffer.add(message)) 
		{
			return true;
		}
		return false;
	}
	
	@SuppressLint("NewApi")
	public synchronized void SendEmergencyMessage(String Message)
	{
		synchronized (lockWrite) {
			if(UsbConnection1==null) {
				LogWriter.writeLogUSB("USBESP","Connection object null");
				return;
			}
			byte[] SendBuffer = new byte[100];
			char tChar;
			int count = Message.length();
			for (count = 0; count < Message.length(); count++) {
				tChar = Message.charAt(count);
				SendBuffer[0] = (byte) tChar;
				UsbConnection1.bulkTransfer(epOUT1, SendBuffer, 1, 500);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean UsbDeviceStatus()
	{
		return UsbConnectionStatus;
	}

	@TargetApi(12)
	private boolean UsbReadWriteOnStart(UsbDevice d) {

		boolean usbOK;
		if(UsbConnectionStatus==true)
		{
			HAUsbDevice  = d;
			if(InitializeUSBChip()) {
				new Thread(UsbReadWriteOnRun).start();
				new Thread(usbWriteThread).start();
				new Thread(UsbRecvdMesgDispacher).start();
				return true;
			}
		}
		return false;
	}
	
	void SendMessageToAndroid(String Data)
	{
		try
		{
     		Data="at+rf "+Data;

		}
		catch (Exception e)
		{
			Log.i("SendMessageToAndroid","Caught:"+e);
		}
	}



	private Runnable usbWriteThread = new Runnable() {
		@Override
		public void run()
		{
			try {
				while (true) {
					String str;
					if (!WritefifoBuffer.isEmpty()) {
						int lenght = WritefifoBuffer.size();
				/*if(lenght>10)
				{
					while(WritefifoBuffer.size()>10)
						WritefifoBuffer.remove();
				}*/
						Log.i("WriteUSB", "Size is " + WritefifoBuffer.size());
						str = WritefifoBuffer.remove();
						Log.i("WriteUSB", "Send:" + str);

						isCommandSend = true;
						trycount = 0;

						SendEmergencyMessage(str);

					} else {
						try {
							Thread.sleep(800);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}catch(Exception exp){
				LogWriter.writeLogException("USB",exp);
			}
		}
	};
	private void setBaudRate(int baudRate) throws IOException {
		byte[] data = new byte[] {
				(byte) ( baudRate & 0xff),
				(byte) ((baudRate >> 8 ) & 0xff),
				(byte) ((baudRate >> 16) & 0xff),
				(byte) ((baudRate >> 24) & 0xff)
		};
		int ret = UsbConnection.controlTransfer(REQTYPE_HOST_TO_DEVICE, SILABSER_SET_BAUDRATE,
				0, 0, data, 4, USB_WRITE_TIMEOUT_MILLIS);
		if (ret < 0) {
			throw new IOException("Error setting baud rate.");
		}
	}


	public void setParameters(int baudRate, int dataBits, int stopBits, int parity)
			throws IOException {
		setBaudRate(baudRate);

		int configDataBits = 0;
		switch (dataBits) {
			case DATABITS_5:
				configDataBits |= 0x0500;
				break;
			case DATABITS_6:
				configDataBits |= 0x0600;
				break;
			case DATABITS_7:
				configDataBits |= 0x0700;
				break;
			case DATABITS_8:
				configDataBits |= 0x0800;
				break;
			default:
				configDataBits |= 0x0800;
				break;
		}

		switch (parity) {
			case PARITY_ODD:
				configDataBits |= 0x0010;
				break;
			case PARITY_EVEN:
				configDataBits |= 0x0020;
				break;
		}

		switch (stopBits) {
			case STOPBITS_1:
				configDataBits |= 0;
				break;
			case STOPBITS_2:
				configDataBits |= 2;
				break;
		}
		setConfigSingle(SILABSER_SET_LINE_CTL_REQUEST_CODE, configDataBits);
	}
	private int setConfigSingle(int request, int value) {
		return UsbConnection.controlTransfer(REQTYPE_HOST_TO_DEVICE, request, value,
				0, null, 0, USB_WRITE_TIMEOUT_MILLIS);
	}
	UsbDeviceConnection UsbConnection1;
	UsbEndpoint epOUT1;
	boolean isCommandSend=false;
	int trycount=0;
	UsbEndpoint epIN = null;
	UsbEndpoint epOUT = null;
	UsbDeviceConnection UsbConnection;
	boolean InitializeUSBChip()
	{
		if(UsbConnectionStatus==false)return false;
		UsbDevice dv = HAUsbDevice ;

		int count;
		char tChar;

		if (dv == null)return false;
		UsbSendReceiveStatus  =  true;
		isCommandSend=false;
		UsbManager usbm = (UsbManager) context.getSystemService(context.USB_SERVICE);
		UsbConnection = usbm.openDevice(dv);
		UsbConnection1=UsbConnection;
		l("UsbConnected in " + String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()));

		trycount=0;
		if(!UsbConnection.claimInterface(dv.getInterface(0), true))return false;

		UsbConnection.controlTransfer(0x40, 0, 0, 0, null, 0, 0);//reset
		UsbConnection.controlTransfer(0x40, 0, 1, 0, null, 0, 0);//clear Rx
		UsbConnection.controlTransfer(0x40, 0, 2, 0, null, 0, 0);//clear Tx
		UsbConnection.controlTransfer(0x40, 0x03, 0x4138, 0, null, 0, 0);//baudrate 9600

		setConfigSingle(SILABSER_IFC_ENABLE_REQUEST_CODE, UART_ENABLE);
		setConfigSingle(SILABSER_SET_MHS_REQUEST_CODE, MCR_ALL | CONTROL_WRITE_DTR | CONTROL_WRITE_RTS);
		setConfigSingle(SILABSER_SET_BAUDDIV_REQUEST_CODE, BAUD_RATE_GEN_FREQ / DEFAULT_BAUD_RATE);

		try {
			setParameters(115200, DATABITS_8, STOPBITS_2, PARITY_NONE);
		}catch (Exception exp){

		}

		UsbInterface usbInterface = dv.getInterface(0);
		for(int i = 0; i < usbInterface.getEndpointCount(); i++)
		{
			if(usbInterface.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
			{
				if(usbInterface.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN)
					epIN = usbInterface.getEndpoint(i);
				else
				{
					epOUT = usbInterface.getEndpoint(i);
					epOUT1=epOUT;
				}

			}
		}

		if((epOUT!=null)&&(epIN!=null)) return true;
		else return  false;
	}
	private Runnable UsbRecvdMesgDispacher = new Runnable() {
		@Override
		public void run()
		{
			while(true)
			{
				try {
					while (true) {
						synchronized (ReadfifoBuffer) {
							try {
								ReadfifoBuffer.wait();
							} catch (InterruptedException exp) {
								LogWriter.writeLogUSB("USB", "Read exception");
							}
							try {
								if (!ReadfifoBuffer.isEmpty()) {
									String cmd = ReadfifoBuffer.remove();
									Log.e("Dispatching cmd->", cmd);
									dataSink.ReceivedDataSink(cmd);
								}
							} catch (Exception exp) {
								LogWriter.writeLogUSB("USB", exp.getMessage());
							}
						}
					}
				}catch (Exception exp){
					LogWriter.writeLogUSB("USB", exp.getMessage());
				}
			}
		}
	};
	private Runnable UsbReadWriteOnRun = new Runnable() {

		//@Override
		@TargetApi(12)
		public void run() 
		{
			try {
				byte[] ReceiveBuffer = new byte[100];
				byte[] ProcessBuffer = new byte[1000];
				String str1 = "";

				int readLen = 0;
				String str = "";
				StringBuilder strBlder = new StringBuilder();
				for (; ; ) {
					ProcessBuffer = new byte[1000];
					try {
						if (UsbConnectionStatus == false) {
							UsbSendReceiveStatus = false;
							return;
						}

						boolean endFlagRead = false;
						int locInProcessBuf = 0;
						boolean startFilling = false;
						do {
							ReceiveBuffer = new byte[100];
							readLen = UsbConnection.bulkTransfer(epIN, ReceiveBuffer, 100, 500);
							if ((readLen == -1) && startFilling) break;
							if (readLen == -1) continue;
							;
							startFilling = true;
							//if(readLen<=2) { endFlagRead=true; break;}
							if (readLen > 0) {
								System.arraycopy(ReceiveBuffer, 0, ProcessBuffer, locInProcessBuf, readLen);
								String cmd2 = new String(ProcessBuffer);
								LogWriter.writeLogUSB("USB", cmd2);
								locInProcessBuf += (readLen);
							}

						}
						while (!endFlagRead);

						String cmd1 = new String(ProcessBuffer);
						LogWriter.writeLogUSB("USB", cmd1);
						boolean endFound = false;
						int lastPos = 0;
						if (locInProcessBuf > 0) {
							for (int i = 0; i < locInProcessBuf; i++) {
								if ((ProcessBuffer[i] == '\r') | (ProcessBuffer[i] == '\n')) {
									try {
										if ((i - lastPos) == 1) {
											lastPos = i;
											continue;
										}
										byte[] temp = new byte[200];
										System.arraycopy(ProcessBuffer, lastPos, temp, 0, (i - lastPos));
										//strBlder.append(ProcessBuffer,lastPos,i);
										String cmd = new String(temp);
										cmd = cmd.trim();
										synchronized (ReadfifoBuffer) {
											ReadfifoBuffer.add(cmd);
											ReadfifoBuffer.notify();
										}

										lastPos = i;
									} catch (Exception exp) {
										LogWriter.writeLogUSB("USB Recv", "Exception->" + exp.getMessage());
									}

								}
							}

						}

					} catch (Exception exp) {
						exp.printStackTrace();
						Log.e("USBSendRecvThread", exp.getMessage());
					}
				}
			}catch(Exception exp){
				LogWriter.writeLogException("USB",exp);
			}
		}
	};
	
	String LastSendData="";
	
	int usbnotRespondingCount=0;
	
	BroadcastReceiver UsbDeviceDetect = new BroadcastReceiver() {
		String deviceVID_PID;
		@TargetApi(12)
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				String action = intent.getAction();
				if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
					try {
						UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
						if ((String.format("%04X:%04X", device.getVendorId(), device.getProductId()).equals(VID_PID)) || (String.format("%04X:%04X", device.getVendorId(), device.getProductId()).equals(VID1_PID1))) {
							UsbConnectionStatus = false;
							WritefifoBuffer.clear();

						}
					} catch (Exception exp) {

					}
					sendStatus(context, CommonDataArea.REQUEST_USB_STATUS, CommonDataArea.REQUEST_STATUS_FAIL, "Device Not Connected");

				}
				if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
					UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if ((String.format("%04X:%04X", device.getVendorId(), device.getProductId()).equals(VID_PID)) || (String.format("%04X:%04X", device.getVendorId(), device.getProductId()).equals(VID1_PID1))) {
						UsbConnectionStatus = true;
						if (!usbManager.hasPermission(device)) {

							usbManager.requestPermission(device, pendingIntent);


						} else {
							if (!UsbReadWriteOnStart(device))
								sendStatus(context, CommonDataArea.REQUEST_USB_STATUS, CommonDataArea.REQUEST_STATUS_FAIL, "USB device not found");

							else
								sendStatus(context, CommonDataArea.REQUEST_USB_STATUS, CommonDataArea.REQUEST_STATUS_SUCCESS, "Device Connected");

						}
					}

				}
			}catch(Exception exp){
			    LogWriter.writeLogException("USB",exp);
            }
		}
	};
	private void sendStatus(Context context, int cmd , int status, String statusMesg)
	{
		LogWriter.writeLogUSB("ConnectionStstusEevent", "Con status esg from service ->" + status);
		Intent intent = new Intent(CommonDataArea.NOTIFICATIONTOSERVICE);
		intent.putExtra(CommonDataArea.MESGPARAM_REQUEST_CMD, cmd);
		intent.putExtra(CommonDataArea.MESGPARAM_STATUS_CMDSTATUSVAL, status);
		intent.putExtra(CommonDataArea.MESGPARAM_STATUS_CMDSTATUSMESG, statusMesg);

		context.sendBroadcast(intent);
	}
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		
		@TargetApi(12)
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (intent.getAction().equals(ACTION_USB_PERMISSION))
			{
				if (!intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
				{
					e("HA usb Device User Permission not granted :(");
				} 
				else
				{
					l("HA usb Device User Permission granted");
					UsbDevice dv = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (dv != null) 
					{
						if ((String.format("%04X:%04X", dv.getVendorId(),dv.getProductId()).equals(VID_PID))||(String.format("%04X:%04X", dv.getVendorId(),dv.getProductId()).equals(VID1_PID1)))
						{
							if(!UsbReadWriteOnStart(dv))
							{
								sendStatus(context, CommonDataArea.REQUEST_USB_STATUS, CommonDataArea.REQUEST_STATUS_FAIL,"USB device not found");

							}
							else
							{
								sendStatus(context, CommonDataArea.REQUEST_USB_STATUS, CommonDataArea.REQUEST_STATUS_SUCCESS,"USB device not found");

							}
						}
						else
						{
							e("HA usb Device device not present!");	
						}
					}
				}	
				
			}			
		}
	};
	

class UsbDeviceDetectTask extends TimerTask {
	String deviceVID_PID;

	@TargetApi(12)
	@Override
	public void run() {
		if (UsbConnectionStatus) return;
		usbManager = (UsbManager) context.getSystemService(context.USB_SERVICE);
		devlist = usbManager.getDeviceList();
		deviter = devlist.values().iterator();
		pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
		while (deviter.hasNext()) {
			dv = deviter.next();
			if ((String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID_PID))||(String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID1_PID1))) {
				l("HA usb Device Connected");
				UsbConnectionStatus = true;


				context.registerReceiver(mUsbReceiver, new IntentFilter(ACTION_USB_PERMISSION));
				//usbManager.

				if (!usbManager.hasPermission(dv)) {

					usbManager.requestPermission(dv, pendingIntent);
					//	  usbManager.re
					UsbReadWriteOnStart(dv);
				} else {
					UsbReadWriteOnStart(dv);
				}
				return;
			}


		}
	}


}

private static void l(Object s) {
	Log.i("Ebird_USB", ">= " + s.toString() + " =<");
}

private static void e(Object s) {
	Log.e("Ebird_USB", ">= " + s.toString() + " =<");
}

	public static abstract class UsbDataRecvd
	{
		public abstract void ReceivedDataSink(String dataStr);
	}

}

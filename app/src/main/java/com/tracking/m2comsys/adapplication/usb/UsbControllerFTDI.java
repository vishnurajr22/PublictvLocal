
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

public class UsbControllerFTDI
{
	public Context context;
	Activity activity;
	//private String VID_PID = "0403:6015";//DTDI old
	private String VID_PID = "0403:6001";//Bijoy Dev FTDI module

	protected static final String ACTION_USB_PERMISSION = "ch.serverbox.android.USB";
	private boolean 	UsbSendReceiveStatus = false;
	private boolean		UsbConnectionStatus = false;
	private boolean     UsbPermissionStatus = false;

	private static final int DEFAULT_BAUD_RATE = 9600;
	private static final int REQTYPE_HOST_TO_DEVICE = 0x41;

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

	public static final int USB_TYPE_STANDARD = 0x00 << 5;
	public static final int USB_TYPE_CLASS = 0x00 << 5;
	public static final int USB_TYPE_VENDOR = 0x00 << 5;
	public static final int USB_TYPE_RESERVED = 0x00 << 5;

	public static final int USB_RECIP_DEVICE = 0x00;
	public static final int USB_RECIP_INTERFACE = 0x01;
	public static final int USB_RECIP_ENDPOINT = 0x02;
	public static final int USB_RECIP_OTHER = 0x03;

	public static final int USB_ENDPOINT_IN = 0x80;
	public static final int USB_ENDPOINT_OUT = 0x00;

	public static final int USB_WRITE_TIMEOUT_MILLIS = 5000;
	public static final int USB_READ_TIMEOUT_MILLIS = 5000;

	// From ftdi.h
	/**
	 * Reset the port.
	 */
	private static final int SIO_RESET_REQUEST = 0;

	/**
	 * Set the modem control register.
	 */
	private static final int SIO_MODEM_CTRL_REQUEST = 1;

	/**
	 * Set flow control register.
	 */
	private static final int SIO_SET_FLOW_CTRL_REQUEST = 2;

	/**
	 * Set baud rate.
	 */
	private static final int SIO_SET_BAUD_RATE_REQUEST = 3;

	/**
	 * Set the data characteristics of the port.
	 */
	private static final int SIO_SET_DATA_REQUEST = 4;

	private static final int SIO_RESET_SIO = 0;
	private static final int SIO_RESET_PURGE_RX = 1;
	private static final int SIO_RESET_PURGE_TX = 2;

	public static final int FTDI_DEVICE_OUT_REQTYPE =
			UsbConstants.USB_TYPE_VENDOR | USB_RECIP_DEVICE | USB_ENDPOINT_OUT;

	public static final int FTDI_DEVICE_IN_REQTYPE =
			UsbConstants.USB_TYPE_VENDOR | USB_RECIP_DEVICE | USB_ENDPOINT_IN;

	boolean IsRegisteringSensor;
	boolean rfEnrollSuccess;
	boolean isReceivingValue;
	String ReceivedMessage;

	private static enum DeviceType {
		TYPE_BM, TYPE_AM, TYPE_2232C, TYPE_R, TYPE_2232H, TYPE_4232H;
	}


	private DeviceType mType;
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

	public UsbControllerFTDI(Context curContext)
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
			LogWriter.writeLogUSB("USBFTDI","Init USB FTDI");
			if (UsbConnectionStatus) return  2; //already connected
		    LogWriter.writeLogUSB("USBFTDI","Checking USB list");
			usbManager = (UsbManager) context.getSystemService(context.USB_SERVICE);
			devlist = usbManager.getDeviceList();
		    if(devlist.size()==0) return -1; //device not attched

			dataSink=dataRecvd;

			deviter = devlist.values().iterator();
			pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);

			while (deviter.hasNext()) {
				dv = deviter.next();
				LogWriter.writeLogUSB("USBFTDI","Checking device->"+ dv.getProductId());
				if (String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID_PID))
				{
					LogWriter.writeLogUSB("USBFTDI","Device Found-"+VID_PID);
					UsbConnectionStatus = true;

					context.registerReceiver(mUsbReceiver, new IntentFilter(ACTION_USB_PERMISSION));
					//usbManager.
					if (!usbManager.hasPermission(dv)) {
						LogWriter.writeLogUSB("USBFTDI","USB permission not available");
						usbManager.requestPermission(dv, pendingIntent);
						//	  usbManager.re
						return 1;  //pending init

					} else {
						LogWriter.writeLogUSB("USBFTDI","Starting read write thread");
						if(!UsbReadWriteOnStart(dv)) return -1;
						else return 2;
					}

				}
			}
		return -1;// device not fornd in connected dev list
	}


	public void reset() throws IOException {
		int result = UsbConnection1.controlTransfer(FTDI_DEVICE_OUT_REQTYPE, SIO_RESET_REQUEST,
				SIO_RESET_SIO, 0 /* index */, null, 0, USB_WRITE_TIMEOUT_MILLIS);
		if (result != 0) {
			throw new IOException("Reset failed: result=" + result);
		}

		// TODO(mikey): autodetect.
		mType = DeviceType.TYPE_R;
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
	public synchronized int SendEmergencyMessage(String Message)
	{
		try {
			synchronized (lockWrite) {
				if(UsbConnection1==null) {
					LogWriter.writeLog("USBFDI","Connection object null");
					return 0;
				}
				byte[] SendBuffer = new byte[100];
				char tChar;
				int count = Message.length();
				//return UsbConnection1.bulkTransfer(epOUT1, SendBuffer, count, 500);
				for (count = 0; count < Message.length(); count++) {
					tChar = Message.charAt(count);
					SendBuffer[0] = (byte) tChar;
					UsbConnection1.bulkTransfer(epOUT1, SendBuffer, 1, 500);

				}
				return 1;
			}
		}catch (Exception exp )
		{
			return -1;
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
			LogWriter.writeLogUSB("USB","Initializing USB chip");
			if(InitializeUSBChip()) {
				LogWriter.writeLogUSB("USB","USB chip initialized successfully, Starting USB threads");
				new Thread(UsbReadWriteOnRun).start();
				new Thread(usbWriteThread).start();
				new Thread(UsbRecvdMesgDispacher).start();
				return true;
			}
			LogWriter.writeLogUSB("USB","USB chip init failed");
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
		public void run() {
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

						if (SendEmergencyMessage(str) == -1) {
							Log.i("WriteUSB", "Failed to Send:" + str);
						}

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


	private int setBaudRate(int baudRate) throws IOException {
		long[] vals = convertBaudrate(baudRate);
		long actualBaudrate = vals[0];
		long index = vals[1];
		long value = vals[2];
		int result = UsbConnection.controlTransfer(FTDI_DEVICE_OUT_REQTYPE,
				SIO_SET_BAUD_RATE_REQUEST, (int) value, (int) index,
				null, 0, USB_WRITE_TIMEOUT_MILLIS);
		if (result != 0) {
			throw new IOException("Setting baudrate failed: result=" + result);
		}
		return (int) actualBaudrate;
	}


	public void setParameters(int baudRate, int dataBits, int stopBits, int parity)
			throws IOException {
		setBaudRate(baudRate);

		int config = dataBits;

		switch (parity) {
			case PARITY_NONE:
				config |= (0x00 << 8);
				break;
			case PARITY_ODD:
				config |= (0x01 << 8);
				break;
			case PARITY_EVEN:
				config |= (0x02 << 8);
				break;
			case PARITY_MARK:
				config |= (0x03 << 8);
				break;
			case PARITY_SPACE:
				config |= (0x04 << 8);
				break;
			default:
				throw new IllegalArgumentException("Unknown parity value: " + parity);
		}

		switch (stopBits) {
			case STOPBITS_1:
				config |= (0x00 << 11);
				break;
			case STOPBITS_1_5:
				config |= (0x01 << 11);
				break;
			case STOPBITS_2:
				config |= (0x02 << 11);
				break;
			default:
				throw new IllegalArgumentException("Unknown stopBits value: " + stopBits);
		}

		int result = UsbConnection.controlTransfer(FTDI_DEVICE_OUT_REQTYPE,
				SIO_SET_DATA_REQUEST, config, 0 /* index */,
				null, 0, USB_WRITE_TIMEOUT_MILLIS);
		if (result != 0) {
			throw new IOException("Setting parameters failed: result=" + result);
		}
	}

	private long[] convertBaudrate(int baudrate) {
		// TODO(mikey): Braindead transcription of libfti method.  Clean up,
		// using more idiomatic Java where possible.
		int divisor = 24000000 / baudrate;
		int bestDivisor = 0;
		int bestBaud = 0;
		int bestBaudDiff = 0;
		int fracCode[] = {
				0, 3, 2, 4, 1, 5, 6, 7
		};

		for (int i = 0; i < 2; i++) {
			int tryDivisor = divisor + i;
			int baudEstimate;
			int baudDiff;

			if (tryDivisor <= 8) {
				// Round up to minimum supported divisor
				tryDivisor = 8;
			} else if (mType != DeviceType.TYPE_AM && tryDivisor < 12) {
				// BM doesn't support divisors 9 through 11 inclusive
				tryDivisor = 12;
			} else if (divisor < 16) {
				// AM doesn't support divisors 9 through 15 inclusive
				tryDivisor = 16;
			} else {
				if (mType == DeviceType.TYPE_AM) {
					// TODO
				} else {
					if (tryDivisor > 0x1FFFF) {
						// Round down to maximum supported divisor value (for
						// BM)
						tryDivisor = 0x1FFFF;
					}
				}
			}

			// Get estimated baud rate (to nearest integer)
			baudEstimate = (24000000 + (tryDivisor / 2)) / tryDivisor;

			// Get absolute difference from requested baud rate
			if (baudEstimate < baudrate) {
				baudDiff = baudrate - baudEstimate;
			} else {
				baudDiff = baudEstimate - baudrate;
			}

			if (i == 0 || baudDiff < bestBaudDiff) {
				// Closest to requested baud rate so far
				bestDivisor = tryDivisor;
				bestBaud = baudEstimate;
				bestBaudDiff = baudDiff;
				if (baudDiff == 0) {
					// Spot on! No point trying
					break;
				}
			}
		}

		// Encode the best divisor value
		long encodedDivisor = (bestDivisor >> 3) | (fracCode[bestDivisor & 7] << 14);
		// Deal with special cases for encoded value
		if (encodedDivisor == 1) {
			encodedDivisor = 0; // 3000000 baud
		} else if (encodedDivisor == 0x4001) {
			encodedDivisor = 1; // 2000000 baud (BM only)
		}

		// Split into "value" and "index" values
		long value = encodedDivisor & 0xFFFF;
		long index;
		if (mType == DeviceType.TYPE_2232C || mType == DeviceType.TYPE_2232H
				|| mType == DeviceType.TYPE_4232H) {
			index = (encodedDivisor >> 8) & 0xffff;
			index &= 0xFF00;
			index |= 0 /* TODO mIndex */;
		} else {
			index = (encodedDivisor >> 16) & 0xffff;
		}

		// Return the nearest baud rate
		return new long[] {
				bestBaud, index, value
		};
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
		if(UsbConnectionStatus==false){
			LogWriter.writeLogUSB("InitializeUSBChip","Connection status false");
			return false;
		}
		UsbDevice dv = HAUsbDevice ;

		int count;
		char tChar;

		if (dv == null)return false;
		UsbSendReceiveStatus  =  true;
		isCommandSend=false;
		UsbManager usbm = (UsbManager) context.getSystemService(context.USB_SERVICE);
		UsbConnection = usbm.openDevice(dv);
		if(UsbConnection==null){
			LogWriter.writeLogUSB("InitializeUSBChip","USB open failed");
		}
		UsbConnection1=UsbConnection;
		LogWriter.writeLogUSB("InitializeUSBChip","UsbConnected in " + String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()));

		trycount=0;
		if(!UsbConnection.claimInterface(dv.getInterface(0), true)){
			LogWriter.writeLogUSB("InitializeUSBChip","Clainimg USB interface failed");
			return false;
		}
		LogWriter.writeLogUSB("InitializeUSBChip","Sensing initialization params");

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
			LogWriter.writeLogUSB("InitializeUSBChip","Setting USB parameters failed");
		}

		UsbInterface usbInterface = dv.getInterface(0);
		for(int i = 0; i < usbInterface.getEndpointCount(); i++)
		{
			if(usbInterface.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
			{
				if(usbInterface.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN) {
					LogWriter.writeLogUSB("InitializeUSBChip","USBIN Interface claim succeeded");
					epIN = usbInterface.getEndpoint(i);
				}
				else
				{
					LogWriter.writeLogUSB("InitializeUSBChip","USBOUT Interface claim succeeded");
					epOUT = usbInterface.getEndpoint(i);
					epOUT1=epOUT;
				}

			}
		}

		if((epOUT!=null)&&(epIN!=null)) return true;
		else {
			LogWriter.writeLogUSB("InitializeUSBChip","USBIN Interface claim succeeded");
			return false;
		}
	}
	private Runnable UsbRecvdMesgDispacher = new Runnable() {
		@Override
		public void run()
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
	};
	private Runnable UsbReadWriteOnRun = new Runnable() {

		//@Override
		@TargetApi(12)
		public void run()
		{
			try {
				byte[] ReceiveBuffer = new byte[500];
				byte[] ProcessBuffer = new byte[1000];
				String str1 = "";

				int readLen = 0;
				String str = "";
				StringBuilder strBlder = new StringBuilder();
				for (; ; ) {
					try {
						if (UsbConnectionStatus == false) {
							UsbSendReceiveStatus = false;
							return;
						}

						boolean endFlagRead = false;
						int locInProcessBuf = 0;

						do {
							readLen = UsbConnection.bulkTransfer(epIN, ReceiveBuffer, 500, 500);
							if (readLen <= 2) {
								endFlagRead = true;
								break;
							}
							String cmd = new String(ReceiveBuffer);
							System.arraycopy(ReceiveBuffer, 2, ProcessBuffer, locInProcessBuf, readLen - 2);
							locInProcessBuf += (readLen - 2);
							ReceiveBuffer = new byte[500];

							cmd = new String(ProcessBuffer);
							LogWriter.writeLogUSB("USB Recv Data", cmd);
						}
						while (!endFlagRead);

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
										byte[] temp = new byte[500];
										System.arraycopy(ProcessBuffer, lastPos, temp, 0, (i - lastPos));
										//strBlder.append(ProcessBuffer,lastPos,i);
										String cmd = new String(temp);
										cmd = cmd.trim();
										LogWriter.writeLogUSB("USB", "Cur Data->" + cmd);
										synchronized (ReadfifoBuffer) {
											ReadfifoBuffer.add(cmd);
											ReadfifoBuffer.notify();
										}

										lastPos = i;
									} catch (Exception exp) {
										LogWriter.writeLogUSB("USB", "Exception->" + exp.getMessage());

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
			String action = intent.getAction();
			if (UsbManager.EXTRA_PERMISSION_GRANTED.equals(action))
			{
				LogWriter.writeLog("Permission","Granted");
			}
			if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))
			{
				try {
					UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (String.format("%04X:%04X", device.getVendorId(),device.getProductId()).equals(VID_PID))
					{
						UsbConnectionStatus = false;
						WritefifoBuffer.clear();
					}
				}
				catch(Exception exp)
				{

				}
				sendStatus(context, CommonDataArea.REQUEST_USB_STATUS, CommonDataArea.REQUEST_STATUS_FAIL,"Device Not Connected");
			}
			if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action))
			{
				UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
				if (String.format("%04X:%04X", device.getVendorId(),device.getProductId()).equals(VID_PID))
				{
					UsbConnectionStatus=true;
					if (!usbManager.hasPermission(device)) {

						usbManager.requestPermission(device, pendingIntent);
					} else {
						if(!UsbReadWriteOnStart(device)) sendStatus(context, CommonDataArea.REQUEST_USB_STATUS, CommonDataArea.REQUEST_STATUS_FAIL,"USB device not found");
						else sendStatus(context, CommonDataArea.REQUEST_USB_STATUS, CommonDataArea.REQUEST_STATUS_SUCCESS,"Device Connected");
					}
				}

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
						if (String.format("%04X:%04X", dv.getVendorId(),dv.getProductId()).equals(VID_PID))
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
			if (String.format("%04X:%04X", dv.getVendorId(), dv.getProductId()).equals(VID_PID)) {
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

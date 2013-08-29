package android.TextMessenger.view;

import java.net.BindException;
import java.net.SocketException;
import java.net.UnknownHostException;

import adhoc.aodv.Node;
import adhoc.aodv.ea.Config;
import adhoc.aodv.ea.EnergyAware;
import adhoc.aodv.exception.InvalidNodeAddressException;
import adhoc.etc.Debug;
import adhoc.setup.AdhocManager;
import adhoc.setup.PhoneType;
import android.TextMessenger.view.ButtonListner;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class Connect extends Activity {
	private Button connect;
	private ButtonListner listener;
	private int myContactID;
	private Node node;
	AdhocManager adHoc;
	String ip;
	int phoneType;
	
	static IntentFilter ifilter;
	public static Intent batteryStatus;

	public Connect() {
		
		
	}
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		EnergyAware ea = new EnergyAware(this);
		
		Log.d("DEBUG", "iniciou@");
		setContentView(R.layout.connect);
		listener = new ButtonListner(this);
		connect = (Button) findViewById(R.id.connectButton);
		connect.setOnClickListener(listener);
		

		// TODO Start ADHOC NETWORK
	}
	
	public Intent getBatteryStatus() {

		ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		batteryStatus = this.registerReceiver(null, ifilter); 
		
		return batteryStatus;
	}
	
	private String getPhoneIP(){

		Config config = new Config();
		if (config.load()) {
			myContactID = Integer.parseInt(config.get("ipv4_address"));
			return "192.168.2." + myContactID;
		}	
		
		return "-1";
	}

	public static native int runCommand(String command);

	static {
		System.loadLibrary("adhocsetup");
	}

	/**
	 * When connect is clicked, a ad-hoc network is startet
	 */
	public void clickConnect() {
		

		/*try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		Log.d("DEBUG", Float.toString(EnergyAware.getOwnBatteryLevel()));
		
		try {
			ip = getPhoneIP();
			Log.d("DEBUG", ip);
			if(ip == "-1"){
				Log.d("PHONE", "No such phoneIP");
				return;
			}
			
			WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			adHoc = new AdhocManager(this, wifi);
						
			//int ligarWifi = Connect.runCommand("su -c \"svc wifi enable\"");
			Thread.sleep(2000);
			int setarIPeth0 = Connect.runCommand("su -c \"ifconfig eth0 "+ip+"\"");
			int setarIPwlan0 = Connect.runCommand("su -c \"ifconfig wlan0 "+ip+"\"");
		
			//Starting an ad-hoc network
			int setAdhocModeeth0 = Connect.runCommand("su - c \"/data/data/com.googlecode.android.wifi.tether/bin/iwconfig eth0 mode adhoc\"");
			int setAdhocModewlan0 = Connect.runCommand("su - c \"/data/data/com.googlecode.android.wifi.tether/bin/iwconfig wlan0 mode adhoc\"");
			
			int startTether = Connect.runCommand("su - c \"/data/data/com.googlecode.android.wifi.tether/bin/tether start\"");

			//Starting the routing protocol
			node = new Node(myContactID); 

			Debug.setDebugStream(System.out);
			node.startThread();
			//Intent i = new Intent(this, UDPTest.class);
			//startActivity(i);
			
		} catch (BindException e) {
			e.printStackTrace();
		} catch (InvalidNodeAddressException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("DEBUG", "Node startet ");
		
		/*
		 * ENVIO DE DADOS
		 */		
		
		/*String data = "";
		//55Kb
		for (int i = 0; i < 1100; i++) {
			data += "a";
		}
		Long time_init;
		//Segundos de execução - 5 minutos
		int total_exec = 300;
		int num_ite = 0;
		String datafin = "";
		long timetowait = 0;
		while (num_ite < total_exec*50) {
			datafin = num_ite + data;
			Log.d("DEBUG", "Enviando "+(num_ite+1.0)+" pacote");
			time_init = System.currentTimeMillis();
			//id_pacote (1) to destination (254)!
			node.sendData(num_ite, 6, datafin.getBytes());
			
			num_ite++;
			try {
				timetowait = 20 - (System.currentTimeMillis() - time_init);
				if (timetowait < 0) {
					timetowait = 0;
				}
				Log.d("DEBUG", "Esperando "+timetowait+" ms");
				Thread.sleep(timetowait);
			} catch (InterruptedException e) {
				 //TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		/* 
		 * FIM ENVIO DE DADOS
		 */
	
	}

	// FIXME HVORDAN SKAL MAN Faa TILDELT ID
	private int nameToID(String displayName) {
		return (int)(Math.random()*100); 
	}

	@Override
	protected void onResume() {
		super.onResume();
		// FIXME go to tabView
	}

	@Override
	protected void onDestroy() {
		if(node != null){
			node.stopThread();
		}
		super.onDestroy();
		if (adHoc != null) {
			Connect.runCommand("su - c \"/data/data/com.googlecode.android.wifi.tether/bin/tether stop\"");
		}
	}
}

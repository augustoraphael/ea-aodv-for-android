package adhoc.aodv.ea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.TextMessenger.view.Connect;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

public class EnergyAware {
	
	public static Connect connect;
	
	public EnergyAware(Connect c) {
		connect = c;
	}
	
	public static int getOwnBatteryLevel() {
		
		int level = connect.getBatteryStatus().getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = connect.getBatteryStatus().getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		float batteryPct = 100 * (level / (float) scale);
		
		return (int) batteryPct;
	}
	
	public static int getBatteryLevel(int ip) {
		BufferedReader br = openNEDB();

	    String line;
		try {
			while ((line = br.readLine()) != null) {
			    String[] linha = line.split(";");
			    int batteryLevel = (int) Float.parseFloat(linha[0]);
			    int ip_address = Integer.parseInt(linha[1]);
			    long timestamp = Long.parseLong(linha[2]);
			    
			    if (ip == ip_address) {
			    	return batteryLevel;
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static boolean isMoreRecent(int ip, long timestamp_new) {
		BufferedReader br = openNEDB();

	    String line;
		try {
			while ((line = br.readLine()) != null) {
			    String[] linha = line.split(";");
			    int batteryLevel = (int) Float.parseFloat(linha[0]);
			    int ip_address = Integer.parseInt(linha[1]);
			    long timestamp = Long.parseLong(linha[2]);
			    
			    if (ip == ip_address) {
			    	if (timestamp_new > timestamp) {
			    		return true;
			    	} else {
			    		return false;
			    	}
			    }
			    
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static void store(int ip_new, int batteryLevel_new, long timestamp_new) {
		BufferedReader br = openNEDB();

	    String line;
		try {
		    String aDataRow;
			String aBuffer = "";
			boolean found = false;
			while ((line = br.readLine()) != null) {
			    String[] linha = line.split(";");
			    int batteryLevel = (int) Float.parseFloat(linha[0]);
			    int ip_address = Integer.parseInt(linha[1]);
			    long timestamp = Long.parseLong(linha[2]);
			    if (ip_new == ip_address) {
			    	aDataRow = batteryLevel_new+";"+ip_new+";"+timestamp_new;
			    	found = true;
			    } else {
			    	aDataRow = batteryLevel+";"+ip_address+";"+timestamp;
			    }

				aBuffer += aDataRow + "\n";
			}
			if (!found) {
				aDataRow = batteryLevel_new+";"+ip_new+";"+timestamp_new;
				aBuffer += aDataRow + "\n";
			}
			
			File sdcard = Environment.getExternalStorageDirectory();

			File myFile = new File(sdcard,"NEDB.txt");
			
			myFile.createNewFile();
			FileWriter fOut = new FileWriter(myFile);
			
			//OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			fOut.write(aBuffer);
			fOut.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static BufferedReader openNEDB() {
		File sdcard = Environment.getExternalStorageDirectory();

		//Get the text file
		File file = new File(sdcard,"NEDB.txt");

		//Read text from file
		BufferedReader br = null; 
	    try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return br;
	}
	
}

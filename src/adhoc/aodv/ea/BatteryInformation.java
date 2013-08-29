package adhoc.aodv.ea;

import android.util.Log;

public class BatteryInformation {
	
	private int battery_level;
	private int ipv4_address;
	private long timestamp;
	
    public BatteryInformation(int bl, int ipv4, long time) {
	   battery_level = bl;
	   ipv4_address = ipv4;
	   timestamp = time;
   }
	
	public float getBatteryLevel() {
		return battery_level;
	}
	
	public void setBatteryLevel(int battery_level) {
		this.battery_level = battery_level;
	}
	
	public int getIpv4Address() {
		return ipv4_address;
	}
	
	public void setIpv4Address(int ipv4_address) {
		this.ipv4_address = ipv4_address;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String toString() {
		String bi_string = this.getBatteryLevel()+","+this.getIpv4Address()+","+this.getTimestamp();
		return bi_string;
	}

	public static void updateNEDB(String energy_info) {
		
		String[] separated = energy_info.split("-");
		for(int i = 0; i < separated.length; i++) {
			String[] bi = separated[i].split(",");
			int batterylevel = (int) Float.parseFloat(bi[0]);
			int ip = Integer.parseInt(bi[1]);
			long timestamp = Long.parseLong(bi[2]);
			if (EnergyAware.isMoreRecent(ip, timestamp)) {

				EnergyAware.store(ip, batterylevel, timestamp);
			}
		}		
		
	}
	
public static float selectMMBCR(String energy_info) {
		float MMBCR = Integer.MIN_VALUE;

		String[] separated = energy_info.split("-");
		for(int i = 0; i < separated.length; i++) {
			String[] bi = separated[i].split(",");
			int batterylevel = (int) Float.parseFloat(bi[0]);
			if (100.0F/(float)batterylevel > MMBCR) {
				MMBCR = 100.0F/(float)batterylevel;
			}
			
		}		
		return MMBCR;
	}
}

package adhoc.aodv.pdu;

import android.TextMessenger.view.Connect;

public abstract class AodvPDU implements Packet {
	protected byte pduType;
	protected int srcAddress, destAddress;
	protected int destSeqNum;
	
	protected float batteryPct;
	
	public AodvPDU() {
		
	}
	
	public AodvPDU(int sourceAddress, int destinationAddess,
			int destinationSequenceNumber) {
		srcAddress = sourceAddress;
		destAddress = destinationAddess;
		destSeqNum = destinationSequenceNumber;
	}

	public int getSourceAddress() {
		return srcAddress;
	}

	//@Override
	public int getDestinationAddress() {
		return destAddress;
	}

	public int getDestinationSequenceNumber() {
		return destSeqNum;
	}
	
	public float getBatteryPct() {
		return batteryPct;
	}

	public byte getType() {
		return pduType;
	}

	@Override
	public String toString() {
		return Byte.toString(pduType) + ";" + srcAddress + ";" + destAddress
				+ ";" + destSeqNum + ";";
	}
}
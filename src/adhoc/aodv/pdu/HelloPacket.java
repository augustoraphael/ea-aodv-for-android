package adhoc.aodv.pdu;

import adhoc.aodv.Constants;
import adhoc.aodv.exception.BadPduFormatException;
import android.TextMessenger.view.Connect;
import android.util.Log;

public class HelloPacket implements Packet {
	private byte pduType;
	private int sourceAddress;
	private int sourceSeqNr;
	
	private String energy_info;

	public HelloPacket() {

	}

	public HelloPacket(int sourceAddress, int sourceSeqNr, String energyInfo) {
		pduType = Constants.HELLO_PDU;
		this.sourceAddress = sourceAddress;
		this.sourceSeqNr = sourceSeqNr;
		this.energy_info = energyInfo;
	}

	public int getSourceAddress() {
		return sourceAddress;
	}

	//@Override
	public int getDestinationAddress() {
		// broadcast address
		return Constants.BROADCAST_ADDRESS;
	}

	public int getSourceSeqNr() {
		return sourceSeqNr;
	}
	
	public String getEnergyInfo() {
		return energy_info;
	}

	//@Override
	public byte[] toBytes() {
		return toString().getBytes();
	}

	@Override
	public String toString() {
		Log.d("DEBUG", "HELLO pacote: "+pduType + ";" + sourceAddress + ";" + sourceSeqNr + ";" + energy_info);
		return pduType + ";" + sourceAddress + ";" + sourceSeqNr + ";" + energy_info;
	}

	//@Override
	public void parseBytes(byte[] rawPdu) throws BadPduFormatException {
		String[] s = new String(rawPdu).split(";", 4);
		if (s.length != 4) {
			throw new BadPduFormatException(" HelloPacket : could not split "
					+ " the expected # of arguments from rawPdu . "
					+ " Expecteded 4 args but were given " + s.length);
		}
		try {
			pduType = Byte.parseByte(s[0]);
			if (pduType != Constants.HELLO_PDU) {
				throw new BadPduFormatException(
						" HelloPacket : pdu type did not match . "
								+ " Was expecting : " + Constants.HELLO_PDU
								+ " but parsed : " + pduType);
			}
			sourceAddress = Integer.parseInt(s[1]);
			sourceSeqNr = Integer.parseInt(s[2]);
			energy_info = s[3];
		} catch (NumberFormatException e) {
			throw new BadPduFormatException(
					" HelloPacket : falied in parsing arguments to the desired types ");
		}
	}

}
package adhoc.aodv.pdu;

import adhoc.aodv.Constants;
import adhoc.aodv.exception.BadPduFormatException;
import android.TextMessenger.view.Connect;

public class RREP extends AodvPDU {
	private int hopCount = 0;
	private int srcSeqNum;

	private String energy_info;
	public RREP() {
	}

	public RREP(int sourceAddress, int destinationAddress,
			int sourceSequenceNumber, int destinationSequenceNumber,
			int hopCount, String energyInfo) {

		super(sourceAddress, destinationAddress, destinationSequenceNumber);
		pduType = Constants.RREP_PDU;
		srcSeqNum = sourceSequenceNumber;
		this.hopCount = hopCount;
		this.energy_info = energyInfo;

	}
	public RREP(int sourceAddress, int destinationAddress,
			int sourceSequenceNumber, int destinationSequenceNumber, String energyInfo) {

		super(sourceAddress, destinationAddress, destinationSequenceNumber);
		pduType = Constants.RREP_PDU;
		srcSeqNum = sourceSequenceNumber;
		this.energy_info = energyInfo;

	}

	public int getHopCount() {
		return hopCount;
	}

	public void incrementHopCount() {
		hopCount++;
	}

	public int getDestinationSequenceNumber() {
		return destSeqNum;
	}
	
	public String getEnergyInfo() {
		return energy_info;
	}
	
	public void setEnergyInfo(String ei) {
		energy_info = ei;
	}

	//@Override
	public byte[] toBytes() {
		return this.toString().getBytes();
	}

	@Override
	public String toString() {
		return super.toString() + srcSeqNum + ";" + hopCount + ";" + energy_info;
	}

	//@Override
	public void parseBytes(byte[] rawPdu) throws BadPduFormatException {
		String[] s = new String(rawPdu).split(";", 7);
		if (s.length != 7) {
			throw new BadPduFormatException(" RREP : could not split "
					+ " the expected # of arguments from rawPdu . "
					+ " Expecteded 7 args but were given " + s.length);
		}
		try {
			pduType = Byte.parseByte(s[0]);
			if (pduType != Constants.RREP_PDU) {
				throw new BadPduFormatException(
						" RREP : pdu type did not match . "
								+ " Was expecting : " + Constants.RREP_PDU
								+ " but parsed : " + pduType);
			}
			srcAddress = Integer.parseInt(s[1]);
			destAddress = Integer.parseInt(s[2]);
			destSeqNum = Integer.parseInt(s[3]);
			srcSeqNum = Integer.parseInt(s[4]);
			hopCount = Integer.parseInt(s[5]);
			energy_info = s[6];
		} catch (NumberFormatException e) {
			throw new BadPduFormatException(
					" RREP : falied in parsing arguments to the desired types ");
		}

	}
}
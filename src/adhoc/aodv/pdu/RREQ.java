package adhoc.aodv.pdu;

import adhoc.aodv.Constants;
import adhoc.aodv.exception.BadPduFormatException;
import android.TextMessenger.view.Connect;

public class RREQ extends AodvPDU {
	private int srcSeqNum;
	private int hopCount = 0;
	private int broadcastID;

	private String energy_info;
	
	public RREQ() {

	}

	/**
	 * Constructor for creating a route request PDU
	 * 
	 * @param sourceNodeAddress
	 *            the originators node address
	 * @param destinationNodeAddress
	 *            the address of the desired node
	 * @param sourceSequenceNumber
	 *            originators sequence number
	 * @param destinationSequenceNumber
	 *            should be set to the last known sequence number of the
	 *            destination
	 * @param broadcast
	 *            Id along with the source address this number uniquely
	 *            identifies this route request PDU
	 */
	public RREQ(int sourceNodeAddress, int destinationNodeAddress,
			int sourceSequenceNumber, int destinationSequenceNumber,
			int broadcastId, String energyInfo) {
		super(sourceNodeAddress, destinationNodeAddress,
				destinationSequenceNumber);
		pduType = Constants.RREQ_PDU;
		srcSeqNum = sourceSequenceNumber;
		this.broadcastID = broadcastId;
		this.energy_info = energyInfo;

	}

	public int getBroadcastId() {
		return broadcastID;
	}

	public int getSourceSequenceNumber() {
		return srcSeqNum;
	}

	public void setDestSeqNum(int destinationSequenceNumber) {
		destSeqNum = destinationSequenceNumber;
	}

	public int getHopCount() {
		return hopCount;
	}

	public void incrementHopCount() {
		hopCount++;
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
		return super.toString() + srcSeqNum + ";" + hopCount + ";"
				+ broadcastID + ";" + energy_info;
	}

	//@Override
	public void parseBytes(byte[] rawPdu) throws BadPduFormatException {
		String[] s = new String(rawPdu).split(";", 8);
		if (s.length != 8) {
			throw new BadPduFormatException(" RREQ : could not split "
					+ " the expected # of arguments from rawPdu . "
					+ " Expecteded 8 args but were given " + s.length);
		}
		try {
			pduType = Byte.parseByte(s[0]);
			if (pduType != Constants.RREQ_PDU) {
				throw new BadPduFormatException(
						" RREQ : pdu type did not match . "
								+ " Was expecting : " + Constants.RREQ_PDU
								+ " but parsed : " + pduType);
			}
			srcAddress = Integer.parseInt(s[1]);
			destAddress = Integer.parseInt(s[2]);
			destSeqNum = Integer.parseInt(s[3]);
			srcSeqNum = Integer.parseInt(s[4]);
			hopCount = Integer.parseInt(s[5]);
			broadcastID = Integer.parseInt(s[6]);
			energy_info = s[7];
		} catch (NumberFormatException e) {
			throw new BadPduFormatException(
					" RREQ : falied in parsing arguments to the desired types ");
		}
	}
}
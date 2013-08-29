package adhocStressTest;

import java.net.BindException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import adhoc.aodv.Node;
import adhoc.aodv.ObserverConst;
import adhoc.aodv.Node.MessageToObserver;
import adhoc.aodv.Node.PacketToObserver;
import adhoc.aodv.exception.InvalidNodeAddressException;
import adhoc.etc.Debug;

public class FunctionalTest implements Observer {
	private Node node;
	private Random generator = new Random();
	private volatile boolean readyToResume = false;

	private final static int srcAddress = 1;
	private final static int destAddress = 2;

	public static void main(String[] args) {
		// TODO c h e c k t h a t t h i s a d d r e s s c o r r e s p o n d s t
		// o t h e d e v i c e .
		new FunctionalTest(srcAddress);
	}

	public FunctionalTest(int myAddress) {
		try {
			Debug.setDebugStream(System.out);
			node = new Node(myAddress);
		} catch (BindException e) {
			e.printStackTrace();
		} catch (InvalidNodeAddressException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		node.addObserver(this);
		node.startThread();

		if (myAddress == srcAddress) {
			SourceActionsInFuncTestWithTwoNodes();
		}
	}

	private void destActionsInFuncTestWithTwoNodes() {
		node.stopThread();
		try {
			synchronized (this) {
				this.wait(10000);
			}
		} catch (InterruptedException e) {

		}
		node.startThread();

		node.sendData(1, srcAddress,
				new String("terminate test now").getBytes());

		node.stopThread();
	}

	private void SourceActionsInFuncTestWithTwoNodes() {
		try {
			synchronized (this) {
				while (!readyToResume) {
					this.wait();
				}
			}

			node.sendData(1, 255, new String("broadcast test").getBytes());

			node.sendData(2, 2, new String("unicast test").getBytes());

			node.sendData(3, srcAddress,
					new String("unicast  test to src").getBytes());

			node.sendData(4, 5, new String("RREQ fail test").getBytes());

			readyToResume = false;
			synchronized (this) {
				while (!readyToResume) {
					this.wait();
				}
			}
			node.sendData(5, -1, new String("invalid address test").getBytes());

			byte[] data = new byte[54001];
			generator.nextBytes(data);
			node.sendData(6, 255, data);

			readyToResume = false;
			synchronized (this) {
				while (!readyToResume) {
					this.wait();
				}
			}

			generator.nextBytes(data);
			node.sendData(7, 2, data);

			node.sendData(8, 2, new String("dest stop now").getBytes());

			readyToResume = false;
			synchronized (this) {
				while (!readyToResume) {
					this.wait();
				}
			}

			node.stopThread();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//@Override
	public void update(Observable arg0, Object arg1) {
		MessageToObserver msg = (MessageToObserver) arg1;

		int userPacketID, destination, type = msg.getMessageType();

		switch (type) {
		case ObserverConst.ROUTE_ESTABLISHMENT_FAILURE:
			// N o t e : any m e s s a g e s t h a t had same d e s t i n a t i
			// o n h a s b e e n r e m o v e d f r o m s e n d i n g
			int unreachableDestinationAddrerss = (Integer) msg
					.getContainedData();
			if (readyToResume == false && unreachableDestinationAddrerss == 5) {
				readyToResume = true;
				synchronized (this) {
					this.notify();
				}
			}
			Debug.print("FuncTest : ROUTE_ESTABLISHMENT_FAILURE notification received - Unreachable node : "
					+ unreachableDestinationAddrerss);
			break;
		case ObserverConst.DATA_RECEIVED:
			byte[] data = (byte[]) msg.getContainedData();
			int senderAddress = (Integer) ((PacketToObserver) msg)
					.getSenderNodeAddress();
			Debug.print("FuncTest : DATA_RECEIVED notification  received -  from destAdr : "
					+ senderAddress + "  containing : " + new String(data));
			if (senderAddress == srcAddress
					&& (new String(data)).equals("dest stop now")) {
				destActionsInFuncTestWithTwoNodes();
			}
			if (senderAddress == destAddress
					&& (new String(data).equals("terminate test now"))) {
				readyToResume = true;
				synchronized (this) {
					this.notify();
				}
			}
			break;
		case ObserverConst.DATA_SENT_SUCCESS:
			userPacketID = (Integer) msg.getContainedData();
			Debug.print("FuncTest : DATA_SENT_SUCCESS notification received - packetID : "
					+ userPacketID);
			break;
		case ObserverConst.INVALID_DESTINATION_ADDRESS:
			userPacketID = (Integer) msg.getContainedData();
			Debug.print("FuncTest : INVALID_DESTINATION_ADDRESS notification received - packetID : "
					+ userPacketID);
			break;
		case ObserverConst.DATA_SIZE_EXCEEDES_MAX:
			userPacketID = (Integer) msg.getContainedData();
			Debug.print(" FuncTest : DATA_SIZE_EXCEEDES_MAX  notification received - packetID : "
					+ userPacketID);
			if (readyToResume == false
					&& (userPacketID == 5 || userPacketID == 6)) {
				readyToResume = true;

				synchronized (this) {
					this.notify();
				}
			}
			break;
		case ObserverConst.ROUTE_INVALID:
			destination = (Integer) msg.getContainedData();
			Debug.print(" FuncTest : ROUTE_INVALID notification  received -  for destAdr : "
					+ destination);
			break;
		case ObserverConst.ROUTE_CREATED:
			destination = (Integer) msg.getContainedData();
			if (readyToResume == false && destination == destAddress) {
				readyToResume = true;
				synchronized (this) {
					this.notify();
				}
			}
			Debug.print(" FuncTest : ROUTE_CREATED notification  received - to node : "
					+ destination);
			break;
		default:
			break;
		}
	}

}

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import cscl.lib.edu.rit.ds.Lease;
import cscl.lib.edu.rit.ds.RemoteEventGenerator;
import cscl.lib.edu.rit.ds.RemoteEventListener;
import cscl.lib.edu.rit.ds.registry.AlreadyBoundException;
import cscl.lib.edu.rit.ds.registry.RegistryProxy;

/**
 * Class Microblog provides a Java RMI distributed Microblog object for project.
 * It maintains a list of messages added by the user. It also provides methods
 * to add and remove messages
 * 
 * Reference : P2Pedia version 2, Distributed Systems, Prof. Alan Kaminsky,
 * Department of Computer Science, Rochester Institute of Technology. <BR>
 * URL : http://www.cs.rit.edu/~ark/730/p2pedia02/java2html.php?file=4
 * 
 * Usage : java Start Microblog <host> <port> <name> <BR>
 * <host> is the name of the host computer where the Registry Server is running. <BR>
 * <port> is the port number to which the Registry Server is listening. <BR>
 * <name> is the microblog owner's name.
 */
public class Microblog implements IMicroblog {

	/** The host. */
	private String host;

	/** The port of the registry server. */
	private int port;

	/** The name of the blog. */
	private String name;

	/** The proxy for the registry server. */
	private RegistryProxy registry;

	/** The message to be added or removed in this microblog. */
	private String message;

	/** The serial number of the message. */
	private static int serialNumber;

	/** The message list. */
	private ArrayList<Message> messageList;

	/** The event generator . */
	private RemoteEventGenerator<MessageEvent> eventGenerator;

	/**
	 * Instantiates a new microblog.
	 * 
	 * @param args
	 *            the args
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Microblog(String[] args) throws IOException {
		// Parse command line arguments.
		if (args.length < 3) {
			System.out.println("Required arguments are missing. "
					+ "Please see the Usage for this class");
			usage();
		} else if (args.length > 3) {
			System.out.println("There are extra arguments. "
					+ "Please see the Usage for this class");
			usage();
		}
		host = args[0];
		port = parseInt(args[1], "Port");
		name = args[2];
		// List to store messages.
		messageList = new ArrayList();
		// Get a proxy for the Registry Server.
		registry = new RegistryProxy(host, port);
		serialNumber = 0;
		// Prepare to generate remote events.
		eventGenerator = new RemoteEventGenerator<MessageEvent>();
		// Export this microblog object.
		UnicastRemoteObject.exportObject(this, 0);

		// Bind this node into the Registry Server.
		try {
			registry.bind(name, this);
		} catch (AlreadyBoundException exc) {
			try {
				UnicastRemoteObject.unexportObject(this, true);
			} catch (NoSuchObjectException exc2) {
			}
			throw new IllegalArgumentException("Microblog: <name> = \"" + name
					+ "\" already exists");
		} catch (RemoteException exc) {
			try {
				UnicastRemoteObject.unexportObject(this, true);
			} catch (NoSuchObjectException exc2) {
			}
		}
	}

	/**
	 * Adds the message to the list .
	 * 
	 * @param text
	 *            the message to be added.
	 * @return the string, as the added message in the format required in the
	 *         project.
	 * @throws RemoteException
	 *             the remote exception.
	 */

	public Message addMessage(String text) throws RemoteException {
		// Create Message object to be added in the List.
		Message addedMessage = createMessage(text, ++serialNumber, name);
		messageList.add(addedMessage);
		// Report a MessageEvent to any remote event listeners.
		eventGenerator.reportEvent(new MessageEvent(addedMessage));
		// return the message object.
		return addedMessage;

	}

	/**
	 * Removes the message from the microblog.
	 * 
	 * @param index
	 *            is the serial number number of the message to be removed.
	 * @return the string, as the added message in the format required in the
	 *         project.
	 * @throws RemoteException
	 *             the remote exception
	 */
	public Message removeMessage(int index) throws RemoteException {
		Message removedMessage = null;
		for (int i = 0; i < messageList.size(); i++) {
			// Check if the message with the given serial number exists.
			if (messageList.get(i).getSerialNumber() == index) {
				removedMessage = messageList.get(i);
				// remove the message if it exists.
				messageList.remove(i);
			}
		}
		// return the message object.
		return removedMessage;
	}

	/**
	 * Checks for two most recently added messages in microblog.
	 * 
	 * @return the ArrayList<Message>, consisting of zero, one, or two most
	 *         recently added messages.
	 * @throws RemoteException
	 *             the remote exception
	 */
	public ArrayList<Message> printInitialMessages() throws RemoteException {
		ArrayList<Message> initMessageList = new ArrayList();
		// return null string if their is no messages in the blog.
		if (messageList.size() == 0) {
			return initMessageList;
		} else if (messageList.size() == 1) {
			// return the only message in the blog
			initMessageList.add(messageList.get(0));
			return initMessageList;
		} else {
			// return the recent two messages in the blog
			initMessageList.add(messageList.get(messageList.size() - 2));
			initMessageList.add(messageList.get(messageList.size() - 1));
			return initMessageList;
		}
	}

	/**
	 * Creates the Message object.
	 * 
	 * @param text
	 *            is the message to be retained in the blog.
	 * @param serialNumber
	 *            is the serial number of the message.
	 * @return the message
	 */
	private Message createMessage(String text, int serialNumber, String blogName) {
		return new Message(text, serialNumber, blogName);
	}

	/**
	 * Add the given remote event listener to this Microblog. Whenever a message
	 * is forwarded to this microblog, this microblog will report a MessageEvent
	 * to the given listener.
	 * 
	 * @param listener
	 *            Remote event listener.
	 * @return the lease
	 * @throws RemoteException
	 *             the remote exception
	 */
	public Lease addListener(RemoteEventListener<MessageEvent> listener)
			throws RemoteException {
		return eventGenerator.addListener(listener, 20000L);
	}

	/**
	 * Parse an integer command line argument. <BR>
	 * Reference : P2Pedia version 2, Distributed Systems, Prof. Alan Kaminsky,
	 * Department of Computer Science, Rochester Institute of Technology. <BR>
	 * URL : http://www.cs.rit.edu/~ark/730/p2pedia02/java2html.php?file=4
	 * 
	 * @param arg
	 *            Command line argument.
	 * @param name
	 *            Argument name.
	 * @return Integer value of <TT>arg</TT>.
	 */
	private static int parseInt(String arg, String name) {
		try {
			return Integer.parseInt(arg);
		} catch (NumberFormatException exc) {
			throw new IllegalArgumentException(": Invalid <" + name + ">: \""
					+ arg + "\"");
		}
	}

	/**
	 * Display the usage message and exit.
	 */
	private static void usage() {
		System.err.println("Usage: java Start Microblog <host> <port> <name>");
		System.err
				.println("<host> is the name of the host computer where the Registry Server is running.");
		System.err
				.println("<<port> is the port number to which the Registry Server is listening.");
		System.err.println("<name> is the microblog owner's name.");
		System.exit(1);
	}
}
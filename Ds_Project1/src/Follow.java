import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;

import cscl.lib.edu.rit.ds.Lease;
import cscl.lib.edu.rit.ds.RemoteEventListener;
import cscl.lib.edu.rit.ds.registry.NotBoundException;
import cscl.lib.edu.rit.ds.registry.RegistryEvent;
import cscl.lib.edu.rit.ds.registry.RegistryEventFilter;
import cscl.lib.edu.rit.ds.registry.RegistryEventListener;
import cscl.lib.edu.rit.ds.registry.RegistryProxy;

/**
 * The Class Follow displays all the current messages from the blog it is
 * following.
 * 
 * Reference : P2Pedia version 2, Distributed Systems, Prof. Alan Kaminsky,
 * Department of Computer Science, Rochester Institute of Technology. <BR>
 * URL : http://www.cs.rit.edu/~ark/730/p2pedia02/java2html.php?file=4
 * 
 * Usage : java Start Microblog <host> <port> <name>.... <BR>
 * <host> is the name of the host computer where the Registry Server is running. <BR>
 * <port> is the port number to which the Registry Server is listening. <BR>
 * <name> is the one or more names of the blogs to be followed.
 */
public class Follow {
	/** The host. */
	private static String host;

	/** The port. */
	private static int port;

	/** The number of blog to follow. */
	private static int numberOfBlogToFollow = 0;

	/** The list of blog to follow. */
	private static ArrayList<String> listOfBlogToFollow;

	/** The list of lease for every blog. */
	private static ArrayList<Lease> listOfLease;

	/** The initial message to be displayed. */
	private static String initialMessage;

	/** The proxy for the registry. */
	private static RegistryProxy registry;

	/** The registry listener. */
	private static RegistryEventListener registryListener;

	/** The registry filter. */
	private static RegistryEventFilter registryFilter;

	/** The remote event listener for the blog. */
	private static RemoteEventListener<MessageEvent> blogListener;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args) throws Exception {
		// Parse command line arguments.
		if (args.length < 3) {
			System.out.println("Required arguments are missing. "
					+ "Please see the Usage for this class");
			usage();
		}

		String host = args[0];
		int port = parseInt(args[1], "port");
		numberOfBlogToFollow = args.length - 2;
		// Copy the names of the blogs to be followed in the list
		listOfBlogToFollow = new ArrayList();
		for (int i = 0; i < numberOfBlogToFollow; i++) {
			listOfBlogToFollow.add(args[i + 2]);
		}

		// Get proxy for the Registry Server and print initial messages.
		printInitMessages(host, port);

		// Export a remote event listener object for receiving notifications
		// from the Registry Server.
		registryListener = new RegistryEventListener() {
			public void report(long seqnum, RegistryEvent event) {
				listenToMicroblog(event.objectName());
			}
		};
		UnicastRemoteObject.exportObject(registryListener, 0);

		// Export a remote event listener object for receiving notifications
		// from Node objects.
		blogListener = new RemoteEventListener<MessageEvent>() {
			public void report(long seqnum, MessageEvent message) {
				// Print log report on the console if the blog is being
				// followed.
				if (listOfBlogToFollow.contains(message.getMessage()
						.getBlogName())) {
					System.out.println(message.getMessage());
				}
			}
		};
		UnicastRemoteObject.exportObject(blogListener, 0);

		// Tell the Registry Server to notify us when a new Microblog object is
		// bound.
		registryFilter = new RegistryEventFilter().reportType("Microblog")
				.reportBound();
		registry.addEventListener(registryListener, registryFilter);

		// Tell all existing Microblog objects to notify us of added Messages.
		for (String objectName : registry.list("Microblog")) {
			if (listOfBlogToFollow.contains(objectName)) {
				listenToMicroblog(objectName);
			}
		}

	}

	/**
	 * Prints on the console the zero, one, or two most recently added messages
	 * from the given owners' microblogs
	 * 
	 * @param host
	 *            is the host address of the Registry Server
	 * @param port
	 *            the port of Registry Server.
	 */
	private static void printInitMessages(String host, int port) {
		try {
			ArrayList<Message> initMessage = new ArrayList();
			registry = new RegistryProxy(host, port);
			for (String blogName : listOfBlogToFollow) {
				try {
					IMicroblog blog = (IMicroblog) registry.lookup(blogName);
					// Get the list of messages to be printed.
					initMessage.addAll(blog.printInitialMessages());
				} catch (NotBoundException exc) {
				}
			}
			if (initMessage.size() != 0) {
				// Sort the messages in the format given by the project
				// requirements.
				Collections.sort(initMessage, new Message());
				// Print the message/s
				for (Message m : initMessage) {
					System.out.println(m);
				}
			}
		} catch (RemoteException exc) {
			System.err
					.println("Couldn't establish connection to Registry Server."
							+ " Please verify the host and port number");
			System.out.println(exc.getMessage());
			System.exit(1);
		} catch (Exception exc) {
			System.err.println("Registry Server is not running on the "
					+ "given host or port number");
			System.out.println(exc.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Tell the given node object to notify us if a message is added.
	 * 
	 * @param objectName
	 *            Node object's name.
	 */
	private static void listenToMicroblog(String objectName) {
		try {
			if (listOfLease == null)
				listOfLease = new ArrayList();

			IMicroblog blog = (IMicroblog) registry.lookup(objectName);
			// Adding LeaseListener in the Lease Object.
			Lease lease = blog.addListener(blogListener);
			lease.setListener(new LeaseListenerImpl(objectName));
			listOfLease.add(lease);
		}

		catch (NotBoundException exc) {
		} catch (RemoteException exc) {
		}
	}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage() {
		System.err.println("Usage: java Follow <host> <port> <name> ...");
		System.err.println("<host> = Registry Server's host");
		System.err.println("<port> = Registry Server's port");
		System.err
				.println("<name> = One or more names of the blogs to be followed");
		System.exit(1);
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
			System.err.printf("Query: Invalid <%s>: \"%s\"", name, arg);
			return 0;
		}
	}
}

import java.rmi.RemoteException;

import cscl.lib.edu.rit.ds.registry.NotBoundException;
import cscl.lib.edu.rit.ds.registry.RegistryProxy;

/**
 * Class AddMessage adds Message to the Microblog bound to the Registry Server.
 * AddMessage has the main program to add Messages to the blog.
 * 
 * Reference : P2Pedia version 2, Distributed Systems, Prof. Alan Kaminsky,
 * Department of Computer Science, Rochester Institute of Technology. <BR>
 * <BR>
 * URL : http://www.cs.rit.edu/~ark/730/p2pedia02/java2html.php?file=4
 * 
 * Usage: java AddMessage <host> <port> <name> "<text>" <BR>
 * <host> = Registry Server's host. <BR>
 * <port> = Registry Server's port. <BR>
 * <name> = Name of the microblog to which the message is added. <BR>
 * <text> = Message to be added.
 */
public class AddMessage {

	/**
	 * The main method for class AddMessage.
	 * 
	 * @param args
	 *            the command line arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args) throws Exception {
		// Parse command line arguments.
		if (args.length < 4) {
			System.out.println("Required arguments are missing. "
					+ "Please see the Usage for this class");
			usage();
		} else if (args.length > 4) {
			System.out.println("There are extra arguments. "
					+ "Please see the Usage for this class");
			usage();
		}
		String host = args[0];
		int port = parseInt(args[1], "port");
		String name = args[2];
		String text = args[3];
		// Look up the blog name in the Registry Server.
		try {
			RegistryProxy registry = new RegistryProxy(host, port);
			IMicroblog blog = (IMicroblog) registry.lookup(name);
			// Display the added message with the given requirements
			// of the project
			Message addedMessage = blog.addMessage(text);
			System.out.println(addedMessage);
		} catch (RemoteException exc) {
			System.err
					.println("Couldn't establish connection to Registry Server."
							+ " Please verify the host and port number");
			System.out.println(exc.getMessage());
			System.exit(1);
		} catch (NotBoundException exc) {
			System.err.println("The blog " + name
					+ " is not bound to the registry server.");
			System.out.println(exc.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Display the usage message and exit.
	 */
	private static void usage() {
		System.err
				.println("Usage: java AddMessage <host> <port> <name> \"<text>\"");
		System.err.println("<host> = Registry Server's host.");
		System.err.println("<port> = Registry Server's port.");
		System.err
				.println("<name> = Name of the microblog to which the message is added.");
		System.err.println("<text> = Message to be added.");
		System.exit(1);
	}

	/**
	 * Parse an integer command line argument. <BR>
	 * Reference : P2Pedia version 2, Distributed Systems, Prof. Alan Kaminsky,
	 * Department of Computer Science, Rochester Institute of Technology. <BR>
	 * URL : http://www.cs.rit.edu/~ark/730/p2pedia02/java2html.php?file=4
	 * 
	 * 
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
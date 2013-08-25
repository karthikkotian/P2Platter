import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import cscl.lib.edu.rit.ds.Lease;
import cscl.lib.edu.rit.ds.RemoteEventListener;

/**
 * Interface IMicroblog specifies the Java RMI remote interface for a
 * distributed MicroBlog object for Project 1.
 */
public interface IMicroblog extends Remote {

	/**
	 * Adds the message to the microblog.
	 * 
	 * @param message
	 *            the message to be added.
	 * @return the string, as the added message in the format required in the
	 *         project.
	 * @throws RemoteException
	 *             the remote exception
	 */
	public Message addMessage(String message) throws RemoteException;

	/**
	 * Removes the message from the microblog if it exists.
	 * 
	 * @param index
	 *            is the serial number number of the message to be removed.
	 * @return the string, as the removed message in the format required in the
	 *         project.
	 * @throws RemoteException
	 *             the remote exception
	 */
	public Message removeMessage(int index) throws RemoteException;

	/**
	 * Checks for two most recently added messages in microblog.
	 * 
	 * @return ArrayList<Message>, consisting of zero, one, or two most recently
	 *         added messages.
	 * @throws RemoteException
	 *             the remote exception
	 */
	public ArrayList<Message> printInitialMessages() throws RemoteException;

	/**
	 * Add the given remote event listener to this Microblog. Whenever a message
	 * is added to this microblog, this microblog will report a MessageEvent to
	 * the given listener.
	 * 
	 * @param listener
	 *            Remote event listener.
	 * @return the lease
	 * @throws RemoteException
	 *             the remote exception
	 */
	public Lease addListener(RemoteEventListener<MessageEvent> listener)
			throws RemoteException;
}

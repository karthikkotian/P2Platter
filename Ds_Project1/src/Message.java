import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * The Class Message encapsulates the message from the user along with the time
 * and unique serial number of the message.
 */
public class Message implements Serializable, Comparator<Message> {
	/** The dashline. */
	private static String DASHLINE = "-------------------------------------"
			+ "-------------------------------------------";
	/** The message text. */
	private String message;

	/** The time of the message was created. */
	private Timestamp messageTime;

	/** The serial number of the message. */
	private int serialNumber;

	/** The blog name. */
	private String blogName;

	/**
	 * Instantiates a new message.
	 */
	public Message() {

	}

	/**
	 * Instantiates a new message.
	 * 
	 * @param message
	 *            the message
	 * @param serialNumber
	 *            the serial number
	 */

	public Message(String message, int serialNumber, String blogName) {
		this.message = message;
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		this.messageTime = new Timestamp(now.getTime());
		this.serialNumber = serialNumber;
		this.blogName = blogName;
	}

	/**
	 * Gets the serial number.
	 * 
	 * @return the serial number
	 */
	public int getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the message time.
	 * 
	 * @return the message time
	 */
	public Calendar getMessageTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(messageTime.getTime());
		return cal;
	}

	/**
	 * Sets the message time.
	 * 
	 * @param messageTime
	 *            : sets the messageTime.
	 */
	public void setMessageTime(Calendar messageTime) {
		this.messageTime = new Timestamp(messageTime.getTimeInMillis());
	}

	/**
	 * Gets the blog name.
	 * 
	 * @return the blog name
	 */
	public String getBlogName() {
		return blogName;
	}

	/**
	 * The compare method implements the following requirements<BR>
	 * 
	 * 1. The messages are printed in ascending order of date/time <BR>
	 * 2. If multiple messages have the same date/time, the messages are printed
	 * in ascending order of the users names <BR>
	 * 3. Multiple messages have the same date/time and the same user name, the
	 * messages are printed in ascending order of serial number.
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Message arg0, Message arg1) {
		Calendar cal1 = arg0.getMessageTime();
		Calendar cal2 = arg1.getMessageTime();
		// Make Milliseconds of the message time equal
		cal1.set(Calendar.MILLISECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		int calValue = cal1.compareTo(cal2);
		if (calValue == 0) {
			// Compare blog names if message time is equal
			int value = arg0.getBlogName().compareTo(arg1.getBlogName());
			if (value == 0) {
				// Compare with serial number if blog names are same.
				int messageValue = arg0.getSerialNumber()
						- arg0.getSerialNumber();
				if (messageValue > 0) {
					return 1;
				} else if (messageValue < 0) {
					return -1;
				} else {
					return 0;
				}
			} else {
				return value;
			}

		} else {
			return calValue;
		}
	}

	/**
	 * Gives the message in the format required in the project.
	 * 
	 * @param message
	 *            is the message to be printed.
	 * @param name
	 *            is the name of the blog.
	 * @return the string
	 */

	public String toString() {
		Calendar messageTime = getMessageTime();
		return DASHLINE + "\n" + getBlogName() + " -- Message "
				+ getSerialNumber() + " -- " + messageTime.get(Calendar.YEAR)
				+ "/" + (messageTime.get(Calendar.MONTH) + 1) + "/"
				+ messageTime.get(Calendar.DAY_OF_MONTH) + " "
				+ messageTime.get(Calendar.HOUR_OF_DAY) + ":"
				+ messageTime.get(Calendar.MINUTE) + ":"
				+ +messageTime.get(Calendar.SECOND) + "\n" + getMessage()
				+ "\n";
	}
}

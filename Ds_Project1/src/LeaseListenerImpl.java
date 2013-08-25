import cscl.lib.edu.rit.ds.Lease;
import cscl.lib.edu.rit.ds.LeaseListener;

/**
 * The Class LeaseListenerImpl is required to print the Failed condition if any
 * running Microblog object expires.
 */
public class LeaseListenerImpl implements LeaseListener {
	/** The dashline. */
	private static String DASHLINE = "-------------------------------------"
			+ "-------------------------------------------";

	/** The lease object. */
	private Lease theLease;

	/** The blog name which initiates Lease object. */
	private String blogName;

	/**
	 * Construct a new lease listener.
	 */
	public LeaseListenerImpl(String blogName) {
		this.blogName = blogName;
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
	 * The given lease was renewed.
	 * 
	 * @param theLease
	 *            Lease.
	 */
	public void leaseRenewed(Lease theLease) {
	}

	/**
	 * The given lease was canceled.
	 * 
	 * @param theLease
	 *            Lease.
	 */
	public void leaseCanceled(Lease theLease) {
	}

	/**
	 * The given lease expired.
	 * 
	 * @param theLease
	 *            Lease.
	 */
	public void leaseExpired(Lease theLease) {
		System.out.println(DASHLINE + "\n" + getBlogName() + " -- Failed");
	}
}

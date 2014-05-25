package dao;

/**
 * Exception which is thrown when an I/O exception occurred.
 * The I/O exception occurred is passed on.
 */
public class PersistentLayerException extends RuntimeException {
 
	/**
	 * Constructor for the exception.
	 * Passes on the message and the causing exception to the super class {@code RuntimeException}.
	 * 
	 * @param msg the message of the exception
	 * @param e the causing exception
	 */
	public PersistentLayerException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * Constructor for the exception.
	 * Passes on the causing exception to the super class {@code RuntimeException}.
	 * 
	 * @param e the causing exception
	 */
	public PersistentLayerException(Throwable e) {
		super(e);
	}
}

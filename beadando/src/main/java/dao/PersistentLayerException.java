package dao;

public class PersistentLayerException extends RuntimeException {

	public PersistentLayerException(String msg, Throwable e) {
		super(msg, e);
	}

	public PersistentLayerException(Throwable e) {
		super(e);
	}
}

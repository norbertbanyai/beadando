package dao;

public class ExistingWorkSessionException extends Exception {
	private int id;

	/**
	 * @param id
	 */
	public ExistingWorkSessionException(int id) {
		this.id = id;
	}
	
	public int getWorkSessionId() {
		return id;
	}

}

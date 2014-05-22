package dao;

public class WorkSessionNotFoundException extends Exception {
	private int worksession_id;

	/**
	 * @param worksession_id
	 */
	public WorkSessionNotFoundException(int worksession_id) {
		this.worksession_id = worksession_id;
	}
	
	public int getWorkSessionId() {
		return worksession_id;
	}
	
}

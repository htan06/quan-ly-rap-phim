package QL_Phong_soDoGhe;

public class Seat {
	    public enum Status { AVAILABLE, SELECTING, OCCUPIED }

	    private String id;
	    private Status status;

	    public Seat(String id) {
	        this.id = id;
	        this.status = Status.AVAILABLE;
	    }

	    
	    public String getId() { return id; }
	    public Status getStatus() { return status; }
	    public void setStatus(Status status) { this.status = status; }

}

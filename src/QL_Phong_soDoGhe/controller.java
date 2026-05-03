package QL_Phong_soDoGhe;

import java.util.*;

public class controller {


	    private Map<String, Seat> seatMap;

	    public controller() {
	        seatMap = new LinkedHashMap<>(); // Dùng LinkedHashMap để giữ thứ tự
	        String[] rows = {"F", "E", "D", "C", "B", "A"};
	        for (String r : rows) {
	            for (int i = 1; i <= 12; i++) {
	                String id = r + i;
	                seatMap.put(id, new Seat(id));
	            }
	        }
	    }

	    public Map<String, Seat> getAllSeats() { return seatMap; }

	    public void handleSeatClick(String id) {
	        Seat seat = seatMap.get(id);
	        if (seat.getStatus() == Seat.Status.AVAILABLE) {
	            seat.setStatus(Seat.Status.SELECTING);
	        } else if (seat.getStatus() == Seat.Status.SELECTING) {
	            seat.setStatus(Seat.Status.AVAILABLE);
	        }
	    }

	    public List<String> confirmBooking() {
	        List<String> booked = new ArrayList<>();
	        for (Seat s : seatMap.values()) {
	            if (s.getStatus() == Seat.Status.SELECTING) {
	                s.setStatus(Seat.Status.OCCUPIED);
	                booked.add(s.getId());
	            }
	        }
	        return booked;
	}

}

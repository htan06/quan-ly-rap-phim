# Quick Start Guide - Controller Architecture

## How to Use the Controllers

### 1. Starting the Application

```java
// Main.java automatically initializes all controllers
// Simply run Main.main()
public static void main(String[] args) {
    // ... initialize DAOs and Services ...
    
    AppUI appUI = new AppUI(
        bookingService,
        userService,
        movieService,
        authenticationService,
        sessionService,
        showTimeService,
        seatService,
        roomService,
        membershipService
    );
    
    appUI.start();  // Initializes controllers and shows login
}
```

### 2. Implementing the Booking Flow (STAFF)

#### In Your UI Class:
```java
public class MyBookingUI extends JFrame {
    private BookingController bookingController;
    private NavigationController navigationController;
    
    public MyBookingUI(BookingController bookingController, 
                       NavigationController navigationController) {
        this.bookingController = bookingController;
        this.navigationController = navigationController;
    }
    
    // When user selects a movie
    private void onMovieSelected(Long movieId) {
        bookingController.selectMovie(movieId);
        navigationController.navigateToShowTime(movieId);
    }
    
    // When user selects a showtime
    private void onShowTimeSelected(Long showTimeId) {
        bookingController.selectShowTime(showTimeId);
        navigationController.navigateToSeatSelection(showTimeId);
    }
    
    // When user selects seats
    private void onSeatsSelected(List<Long> seatIds) {
        bookingController.selectSeats(seatIds);
        // Proceed to booking confirmation
    }
    
    // When user confirms booking
    private void onConfirmBooking() {
        try {
            Long bookingId = bookingController.createBooking(false);
            JOptionPane.showMessageDialog(this, 
                "Booking created! ID: " + bookingId);
            bookingController.completeBooking();  // Return to dashboard
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error creating booking: " + e.getMessage());
        }
    }
    
    // Navigation back
    private void onBackClick() {
        bookingController.goBackToMovies();
    }
}
```

### 3. Implementing Admin Operations (MANAGER)

#### In Your Admin UI Class:
```java
public class MyAdminUI extends JFrame {
    private AdminController adminController;
    
    public MyAdminUI(AdminController adminController) {
        this.adminController = adminController;
    }
    
    // Get all movies
    private void displayAllMovies() {
        List<Movie> movies = adminController.getAllMovies();
        for (Movie movie : movies) {
            System.out.println(movie.getTitle());
        }
    }
    
    // Select and edit movie
    private void editMovie(Long movieId) {
        adminController.selectMovie(movieId);
        Movie selected = adminController.getSelectedMovie();
        // Populate form with selected movie data
    }
    
    // Get showtimes for a movie
    private void loadShowTimes(Long movieId) {
        List<ShowTime> showTimes = adminController.getShowTimesForMovie(movieId);
        // Display in table
    }
    
    // Get all rooms
    private void displayAllRooms() {
        List<Room> rooms = adminController.getAllRooms();
        for (Room room : rooms) {
            System.out.println(room.getRoomName());
        }
    }
    
    // Get all staff
    private void displayAllStaff() {
        List<User> staff = adminController.getAllStaff();
        for (User user : staff) {
            System.out.println(user.getUsername());
        }
    }
}
```

### 4. Navigation Patterns

#### Pattern 1: Simple Navigation
```java
navigationController.navigateToLogin();
navigationController.navigateToStaffDashboard();
navigationController.navigateToManagerDashboard();
```

#### Pattern 2: Booking Flow Navigation
```java
// Movie -> ShowTime
navigationController.navigateToShowTime(movieId);

// ShowTime -> Seats
navigationController.navigateToSeatSelection(showTimeId);

// Seats -> Booking
navigationController.navigateToBookingManagement();

// Booking -> Reports
navigationController.navigateToRevenueReport();
```

#### Pattern 3: Back Navigation
```java
// In BookingController
bookingController.goBackToMovies();
bookingController.goBackToShowTimes();
bookingController.goBackToSeats();
```

### 5. Getting Current Session Information

```java
// From any controller, get current user via SessionService
SessionService sessionService = // ... from controller
Session currentSession = sessionService.getCurrent();

Long userId = currentSession.getId();
String username = currentSession.getUsername();
String roleName = currentSession.getRole().getRoleName();

// Role-based logic
if ("STAFF".equals(roleName)) {
    // Staff operations
} else if ("MANAGER".equals(roleName)) {
    // Manager operations
}
```

### 6. Error Handling

```java
try {
    // Controller operation
    Long bookingId = bookingController.createBooking(true);
} catch (IllegalStateException e) {
    // State validation error
    JOptionPane.showMessageDialog(frame, "Invalid state: " + e.getMessage());
} catch (Exception e) {
    // Unexpected error
    JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
    e.printStackTrace();
}
```

### 7. Common Use Cases

#### Use Case 1: Complete Booking Flow
```java
// Step 1: Initialize controllers
BookingController bookingController = new BookingController(
    bookingService, movieService, showTimeService, 
    seatService, membershipService, sessionService, 
    navigationController
);

// Step 2: Execute booking flow
Long movieId = 1L;
bookingController.selectMovie(movieId);

Long showTimeId = 5L;
bookingController.selectShowTime(showTimeId);

List<Long> seatIds = Arrays.asList(1L, 2L, 3L);
bookingController.selectSeats(seatIds);

Long bookingId = bookingController.createBooking(false); // No membership

Booking booking = bookingController.getBookingDetails();
System.out.println("Booking Price: " + booking.getTotalPrice());

bookingController.completeBooking();
```

#### Use Case 2: Manage Movies (Admin)
```java
AdminController adminController = new AdminController(
    movieService, showTimeService, roomService, 
    userService, navigationController
);

// Get all movies
List<Movie> allMovies = adminController.getAllMovies();

// Select specific movie
adminController.selectMovie(10L);
Movie selected = adminController.getSelectedMovie();

// Get showtimes for movie
List<ShowTime> showTimes = adminController.getShowTimesForMovie(10L);
```

#### Use Case 3: Role-Based Dashboard Routing
```java
public class AppController {
    public void routeAfterLogin() {
        String role = navigationController.getSessionService()
                                         .getCurrent()
                                         .getRole()
                                         .getRoleName();
        
        if ("STAFF".equals(role)) {
            navigationController.navigateToStaffDashboard();
        } else if ("MANAGER".equals(role)) {
            navigationController.navigateToManagerDashboard();
        } else {
            navigationController.navigateToLogin();
        }
    }
}
```

## Tips and Best Practices

### ✅ DO:
- Use controllers for state management
- Call services through controllers
- Route navigation through NavigationController
- Reset booking state after completion
- Handle exceptions gracefully
- Validate user input in UI, not in controller

### ❌ DON'T:
- Call services directly from UI
- Modify service layer code
- Store business logic in UI classes
- Create new service instances in controllers
- Hardcode navigation paths
- Store user session directly in UI

## Debugging

### Common Issues

**Issue**: Null pointer when accessing booking state
```java
// Solution: Check if controller is initialized
if (bookingController != null) {
    Long movieId = bookingController.getSelectedMovieId();
} else {
    System.err.println("BookingController not initialized");
}
```

**Issue**: Session not found after login
```java
// Solution: Ensure SessionService is properly initialized
// and login was successful
try {
    Session current = sessionService.getCurrent();
    if (current == null) {
        System.err.println("No active session");
    }
} catch (Exception e) {
    System.err.println("Session error: " + e.getMessage());
}
```

**Issue**: Navigation not working
```java
// Solution: Ensure NavigationController is passed correctly
// and all services are initialized
if (navigationController == null) {
    System.err.println("NavigationController is null");
} else {
    navigationController.navigateToShowTime(movieId);
}
```

## Testing

### Unit Test Example
```java
@Test
public void testBookingFlow() {
    BookingController controller = new BookingController(
        mockBookingService, mockMovieService, 
        mockShowTimeService, mockSeatService,
        mockMembershipService, mockSessionService, 
        mockNavigationController
    );
    
    controller.selectMovie(1L);
    assertEquals(1L, controller.getSelectedMovieId());
    
    controller.selectShowTime(5L);
    assertEquals(5L, controller.getSelectedShowTimeId());
    
    controller.selectSeats(Arrays.asList(1L, 2L));
    assertEquals(2, controller.getSelectedSeatIds().size());
}
```

## File Reference

- **Controllers**: `src/main/java/com/application/view/controller/`
- **UI Classes**: `src/main/java/com/application/view/`
- **Services**: `src/main/java/com/application/service/`
- **Entities**: `src/main/java/com/application/entity/`
- **DTOs**: `src/main/java/com/application/dto/`


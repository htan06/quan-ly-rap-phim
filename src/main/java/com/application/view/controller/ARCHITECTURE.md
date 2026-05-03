# Java Swing Movie Booking System - Controller Architecture

## Overview

The application has been refactored to use a clean controller-based architecture that separates concerns between UI and business logic. All controllers are located exclusively in the `com.application.view.controller` package.

---

## Controller Architecture

### 1. **AppController**
**Location**: `com.application.view.controller.AppController`

**Responsibilities**:
- Manages overall application lifecycle
- Handles user login
- Routes authenticated users based on their role
- Manages session state
- Controls logout

**Key Methods**:
- `initializeApp()` - Start the application
- `login(String username, String password)` - Authenticate user
- `routeAfterLogin()` - Route to appropriate dashboard
- `getCurrentUserRole()` - Get current user's role
- `isUserAuthenticated()` - Check authentication status
- `logout()` - Terminate session and return to login

**Usage**:
```java
AppController appController = new AppController(
    authenticationService,
    sessionService,
    navigationController
);
appController.initializeApp();
```

---

### 2. **NavigationController**
**Location**: `com.application.view.controller.NavigationController`

**Responsibilities**:
- Manages screen transitions
- Maintains reference to current active frame
- Switches between different UI screens
- Handles navigation for both STAFF and MANAGER flows
- Routes based on user role

**Key Methods**:
- `navigateToLogin()` - Show login screen
- `navigateAfterLogin()` - Route to dashboard (STAFF or MANAGER)
- `navigateToStaffDashboard()` - Show staff/booking dashboard
- `navigateToManagerDashboard()` - Show manager dashboard
- `navigateToShowTime(Long movieId)` - Navigate to showtime selection
- `navigateToSeatSelection(Long showTimeId)` - Navigate to seat selection
- `navigateToBookingManagement()` - Navigate to booking confirmation
- `navigateToRevenueReport()` - Navigate to revenue/reports
- `logout()` - Logout and return to login

**Architecture**:
- Initializes specialized controllers (BookingController, AdminController)
- Manages frame disposal and creation
- Centralizes all navigation logic

---

### 3. **BookingController**
**Location**: `com.application.view.controller.BookingController`

**Responsibilities**:
- Orchestrates the STAFF booking flow
- Maintains booking state through the entire process
- Enforces booking flow order
- Manages user selections: Movie → ShowTime → Seats → Booking

**Booking Flow State**:
```
Movie → ShowTime → Seats → Booking → Payment → Invoice → Dashboard
```

**Key Methods**:
- `selectMovie(Long movieId)` - Step 1: Select movie
- `selectShowTime(Long showTimeId)` - Step 2: Select showtime
- `selectSeats(List<Long> seatIds)` - Step 3: Select seats
- `createBooking(Boolean useMembership)` - Step 4: Create booking
- `getBookingDetails()` - Get current booking info
- `completeBooking()` - Step 6: Finish and return to dashboard
- `cancelBooking()` - Cancel and return to dashboard
- `goBackToMovies()` - Navigate back to movie list
- `goBackToShowTimes()` - Navigate back to showtime selection
- `goBackToSeats()` - Navigate back to seat selection

**State Properties**:
- `selectedMovieId` - Currently selected movie
- `selectedShowTimeId` - Currently selected showtime
- `selectedSeatIds` - List of selected seats
- `useMembership` - Whether membership is applied
- `bookingId` - ID of created booking

---

### 4. **AdminController**
**Location**: `com.application.view.controller.AdminController`

**Responsibilities**:
- Orchestrates MANAGER/ADMIN operations
- Manages CRUD operations for:
  - Movies
  - ShowTimes
  - Rooms
  - Staff (Users)
  - Reports

**Key Methods**:

**Movie Operations**:
- `getAllMovies()` - Get all movies
- `getMovieById(Long id)` - Get specific movie
- `selectMovie(Long movieId)` - Select movie for editing
- `getSelectedMovie()` - Get currently selected movie

**ShowTime Operations**:
- `getAllShowTimes()` - Get all showtimes
- `getShowTimesForMovie(Long movieId)` - Get showtimes for specific movie
- `getShowTimeById(Long id)` - Get specific showtime
- `selectShowTime(Long showTimeId)` - Select showtime
- `getSelectedShowTime()` - Get currently selected

**Room Operations**:
- `getAllRooms()` - Get all rooms
- `getRoomById(Long id)` - Get specific room
- `selectRoom(Long roomId)` - Select room
- `getSelectedRoom()` - Get currently selected

**Staff Operations**:
- `getAllStaff()` - Get all staff users
- `getUserById(Long id)` - Get specific user
- `selectUser(Long userId)` - Select user
- `getSelectedUser()` - Get currently selected

---

## Modified UI Classes

### 1. **AppUI**
**Location**: `com.application.view.AppUI`

**Changes**:
- Refactored to use controller architecture
- Initializes all controllers
- Passes all services to controllers
- Manages lifecycle of application

**Key Methods**:
- `start()` - Initialize and start application
- `getAppController()` - Get AppController reference
- `getNavigationController()` - Get NavigationController reference

---

### 2. **LoginUI**
**Location**: `com.application.view.LoginUI`

**Changes**:
- Updated to use NavigationController instead of AppUI
- Changed constructor to accept NavigationController
- Routes to appropriate dashboard after login

**Constructor**:
```java
public LoginUI(AuthenticationService authenticationService, 
               NavigationController navigationController)
```

---

### 3. **ListMovieUI**
**Location**: `com.application.view.ListMovieUI`

**Changes**:
- Accepts both NavigationController and BookingController
- Added row selection listener
- Added double-click handler for booking flow
- Navigates to ShowTime on movie selection

**Constructors**:
```java
// Admin flow
public ListMovieUI(MovieService movieService, 
                   NavigationController navigationController)

// Booking flow
public ListMovieUI(MovieService movieService, 
                   NavigationController navigationController, 
                   BookingController bookingController)
```

---

### 4. **ShowTimeUI**
**Location**: `com.application.view.ShowTimeUI`

**Changes**:
- Supports both admin and booking flows
- New constructor for booking flow with movieId parameter
- Separate UI initialization for booking flow
- Row selection listener for showtime selection
- Double-click navigation to seat selection

**Constructors**:
```java
// Admin flow
public ShowTimeUI(ShowTimeService showTimeService, 
                  NavigationController navigationController)

// Booking flow
public ShowTimeUI(Long movieId, 
                  NavigationController navigationController, 
                  BookingController bookingController)
```

---

### 5. **SoDoGhe (Seat Selection)**
**Location**: `com.application.view.SoDoGhe`

**Changes**:
- Added constructor for booking flow
- Added back button for navigation
- Modified confirmation to work with BookingController
- Collects selected seat IDs for booking

**Constructors**:
```java
// Admin/Standalone flow
public SoDoGhe()

// Booking flow
public SoDoGhe(Long showTimeId, 
               NavigationController navigationController, 
               BookingController bookingController)
```

---

## Data Flow

### STAFF Booking Flow
```
LoginUI
  ↓ (authenticate)
ListMovieUI (Movie Selection)
  ↓ (selectMovie)
ShowTimeUI (ShowTime Selection)
  ↓ (selectShowTime)
SoDoGhe (Seat Selection)
  ↓ (selectSeats)
BookingManagementUI (Booking Confirmation)
  ↓ (createBooking)
Payment Processing
  ↓
Invoice Display
  ↓
ListMovieUI (Return to Dashboard)
```

### MANAGER Admin Flow
```
LoginUI
  ↓ (authenticate)
StaffManagement (Manager Dashboard)
  ↓ (CRUD Operations)
Movies, ShowTimes, Rooms, Staff Management
  ↓
Reports/Analytics
```

---

## Service Layer Integration

All controllers are **read-only consumers** of services. Business logic remains in the service layer:

### Services Used:
- `BookingService` - Booking creation/updates
- `MovieService` - Movie queries
- `ShowTimeService` - ShowTime queries
- `SeatService` - Seat queries
- `RoomService` - Room queries
- `UserService` - User queries
- `MembershipService` - Membership queries
- `SessionService` - Session management
- `AuthenticationService` - Login/authentication

### Constraint:
✅ **Controllers can ONLY call service methods** - they do not contain business logic.

---

## Session Management

Sessions are managed through `SessionService`:
- Stores current logged-in user
- Retrieved through `sessionService.getCurrent()`
- Contains user info: ID, username, role, etc.
- Cleared on logout

---

## Error Handling

Each controller includes:
- Try-catch blocks for service calls
- User-friendly error messages
- Null checks for navigation states
- Validation before state transitions

---

## Testing the Flow

### To Test Login:
1. Run `Main.java`
2. LoginUI appears
3. Login with credentials:
   - Username: (any registered user)
   - Password: (corresponding password)

### To Test STAFF Booking:
1. Login as STAFF user
2. Select movie (double-click)
3. Select showtime (double-click)
4. Select seats (click seats, then confirm)
5. Complete booking

### To Test MANAGER Admin:
1. Login as MANAGER user
2. Access admin dashboard
3. Perform CRUD operations

---

## Key Design Principles

1. **Single Responsibility**: Each controller has one clear purpose
2. **Separation of Concerns**: UI logic separate from business logic
3. **Read-Only Service Access**: Controllers don't modify business logic
4. **Navigation Centralization**: All screen transitions go through NavigationController
5. **State Management**: Booking flow state maintained in BookingController
6. **Role-Based Routing**: Different flows for STAFF and MANAGER
7. **Frame Management**: Single frame at a time, proper disposal

---

## File Structure

```
com/application/view/
├── controller/
│   ├── AppController.java          ← App lifecycle
│   ├── NavigationController.java   ← Screen navigation
│   ├── BookingController.java      ← Booking flow orchestration
│   └── AdminController.java        ← Manager operations
├── AppUI.java                      ← Entry point (refactored)
├── LoginUI.java                    ← Login (updated)
├── ListMovieUI.java                ← Movie selection (updated)
├── ShowTimeUI.java                 ← ShowTime selection (updated)
├── SoDoGhe.java                    ← Seat selection (updated)
├── BookingManagementUI.java        ← Booking confirmation
├── StaffManagement.java            ← Manager dashboard
├── ThongKeDoanhThu.java            ← Revenue reports
└── [other UI classes...]
```

---

## Important Notes

⚠️ **CRITICAL CONSTRAINTS**:
- ❌ Never modify service/entity/DTO/DAO packages
- ✅ Only add/modify code in `view` package
- ✅ Only call services for data, never modify them
- ✅ All navigation goes through NavigationController
- ✅ All booking state goes through BookingController
- ✅ All admin operations go through AdminController

---

## Future Enhancements

Potential improvements (maintaining constraints):
1. Add persistence for booking state (local cache)
2. Implement booking confirmation dialog
3. Add payment gateway integration UI
4. Add invoice generation UI
5. Add more detailed validation
6. Implement booking history view
7. Add membership selection UI
8. Implement admin reports dashboard


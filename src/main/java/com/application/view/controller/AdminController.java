package com.application.view.controller;

import com.application.entity.*;
import com.application.service.*;

import java.util.List;

public class AdminController {

    private final MovieService movieService;
    private final ShowTimeService showTimeService;
    private final RoomService roomService;
    private final UserService userService;

    private Long selectedMovieId;
    private Long selectedShowTimeId;
    private Long selectedRoomId;
    private Long selectedUserId;

    public AdminController(MovieService movieService,
                           ShowTimeService showTimeService,
                           RoomService roomService,
                           UserService userService) {
        this.movieService = movieService;
        this.showTimeService = showTimeService;
        this.roomService = roomService;
        this.userService = userService;
    }

    // Movie
    public List<Movie> getAllMovies()            { return movieService.findAll(); }
    public Movie getMovieById(Long id)           { return movieService.findById(id); }
    public void selectMovie(Long id)             { this.selectedMovieId = id; }
    public Long getSelectedMovieId()             { return selectedMovieId; }

    // ShowTime
    public List<ShowTime> getAllShowTimes()       { return showTimeService.findAll(); }
    public ShowTime getShowTimeById(Long id)      { return showTimeService.findById(id); }
    public void selectShowTime(Long id)           { this.selectedShowTimeId = id; }
    public Long getSelectedShowTimeId()           { return selectedShowTimeId; }

    // Room
    public List<Room> getAllRooms()              { return roomService.findAll(); }
    public Room getRoomById(Integer id)             { return roomService.findById(id); }
    public void selectRoom(Long id)              { this.selectedRoomId = id; }
    public Long getSelectedRoomId()              { return selectedRoomId; }

    // Staff
    public List<User> getAllStaff()              { return userService.findAll(); }
    public User getUserById(Long id)             { return userService.findById(id); }
    public void selectUser(Long id)              { this.selectedUserId = id; }
    public Long getSelectedUserId()              { return selectedUserId; }
}
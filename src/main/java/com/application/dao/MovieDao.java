package com.application.dao;

import com.application.entity.Genre;
import com.application.entity.Movie;
import com.application.entity.Room;
import com.application.entity.enums.MovieStatus;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class MovieDao {
    private final Connection connectionDB;

    public Long createMovie(Movie movie) {
        String sql = "INSERT INTO movies (" +
                "    title, " +
                "    description, " +
                "    director, " +
                "    cast, " +
                "    poster_path, " +
                "    release_date, " +
                "    end_date, " +
                "    language, " +
                "    subtitle_language, " +
                "    country, " +
                "    age_rating, " +
                "    status " +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            connectionDB.setAutoCommit(false);

            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getDescription());
            statement.setString(3, movie.getDirector());
            statement.setString(4, movie.getCast());
            statement.setString(5, movie.getPosterPath());
            statement.setTimestamp(6, movie.getReleaseDate());
            statement.setTimestamp(7, movie.getEndDate());
            statement.setString(8, movie.getLanguage());
            statement.setString(9, movie.getSubtitleLanguage());
            statement.setString(10, movie.getCountry());
            statement.setString(11, movie.getAgeRating());
            statement.setString(12, movie.getStatus().name());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("Tao room khong thanh cong");
            }

            ResultSet rows = statement.getGeneratedKeys();
            rows.next();
            long movieId = rows.getLong(1);

            List<Integer> genreIds = movie.getGenres().stream().map(Genre::getId).toList();
            setMovieGenre(movieId, genreIds);

            connectionDB.commit();

            return movieId;
        } catch (SQLException e) {
            try {
                connectionDB.rollback();
            } catch (SQLException ignore) {}
                throw new RuntimeException("Tao movie khong thanh cong");
        } finally {
            try {
                connectionDB.setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }

    public List<Movie> findAll() {
        String sql ="SELECT * FROM movies;";

        try (Statement statement = connectionDB.createStatement()) {

            ResultSet rows = statement.executeQuery(sql);

            return mapResultToObj(rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Movie> findAllByStatus(MovieStatus status) {
        String sql ="SELECT * FROM movies WHERE status = ?;";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setString(1, status.name());
            ResultSet rows = statement.executeQuery(sql);

            return mapResultToObj(rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Movie> findById(Long id) {
        String sql = "SELECT * FROM movies WHERE id = ?;";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setLong(1, id);

            ResultSet rows = statement.executeQuery();

            List<Movie> rooms = mapResultToObj(rows);
            return (rooms.isEmpty()) ? Optional.empty() : Optional.of(rooms.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInfo(Movie movie) {
        String sql = "UPDATE movies SET " +
                    "title = ?, " +
                    "description = ?, " +
                    "director = ?, " +
                    "cast = ?, " +
                    "poster_path = ?, " +
                    "release_date = ?, " +
                    "end_date = ?, " +
                    "language = ?, " +
                    "subtitle_language = ?, " +
                    "country = ?, " +
                    "age_rating = ?, " +
                    "status = ? " +
                "WHERE id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            connectionDB.setAutoCommit(false);

            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getDescription());
            statement.setString(3, movie.getDirector());
            statement.setString(4, movie.getCast());
            statement.setString(5, movie.getPosterPath());
            statement.setTimestamp(6, movie.getReleaseDate());
            statement.setTimestamp(7, movie.getEndDate());
            statement.setString(8, movie.getLanguage());
            statement.setString(9, movie.getSubtitleLanguage());
            statement.setString(10, movie.getCountry());
            statement.setString(11, movie.getAgeRating());
            statement.setString(12, movie.getStatus().name());
            statement.setLong(13, movie.getId());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("cap nhat movie info khong thanh cong");
            }

            List<Integer> genreIds = movie.getGenres().stream().map(Genre::getId).toList();
            setMovieGenre(movie.getId(), genreIds);

            connectionDB.commit();
        } catch (SQLException e) {
            try {
                connectionDB.rollback();
            } catch (SQLException ignore) {}
            throw new RuntimeException("Tao movie khong thanh cong");
        } finally {
            try {
                connectionDB.setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }

    public void updateStatus(Long id, MovieStatus status) {
        String sql = "UPDATE movies SET status = ? WHERE id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setLong(2, id);

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("cap nhat movie status khong thanh cong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Movie> mapResultToObj(ResultSet rows) throws SQLException {
        List<Movie> movies = new ArrayList<>();

        while (rows.next()) {
            movies.add(
                    Movie.builder()
                            .id(rows.getLong("id"))
                            .title(rows.getString("title"))
                            .description(rows.getString("description"))
                            .director(rows.getString("director"))
                            .cast(rows.getString("cast"))
                            .posterPath(rows.getString("poster_path"))
                            .releaseDate(rows.getTimestamp("release_date"))
                            .endDate(rows.getTimestamp("end_date"))
                            .language(rows.getString("language"))
                            .subtitleLanguage(rows.getString("subtitle_language"))
                            .country(rows.getString("country"))
                            .ageRating(rows.getString("age_rating"))
                            .status(MovieStatus.valueOf(rows.getString("status")))
                            .createdAt(rows.getTimestamp("created_at"))
                            .updatedAt(rows.getTimestamp("updated_at"))
                            .build()
            );
        }

        return movies;
    }

    private void setMovieGenre(Long movieId, List<Integer> genreIds) {
        String sql = "INSERT INTO movie_genre (movie_id, genre_id) VALUES (?, ?)";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            for (Integer genreId : genreIds) {
                statement.setLong(1, movieId);
                statement.setLong(2, genreId);
                statement.addBatch();
            }
            int rowUpdated = statement.executeUpdate();
            if (rowUpdated == 0) {
                throw  new RuntimeException("set movie genre khong thanh cong");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Set genre cho movie khong thanh cong");
        }
    }
}
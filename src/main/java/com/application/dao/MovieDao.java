package com.application.dao;

import com.application.entity.Movie;
import com.application.entity.enums.MovieStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.*;

@RequiredArgsConstructor
public class MovieDao {

    private final Connection connectionDB;

    public Long createMovie(Movie movie) {
        String sql = "INSERT INTO movies (" +
                "    title, " +
                "    description, " +
                "    director, " +
                "    cast, " +
                "    genre, " +
                "    duration, " +
                "    release_date, " +
                "    end_date, " +
                "    language, " +
                "    subtitle_language, " +
                "    country, " +
                "    age_rating, " +
                "    status " +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getDescription());
            statement.setString(3, movie.getDirector());
            statement.setString(4, movie.getCast());
            statement.setString(5, movie.getGenre());
            statement.setInt(6, movie.getDuration());
            statement.setTimestamp(7, movie.getReleaseDate());
            statement.setTimestamp(8, movie.getEndDate());
            statement.setString(9, movie.getLanguage());
            statement.setString(10, movie.getSubtitleLanguage());
            statement.setString(11, movie.getCountry());
            statement.setString(12, movie.getAgeRating());
            statement.setString(13, movie.getStatus().name());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("Tao movie khong thanh cong");
            }

            ResultSet rows = statement.getGeneratedKeys();
            rows.next();
            long movieId = rows.getLong(1);
            rows.close();
            return movieId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Tao movie khong thanh cong");
        }
    }

    public List<Movie> findAll() {
        String sql =
                "SELECT * FROM movies";

        try (Statement statement = connectionDB.createStatement()) {
            ResultSet rows = statement.executeQuery(sql);
            return mapResultToObj(rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Movie> findAllByStatus(MovieStatus status) {
        String sql =
                "SELECT * FROM movies WHERE status = ? ORDER BY id";

        try (PreparedStatement stmt = connectionDB.prepareStatement(sql)) {

            stmt.setString(1, status.name());

            try (ResultSet rs = stmt.executeQuery()) {
                return mapResultToObj(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Find movies by status failed", e);
        }
    }

    public Optional<Movie> findById(Long id) {
        String sql =
                "SELECT * FROM movies WHERE id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setLong(1, id);

            ResultSet rows = statement.executeQuery();
            List<Movie> movies = mapResultToObj(rows);

            return movies.isEmpty()
                    ? Optional.empty()
                    : Optional.of(movies.getFirst());

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
                "genre = ?, " +
                "duration = ?, " +
                "release_date = ?, " +
                "end_date = ?, " +
                "language = ?, " +
                "subtitle_language = ?, " +
                "country = ?, " +
                "age_rating = ? " +
                "WHERE id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            connectionDB.setAutoCommit(false);

            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getDescription());
            statement.setString(3, movie.getDirector());
            statement.setString(4, movie.getCast());
            statement.setString(5, movie.getGenre());
            statement.setInt(6, movie.getDuration());
            statement.setTimestamp(7, movie.getReleaseDate());
            statement.setTimestamp(8, movie.getEndDate());
            statement.setString(9, movie.getLanguage());
            statement.setString(10, movie.getSubtitleLanguage());
            statement.setString(11, movie.getCountry());
            statement.setString(12, movie.getAgeRating());
            statement.setLong(13, movie.getId());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Cap nhat movie info khong thanh cong");
            }

            connectionDB.commit();
        } catch (SQLException e) {
            try {
                connectionDB.rollback();
            } catch (SQLException ignore) {}

            System.out.println(e.getMessage());
            throw new RuntimeException("Cap nhat movie khong thanh cong");
        } finally {
            try {
                connectionDB.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
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

            Movie movie = Movie.builder()
                    .id(rows.getLong("id"))
                    .title(rows.getString("title"))
                    .description(rows.getString("description"))
                    .director(rows.getString("director"))
                    .cast(rows.getString("cast"))
                    .genre(rows.getString("genre"))
                    .duration(rows.getInt("duration"))
                    .releaseDate(rows.getTimestamp("release_date"))
                    .endDate(rows.getTimestamp("end_date"))
                    .language(rows.getString("language"))
                    .subtitleLanguage(rows.getString("subtitle_language"))
                    .country(rows.getString("country"))
                    .ageRating(rows.getString("age_rating"))
                    .status(MovieStatus.valueOf(rows.getString("status")))
                    .createdAt(rows.getTimestamp("created_at"))
                    .updatedAt(rows.getTimestamp("updated_at"))
                    .build();

            movies.add(movie);
        }
        return movies;
    }
}
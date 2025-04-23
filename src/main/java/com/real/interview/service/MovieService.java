package com.real.interview.service;

import com.real.interview.entity.Movie;
import com.real.interview.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie updateMovie(Long id, Movie updatedMovie) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movie.setTitle(updatedMovie.getTitle());
                    movie.setReleaseYear(updatedMovie.getReleaseYear());
                    return movieRepository.save(movie);
                })
                .orElse(null);
    }

    public boolean deleteMovie(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Movie> searchMovies(String title, Integer releaseYear) {
        if (title != null && releaseYear != null) {
            return movieRepository.findByTitleAndReleaseYear(title, releaseYear);
        } else if (title != null) {
            return movieRepository.findByTitle(title);
        } else if (releaseYear != null) {
            return movieRepository.findByReleaseYear(releaseYear);
        } else {
            return movieRepository.findAll(); // Return all if no search criteria provided
        }
    }
}

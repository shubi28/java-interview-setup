package com.real.interview.service;

import com.real.interview.entity.Movie;
import com.real.interview.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // For Mockito
public class MovieServiceTest {
    @Mock
    private MovieRepository movieRepository;

    @InjectMocks // Inject mocks into MovieService
    private MovieService movieService;

    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void setUp() {
        movie1 = new Movie("The Matrix", 1999);
        movie2 = new Movie("The Matrix Reloaded", 2003);
    }

    @Test
    void testCreateMovie() {
        // Given
        when(movieRepository.save(any(Movie.class))).thenReturn(movie1);

        // When
        Movie createdMovie = movieService.createMovie(movie1);

        // Then
        assertThat(createdMovie).isEqualTo(movie1);
        verify(movieRepository).save(movie1);
    }

    @Test
    void testGetAllMovies() {
        // Given
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));

        // When
        List<Movie> movies = movieService.getAllMovies();

        // Then
        assertThat(movies).hasSize(2);
        assertThat(movies).containsExactly(movie1, movie2);
        verify(movieRepository).findAll();
    }
    @Test
    void testGetMovieById() {
        // Given
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie1));

        // When
        Optional<Movie> foundMovie = movieService.getMovieById(1L);

        // Then
        assertThat(foundMovie).isPresent();
        assertThat(foundMovie.get()).isEqualTo(movie1);
        verify(movieRepository).findById(1L);
    }

    @Test
    void testUpdateMovie() {
        // Given
        Movie existingMovie = new Movie("The Matrix", 1999);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(any(Movie.class))).thenAnswer(i -> i.getArguments()[0]); //Return saved movie
        Movie updatedMovie = new Movie("The Matrix Updated", 2002);

        // When
        Movie result = movieService.updateMovie(1L, updatedMovie);

        //Then
        assertThat(result.getTitle()).isEqualTo("The Matrix Updated");
        assertThat(result.getReleaseYear()).isEqualTo(2002);
        verify(movieRepository).findById(1L);
        verify(movieRepository).save(any(Movie.class));
    }
    @Test
    void testDeleteMovie() {
        //Given
        when(movieRepository.existsById(1L)).thenReturn(true);
        //When
        boolean result = movieService.deleteMovie(1L);

        //Then
        assertThat(result).isTrue();
        verify(movieRepository).existsById(1L);
        verify(movieRepository).deleteById(1L);
    }

    @Test
    void testSearchMoviesByTitle() {
        // Given
        when(movieRepository.findByTitle("Matrix")).thenReturn(List.of(movie1));

        // When
        List<Movie> foundMovies = movieService.searchMovies("Matrix", null);

        // Then
        assertThat(foundMovies).hasSize(1);
        assertThat(foundMovies.get(0)).isEqualTo(movie1);
        verify(movieRepository).findByTitle("Matrix");
    }

    @Test
    void testSearchMoviesByReleaseYear() {
        // Given
        when(movieRepository.findByReleaseYear(1999)).thenReturn(List.of(movie1));

        // When
        List<Movie> foundMovies = movieService.searchMovies(null, 1999);

        // Then
        assertThat(foundMovies).hasSize(1);
        assertThat(foundMovies.get(0)).isEqualTo(movie1);
        verify(movieRepository).findByReleaseYear(1999);
    }

    @Test
    void testSearchMoviesByTitleAndReleaseYear() {
        // Given
        when(movieRepository.findByTitleAndReleaseYear("Matrix", 1999))
                .thenReturn(List.of(movie1));

        // When
        List<Movie> foundMovies = movieService.searchMovies("Matrix", 1999);

        // Then
        assertThat(foundMovies).hasSize(1);
        assertThat(foundMovies.get(0)).isEqualTo(movie1);
        verify(movieRepository).findByTitleAndReleaseYear("Matrix", 1999);
    }

    @Test
    void testSearchMoviesNoCriteria() {
        // Given
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));

        // When
        List<Movie> foundMovies = movieService.searchMovies(null, null);

        // Then
        assertThat(foundMovies).hasSize(2);
        assertThat(foundMovies).containsExactly(movie1, movie2);
        verify(movieRepository).findAll();
    }
}

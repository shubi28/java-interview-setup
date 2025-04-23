package com.real.interview.repository;

import com.real.interview.entity.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class MovieRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void testFindByTitle() {
        // Given
        Movie movie1 = new Movie("The Matrix", 1999);
        Movie movie2 = new Movie("Matrix Reloaded", 2003);
        entityManager.persist(movie1);
        entityManager.persist(movie2);
        entityManager.flush();

        // When
        List<Movie> foundMovies = movieRepository.findByTitle("matrix");

        // Then
        assertThat(foundMovies).hasSize(2);
        assertThat(foundMovies.get(0).getTitle()).containsIgnoringCase("Matrix");
        assertThat(foundMovies.get(1).getTitle()).containsIgnoringCase("Matrix");
    }

    @Test
    public void testFindByReleaseYear() {
        // Given
        Movie movie1 = new Movie("The Matrix", 1999);
        Movie movie2 = new Movie("Fight Club", 1999);
        entityManager.persist(movie1);
        entityManager.persist(movie2);
        entityManager.flush();

        // When
        List<Movie> foundMovies = movieRepository.findByReleaseYear(1999);

        // Then
        assertThat(foundMovies).hasSize(2);
        assertThat(foundMovies.get(0).getReleaseYear()).isEqualTo(1999);
        assertThat(foundMovies.get(1).getReleaseYear()).isEqualTo(1999);
    }
}

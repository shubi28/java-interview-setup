package com.real.interview.repository;

import com.real.interview.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>{

    List<Movie> findByTitle(String title);
    List<Movie> findByReleaseYear(Integer releaseYear);
    List<Movie> findByTitleAndReleaseYear(String title, Integer releaseYear);

}

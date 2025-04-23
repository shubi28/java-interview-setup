package com.real.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.real.interview.entity.Movie;
import com.real.interview.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(MovieController.class) // Test only the MovieController
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // For converting objects to JSON

    @MockBean
    private MovieService movieService; // Mock the MovieService

    @Test
    public void testCreateMovie() throws Exception {
        // Given
        Movie movieToCreate = new Movie("The Matrix", 1999);
        when(movieService.createMovie(any(Movie.class))).thenReturn(movieToCreate);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieToCreate)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("The Matrix"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(1999));
        verify(movieService).createMovie(any(Movie.class));
    }

    @Test
    public void testGetAllMovies() throws Exception {
        // Given
        Movie movie1 = new Movie("The Matrix", 1999);
        Movie movie2 = new Movie("The Matrix Reloaded", 2003);
        when(movieService.getAllMovies()).thenReturn(List.of(movie1, movie2));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("The Matrix"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("The Matrix Reloaded"));
        verify(movieService).getAllMovies();
    }
    @Test
    void testGetMovieById_Found() throws Exception {
        //Given
        Movie movie = new Movie("The Matrix", 1999);
        when(movieService.getMovieById(1L)).thenReturn(Optional.of(movie));

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("The Matrix"));
        verify(movieService).getMovieById(1L);

    }

    @Test
    void testGetMovieById_NotFound() throws Exception{
        //Given
        when(movieService.getMovieById(1L)).thenReturn(Optional.empty());

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        verify(movieService).getMovieById(1L);
    }

//    @Test
//    void testUpdateMovie_Found() throws Exception{
//        //Given
//        Movie updatedMovie = new Movie("The Matrix Updated", 2002);
//        when(movieService.updateMovie(1L, updatedMovie)).thenReturn(updatedMovie);
//
//        //When & Then
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/movies/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedMovie)))
//                .andExpect(MockMvcResultMatchers.status().isOk()) // Expect 200 OK
//                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("The Matrix Updated"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(2002));
//        verify(movieService).updateMovie(1L, updatedMovie);
//    }

//    @Test
//    void testUpdateMovie_NotFound() throws Exception{
//        //Given
//        Movie updatedMovie = new Movie("The Matrix Updated", 2002);
//        when(movieService.updateMovie(1L, updatedMovie)).thenReturn(null);
//
//        //When & Then
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/movies/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedMovie)))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//        verify(movieService).updateMovie(1L, updatedMovie);
//    }

    @Test
    void testDeleteMovie_Found() throws Exception{
        //Given
        when(movieService.deleteMovie(1L)).thenReturn(true);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(movieService).deleteMovie(1L);
    }

    @Test
    void testDeleteMovie_NotFound() throws Exception {
        // Given
        when(movieService.deleteMovie(1L)).thenReturn(false);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        verify(movieService).deleteMovie(1L);
    }
    @Test
    public void testSearchMoviesByTitle() throws Exception {
        // Given
        Movie movie1 = new Movie("The Matrix", 1999);
        when(movieService.searchMovies("Matrix", null)).thenReturn(List.of(movie1));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/search?title=Matrix"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("The Matrix"));
        verify(movieService).searchMovies("Matrix", null);
    }

    @Test
    public void testSearchMoviesByReleaseYear() throws Exception {
        // Given
        Movie movie1 = new Movie("The Matrix", 1999);
        when(movieService.searchMovies(null, 1999)).thenReturn(List.of(movie1));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/search?releaseYear=1999"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("The Matrix"));
        verify(movieService).searchMovies(null, 1999);
    }
    @Test
    public void testSearchMoviesByTitleAndReleaseYear() throws Exception {
        // Given
        Movie movie1 = new Movie("The Matrix", 1999);
        when(movieService.searchMovies("The Matrix", 1999)).thenReturn(List.of(movie1));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/search?title=The Matrix&releaseYear=1999"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("The Matrix"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseYear").value(1999));
        verify(movieService).searchMovies("The Matrix", 1999);
    }
    @Test
    public void testSearchMoviesNoCriteria() throws Exception {
        // Given
        Movie movie1 = new Movie("The Matrix", 1999);
        Movie movie2 = new Movie("The Matrix Reloaded", 2003);
        when(movieService.searchMovies(null, null)).thenReturn(List.of(movie1, movie2));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/search"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("The Matrix"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("The Matrix Reloaded"));
        verify(movieService).searchMovies(null, null);
    }

}

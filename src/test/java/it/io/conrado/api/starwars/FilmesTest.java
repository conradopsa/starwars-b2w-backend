package it.io.conrado.api.starwars;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.conrado.api.starwars.services.FilmesService;

public class FilmesTest {
    
    FilmesService f = new FilmesService();

    @Test()
    void testFilmesServiceTatooine() {
        assertEquals(f.countMoviesByName("Tatooine"), 5);
    }

    @Test()
    void testFilmesServiceHothNotCaseSensitive() {
        assertEquals(f.countMoviesByName("Hoth"), 1);
    }

    @Test()
    void testFilmesServiceMirialZero() {
        assertEquals(f.countMoviesByName("Mirial"), 0);
    }

    @Test()
    void testFilmesServiceNotFound() {
        assertEquals(f.countMoviesByName("qualquerNome"), 0);
    }
}

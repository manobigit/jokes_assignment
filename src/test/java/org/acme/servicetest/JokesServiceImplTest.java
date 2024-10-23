package org.acme.servicetest;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.BadRequestException;
import org.acme.dtos.JokesDto;
import org.acme.entity.Jokes;
import org.acme.integration.JokeClient;
import org.acme.repository.JokesRepo;
import org.acme.service.JokesServiceImpl;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class JokesServiceImplTest {

    @Mock
    @RestClient
    JokeClient jokeClient;

    @Mock
    JokesRepo jokesRepo;

    @InjectMocks
    JokesServiceImpl jokesService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetJokesByCount_ValidCount() {
        // Prepare mocks
        int count = 5;
        JokesDto mockJokeDto = new JokesDto("Why did the chicken cross the road?", "To get to the other side.");
        Jokes mockJoke = new Jokes();
        mockJoke.setQuestion(mockJokeDto.getSetup());
        mockJoke.setAnswer(mockJokeDto.getPunchline());

        when(jokeClient.getJokes()).thenReturn(mockJokeDto);
        when(jokesRepo.findJokeByQuestionAndAnswer(anyString(), anyString())).thenReturn(null);
        doNothing().when(jokesRepo).saveJokes(any(Jokes.class));

        List<Jokes> result = jokesService.getJokesByCount(count);

        // Assert
        assertNotNull(result);
        assertEquals(count, result.size());
        verify(jokeClient, times(count)).getJokes();  // Verifies that `jokeClient.getJokes()` was called `count` times
        verify(jokesRepo, times(count)).saveJokes(any(Jokes.class));  // Verifies that each joke was saved
    }

    @Test
    public void testGetJokesByCount_InvalidCount_ThrowsException() {
        int invalidCountLow = 0;
        int invalidCountHigh = 101;

        //Assert
        assertThrows(BadRequestException.class, () -> jokesService.getJokesByCount(invalidCountLow));
        assertThrows(BadRequestException.class, () -> jokesService.getJokesByCount(invalidCountHigh));
    }

    @Test
    public void testGetJokesByCount_ValidCountButDuplicateJoke() {

        int count = 3;
        JokesDto mockJokeDto = new JokesDto("Why did the chicken cross the road?", "To get to the other side.");
        Jokes mockJoke = new Jokes();
        mockJoke.setQuestion(mockJokeDto.getSetup());
        mockJoke.setAnswer(mockJokeDto.getPunchline());

        when(jokeClient.getJokes()).thenReturn(mockJokeDto);
        when(jokesRepo.findJokeByQuestionAndAnswer(mockJokeDto.getSetup(), mockJokeDto.getPunchline()))
                .thenReturn(mockJoke); // Simulate joke already exists

        List<Jokes> result = jokesService.getJokesByCount(count);

        // Assert
        assertNotNull(result);
        assertEquals(count, result.size());
        verify(jokesRepo, never()).saveJokes(any(Jokes.class)); // Ensure jokesRepo.saveJokes() is never called since the joke already exists
    }

    @Test
    public void testFetchJokes_ClientThrowsException() {

        int batchSize = 2;
        when(jokeClient.getJokes()).thenThrow(new RuntimeException("Joke service unavailable"));

        //Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            jokesService.getJokesByCount(batchSize);
        });

        assertEquals("Joke service unavailable", exception.getMessage());
    }
}

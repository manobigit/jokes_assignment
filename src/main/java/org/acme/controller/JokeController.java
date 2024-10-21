package org.acme.controller;

import jakarta.ws.rs.*;
import org.acme.dtos.JokesDto;
import org.acme.integration.JokeClient;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is used to fetch the jokes from the client by get operation in batches of 10.
 */
@Path("/jokes")
public class JokeController {

    @Inject
    @RestClient
    JokeClient jokeClient;

    private static final Logger log = LoggerFactory.getLogger(JokeController.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponseSchema(value = JokesDto.class,
            responseDescription = "List of jokes.",
            responseCode = "200")
    @Operation(
            summary = "Returns a list of jokes.",
            description = "Returns a list of jokes.")
    public List<JokesDto> getJokesByBatches(@QueryParam("count") int count) {
        log.info("GET /jokes " + count);
        // Validate the count parameter
        if (count <= 0 || count > 100) {
            log.error("count is not in valid range " + count);
            throw new BadRequestException("Count must be between 1 and 100.");
        }

        List<JokesDto> jokesList = new ArrayList<>();

        // Fetch jokes in batches of 10
        int batchSize = 10;
        int batches = (count + batchSize - 1) / batchSize; // Calculate the number of batches

        for (int batch = 0; batch < batches; batch++) {
            int currentBatchSize = Math.min(batchSize, count - (batch * batchSize));

            List<JokesDto> batchJokes = fetchJokes(currentBatchSize);

            // Add the jokes from the current batch to the main list
            jokesList.addAll(batchJokes);
        }

        return jokesList;
    }

    private List<JokesDto> fetchJokes(int batchSize) {
        List<JokesDto> jokes = new ArrayList<>();

        // Fetch jokes in the specified batch size
        for (int i = 0; i < batchSize; i++) {
            try {
                // Assuming jokeClient.getJokes() returns a Joke
                jokes.add(jokeClient.getJokes());
            } catch (Exception e) {
                //  show the exception failed joke fetch
                log.error("failed while fetching jokes from client " + e.getMessage());
                throw new BadRequestException(e.getMessage());
            }
        }

        return jokes;
    }

}

package org.acme.controller;

import jakarta.ws.rs.*;
import org.acme.dtos.JokesDto;
import org.acme.entity.Jokes;

import org.acme.service.JokesService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;

import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;


/**
 * This class is used to fetch the jokes from the client by get operation in batches of 10.
 */
@Path("/jokes")
public class JokeController {


    private static final Logger log = LoggerFactory.getLogger(JokeController.class);

    private JokesService jokesService;

    public JokeController(JokesService jokesService) {
        this.jokesService = jokesService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponseSchema(value = JokesDto.class,
            responseDescription = "List of jokes.",
            responseCode = "200")
    @Operation(
            summary = "Returns a list of jokes.",
            description = "Returns a list of jokes.")
    public List<Jokes> getJokesByBatches(@QueryParam("count") int count) {
        log.info("GET /jokes " + count);
        return jokesService.getJokesByCount(count);
    }
}

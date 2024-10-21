package org.acme.integration;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.dtos.JokesDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * This interface acts as a REST client for interacting with the external Joke API
 * located at "https://official-joke-api.appspot.com/random_joke". It provides a method to
 * retrieve random jokes in the form of a JSON object, represented by the JokesDto class.
 * <p>
 * The @RegisterRestClient annotation allows this interface to be used as a
 * REST client in a Quarkus application, automatically handling requests to
 * the Joke API.
 */
@RegisterRestClient(baseUri = "https://official-joke-api.appspot.com/")
public interface JokeClient {

    @GET
    @Path("/random_joke")
    JokesDto getJokes();

}

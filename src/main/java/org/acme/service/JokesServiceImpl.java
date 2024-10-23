package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

import org.acme.dtos.JokesDto;
import org.acme.entity.Jokes;
import org.acme.integration.JokeClient;
import org.acme.repository.JokesRepo;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class JokesServiceImpl implements JokesService {

    private static final Logger log = LoggerFactory.getLogger(JokesServiceImpl.class);

    private JokeClient jokeClient;
    private JokesRepo repo;

    public JokesServiceImpl(@RestClient JokeClient jokeClient, JokesRepo repo) {
        this.jokeClient = jokeClient;
        this.repo = repo;
    }

    @Override
    @Transactional
    public List<Jokes> getJokesByCount(int count) {
        if (count <= 0 || count > 100) {
            log.error("count is not in valid range " + count);
            throw new BadRequestException("Count must be between 1 and 100.");
        }

        List<Jokes> jokesList = new ArrayList<>();

        int batchSize = 10;
        int batches = (count + batchSize - 1) / batchSize;

        for (int batch = 0; batch < batches; batch++) {
            int currentBatchSize = Math.min(batchSize, count - (batch * batchSize));
            List<Jokes> batchJokes = fetchJokes(currentBatchSize);
            jokesList.addAll(batchJokes);
        }

        return jokesList;
    }


    private List<Jokes> fetchJokes(int batchSize) {
        List<Jokes> jokes = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            Jokes joke = new Jokes();
            try {
                JokesDto jokesDto = jokeClient.getJokes();
                if (null == repo.findJokeByQuestionAndAnswer(jokesDto.getSetup(), jokesDto.getPunchline())) {
                    log.info("joke does not exist");
                    joke.setQuestion(jokesDto.getSetup());
                    joke.setAnswer(jokesDto.getPunchline());
                    repo.saveJokes(joke);

                } else {
                    joke = repo.findJokeByQuestionAndAnswer(jokesDto.getSetup(), jokesDto.getPunchline());
                }
                jokes.add(joke);
            } catch (Exception e) {
                log.error("failed to fetch jokes due to "+e.getMessage());
                throw new BadRequestException(e.getMessage());
            }
        }

        return jokes;
    }
}

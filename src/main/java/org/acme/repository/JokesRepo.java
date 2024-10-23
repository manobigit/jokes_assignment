package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entity.Jokes;


@ApplicationScoped
public class JokesRepo implements PanacheRepository<Jokes> {

    public void saveJokes(Jokes joke) {
        persist(joke);
    }

    public Jokes findJokeByQuestionAndAnswer(String question, String answer) {
        return find("question =?1 AND answer = ?2", question, answer).firstResult();
    }
}

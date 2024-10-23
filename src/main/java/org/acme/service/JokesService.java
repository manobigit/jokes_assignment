package org.acme.service;

import org.acme.entity.Jokes;

import java.util.List;

public interface JokesService {

    List<Jokes> getJokesByCount(int count);
}

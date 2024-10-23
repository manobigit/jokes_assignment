package org.acme.entity;

import jakarta.persistence.*;

@Entity
public class Jokes {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String question;
    private String answer;

    public String getId() {
        return id;
    }

    public Jokes() {
    }

    @Override
    public String toString() {
        return "Jokes{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

    public Jokes(String id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

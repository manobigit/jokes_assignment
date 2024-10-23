package org.acme.dtos;

public class JokesDto {
    private String type;
    private String setup;
    private String punchline;
    private long id;

    public JokesDto() {
    }

    public JokesDto(String type, String setup, String punchline, long id) {
        this.type = type;
        this.setup = setup;
        this.punchline = punchline;
        this.id = id;
    }

    public JokesDto(String setup, String punchline) {
        this.setup = setup;
        this.punchline = punchline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public String getPunchline() {
        return punchline;
    }

    public void setPunchline(String punchline) {
        this.punchline = punchline;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "JokesDto{" +
                "type='" + type + '\'' +
                ", setup='" + setup + '\'' +
                ", punchline='" + punchline + '\'' +
                ", id=" + id +
                '}';
    }
}

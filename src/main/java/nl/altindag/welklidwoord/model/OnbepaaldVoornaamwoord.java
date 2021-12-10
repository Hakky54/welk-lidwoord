package nl.altindag.welklidwoord.model;

public enum OnbepaaldVoornaamwoord {

    ELKE("elke"),
    ELK("elk");

    private final String value;

    OnbepaaldVoornaamwoord(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}

package nl.altindag.welklidwoord.model;

public enum BezittelijkVoornaamwoord {

    ONZE("onze"),
    ONS("ons");

    private final String value;

    BezittelijkVoornaamwoord(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}

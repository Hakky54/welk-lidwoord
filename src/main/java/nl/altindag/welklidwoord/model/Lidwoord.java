package nl.altindag.welklidwoord.model;

public enum Lidwoord {

    DE("de"),
    HET("het");

    private String value;

    Lidwoord(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }
}

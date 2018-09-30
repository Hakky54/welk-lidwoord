package nl.altindag.welklidwoord.model;

public enum AanwijzendVoornaamwoordVer {

    DIE("die"),
    DAT("dat");

    private String value;

    AanwijzendVoornaamwoordVer(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }
}

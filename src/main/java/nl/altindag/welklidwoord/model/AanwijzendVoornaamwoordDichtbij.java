package nl.altindag.welklidwoord.model;

public enum AanwijzendVoornaamwoordDichtbij {

    DEZE("deze"),
    DIT("dit");

    private String value;

    AanwijzendVoornaamwoordDichtbij(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }
}

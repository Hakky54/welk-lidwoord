package nl.altindag.welklidwoord.model;

public enum Field {

    DE_OF_HET("de of het"),
    DEZE_OF_DIT("deze of dit"),
    ELK_OF_ELKE("elk of elke"),
    DIE_OF_DAT("die of dat"),
    ONS_OF_ONZE("ons of onze");

    private String value;

    Field(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }
}

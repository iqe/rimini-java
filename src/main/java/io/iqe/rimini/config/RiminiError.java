package io.iqe.rimini.config;

public enum RiminiError {
    /* Common Errors */
    UNKNOWN_FEATURE_ID(10),
    MESSAGE_TOO_SMALL(11),

    /* Feature Repository */
    PINS_IN_USE(101),
    NO_FEATURE_FACTORY_METHOD(102),
    CREATE_FEATURE_FAILED(103);

    private int errorcode;

    private RiminiError(int errorcode) {
        this.errorcode = errorcode;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public static RiminiError fromErrorcode(int errorcode) {
        for (RiminiError error : values()) {
            if (error.getErrorcode() == errorcode) {
                return error;
            }
        }
        throw new IllegalArgumentException(String.format("Unknown error code: %d", errorcode));
    }
}

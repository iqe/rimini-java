package io.iqe.rimini.config;

public class CreateFeatureResponse extends AbstractConfigAction {
    private int errorCode;

    public CreateFeatureResponse(int errorCode) {
        super(ActionTypes.CONFIG_RSP_CREATE);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return String.format("%s: %d", super.toString(), errorCode);
    }
}

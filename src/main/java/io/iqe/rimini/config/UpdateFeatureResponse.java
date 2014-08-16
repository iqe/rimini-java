package io.iqe.rimini.config;

public class UpdateFeatureResponse extends AbstractConfigAction {
    private int errorCode;

    public UpdateFeatureResponse(int errorCode) {
        super(ActionTypes.CONFIG_RSP_UPDATE);
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

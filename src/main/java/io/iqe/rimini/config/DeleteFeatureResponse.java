package io.iqe.rimini.config;

public class DeleteFeatureResponse extends AbstractConfigAction {
    private int errorCode;

    protected DeleteFeatureResponse(int errorCode) {
        super(ActionTypes.CONFIG_RSP_DELETE);
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

package io.iqe.rimini.config;

public abstract class AbstractConfigAction {
    private int actionId;

    protected AbstractConfigAction(int actionId) {
        this.actionId = actionId;
    }

    public int getActionId() {
        return actionId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}

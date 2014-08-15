package io.iqe.rimini.config;

public class VersionResponse extends AbstractConfigAction {
    private int major;
    private int minor;
    private int patch;

    public VersionResponse(int major, int minor, int patch) {
        super(ActionTypes.CONFIG_RSP_VERSION);
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public String getVersion() {
        return String.format("%d.%d.%d", major, minor, patch);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", super.toString(), getVersion());
    }
}

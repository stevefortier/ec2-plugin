package hudson.plugins.ec2;

import java.util.concurrent.TimeUnit;
import java.util.Objects;

import hudson.Extension;
import hudson.model.Descriptor;

import hudson.util.Secret;
import org.kohsuke.stapler.DataBoundConstructor;

public class WindowsData extends AMITypeData {

    private final Secret password;
    private final boolean useHTTPS;
    private final String bootDelay;
    private final boolean specifyPassword;

    @DataBoundConstructor
    public WindowsData(String password, boolean useHTTPS, String bootDelay, boolean  specifyPassword) {
        this.password = Secret.fromString(password);
        this.useHTTPS = useHTTPS;
        this.bootDelay = bootDelay;
        this.specifyPassword = specifyPassword;
        //Backwards compatibility
        if (!specifyPassword && !this.password.getPlainText().isEmpty()) {
            specifyPassword = true;
        }
    }

    public WindowsData(String password, boolean useHTTPS, String bootDelay) {
        this(password, useHTTPS, bootDelay, false);
    }

    @Override
    public boolean isWindows() {
        return true;
    }

    @Override
    public boolean isUnix() {
        return false;
    }

    @Override
    public Secret getPassword() {
        return password;
    }

    public boolean isUseHTTPS() {
        return useHTTPS;
    }

    public String getBootDelay() {
        return bootDelay;
    }

    @Override
    public boolean isSpecifyPassword() {
        return specifyPassword;
    }

    public int getBootDelayInMillis() {
        try {
            return (int) TimeUnit.SECONDS.toMillis(Integer.parseInt(bootDelay));
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<AMITypeData> {
        @Override
        public String getDisplayName() {
            return "windows";
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(password,useHTTPS, bootDelay, specifyPassword);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final WindowsData other = (WindowsData) obj;
        if (bootDelay == null) {
            if (other.bootDelay != null)
                return false;
        } else if (!bootDelay.equals(other.bootDelay))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        return useHTTPS == other.useHTTPS && specifyPassword == other.specifyPassword;
    }
}

package hudson.plugins.ec2;

import hudson.Extension;
import hudson.model.Descriptor;

import hudson.util.FormValidation;
import hudson.util.Secret;
import jenkins.model.Jenkins;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

public class UnixData extends AMITypeData {
    private final String rootCommandPrefix;
    private final String slaveCommandPrefix;
    private final String slaveCommandSuffix;
    private final String sshPort;
    private final Secret password;
    private final boolean specifyPassword;

    @DataBoundConstructor
    public UnixData(String rootCommandPrefix, String slaveCommandPrefix, String slaveCommandSuffix, String sshPort, String password, boolean specifyPassword) {
        this.password = Secret.fromString(password);
        this.rootCommandPrefix = rootCommandPrefix;
        this.slaveCommandPrefix = slaveCommandPrefix;
        this.slaveCommandSuffix = slaveCommandSuffix;
        this.sshPort = sshPort;
        this.specifyPassword = specifyPassword;
        //Backwards compatibility
        if (!specifyPassword && !this.password.getPlainText().isEmpty()) {
            specifyPassword = true;
        }

        this.readResolve();
    }

    public UnixData(String rootCommandPrefix, String slaveCommandPrefix, String slaveCommandSuffix, String sshPort) {
        this(rootCommandPrefix, slaveCommandPrefix, slaveCommandSuffix, sshPort, "", false);
    }

    protected Object readResolve() {
        Jenkins.get().checkPermission(Jenkins.RUN_SCRIPTS);
        return this;
    }

    @Override
    public boolean isWindows() {
        return false;
    }

    @Override
    public boolean isUnix() {
        return true;
    }

    @Override
    public Secret getPassword() {
        return password;
    }

    @Override
    public boolean isSpecifyPassword() {
        return specifyPassword;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<AMITypeData> {
        @Override
        public String getDisplayName() {
            return "unix";
        }

        @Restricted(NoExternalUse.class)
        public FormValidation doCheckRootCommandPrefix(@QueryParameter String value){
            if(StringUtils.isBlank(value) || Jenkins.get().hasPermission(Jenkins.RUN_SCRIPTS)){
                return FormValidation.ok();
            }else{
                return FormValidation.error(Messages.General_MissingPermission());
            }
        }

        @Restricted(NoExternalUse.class)
        public FormValidation doCheckSlaveCommandPrefix(@QueryParameter String value){
            if(StringUtils.isBlank(value) || Jenkins.get().hasPermission(Jenkins.RUN_SCRIPTS)){
                return FormValidation.ok();
            }else{
                return FormValidation.error(Messages.General_MissingPermission());
            }
        }

        @Restricted(NoExternalUse.class)
        public FormValidation doCheckSlaveCommandSuffix(@QueryParameter String value){
            if(StringUtils.isBlank(value) || Jenkins.get().hasPermission(Jenkins.RUN_SCRIPTS)){
                return FormValidation.ok();
            }else{
                return FormValidation.error(Messages.General_MissingPermission());
            }
        }
    }

    public String getRootCommandPrefix() {
        return rootCommandPrefix;
    }

    public String getSlaveCommandPrefix() {
        return slaveCommandPrefix;
    }

    public String getSlaveCommandSuffix() {
        return slaveCommandSuffix;
    }

    public String getSshPort() {
        return sshPort == null || sshPort.isEmpty() ? "22" : sshPort;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rootCommandPrefix == null) ? 0 : rootCommandPrefix.hashCode());
        result = prime * result + ((slaveCommandPrefix == null) ? 0 : slaveCommandPrefix.hashCode());
        result = prime * result + ((slaveCommandSuffix == null) ? 0 : slaveCommandSuffix.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + (specifyPassword ? 0 : 1);
        result = prime * result + ((sshPort == null) ? 0 : sshPort.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final UnixData other = (UnixData) obj;
        if (StringUtils.isEmpty(rootCommandPrefix)) {
            if (!StringUtils.isEmpty(other.rootCommandPrefix))
                return false;
        } else if (!rootCommandPrefix.equals(other.rootCommandPrefix))
            return false;
        if (StringUtils.isEmpty(slaveCommandPrefix)) {
            if (!StringUtils.isEmpty(other.slaveCommandPrefix))
                return false;
        } else if (!slaveCommandPrefix.equals(other.slaveCommandPrefix))
            return false;
        if (StringUtils.isEmpty(slaveCommandSuffix)) {
            if (!StringUtils.isEmpty(other.slaveCommandSuffix))
                return false;
        } else if (!slaveCommandSuffix.equals(other.slaveCommandSuffix))
            return false;
        if (StringUtils.isEmpty(sshPort)) {
            if (!StringUtils.isEmpty(other.sshPort))
                return false;
        } else if (!sshPort.equals(other.sshPort)) {
            return false;
        }
        else if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        return true;
    }
}

package hudson.plugins.ec2;

import hudson.model.AbstractDescribableImpl;
import hudson.util.Secret;

public abstract class AMITypeData extends AbstractDescribableImpl<AMITypeData> {
    public abstract boolean isWindows();

    public abstract boolean isUnix();

    public abstract Secret getPassword();

    public abstract boolean isSpecifyPassword();
}

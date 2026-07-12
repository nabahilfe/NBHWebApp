package eu.nabahilfe.webapp;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

@Service
public class VersionService {

    private final BuildProperties buildProperties;

    public VersionService(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    public String getVersion() {
        return buildProperties.getVersion();
    }

    public String getBuildTime() {
        return buildProperties.getTime().toString();
    }
}

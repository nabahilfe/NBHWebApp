package eu.nabahilfe.webapp;

import static eu.nabahilfe.webapp.DateFormatter.dateTimeDE;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
        LocalDateTime lt = LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault());
        return dateTimeDE(lt);
    }
}

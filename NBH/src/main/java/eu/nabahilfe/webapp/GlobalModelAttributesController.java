package eu.nabahilfe.webapp;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributesController {

    private final VersionService versionService;
    private final ReleaseNotesService releaseNotesService;

    private GlobalModelAttributes globalModelAttributes;

    public GlobalModelAttributesController(VersionService versionService, ReleaseNotesService releaseNotesService) {
        this.versionService = versionService;
        this.releaseNotesService = releaseNotesService;
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        if (globalModelAttributes == null) {
            globalModelAttributes = new GlobalModelAttributes(
                versionService.getVersion(),
                versionService.getBuildTime(),
                releaseNotesService.getReleaseNotesHtml()
            );
        }
        model.addAttribute("globalModelAttributes", globalModelAttributes);
    }
}
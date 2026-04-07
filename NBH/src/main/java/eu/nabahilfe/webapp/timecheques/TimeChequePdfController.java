package eu.nabahilfe.webapp.timecheques;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class TimeChequePdfController {
	private final TimeChequePdfPersonalizationService pdfPersonalizationService = new TimeChequePdfPersonalizationService();
	
	@GetMapping("/download/pdf")
	public void downloadPdf(HttpServletResponse response) {
		try {
			pdfPersonalizationService.personalizeTimeCheque("John", "123", response.getOutputStream());
			response.setContentType("application/pdf");
			response.setHeader("COntent-Disposition", "attachment; filename=\"ZeitSchecks.pdf\"");		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

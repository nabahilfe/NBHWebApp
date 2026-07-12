/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import eu.nabahilfe.webapp.textcontent.MarkdownService;

@Service
public class ReleaseNotesService {

    private final MarkdownService markdownService;

    public ReleaseNotesService(MarkdownService markdownService) {
        this.markdownService = markdownService;
    }

    public String getReleaseNotesHtml() {
        try {
            ClassPathResource resource = new ClassPathResource("Release Notes.md");
            try (InputStream is = resource.getInputStream()) {
                String markdown = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                return markdownService.toHtml(markdown);
            }
        } catch (IOException e) {
            return "<p>Release notes not available.</p>";
        }
    }
}

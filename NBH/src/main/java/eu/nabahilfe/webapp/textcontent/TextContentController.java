/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.textcontent;

import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;


@Controller
@RequestMapping("/textcontent")
public class TextContentController {

    private static final Logger log = LoggerFactory.getLogger(TextContentController.class);

    private final TextContentRepository repo;
    private final MarkdownService markdown;

    public TextContentController(TextContentRepository repo, MarkdownService markdown) {
        this.repo = repo;
        this.markdown = markdown;
    }


    @ModelAttribute("textContentForm")
    public TextContentForm addTextContentForm(HttpServletRequest request) {
        if (request.getSession().getAttribute("textContentForm") != null)
            return (TextContentForm) request.getSession().getAttribute("textContentForm");
        return new TextContentForm();
    }


    /* Liste */
    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER', 'SECRETARY')")
    @GetMapping
    public String list(Model model) {

        model.addAttribute("textContentList", repo.findAll());
        model.addAttribute("textContentTypes", Arrays.asList(TextContentType.values()));

        return "/list";
    }


    /* Edit/Create form */
    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER', 'SECRETARY')")
    @GetMapping({"/edit", "/edit/cc/{contentCode}"})
    public String edit(final Model model, @ModelAttribute("textContentForm") TextContentForm tcf, @PathVariable String contentCode) {

        if (contentCode != null) {
            tcf.setContentCode(contentCode);
            TextContentType value = TextContentType.valueOf(contentCode);
            tcf.setContentDescription(value.getCode());
        }

        Optional<TextContent> content = repo.findByContentCode(tcf.getContentCode());

        if (!content.isEmpty()) {
            log.debug("Loading existing TextContent for contentCode={}", tcf.getContentCode());
            tcf.setContentCode(content.get().getContentCode());
            tcf.setMdText(content.get().getMdText());
            tcf.setHtmlText(content.get().getHtmlText());
        }

        log.debug("Editing TextContent for {}", tcf.toString());

        return "textcontent/edit";
    }


    /* Preview markdown without saving */
    @PostMapping("/preview")
    @GetMapping("/preview")
    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER', 'SECRETARY')")
    public String preview(@ModelAttribute("textContentForm") TextContentForm tcf, Model model) {

        String html = markdown.toHtml(tcf.getMdText());
        tcf.setHtmlText(html);

        log.debug("Previewing TextContent for {}", tcf.toString());
        log.debug("HTML Text {}", html);

        return "textcontent/preview";
    }


    /* Save after preview */
    @Transactional
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'BOARD_MEMBER', 'SECRETARY')")
    public String save(@ModelAttribute("textContentForm") TextContentForm tcf, Model model) {

        TextContent content = repo.findByContentCode(tcf.getContentCode()).orElseGet(TextContent::new);

        content.setContentCode(tcf.getContentCode());
        content.setMdText(tcf.getMdText());
        content.setHtmlText(markdown.toHtml(Optional.ofNullable(tcf.getMdText()).orElse("")));

        repo.save(content);
        log.debug("Saved TextContent for contentCode={}", content.getContentCode());

        model.addAttribute("successMessage", "TextContent gespeichert.");
        return "textcontent/edit";
    }


}
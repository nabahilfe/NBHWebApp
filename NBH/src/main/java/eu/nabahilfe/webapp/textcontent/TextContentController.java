package eu.nabahilfe.webapp.textcontent;

import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /* Liste */

    @GetMapping
    public String list(Model model) {

        model.addAttribute("textContentList", repo.findAll());
        model.addAttribute("textContentTypes", Arrays.asList(TextContentType.values()));

        return "/list";
    }


    /* Edit/Create form */
    @GetMapping({"/edit", "/edit/cc/{contentCode}"})
    public String edit(final Model model, @PathVariable String contentCode) {

        TextContentForm tcf = new TextContentForm();
        if (contentCode != null) {
            tcf.setContentCode(contentCode);
            tcf.setContentDescription(contentCode);
        }

        Optional<TextContent> content = repo.findByContentCode(contentCode);

        if (!content.isEmpty()) {
            log.debug("Loading existing TextContent for contentCode={}", contentCode);
            tcf.setContentCode(content.get().getContentCode());
            tcf.setMdText(content.get().getMdText());
            tcf.setHtmlText(content.get().getHtmlText());
        }

        model.addAttribute("textContentForm", tcf);
        return "textcontent/edit";
    }


    /* Preview markdown without saving */
    @PostMapping("/preview")
    public String preview(@ModelAttribute TextContentForm textContentForm, Model model) {

        String html = markdown.toHtml(textContentForm.getMdText());
        log.debug("Markdown: {} -> HTML: {}", textContentForm.getMdText(), html);

        textContentForm.setHtmlText(html);

        model.addAttribute("textContentForm", textContentForm);
        return "textcontent/view";
    }


    /* Save after preview */
    @PostMapping("/save")
    public String save(@ModelAttribute TextContentForm textContentForm, Model model) {

        TextContent content = repo.findByContentCode(textContentForm.getContentCode())
                .orElseGet(TextContent::new);

        content.setContentCode(textContentForm.getContentCode());
        content.setMdText(textContentForm.getMdText());
        content.setHtmlText(markdown.toHtml(Optional.ofNullable(textContentForm.getMdText()).orElse("")));

        repo.save(content);
        log.debug("Saved TextContent for contentCode={}", content.getContentCode());

        model.addAttribute("textContentForm", textContentForm);

        model.addAttribute("successMessage", "TextContent gespeichert.");
        return "textcontent/edit";
    }


}
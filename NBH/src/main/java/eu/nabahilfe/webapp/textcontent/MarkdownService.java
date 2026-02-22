package eu.nabahilfe.webapp.textcontent;

import org.springframework.stereotype.Service;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

@Service
public class MarkdownService {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownService() {
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
    }

    public String toHtml(String markdown) {
        return renderer.render(parser.parse(markdown));
    }
}
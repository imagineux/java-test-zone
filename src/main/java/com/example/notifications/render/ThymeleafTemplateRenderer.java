package com.example.notifications.render;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

@Component
public class ThymeleafTemplateRenderer implements TemplateRenderer {

    private final TemplateEngine templateEngine;

    public ThymeleafTemplateRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String render(String templatePath, Map<String, Object> model) {
        Context context = new Context(Locale.US, model);
        return templateEngine.process(templatePath, context);
    }
}

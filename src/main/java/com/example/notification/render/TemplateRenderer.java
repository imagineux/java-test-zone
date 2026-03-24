package com.example.notification.render;

import java.util.Map;

public interface TemplateRenderer {
    String render(String templatePath, Map<String, Object> model);
}

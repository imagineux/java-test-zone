package com.example.notification.rendering;

import java.util.Map;

public interface TemplateRenderer {
    String render(String templatePath, Map<String, Object> model);
}

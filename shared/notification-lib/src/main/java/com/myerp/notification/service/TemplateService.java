package com.myerp.notification.service;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class TemplateService {
    
    public String processTemplate(String template, Map<String, Object> variables) {
        if (template == null || variables == null) {
            return template;
        }
        
        String result = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }
        
        return result;
    }
    
    public String getEmailTemplate(String templateName) {
        // Templates básicos embutidos
        return switch (templateName) {
            case "welcome" -> """
                <html>
                <body>
                    <h2>Bem-vindo ao MyERP!</h2>
                    <p>Olá {{nome}},</p>
                    <p>Seu login: <strong>{{login}}</strong></p>
                    <p>Acesse o sistema em: <a href="{{url}}">{{url}}</a></p>
                </body>
                </html>
                """;
            case "ponto-registro" -> """
                <html>
                <body>
                    <h3>Ponto Registrado</h3>
                    <p>Funcionário: {{nome}}</p>
                    <p>Tipo: {{tipo}}</p>
                    <p>Data/Hora: {{dataHora}}</p>
                </body>
                </html>
                """;
            default -> "<p>{{message}}</p>";
        };
    }
}
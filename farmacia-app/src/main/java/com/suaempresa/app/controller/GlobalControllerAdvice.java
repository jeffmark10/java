package com.suaempresa.app.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession; // <<<<<<< A CORREÇÃO ESTÁ AQUI
import java.time.Year;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    private static final Map<String, Map<String, Object>> SUBTOPIC_ROUTES_MAP;

    // Bloco estático para inicializar o mapa de forma segura quando a classe é carregada
    static {
        SUBTOPIC_ROUTES_MAP = new LinkedHashMap<>();

        Map<String, Object> saude = new LinkedHashMap<>();
        saude.put("display_name", "Saúde e Bem-estar");
        Map<String, String> saudeSubtopics = new LinkedHashMap<>();
        saudeSubtopics.put("vitaminas", "Vitaminas e Suplementos");
        saudeSubtopics.put("glicemia", "Controle de Glicemia");
        // Adicione outros subtópicos de saúde aqui...
        saude.put("subtopics", saudeSubtopics);
        SUBTOPIC_ROUTES_MAP.put("saude", saude);

        Map<String, Object> beleza = new LinkedHashMap<>();
        beleza.put("display_name", "Beleza");
        Map<String, String> belezaSubtopics = new LinkedHashMap<>();
        belezaSubtopics.put("pele", "Pele");
        belezaSubtopics.put("cabelo", "Cabelo");
        beleza.put("subtopics", belezaSubtopics);
        SUBTOPIC_ROUTES_MAP.put("beleza", beleza);

        // Adicione outras categorias principais aqui...
    }
    
    @ModelAttribute("year")
    public int getYear() {
        return Year.now().getValue();
    }

    @ModelAttribute("SUBTOPIC_ROUTES_MAP")
    public Map<String, Map<String, Object>> getSubtopicRoutesMap() {
        return SUBTOPIC_ROUTES_MAP;
    }

    @ModelAttribute("cart_item_count")
    public int getCartItemCount(HttpSession session) {
        // O cast precisa ser seguro, então verificamos o tipo
        Object cartObject = session.getAttribute("cart");
        if (!(cartObject instanceof List<?>)) {
            return 0;
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cart = (List<Map<String, Object>>) cartObject;
        
        return cart.stream()
                   .mapToInt(item -> {
                       Object quantity = item.get("quantity");
                       return (quantity instanceof Number) ? ((Number) quantity).intValue() : 0;
                   })
                   .sum();
    }
}
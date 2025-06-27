package com.suaempresa.app.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot; // Importação correta
import com.google.cloud.firestore.QuerySnapshot;
// import com.suaempresa.app.model.Product; // Removido pois não está em uso
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class HomeController {

    @Autowired
    private Firestore db; // Injeta a instância do Firestore

    // Mapeia as rotas '/' e '/inicio' para este método
    @GetMapping({"/", "/inicio"})
    public String home(Model model) throws ExecutionException, InterruptedException {
        // Lógica para buscar produtos em destaque (similar à de app.py)
        CollectionReference productsRef = db.collection("artifacts/default-app-id/public/data/products");
        ApiFuture<QuerySnapshot> future = productsRef.limit(3).get();
        
        // CORREÇÃO AQUI: A lista é de 'QueryDocumentSnapshot'
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Map<String, Object>> featuredProducts = new ArrayList<>();
        // E o loop itera sobre o tipo correto
        for (QueryDocumentSnapshot document : documents) {
            Map<String, Object> productData = document.toObject(Map.class);
            productData.put("id", document.getId());
            if (featuredProducts.isEmpty()) {
                productData.put("badge", "NOVIDADE");
            }
            featuredProducts.add(productData);
        }

        // Adiciona a lista de produtos ao modelo para ser usada no template
        model.addAttribute("featured_products", featuredProducts);

        // Retorna o nome do arquivo HTML em 'src/main/resources/templates'
        return "index";
    }
    
    // Outras rotas estáticas
    @GetMapping("/sobre")
    public String about() {
        return "about";
    }

    @GetMapping("/contato")
    public String contact() {
        return "contact";
    }
}
package com.suaempresa.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.io.InputStream;

// A anotação @Configuration é VITAL. Ela diz ao Spring para ler esta classe
// em busca de definições de Beans.
@Configuration
public class FirebaseConfig {

    // Lê o valor da propriedade 'app.firebase-configuration-file' do seu application.properties
    @Value("${app.firebase-configuration-file}")
    private Resource serviceAccount;

    // A anotação @Bean diz ao Spring: "execute este método e gerencie o objeto retornado".
    // Agora o Spring terá um Bean do tipo FirebaseApp.
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream serviceAccountStream = serviceAccount.getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .build();
        
        // Garante que o app não seja inicializado mais de uma vez
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }

    // Este é o Bean que está faltando!
    // O Spring vê que este método precisa de um FirebaseApp, e ele o obtém do método acima.
    // Em seguida, ele executa este método e cria o Bean do tipo Firestore.
    @Bean
    public Firestore firestore(FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore(firebaseApp);
    }
}
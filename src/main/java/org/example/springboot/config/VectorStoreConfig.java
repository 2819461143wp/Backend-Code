//package org.example.springboot.config;
//
//import jakarta.activation.DataSource;
//import org.springframework.ai.embedding.EmbeddingClient;
//import org.springframework.ai.openai.OpenAiEmbeddingClient;
//import org.springframework.ai.openai.api.OpenAiApi;
//import org.springframework.ai.vectorstore.PgVectorStore;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class VectorStoreConfig {
//
//    @Bean
//    public PgVectorStore pgVectorStore(DataSource dataSource) {
//        return new PgVectorStore(dataSource, "embeddings", TransformerEmbeddingClient.builder().build());
//    }
//
//    @Bean
//    public EmbeddingClient embeddingClient() {
//        return new OpenAiEmbeddingClient(new OpenAiApi(apiKey, baseUrl));
//    }
//}
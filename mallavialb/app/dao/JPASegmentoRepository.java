package dao;

import models.Segmento;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;
import java.util.concurrent.CompletableFuture;

public class JPASegmentoRepository implements SegmentoRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPASegmentoRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<String> saveSegmento(Segmento segmento) {
        return CompletableFuture.supplyAsync(() -> 
            jpaApi.withTransaction(entityManager -> {
                entityManager.persist(segmento);
                return "Segmento saved with ID: " + segmento.getId();
            })
        , executionContext);
    }
    
    @Override
    public CompletionStage<Segmento> findSegmento(Long id) {
        return CompletableFuture.supplyAsync(() ->
            jpaApi.withTransaction(entityManager -> {
                return entityManager.find(Segmento.class, id);
            })
        , executionContext);
    }
    
    @Override
    public CompletionStage<Stream<Segmento>> findAllSegmentos() {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(entityManager -> {
                return entityManager.createQuery("SELECT s FROM Segmento s", Segmento.class)
                                    .getResultList()
                                    .stream();
                
  
            });
        }, executionContext);
    }
    
    

    @Override
    public CompletionStage<String> updateSegmento(Segmento segmento) {
        return CompletableFuture.supplyAsync(() ->
            jpaApi.withTransaction(entityManager -> {
                entityManager.merge(segmento);
                return "Segmento updated with ID: " + segmento.getId();
            }), executionContext
        );
    }
    
    
    @Override
    public CompletionStage<String> deleteSegmento(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(entityManager -> {
                Segmento segmento = entityManager.find(Segmento.class, id);
                if (segmento != null) {
                    entityManager.remove(segmento);
                    return "Segmento deleted with ID: " + id;
                } else {
                    return "Segmento not found with ID: " + id;
                }
            });
        }, executionContext);
    }

}

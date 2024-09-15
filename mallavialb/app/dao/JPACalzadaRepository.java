package dao;

import models.Calzada;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JPACalzadaRepository implements CalzadaRepository {
	
	private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPACalzadaRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }
    
    @Override
    public CompletionStage<String> saveCalzada(Calzada calzada) {
        return CompletableFuture.supplyAsync(() -> 
            jpaApi.withTransaction(entityManager -> {
                entityManager.persist(calzada);
                return "Segmento saved with ID: " + calzada.getId();
            })
        , executionContext);
    }
    
    @Override
    public CompletionStage<List<Calzada>> saveCalzadaAll(List<Calzada> calzadas) {
        return CompletableFuture.supplyAsync(() ->
            jpaApi.withTransaction(entityManager -> {
                for (Calzada calzada : calzadas) {
                    entityManager.persist(calzada);
                }
                return calzadas;
            }), executionContext
        );
    }
    
    @Override
    public CompletionStage<List<Calzada>> findAllCalzadas(long segmentoId) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(entityManager -> {
                return entityManager.createQuery("SELECT c FROM Calzada c WHERE c.segmento.id = :segmentoId", Calzada.class)
                                    .setParameter("segmentoId", segmentoId)
                                    .getResultList();
            });
        }, executionContext);
    }
    
    @Override
    public CompletionStage<Void> updateMaterialBySegmentoId(Long segmentoId, String material) {
        return CompletableFuture.runAsync(() -> {
            jpaApi.withTransaction(entityManager -> {
                entityManager.createQuery("UPDATE Calzada c SET c.material = :material WHERE c.segmento.id = :segmentoId")
                             .setParameter("material", material)
                             .setParameter("segmentoId", segmentoId)
                             .executeUpdate();
            });
        }, executionContext);
    }
    

}

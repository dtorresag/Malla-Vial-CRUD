package dao;

import models.Bordillo;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JPABordilloRepository implements BordilloRepository {
	
	private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPABordilloRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }
    
    @Override
    public CompletionStage<String> saveBordillo(Bordillo bordillo) {
        return CompletableFuture.supplyAsync(() -> 
            jpaApi.withTransaction(entityManager -> {
                entityManager.persist(bordillo);
                return "Bordillo saved with ID: " + bordillo.getId();
            })
        , executionContext);
    }
    
    @Override
    public CompletionStage<List<Bordillo>> saveBordilloAll(List<Bordillo> bordillos) {
        return CompletableFuture.supplyAsync(() ->
            jpaApi.withTransaction(entityManager -> {
                for (Bordillo bordillo : bordillos) {
                    entityManager.persist(bordillo);
                }
                return bordillos;
            }), executionContext
        );
    }
    
    public CompletionStage<List<Bordillo>> findAllBordillos(long segmentoId) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(entityManager -> {
                return entityManager.createQuery("SELECT b FROM Bordillo b WHERE b.segmento.id = :segmentoId", Bordillo.class)
                                    .setParameter("segmentoId", segmentoId)
                                    .getResultList();
            });
        }, executionContext);
    }
    
    @Override
    public CompletionStage<Void> updateMaterialBySegmentoId(Long segmentoId, String material) {
        return CompletableFuture.runAsync(() -> {
            jpaApi.withTransaction(entityManager -> {
                entityManager.createQuery("UPDATE Bordillo b SET b.material = :material WHERE b.segmento.id = :segmentoId")
                             .setParameter("material", material)
                             .setParameter("segmentoId", segmentoId)
                             .executeUpdate();
            });
        }, executionContext);
    }

}

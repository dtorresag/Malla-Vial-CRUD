package dao;

import models.Calzada;

import java.util.List;
import java.util.concurrent.CompletionStage;
import com.google.inject.ImplementedBy;


@ImplementedBy(JPACalzadaRepository.class)
public interface CalzadaRepository {
	CompletionStage<String> saveCalzada(Calzada calzada);
	CompletionStage<List<Calzada>> saveCalzadaAll(List<Calzada> calzadas);
	CompletionStage<List<Calzada>> findAllCalzadas(long segmentoId);
	CompletionStage<Void> updateMaterialBySegmentoId(Long segmentoId, String material);
}

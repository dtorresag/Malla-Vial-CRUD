package dao;

import models.Bordillo;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.google.inject.ImplementedBy;


@ImplementedBy(JPABordilloRepository.class)
public interface BordilloRepository {
	CompletionStage<String> saveBordillo(Bordillo bordillo);
	CompletionStage<List<Bordillo>> saveBordilloAll(List<Bordillo> calzadas);
	CompletionStage<List<Bordillo>> findAllBordillos(long segmentoId);
	CompletionStage<Void> updateMaterialBySegmentoId(Long segmentoId, String material);
}

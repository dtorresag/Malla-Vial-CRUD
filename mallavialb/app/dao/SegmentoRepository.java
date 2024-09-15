package dao;

import models.Segmento;
import java.util.concurrent.CompletionStage;
import com.google.inject.ImplementedBy;
import java.util.stream.Stream;

@ImplementedBy(JPASegmentoRepository.class)
public interface SegmentoRepository {
    CompletionStage<String> saveSegmento(Segmento segmento);
    CompletionStage<Segmento> findSegmento(Long id);
    CompletionStage<Stream<Segmento>> findAllSegmentos();
    CompletionStage<String> updateSegmento(Segmento segmento);
    CompletionStage<String> deleteSegmento(Long id);
}
package controllers;

import dao.JPABordilloRepository;
import dao.JPACalzadaRepository;
import dao.JPASegmentoRepository;
import models.*;
import play.mvc.*;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class SegmentoController extends Controller {

	 private final JPASegmentoRepository segmentoRepository;
	 private final JPABordilloRepository bordilloRepository;
	 private final JPACalzadaRepository calzadaRepository;

	 @Inject
	 public SegmentoController(JPASegmentoRepository segmentoRepository, JPACalzadaRepository calzadaRepository, JPABordilloRepository bordilloRepository) {
		 this.segmentoRepository = segmentoRepository;
		 this.calzadaRepository = calzadaRepository;
		 this.bordilloRepository = bordilloRepository;
	 }
	 
	 public CompletionStage<Result> createSegmento(Http.Request request) {
		 JsonNode json = request.body().asJson();

		    // Verificar que el JSON no sea nulo y que contenga todos los campos necesarios
		 if (json == null || !json.hasNonNull("largo") || !json.hasNonNull("direccion") || 
				 !json.hasNonNull("tipoDeVia") || !json.hasNonNull("numeroDeCalzadas") || !json.hasNonNull("numeroDeBordillos")
				 || !json.hasNonNull("materialCalzada") || !json.hasNonNull("materialBordillo") ) {
		        return CompletableFuture.completedFuture(badRequest("Faltan campos obligatorios o algunos campos son nulos."));
		 }
		 
		 Integer largo = json.get("largo").asInt();	    
		 String direccion = json.get("direccion").asText();		    
		 String tipoDeVia = json.get("tipoDeVia").asText();
		 Integer numeroDeCalzadas = json.get("numeroDeCalzadas").asInt();
		 Integer numeroDeBordillos = json.get("numeroDeBordillos").asInt();
		 String materialCalzada = json.get("materialCalzada").asText();
	     String materialBordillo = json.get("materialBordillo").asText();
		 
		// Verificar que los campos no estén vacíos o no válidos
		 if (largo <= 0 || direccion.isEmpty() || tipoDeVia.isEmpty() || numeroDeCalzadas <= 0 || numeroDeBordillos < 0) {     
			 return CompletableFuture.completedFuture(badRequest("Los campos no son válidos. Verifica el largo, dirección, tipo de vía y número de calzadas y bordillos."));	    
		 }		 
		 	        
		// Crear el objeto Segmento
		Segmento segmento = new Segmento(largo, direccion, tipoDeVia, numeroDeCalzadas, numeroDeBordillos);
		
		// se crean las calzadas asociadas al segmento
		List<Calzada> calzadas = new ArrayList<>();	        
		for (int i = 0; i < numeroDeCalzadas; i++) {
			calzadas.add(new Calzada(segmento, materialCalzada));
	    }
		 
		List<Bordillo> bordillos = new ArrayList<>();	        
		for (int i = 0; i < numeroDeCalzadas; i++) {
			bordillos.add(new Bordillo(segmento, materialBordillo));
	    }
		
		segmento.setCalzadas(calzadas);
		segmento.setBordillos(bordillos);
	        
		return segmentoRepository.saveSegmento(segmento)
		        .thenApplyAsync(savedSegmento -> ok("Segmento creado correctamente"))
		        .exceptionally(ex -> {
		            return internalServerError("Ocurrió un error: " + ex.getMessage());
		        });
	 }
	 
	 public CompletionStage<Result> listSegmentos() {
		    return segmentoRepository.findAllSegmentos()
		        .thenApplyAsync(segmentosStream -> {
		            List<ObjectNode> resultList = new ArrayList<>();

		            // Convertir el stream a lista para manipularlo
		            segmentosStream.forEach(segmento -> {
		                ObjectNode jsonSegmento = Json.newObject();
		                jsonSegmento.put("largo", segmento.getLargo());
		                jsonSegmento.put("direccion", segmento.getDireccion());
		                jsonSegmento.put("tipoDeVia", segmento.getTipoDeVia());
		                jsonSegmento.put("numeroDeCalzadas", segmento.getNumeroDeCalzadas());
		                jsonSegmento.put("numeroDeBordillos", segmento.getNumeroDeCalzadas());

		                resultList.add(jsonSegmento);
		            });

		            return ok(Json.toJson(resultList));
		        })
		        .exceptionally(ex -> internalServerError("Error al obtener los segmentos: " + ex.getMessage()));
		}
	 
	 public CompletionStage<Result> getSegmento(Long id) {
	        return segmentoRepository.findSegmento(id)
	            .thenComposeAsync(segmentoOpt -> {
	                if (segmentoOpt == null) {
	                    return CompletableFuture.completedFuture(notFound("Segmento no encontrado"));
	                }

	                Segmento segmento = segmentoOpt;
	                CompletionStage<List<Calzada>> calzadasStage = calzadaRepository.findAllCalzadas(id);
	                CompletionStage<List<Bordillo>> bordillosStage = bordilloRepository.findAllBordillos(id);

	                return CompletableFuture.allOf(calzadasStage.toCompletableFuture(), bordillosStage.toCompletableFuture())
	                    .thenApplyAsync(v -> {
	                        try {
	                            ObjectNode jsonSegmento = Json.newObject();
	                            jsonSegmento.put("largo", segmento.getLargo());
	                            jsonSegmento.put("direccion", segmento.getDireccion());
	                            jsonSegmento.put("tipoDeVia", segmento.getTipoDeVia());
	                            jsonSegmento.put("numeroDeCalzadas", segmento.getNumeroDeCalzadas());
	                            jsonSegmento.put("numeroDeBordillos", segmento.getNumeroDeBordillos());
	                            	
	                            // Agregar los materiales, se asume que todos tienen el mismo material
	                            List<Calzada> calzadas = calzadasStage.toCompletableFuture().join();
	                            if (!calzadas.isEmpty()) {
	                                jsonSegmento.put("materialCalzada", calzadas.get(0).getMaterial());
	                            }

	                            List<Bordillo> bordillos = bordillosStage.toCompletableFuture().join();
	                            if (!bordillos.isEmpty()) {
	                                jsonSegmento.put("materialBordillo", bordillos.get(0).getMaterial());
	                            }

	                            return ok(jsonSegmento);
	                        } catch (Exception e) {
	                            return internalServerError("Error al procesar la información del segmento: " + e.getMessage());
	                        }
	                    });
	            })
	            .exceptionally(ex -> internalServerError("Error al obtener el segmento: " + ex.getMessage()));
	    }
	 
	 public CompletionStage<Result> updateSegmento(Long id, Http.Request request) {
	        JsonNode json = request.body().asJson();

	        // Validar que el JSON contenga todos los campos necesarios
	        if (json == null || !json.hasNonNull("largo") || !json.hasNonNull("direccion") || 
	            !json.hasNonNull("tipoDeVia") || !json.hasNonNull("numeroDeCalzadas") || 
	            !json.hasNonNull("numeroDeBordillos") || !json.hasNonNull("materialCalzada") || 
	            !json.hasNonNull("materialBordillo")) {
	            return CompletableFuture.completedFuture(badRequest("Faltan campos obligatorios o algunos campos son nulos."));
	        }

	        Integer largo = json.get("largo").asInt();
	        String direccion = json.get("direccion").asText();
	        String tipoDeVia = json.get("tipoDeVia").asText();
	        Integer numeroDeCalzadas = json.get("numeroDeCalzadas").asInt();
	        Integer numeroDeBordillos = json.get("numeroDeBordillos").asInt();
	        String materialCalzada = json.get("materialCalzada").asText();
	        String materialBordillo = json.get("materialBordillo").asText();

	        // Validar que los datos no sean vacíos o nulos
	        if (largo <= 0 || direccion.isEmpty() || tipoDeVia.isEmpty() || 
	            numeroDeCalzadas <= 0 || numeroDeBordillos < 0) {
	            return CompletableFuture.completedFuture(badRequest("Los campos no son válidos. Verifica el largo, dirección, tipo de vía y número de calzadas y bordillos."));
	        }

	        // Obtener el segmento existente
	        return segmentoRepository.findSegmento(id)
	            .thenComposeAsync(segmento -> {
	                if (segmento == null) {
	                    return CompletableFuture.completedFuture(notFound("Segmento no encontrado"));
	                }

	                // Actualizar los atributos del segmento
	                segmento.setLargo(largo);
	                segmento.setDireccion(direccion);
	                segmento.setTipoDeVia(tipoDeVia);
	                segmento.setNumeroDeCalzadas(numeroDeCalzadas);
	                segmento.setNumeroDeBordillos(numeroDeBordillos);
	                
	             // Convertir los CompletionStage en CompletableFuture
		            CompletableFuture<Void> calzadasUpdateFuture = calzadaRepository.updateMaterialBySegmentoId(id, materialCalzada).toCompletableFuture();
		            CompletableFuture<Void> bordillosUpdateFuture = bordilloRepository.updateMaterialBySegmentoId(id, materialBordillo).toCompletableFuture();

	                // Actualizar el segmento en la base de datos
	                return segmentoRepository.updateSegmento(segmento)
	                    .thenComposeAsync(updateMessage -> {
	                        // Actualizar materiales de calzadas y bordillos
	                        return CompletableFuture.allOf(calzadasUpdateFuture, bordillosUpdateFuture
	                        ).thenApply(v -> ok("Segmento actualizado correctamente"));
	                    });
	            })
	            .exceptionally(ex -> internalServerError("Error al actualizar el segmento: " + ex.getMessage()));
	    }
	 
	 public CompletionStage<Result> deleteSegmento(Long id) {
	        return segmentoRepository.deleteSegmento(id)
	            .thenApplyAsync(responseMessage -> {
	                if (responseMessage.startsWith("Segmento deleted")) {
	                    return ok(responseMessage); // Devuelve un mensaje de éxito
	                } else {
	                    return notFound(responseMessage); // Devuelve un mensaje de error si no se encuentra el segmento
	                }
	            })
	            .exceptionally(ex -> internalServerError("Error al eliminar el segmento: " + ex.getMessage())); // Manejo de excepciones
	    } 
	 
	 
}

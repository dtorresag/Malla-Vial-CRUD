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
			 return CompletableFuture.completedFuture(
			            badRequest(Json.newObject().put("error", "Faltan campos obligatorios o algunos campos son nulos."))
			                .withHeader("Access-Control-Allow-Origin", "*")
			                .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
			                .withHeader("Access-Control-Allow-Headers", "Content-Type")
			        );
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
			 return CompletableFuture.completedFuture(
			            badRequest(Json.newObject().put("error", "Los campos no son válidos. Verifica el largo, dirección, tipo de vía, calzadas y bordillos."))
			                .withHeader("Access-Control-Allow-Origin", "*")
			                .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
			                .withHeader("Access-Control-Allow-Headers", "Content-Type")
			        );    
		 }		 
		 	        
		// Crear el objeto Segmento
		Segmento segmento = new Segmento(largo, direccion, tipoDeVia, numeroDeCalzadas, numeroDeBordillos);
		
		// se crean las calzadas asociadas al segmento
		List<Calzada> calzadas = new ArrayList<>();	        
		for (int i = 0; i < numeroDeCalzadas; i++) {
			calzadas.add(new Calzada(segmento, materialCalzada));
	    }
		 
		//se crean los bordillos asociados al segmento
		List<Bordillo> bordillos = new ArrayList<>();	        
		for (int i = 0; i < numeroDeCalzadas; i++) {
			bordillos.add(new Bordillo(segmento, materialBordillo));
	    }
		
		segmento.setCalzadas(calzadas);
		segmento.setBordillos(bordillos);
	        
		return segmentoRepository.saveSegmento(segmento)
		        .thenApplyAsync(savedSegmento -> {
		            ObjectNode result = Json.newObject();
		            result.put("message", "Segmento creado correctamente");
		            result.set("segmento", Json.toJson(savedSegmento));
		            return ok(result)
		                .withHeader("Access-Control-Allow-Origin", "*")
		                .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		                .withHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		        })
		        .exceptionally(ex -> {
		            ObjectNode error = Json.newObject();
		            error.put("error", "Ocurrió un error: " + ex.getMessage());
		            return internalServerError(error)
		                .withHeader("Access-Control-Allow-Origin", "*")
		                .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		                .withHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		        });
	 }
	 
	 public CompletionStage<Result> listSegmentos() {
		    return segmentoRepository.findAllSegmentos()
		        .thenApplyAsync(segmentosStream -> {
		            List<ObjectNode> resultList = new ArrayList<>();

		            // Convertir el stream a lista para manipularlo
		            segmentosStream.forEach(segmento -> {
		                ObjectNode jsonSegmento = Json.newObject();
		                jsonSegmento.put("id", segmento.getId());
		                jsonSegmento.put("largo", segmento.getLargo());
		                jsonSegmento.put("direccion", segmento.getDireccion());
		                jsonSegmento.put("tipoDeVia", segmento.getTipoDeVia());
		                jsonSegmento.put("numeroDeCalzadas", segmento.getNumeroDeCalzadas());
		                jsonSegmento.put("numeroDeBordillos", segmento.getNumeroDeBordillos());

		                resultList.add(jsonSegmento);
		            });

		            return ok(Json.toJson(resultList))
		            		.withHeader("Access-Control-Allow-Origin", "*")
		                    .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		                    .withHeader("Access-Control-Allow-Headers", "Content-Type");
		        })
		        .exceptionally(ex -> internalServerError("Error al obtener los segmentos: " + ex.getMessage()));
		}
	 
	 public CompletionStage<Result> getSegmento(long id) {
		    return segmentoRepository.findSegmento(id)
		        .thenComposeAsync(segmentoOpt -> {
		            if (segmentoOpt == null) {
		                return CompletableFuture.completedFuture(notFound("Segmento no encontrado")
		                        .withHeader("Access-Control-Allow-Origin", "*")); // CORS
		            }

		            Segmento segmento = segmentoOpt;
		            CompletionStage<List<Calzada>> calzadasStage = calzadaRepository.findAllCalzadas(id);
		            CompletionStage<List<Bordillo>> bordillosStage = bordilloRepository.findAllBordillos(id);

		            return calzadasStage.thenCombineAsync(bordillosStage, (calzadas, bordillos) -> {
		                ObjectNode jsonSegmento = Json.newObject();
		                jsonSegmento.put("id", segmento.getId());
		                jsonSegmento.put("largo", segmento.getLargo());
		                jsonSegmento.put("direccion", segmento.getDireccion());
		                jsonSegmento.put("tipoDeVia", segmento.getTipoDeVia());
		                jsonSegmento.put("numeroDeCalzadas", segmento.getNumeroDeCalzadas());
		                jsonSegmento.put("numeroDeBordillos", segmento.getNumeroDeBordillos());

		                // Agregar el material de la calzada si existe
		                if (!calzadas.isEmpty()) {
		                    jsonSegmento.put("materialCalzada", calzadas.get(0).getMaterial());
		                }

		                // Agregar el material del bordillo si existe
		                if (!bordillos.isEmpty()) {
		                    jsonSegmento.put("materialBordillo", bordillos.get(0).getMaterial());
		                }

		                return ok(jsonSegmento)
		                    .withHeader("Access-Control-Allow-Origin", "*")
		                    .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		                    .withHeader("Access-Control-Allow-Headers", "Content-Type");
		            }).exceptionally(ex -> {
		                return internalServerError("Error al procesar la información del segmento: " + ex.getMessage())
		                        .withHeader("Access-Control-Allow-Origin", "*"); 
		            });
		        })
		        .exceptionally(ex -> {
		            return internalServerError("Error al obtener el segmento: " + ex.getMessage())
		                    .withHeader("Access-Control-Allow-Origin", "*");
		        });
		}

	 
	 public CompletionStage<Result> updateSegmento(Long id, Http.Request request) {
	        JsonNode json = request.body().asJson();

	        // Validar que el JSON contenga todos los campos necesarios
	        if (json == null || !json.hasNonNull("largo") || !json.hasNonNull("direccion") || 
	            !json.hasNonNull("tipoDeVia") || !json.hasNonNull("numeroDeCalzadas") || 
	            !json.hasNonNull("numeroDeBordillos") || !json.hasNonNull("materialCalzada") || 
	            !json.hasNonNull("materialBordillo")) {
	        	ObjectNode errorResponse = Json.newObject()
	                    .put("error", "Faltan campos obligatorios o algunos campos son nulos.");
	            return CompletableFuture.completedFuture(
	                badRequest(errorResponse)
	                    .withHeader("Access-Control-Allow-Origin", "*")
	                    .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
	                    .withHeader("Access-Control-Allow-Headers", "Content-Type")
	            );
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
	        	ObjectNode errorResponse = Json.newObject()
	                    .put("error", "Los campos no son válidos. Verifica el largo, dirección, tipo de vía y número de calzadas y bordillos.");
	                return CompletableFuture.completedFuture(
	                    badRequest(errorResponse)
	                        .withHeader("Access-Control-Allow-Origin", "*")
	                        .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
	                        .withHeader("Access-Control-Allow-Headers", "Content-Type")
	                );
	        }

	        // Obtener el segmento existente
	        return segmentoRepository.findSegmento(id)
	            .thenComposeAsync(segmento -> {
	                if (segmento == null) {
	                	ObjectNode errorResponse = Json.newObject()
	                            .put("error", "Segmento no encontrado");
	                        return CompletableFuture.completedFuture(
	                            notFound(errorResponse)
	                                .withHeader("Access-Control-Allow-Origin", "*")
	                                .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
	                                .withHeader("Access-Control-Allow-Headers", "Content-Type")
	                        );
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
		                        return CompletableFuture.allOf(calzadasUpdateFuture, bordillosUpdateFuture)
		                            .thenApply(v -> {
		                                ObjectNode successResponse = Json.newObject()
		                                    .put("message", "Segmento actualizado correctamente");
		                                return ok(successResponse)
		                                    .withHeader("Access-Control-Allow-Origin", "*")
		                                    .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		                                    .withHeader("Access-Control-Allow-Headers", "Content-Type");
		                            });
		                    });
		            })
		            .exceptionally(ex -> {
		                ObjectNode errorResponse = Json.newObject()
		                    .put("error", "Error al actualizar el segmento: " + ex.getMessage());
		                return internalServerError(errorResponse)
		                    .withHeader("Access-Control-Allow-Origin", "*")
		                    .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		                    .withHeader("Access-Control-Allow-Headers", "Content-Type");
		            });
	    }
	 
	 public CompletionStage<Result> deleteSegmento(Long id) {
		    return segmentoRepository.deleteSegmento(id)
		        .thenApplyAsync(responseMessage -> {
		            if (responseMessage.startsWith("Segmento deleted")) {
		                return ok(responseMessage)
		                    .withHeader("Access-Control-Allow-Origin", "*")
		                    .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		                    .withHeader("Access-Control-Allow-Headers", "Content-Type"); // Devuelve un mensaje de éxito
		            } else {
		                return notFound(responseMessage)
		                    .withHeader("Access-Control-Allow-Origin", "*")
		                    .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		                    .withHeader("Access-Control-Allow-Headers", "Content-Type"); // Devuelve un mensaje de error si no se encuentra el segmento
		            }
		        })
		        .exceptionally(ex -> 
		            internalServerError("Error al eliminar el segmento: " + ex.getMessage())
		                .withHeader("Access-Control-Allow-Origin", "*")
		                .withHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		                .withHeader("Access-Control-Allow-Headers", "Content-Type, Authorization") // Manejo de excepciones
		        );
		}

	 
	 
}

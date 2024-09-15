package models;

import java.util.List;


import jakarta.persistence.*;

@Entity
@Table(name = "Segmento")
public class Segmento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
    private double largo;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String tipoDeVia;

    @Column(nullable = false)
    private int numeroDeCalzadas;
    
    @Column(nullable = false)
    private int numeroDeBordillos;
    
    @OneToMany(mappedBy = "segmento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Calzada> calzadas;
    
    @OneToMany(mappedBy = "segmento", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<Bordillo> bordillos;
    
 
    public Segmento() {}


    public Segmento(double largo, String direccion, String tipoDeVia, int numeroDeCalzadas, int numeroDeBordillos) {
        this.largo = largo;
        this.direccion = direccion;
        this.tipoDeVia = tipoDeVia;
        this.numeroDeCalzadas = numeroDeCalzadas;
        this.numeroDeBordillos = numeroDeBordillos;
    }


	public long getId() {
		return id;
	}


	public double getLargo() {
		return largo;
	}


	public void setLargo(double largo) {
		this.largo = largo;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public String getTipoDeVia() {
		return tipoDeVia;
	}


	public void setTipoDeVia(String tipoDeVia) {
		this.tipoDeVia = tipoDeVia;
	}


	public int getNumeroDeCalzadas() {
		return numeroDeCalzadas;
	}


	public void setNumeroDeCalzadas(int numeroDeCalzadas) {
		this.numeroDeCalzadas = numeroDeCalzadas;
	}
	
	


	public int getNumeroDeBordillos() {
		return numeroDeBordillos;
	}


	public void setNumeroDeBordillos(int numeroDeBordillos) {
		this.numeroDeBordillos = numeroDeBordillos;
	}


	public List<Calzada> getCalzadas() {
		return calzadas;
	}


	public void setCalzadas(List<Calzada> calzadas) {
		this.calzadas = calzadas;
	}


	public List<Bordillo> getBordillos() {
		return bordillos;
	}


	public void setBordillos(List<Bordillo> bordillos) {
		this.bordillos = bordillos;
	}


	@Override
	public String toString() {
		return "Segmento [largo=" + largo + ", direccion=" + direccion + ", tipoDeVia=" + tipoDeVia
				+ ", numeroDeCalzadas=" + numeroDeCalzadas + "]";
	}

    
    
	
}

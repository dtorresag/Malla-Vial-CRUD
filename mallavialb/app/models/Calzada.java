package models;


import jakarta.persistence.*;

@Entity
@Table(name = "Calzada")
public class Calzada {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
    @JoinColumn(name = "segmento_id")
    private Segmento segmento;
	
	@Column(nullable = false)
	private String material;
	
	public Calzada() {}

	public Calzada(Segmento segmento, String material) {
		this.segmento = segmento;
		this.material = material;
	}

	public String getMaterial() {
		return material;
	}

	public long getId() {
		return id;
	}

	public Segmento getSegmento() {
		return segmento;
	}

	@Override
	public String toString() {
		return "Calzada [id=" + id + ", segmento=" + segmento.getId() + "]";
	}

}

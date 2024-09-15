package models;


import jakarta.persistence.*;

@Entity
@Table(name = "Bordillo")
public class Bordillo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
    @JoinColumn(name = "segmento_id")
    private Segmento segmento;
	
	@Column(nullable = false)
	private String material;
	
	public Bordillo() {}

	public Bordillo(Segmento segmento, String material) {
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
		return "Bordillo [id=" + id + ", segmento=" + segmento.getId() + "]";
	}

}
package aws.mitocode.spring.modelDTO;

import java.io.Serializable;

public class TipoNegocioDTO implements Serializable {

	private static final long serialVersionUID = 7575688805233258323L;
	private int id;
	private String descripcion;
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

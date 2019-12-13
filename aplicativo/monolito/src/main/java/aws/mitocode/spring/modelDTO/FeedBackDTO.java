package aws.mitocode.spring.modelDTO;

import java.io.Serializable;
import java.util.Date;

public class FeedBackDTO implements Serializable {

	private static final long serialVersionUID = -1725563866279171711L;
	private int id;
	private String mensaje;
	private Date fecha;
	private ProblemaDTO problema;
	private String idUsuario;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public ProblemaDTO getProblema() {
		return problema;
	}

	public void setProblema(ProblemaDTO problema) {
		this.problema = problema;
	}
}

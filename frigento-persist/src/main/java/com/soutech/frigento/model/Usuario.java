package com.soutech.frigento.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "users", uniqueConstraints={@UniqueConstraint(columnNames={"username"}, name="ux_username"),
											@UniqueConstraint(columnNames={"email"}, name="ux_email")})
public class Usuario implements Serializable {

	private static final long serialVersionUID = -1039096380771137501L;

	@NotNull
    @Column(name = "username")
    @Size(max = 50)
    private String username;
	
    @Column(name = "password")
    private String password;
	
	@NotNull
    @Column(name = "enabled")
    private Boolean habilitado;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "ID_CATEGORIA")
	private Categoria categoriaProducto;
	
	@NotNull
    @Column(name = "NOMBRE")
    @Size(max = 20)
    private String nombre;

    @Column(name = "APELLIDO")
    @Size(max = 20)
    private String apellido;
    
    @Column(name = "CUIT_CUIL")
    @DecimalMin(value = "20000000000")
    @DecimalMax(value = "36999999999")
    private Long cuitCuil;

    @Column(name = "TELEFONO")
    @Size(max = 15)
    private String telefono;

    @Column(name = "CELULAR")
    @Size(max = 15)
    private String celular;

    @NotNull
    @Column(name = "CALLE")
    @Size(max = 30)
    private String calle;

    @NotNull
    @Column(name = "ALTURA")
    private Short altura;

    @Column(name = "DEPTO")
    @Size(max = 10)
    private String depto;
    
    @NotNull
    @Email(regexp="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    @Column(name = "EMAIL")
    @Size(max = 30)
    private String email;

    @NotNull
    @Column(name = "ES_ADMIN")
    private Boolean esAdmin;

	@Id
    @SequenceGenerator(name = "usuarioGen", sequenceName = "SEQ_USUARIO", initialValue = 2)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "usuarioGen")
    @Column(name = "ID_USUARIO")
    private Integer id;
	
	@NotNull
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "ID_LOCALIDAD")
    private Localidad localidad;
	
	@NotNull
    @Column(name = "DISTANCIA")
    private Integer distancia;
	
	@Transient
	private String identificadoWeb;
	
	@Transient
    private String passwordReingresada;

	public Integer getId() {
        return this.id;
    }

	public void setId(Integer id) {
        this.id = id;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getNombre() {
        return this.nombre;
    }

	public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	public String getApellido() {
        return this.apellido;
    }

	public void setApellido(String apellido) {
        this.apellido = apellido;
    }

	public Long getCuitCuil() {
		return cuitCuil;
	}

	public void setCuitCuil(Long cuitCuil) {
		this.cuitCuil = cuitCuil;
	}

	public String getTelefono() {
        return this.telefono;
    }

	public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

	public String getCelular() {
        return this.celular;
    }

	public void setCelular(String celular) {
        this.celular = celular;
    }

	public String getCalle() {
        return this.calle;
    }

	public void setCalle(String calle) {
        this.calle = calle;
    }

	public Short getAltura() {
        return this.altura;
    }

	public void setAltura(Short altura) {
        this.altura = altura;
    }

	public String getDepto() {
        return this.depto;
    }

	public void setDepto(String depto) {
        this.depto = depto;
    }

	public Boolean getEsAdmin() {
        return this.esAdmin;
    }

	public void setEsAdmin(Boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}

	public Categoria getCategoriaProducto() {
		return categoriaProducto;
	}

	public void setCategoriaProducto(Categoria categoriaProducto) {
		this.categoriaProducto = categoriaProducto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentificadoWeb() {
		return "(".concat(username).concat(") ").concat(nombre).concat(apellido == null ? "" : " ".concat(apellido));
	}

	public void setIdentificadoWeb(String identificadoWeb) {
		this.identificadoWeb = identificadoWeb;
	}

	public String getPasswordReingresada() {
		return passwordReingresada;
	}

	public void setPasswordReingresada(String passwordReingresada) {
		this.passwordReingresada = passwordReingresada;
	}

	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	public Integer getDistancia() {
		return distancia;
	}

	public void setDistancia(Integer distancia) {
		this.distancia = distancia;
	}

}
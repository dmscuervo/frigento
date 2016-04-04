package com.soutech.frigento.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name="authorities", uniqueConstraints=@UniqueConstraint(columnNames={"username", "authority"}))
public class Rol {

	public static final String ROLE_ADMIN = "ROLE_ADMIN";

	@Id
    @SequenceGenerator(name = "authoritiesGen", sequenceName = "SEQ_ROL")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "authoritiesGen")
    @Column(name = "ID_ROL")
    private Integer id;
	
	@NotNull
	@Size(max=50)
	@Column(name="userName")
	private String userName;
	
	@NotNull
	@Size(max=50)
	@Column(name="authority")
	private String rol;

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}

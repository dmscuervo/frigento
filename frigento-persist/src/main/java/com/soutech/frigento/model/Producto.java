package com.soutech.frigento.model;
import java.math.BigDecimal;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.soutech.frigento.model.annotattions.Numeric;


@Entity
@Table(name = "PRODUCTO")
public class Producto {

    /**
     */
    @NotNull
    @NotEmpty
    @Column(name = "CODIGO")
    @Size(max = 15)
    private String codigo;

    /**
     */
    @NotNull
    @NotEmpty
    @Column(name = "DESCRIPCION")
    @Size(max = 100)
    private String descripcion;

    @Numeric(tipo=Numeric.decimal)
    @NotNull
    @Column(name = "COSTO_ACTUAL")
    private BigDecimal costoActual;

    @Numeric(tipo=Numeric.decimal)
    @NotNull
    @Column(name = "STOCK")
    private Float stock;

    /**
     */
    @Column(name = "STOCK_MINIMO")
    private Float stockMinimo;

    /**
     */
    @Column(name = "IMAGEN")
    private Blob imagen;

    @Numeric(tipo=Numeric.decimal)
    @Column(name = "PESO_CAJA")
    private Float pesoCaja;

    @Numeric(tipo=Numeric.decimal)
    @Column(name = "PESO_ENVASE")
    private Float pesoEnvase;

	public String getCodigo() {
        return this.codigo;
    }

	public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

	public String getDescripcion() {
        return this.descripcion;
    }

	public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

	public BigDecimal getCostoActual() {
        return this.costoActual;
    }

	public void setCostoActual(BigDecimal costoActual) {
        this.costoActual = costoActual;
    }

	public Float getStock() {
        return this.stock;
    }

	public void setStock(Float stock) {
        this.stock = stock;
    }

	public Float getStockMinimo() {
        return this.stockMinimo;
    }

	public void setStockMinimo(Float stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

	public Blob getImagen() {
        return this.imagen;
    }

	public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }

	public Float getPesoCaja() {
        return this.pesoCaja;
    }

	public void setPesoCaja(Float pesoCaja) {
        this.pesoCaja = pesoCaja;
    }

	public Float getPesoEnvase() {
        return this.pesoEnvase;
    }

	public void setPesoEnvase(Float pesoEnvase) {
        this.pesoEnvase = pesoEnvase;
    }

	@Id
    @SequenceGenerator(name = "productoGen", sequenceName = "SEQ_PRODUCTO")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "productoGen")
    @Column(name = "ID_PRODUCTO")
    private Integer id;

	public Integer getId() {
        return this.id;
    }

	public void setId(Integer id) {
        this.id = id;
    }
}

package com.soutech.frigento.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import com.soutech.frigento.enums.IVAEnum;
import com.soutech.frigento.model.annotattions.Numeric;


@Entity
@Table(name = "PRODUCTO", uniqueConstraints=@UniqueConstraint(columnNames={"CODIGO"}, name="ux_codigo"))
public class Producto implements Serializable {

	private static final long serialVersionUID = 7139237922807940949L;

	@Id
    @SequenceGenerator(name = "productoGen", sequenceName = "SEQ_PRODUCTO")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "productoGen")
    @Column(name = "ID_PRODUCTO")
    private Integer id;

    @NotNull
    @NotEmpty
    @Column(name = "CODIGO")
    @Size(max = 15)
    private String codigo;

    @NotNull
    @NotEmpty
    @Column(name = "DESCRIPCION")
    @Size(max = 75)
    private String descripcion;
    
    @NotNull
    @NotEmpty
    @Column(name = "DESCRIPCION_VENTA")
    @Size(max = 75)
    private String descripcionVenta;

    @Numeric(regexp = Numeric.decimal_positivo)
    @NotNull
    @Column(name = "COSTO_ACTUAL")
    private BigDecimal costoActual;
    
    @NotNull
    @Type(type="com.soutech.frigento.model.type.IVAEnumType")
	@Column(name="IVA")
	private IVAEnum iva;
    
    @Numeric(regexp = Numeric.decimal_positivo_3decimal)
    @NotNull
    @Column(name = "STOCK")
    private Float stock;
    
    @Transient
    private BigDecimal costoPrevio;
    @Transient
    private Float stockPrevio;
    @Transient
    private Boolean stockControlado;

    @Numeric(regexp = Numeric.decimal_positivo)
    @Column(name = "STOCK_MINIMO")
    private Float stockMinimo;

    @NotNull
    @Numeric(regexp = Numeric.decimal_positivo)
    @Column(name = "PESO_CAJA")
    private Float pesoCaja;

    @Numeric(regexp = Numeric.decimal_positivo)
    @Column(name = "PESO_ENVASE")
    private Float pesoEnvase;

    @Column(name = "FECHA_BAJA")
    private Date fechaBaja;
    
    @NotNull
    @Column(name = "FECHA_ALTA")
    private Date fechaAlta;
    
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="ID_PRODUCTO", referencedColumnName="ID_PRODUCTO", nullable=false)
    private List<Promocion> promociones;
    
    @Transient
    private BigDecimal importeVenta;
    @Transient
    private BigDecimal costoVenta;
    
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

	public String getDescripcionVenta() {
		return descripcionVenta;
	}

	public void setDescripcionVenta(String descripcionVenta) {
		this.descripcionVenta = descripcionVenta;
	}

	public BigDecimal getCostoActual() {
        return this.costoActual;
    }

	public void setCostoActual(BigDecimal costoActual) {
        this.costoActual = costoActual;
    }

	public IVAEnum getIva() {
		return iva;
	}

	public void setIva(IVAEnum iva) {
		this.iva = iva;
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

	public Integer getId() {
        return this.id;
    }

	public void setId(Integer id) {
        this.id = id;
    }

	public BigDecimal getCostoPrevio() {
		return costoPrevio;
	}

	public void setCostoPrevio(BigDecimal costoPrevio) {
		this.costoPrevio = costoPrevio;
	}

	public Float getStockPrevio() {
		return stockPrevio;
	}

	public void setStockPrevio(Float stockPrevio) {
		this.stockPrevio = stockPrevio;
	}

	public Boolean getStockControlado() {
		return stockControlado;
	}

	public void setStockControlado(Boolean stockControlado) {
		this.stockControlado = stockControlado;
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public BigDecimal getImporteVenta() {
		return importeVenta;
	}

	public void setImporteVenta(BigDecimal importeVenta) {
		this.importeVenta = importeVenta;
	}

	public BigDecimal getCostoVenta() {
		return costoVenta;
	}

	public void setCostoVenta(BigDecimal costoVenta) {
		this.costoVenta = costoVenta;
	}

	public List<Promocion> getPromociones() {
		return promociones;
	}

	public void setPromociones(List<Promocion> promociones) {
		this.promociones = promociones;
	}
	
}

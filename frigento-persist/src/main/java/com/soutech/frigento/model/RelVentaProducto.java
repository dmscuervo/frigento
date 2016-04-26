package com.soutech.frigento.model;
import java.io.Serializable;
import java.math.BigDecimal;

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
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "REL_VENTA_PRODUCTO")
public class RelVentaProducto implements Serializable {

    private static final long serialVersionUID = -5309062407934288318L;

    @Id
    @SequenceGenerator(name = "relVentaProductoGen", sequenceName = "SEQ_REL_VTA_PROD")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "relVentaProductoGen")
    @Column(name = "ID_REL_VENTA_PROD")
    private Long id;

	@NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_VENTA")
    private Venta venta;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_REL_PROD_CAT")
    private RelProductoCategoria relProductoCategoria;

    @NotNull
    @Column(name = "CANTIDAD")
    private Float cantidad;
    
    @NotNull
    @Column(name = "PRECIO")
    private BigDecimal precioVenta;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PROMOCION")
    private Promocion promocion;

	public Venta getVenta() {
        return this.venta;
    }

	public void setVenta(Venta venta) {
        this.venta = venta;
    }

	public RelProductoCategoria getRelProductoCategoria() {
        return this.relProductoCategoria;
    }

	public void setRelProductoCategoria(RelProductoCategoria relProductoCategoria) {
        this.relProductoCategoria = relProductoCategoria;
    }

	public Float getCantidad() {
        return this.cantidad;
    }

	public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public Promocion getPromocion() {
		return promocion;
	}

	public void setPromocion(Promocion promocion) {
		this.promocion = promocion;
	}
	
}

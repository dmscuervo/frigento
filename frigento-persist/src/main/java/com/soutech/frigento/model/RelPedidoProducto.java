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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "REL_PEDIDO_PRODUCTO")
public class RelPedidoProducto implements Serializable {

    private static final long serialVersionUID = -4581120924863075425L;

    @Id
    @SequenceGenerator(name = "relPedidoProductoGen", sequenceName = "SEQ_REL_PED_PROD")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "relPedidoProductoGen")
    @Column(name = "ID_REL_PEDIDO_PROD")
    private Long id;

	@NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PEDIDO")
    private Pedido pedido;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PRODUCTO_COSTO")
    private ProductoCosto productoCosto;

    @NotNull
    @Column(name = "CANTIDAD")
    private Float cantidad;
    
    @NotNull
    @Column(name = "COSTO")
    private BigDecimal costo;
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Pedido getPedido() {
        return this.pedido;
    }

	public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

	public ProductoCosto getProductoCosto() {
        return this.productoCosto;
    }

	public void setProductoCosto(ProductoCosto productoCosto) {
        this.productoCosto = productoCosto;
    }

	public Float getCantidad() {
        return this.cantidad;
    }

	public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

}

package com.soutech.frigento.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.CategoriaService;
import com.soutech.frigento.service.ProductoService;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.web.dto.reports.ColumnPrecioCajaConIvaDTO;
import com.soutech.frigento.web.dto.reports.ColumnPrecioCajaDTO;
import com.soutech.frigento.web.dto.reports.ColumnPrecioConIvaDTO;
import com.soutech.frigento.web.dto.reports.ColumnReporteDTO;
import com.soutech.frigento.web.dto.reports.PlanillaClienteDTO;

@Controller
@RequestMapping(value="/planilla")
@Secured({"ROLE_ADMIN"})
@SessionAttributes(names={"planillaDTO"})
public class PlanillaController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String BUSQUEDA_DEFAULT = "usuario?sortFieldName=nombre,apellido&sortOrder=asc,asc";
    
    @Autowired
    public RelProductoCategoriaService relProductoCategoriaService;
    @Autowired
    public CategoriaService categoriaService;
    @Autowired
    public ProductoService productoService;

    
    @RequestMapping(value = "/cliente", params = "filtro", produces = "text/html")
    public String filtroCliente(Model uiModel) {
    	uiModel.addAttribute("categoriaList", categoriaService.obtenerCategorias());
    	PlanillaClienteDTO planilla = new PlanillaClienteDTO();
    	uiModel.addAttribute("planillaDTO", planilla);
    	return "planilla/cliente/filtro";
    }
    
    @RequestMapping(value = "/cliente/{time}/{idCat}", produces = "text/html")
    public String getProductos(@PathVariable("time") Long time, @PathVariable("idCat") Short idCat, Model uiModel) {
    	List<RelProductoCategoria> rpcList = relProductoCategoriaService.obtenerProductosCategoria(idCat, new Date(time), new String[]{"producto.descripcion"}, new String[]{"asc"});
    	uiModel.addAttribute("rpcList", rpcList);
        return "planilla/cliente/grilla";
    }
    
    @RequestMapping(value = "/cliente/{time}/{idCat}/{productos}", produces = "text/html")
    public String getColumnas(@PathVariable("time") Long time, @PathVariable("idCat") Short idCat, @PathVariable("productos") String[] codigosProd, Model uiModel) {
    	PlanillaClienteDTO planilla = (PlanillaClienteDTO) uiModel.asMap().get("planillaDTO");
    	planilla.setFecha(new Date(time));
    	planilla.setIdCategoria(idCat);
    	
    	List<Producto> productos = productoService.obtenerProductos();
    	List<String> prodSeleccionados = Arrays.asList(codigosProd);
    	for (int i = 0; i < productos.size(); i++) {
    		Producto producto = productos.get(i);
    		if(!prodSeleccionados.contains(producto.getCodigo())){
    			productos.remove(producto);
    			i--;
    		}
		}
    	planilla.setRows(productos);
    	
    	List<ColumnReporteDTO> columnas = new ArrayList<ColumnReporteDTO>();
    	
    	ColumnReporteDTO columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.codigo"));
    	columna.setProperty("codigo");
    	columna.setClassName(String.class.getName());
    	columnas.add(columna);
    	
    	columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.descripcion"));
    	columna.setProperty("descripcion");
    	columna.setClassName(String.class.getName());
    	columnas.add(columna);
    	
    	columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.descripcionCliente"));
    	columna.setProperty("descripcionVenta");
    	columna.setClassName(String.class.getName());
    	columnas.add(columna);
    	
    	columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.peso.caja"));
    	columna.setProperty("pesoCaja");
    	columna.setClassName(Float.class.getName());
    	columnas.add(columna);
    	
    	columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.precio.kg"));
    	columna.setProperty("importeVenta");
    	columna.setClassName(BigDecimal.class.getName());
    	columnas.add(columna);
    	
    	columna = new ColumnPrecioConIvaDTO(21f);
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.precio.kg.iva"));
    	columna.setClassName(BigDecimal.class.getName());
    	columnas.add(columna);
    	
    	columna = new ColumnPrecioCajaDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.precio.caja"));
    	columna.setClassName(BigDecimal.class.getName());
    	columnas.add(columna);
    	
    	columna = new ColumnPrecioCajaConIvaDTO(21f);
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.precio.caja.iva"));
    	columna.setClassName(BigDecimal.class.getName());
    	columnas.add(columna);
    	
    	uiModel.addAttribute("columnaList", columnas);
    	return "planilla/cliente/grillaColumna";
    }
}
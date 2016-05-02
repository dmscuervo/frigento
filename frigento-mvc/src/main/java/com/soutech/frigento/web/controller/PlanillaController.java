package com.soutech.frigento.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.soutech.frigento.model.Categoria;
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
import com.soutech.frigento.web.reports.ReportePresupuestoManager;

import ar.com.fdvs.dj.domain.constants.HorizontalAlign;

@Controller
@RequestMapping(value="/planilla")
@Secured({"ROLE_ADMIN"})
@SessionAttributes(names={"planillaDTO", "columnaList"})
public class PlanillaController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String BUSQUEDA_DEFAULT = "usuario?sortFieldName=nombre,apellido&sortOrder=asc,asc";
    
    @Autowired
    private RelProductoCategoriaService relProductoCategoriaService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ReportePresupuestoManager reportePresupuestoManager;

    
    @RequestMapping(value = "/cliente", params = "filtro", produces = "text/html")
    public String filtroCliente(Model uiModel) {
    	List<Categoria> categorias = categoriaService.obtenerCategorias();
    	uiModel.addAttribute("categoriaList", categorias);
    	PlanillaClienteDTO planilla = new PlanillaClienteDTO();
    	uiModel.addAttribute("planillaDTO", planilla);
    	if(!categorias.isEmpty()){
    		List<RelProductoCategoria> rpcList = relProductoCategoriaService.obtenerProductosCategoria(categorias.get(0).getId(), new Date(), new String[]{"producto.descripcion"}, new String[]{"asc"});
    		uiModel.addAttribute("rpcList", rpcList);
    	}
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
    	columna.setAncho(30);
    	columna.setAjustarAncho(false);
    	columnas.add(columna);
    	
    	columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.descripcion"));
    	columna.setProperty("descripcion");
    	columna.setAlineacionHorizontal(HorizontalAlign.LEFT);
    	columna.setClassName(String.class.getName());
    	columna.setAncho(100);
    	columnas.add(columna);
    	
    	columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.descripcionCliente"));
    	columna.setProperty("descripcionVenta");
    	columna.setAlineacionHorizontal(HorizontalAlign.LEFT);
    	columna.setClassName(String.class.getName());
    	columna.setAncho(100);
    	columnas.add(columna);
    	
    	columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.peso.caja"));
    	columna.setProperty("pesoCaja");
    	columna.setClassName(Float.class.getName());
    	columna.setAncho(30);
    	columna.setAjustarAncho(false);
    	columna.setPattern("###,###.##");
    	columnas.add(columna);
    	
    	columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.precio.kg"));
    	columna.setProperty("importeVenta");
    	columna.setClassName(BigDecimal.class.getName());
    	columna.setAncho(30);
    	columna.setAjustarAncho(false);
    	columna.setPattern("$ ###,###.##");
    	columnas.add(columna);
    	
    	columna = new ColumnPrecioConIvaDTO(21f);
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.precio.kg.iva"));
    	columna.setClassName(BigDecimal.class.getName());
    	columna.setAncho(30);
    	columna.setAjustarAncho(false);
    	columna.setPattern("$ ###,###.##");
    	columnas.add(columna);
    	
    	columna = new ColumnPrecioCajaDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.precio.caja"));
    	columna.setClassName(BigDecimal.class.getName());
    	columna.setAncho(50);
    	columna.setPattern("$ ###,###.##");
    	columnas.add(columna);
    	
    	columna = new ColumnPrecioCajaConIvaDTO(21f);
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.precio.caja.iva"));
    	columna.setClassName(BigDecimal.class.getName());
    	columna.setAncho(50);
    	columna.setPattern("$ ###,###.##");
    	columnas.add(columna);
    	
    	planilla.setColumns(columnas);
    	
    	uiModel.addAttribute("planillaDTO", planilla);
    	return "planilla/cliente/grillaColumna";
    }
    
    @RequestMapping(value = "/cliente/generar/{indices}", method = RequestMethod.GET, produces = "text/html")
    public String generar(@PathVariable("indices") Integer[] indices, Model uiModel, HttpServletResponse response) {
    	
    	PlanillaClienteDTO planilla = (PlanillaClienteDTO) uiModel.asMap().get("planillaDTO");
    		
		ByteArrayOutputStream bytes = reportePresupuestoManager.generarReporte(planilla, indices);
		//String fileDownload = "Pedido_"+Utils.generarNroRemito(pedido);
		String fileDownload = "presupuesto_" + planilla.getIdCategoria();
		response.setHeader("Content-Disposition", "attachment;filename=" + fileDownload + ".pdf");
		response.setContentType( "application/pdf" );
        response.setContentLength((int) bytes.size());
	
        try {
	        OutputStream outStream = response.getOutputStream();
			bytes.writeTo(outStream);
			outStream.flush();
			outStream.close();
			bytes.close();
    	} catch (IOException e) {
			return "redirect:/home";
		}
    	return null;
    }
}
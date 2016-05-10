package com.soutech.frigento.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.CategoriaService;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.web.dto.reports.ColumnPrecioCajaConIvaDTO;
import com.soutech.frigento.web.dto.reports.ColumnPrecioCajaDTO;
import com.soutech.frigento.web.dto.reports.ColumnPrecioConIvaDTO;
import com.soutech.frigento.web.dto.reports.ColumnReporteDTO;
import com.soutech.frigento.web.dto.reports.PlanillaClienteDTO;
import com.soutech.frigento.web.reports.ReportePresupuestoManager;
import com.soutech.frigento.web.validator.ErrorJSONHandler;

import ar.com.fdvs.dj.domain.constants.HorizontalAlign;

@Controller
@RequestMapping(value="/planilla")
@Secured({"ROLE_ADMIN"})
@SessionAttributes(names={"planillaDTO", "columnaList", "rpcList"})
public class PlanillaController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String BUSQUEDA_DEFAULT = "usuario?sortFieldName=nombre,apellido&sortOrder=asc,asc";
    
    @Autowired
    private RelProductoCategoriaService relProductoCategoriaService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ReportePresupuestoManager reportePresupuestoManager;
    @Autowired
    private ErrorJSONHandler errorJSONHandler;
    
    @RequestMapping(value = "/cliente", params = "filtro", produces = "text/html")
    public String filtroCliente(Model uiModel) {
    	List<Categoria> categorias = categoriaService.obtenerCategorias();
    	uiModel.addAttribute("categoriaList", categorias);
    	PlanillaClienteDTO planilla = new PlanillaClienteDTO();
    	uiModel.addAttribute("planillaDTO", planilla);
    	if(!categorias.isEmpty()){
    		List<RelProductoCategoria> rpcList = relProductoCategoriaService.obtenerProductosCategoriaParaVenta(new Date(), categorias.get(0).getId());
    		uiModel.addAttribute("rpcList", rpcList);
    	}
    	return "planilla/cliente/filtro";
    }
    
    @RequestMapping(value = "/cliente/{time}/{idCat}", produces = "text/html")
    public String getProductos(@PathVariable("time") Long time, @PathVariable("idCat") Short idCat, Model uiModel) {
    	List<RelProductoCategoria> rpcList = relProductoCategoriaService.obtenerProductosCategoriaParaVenta(new Date(time), idCat);
    	uiModel.addAttribute("rpcList", rpcList);
        return "planilla/cliente/grilla";
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/cliente/{time}/{idCat}/{indices}", produces = "text/html")
    public String getColumnas(@PathVariable("time") Long time, @PathVariable("idCat") Short idCat, @PathVariable("indices") Integer[] indices, Model uiModel) {
    	PlanillaClienteDTO planilla = (PlanillaClienteDTO) uiModel.asMap().get("planillaDTO");
    	planilla.setFecha(new Date(time));
    	planilla.setIdCategoria(idCat);
    	
    	List<RelProductoCategoria> rpcList = (List<RelProductoCategoria>) uiModel.asMap().get("rpcList");
    	List<Producto> productos = new ArrayList<Producto>();
    	for (Integer indice : indices) {
    		productos.add(rpcList.get(indice).getProducto());
		}
    	planilla.setRows(productos);
    	
    	List<ColumnReporteDTO> columnas = new ArrayList<ColumnReporteDTO>();
    	
    	ColumnReporteDTO columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.codigo"));
    	columna.setNombreElegido(getMessage("planilla.cliente.columna.producto.codigo").toUpperCase());
    	columna.setProperty("codigo");
    	columna.setClassName(String.class.getName());
    	columna.setAncho(30);
    	columna.setAjustarAncho(false);
    	columnas.add(columna);
    	
    	columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.descripcion"));
    	columna.setNombreElegido(getMessage("planilla.cliente.columna.producto.descripcion").toUpperCase());
    	columna.setProperty("descripcion");
    	columna.setAlineacionHorizontal(HorizontalAlign.LEFT);
    	columna.setClassName(String.class.getName());
    	columna.setAncho(100);
    	columnas.add(columna);
    	
    	columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.descripcionCliente"));
    	columna.setNombreElegido(getMessage("planilla.cliente.columna.producto.descripcionCliente").toUpperCase());
    	columna.setProperty("descripcionVenta");
    	columna.setAlineacionHorizontal(HorizontalAlign.LEFT);
    	columna.setClassName(String.class.getName());
    	columna.setAncho(100);
    	columnas.add(columna);
    	
    	columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.peso.caja"));
    	columna.setNombreElegido(getMessage("planilla.cliente.columna.producto.peso.caja").toUpperCase());
    	columna.setProperty("pesoCaja");
    	columna.setClassName(Float.class.getName());
    	columna.setAncho(30);
    	columna.setAjustarAncho(false);
    	columna.setPattern("#,##0.00");
    	columnas.add(columna);
    	
    	columna = new ColumnReporteDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.precio.kg"));
    	columna.setNombreElegido(getMessage("planilla.cliente.columna.producto.precio.kg").toUpperCase());
    	columna.setProperty("importeVenta");
    	columna.setClassName(BigDecimal.class.getName());
    	columna.setAncho(30);
    	columna.setAjustarAncho(false);
    	columna.setPattern("$ #,##0.00");
    	columnas.add(columna);
    	
    	columna = new ColumnPrecioConIvaDTO(21f);
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.precio.kg.iva"));
    	columna.setNombreElegido(getMessage("planilla.cliente.columna.producto.precio.kg.iva").toUpperCase());
    	columna.setClassName(BigDecimal.class.getName());
    	columna.setAncho(30);
    	columna.setAjustarAncho(false);
    	columna.setPattern("$ #,##0.00");
    	columnas.add(columna);
    	
    	columna = new ColumnPrecioCajaDTO();
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.precio.caja"));
    	columna.setNombreElegido(getMessage("planilla.cliente.columna.producto.precio.caja").toUpperCase());
    	columna.setClassName(BigDecimal.class.getName());
    	columna.setAncho(30);
    	columna.setPattern("$ #,##0.00");
    	columnas.add(columna);
    	
    	columna = new ColumnPrecioCajaConIvaDTO(21f);
    	columna.setNombre(getMessage("planilla.cliente.columna.producto.precio.caja.iva"));
    	columna.setNombreElegido(getMessage("planilla.cliente.columna.producto.precio.caja.iva").toUpperCase());
    	columna.setClassName(BigDecimal.class.getName());
    	columna.setAncho(30);
    	columna.setPattern("$ #,##0.00");
    	columnas.add(columna);
    	
    	planilla.setColumns(columnas);
    	
    	uiModel.addAttribute("planillaDTO", planilla);
    	return "planilla/cliente/grillaColumna";
    }
    
    @RequestMapping(value = "/cliente/columnas", method = RequestMethod.POST, produces = "text/html")
    public String agregarColumna(@ModelAttribute("planillaDTO") PlanillaClienteDTO columnaForm, Model uiModel, HttpServletRequest request) {
    	
    	try{
    		List<ColumnReporteDTO> columns = columnaForm.getColumns();
    		for (ColumnReporteDTO columna : columns) {
				String nombreElegido = columna.getNombreElegido();
				if(!nombreElegido.equals("")){
					columna.setNombre(columna.getNombreElegido());
				}
			}
    		
    	}catch(Exception e){
    		String json = errorJSONHandler.getMensajeGenericoJSON(getMessage("mensaje.error.generico"));
    		uiModel.addAttribute("messageAjax", json);
    		return "ajax/value";
    	}
    	
    	uiModel.asMap().clear();
    	request.setAttribute("messageAjax", "");
        return "body";
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
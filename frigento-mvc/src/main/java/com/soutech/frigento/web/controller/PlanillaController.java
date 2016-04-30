package com.soutech.frigento.web.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.CategoriaService;
import com.soutech.frigento.service.RelProductoCategoriaService;
import com.soutech.frigento.web.dto.PlanillaClienteDTO;

@Controller
@RequestMapping(value="/planilla")
@Secured({"ROLE_ADMIN"})
public class PlanillaController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String BUSQUEDA_DEFAULT = "usuario?sortFieldName=nombre,apellido&sortOrder=asc,asc";
    
    @Autowired
    public RelProductoCategoriaService relProductoCategoriaService;
    @Autowired
    public CategoriaService categoriaService;

    
    @RequestMapping(value = "/cliente", params = "filtro", produces = "text/html")
    public String filtroCliente(Model uiModel) {
    	uiModel.addAttribute("categoriaList", categoriaService.obtenerCategorias());
    	PlanillaClienteDTO planilla = new PlanillaClienteDTO();
    	planilla.setFecha(new Date());
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
    	
    	uiModel.addAttribute("fechaSel", new Date(time));
    	uiModel.addAttribute("idCatSel", idCat);
    	uiModel.addAttribute("productosSel", codigosProd);
    	return "planilla/cliente/grillaColumna";
    }
}
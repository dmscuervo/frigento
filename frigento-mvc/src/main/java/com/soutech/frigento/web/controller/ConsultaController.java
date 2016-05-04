package com.soutech.frigento.web.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soutech.frigento.dto.ConsultaGananciaDTO;
import com.soutech.frigento.service.AdministracionService;
import com.soutech.frigento.util.Constantes;

@Controller
@RequestMapping(value="/consulta")
@Secured({"ROLE_ADMIN"})
public class ConsultaController extends GenericController {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String BUSQUEDA_DEFAULT = "usuario?sortFieldName=nombre,apellido&sortOrder=asc,asc";
    
    @Autowired
    private AdministracionService administracionService;
    
    @RequestMapping(value = "/ganancia", params = "filtro", produces = "text/html")
    public String filtroGanancia(Model uiModel) {
    	return "consulta/ganancia/filtro";
    }
    
    @RequestMapping(value = "/ganancia/buscar/{tipo}/{periodo}/{idAgrupamiento}", produces = "text/html")
    public String buscarGanancia(@PathVariable("tipo") Short tipo, @PathVariable("periodo") String periodo, @PathVariable("idAgrupamiento") Short idAgrupamiento, Model uiModel) {
    	List<ConsultaGananciaDTO> obtenerGanancia = administracionService.obtenerGanancia(tipo, periodo, idAgrupamiento);
    	uiModel.addAttribute("registros", obtenerGanancia);
    	if(tipo.equals(Constantes.CONSULTA_TIPO_RESUMEN)){
    		
    		return "consulta/ganancia/grillaGananciaResumen";
    		
    	}else if(Constantes.CONSULTA_TIPO_DETALLADO.equals(tipo)){
			if(idAgrupamiento.equals(Constantes.CONSULTA_GANANCIA_PRODUCTO)){
				
				return "consulta/ganancia/grillaGananciaProducto";
				
			}else if(idAgrupamiento.equals(Constantes.CONSULTA_GANANCIA_VENTA)){
				
				
				
			}else if(idAgrupamiento.equals(Constantes.CONSULTA_GANANCIA_AMBOS)){
				
				
				
			}
		}
        return "consulta/ganancia/grilla";
    }
    
}
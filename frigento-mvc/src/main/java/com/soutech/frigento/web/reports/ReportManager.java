package com.soutech.frigento.web.reports;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.soutech.frigento.dto.ItemVentaDTO;
import com.soutech.frigento.exception.ReporteException;
import com.soutech.frigento.model.Parametro;
import com.soutech.frigento.model.Pedido;
import com.soutech.frigento.model.RelPedidoProducto;
import com.soutech.frigento.model.Venta;
import com.soutech.frigento.util.Constantes;
import com.soutech.frigento.util.PrinterStack;
import com.soutech.frigento.util.Utils;
import com.soutech.frigento.web.dto.RemitoDTO;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class ReportManager{
	
	public ReportManager() {
	}
  
	static Logger logger = Logger.getLogger(ReportManager.class);
	
	public ByteArrayOutputStream generarRemitoPedido(List<RelPedidoProducto> relPedProdList) throws ReporteException{
		Pedido pedido = relPedProdList.get(0).getPedido();
		Map<String, Object> parameters = new HashMap<String, Object>();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(pedido.getFecha());
    	parameters.put("dia", Utils.aTextoConCeroIzqSegunCantDigitos(cal.get(Calendar.DAY_OF_MONTH), 2));
    	parameters.put("mes", Utils.aTextoConCeroIzqSegunCantDigitos(cal.get(Calendar.MONTH), 2));
    	parameters.put("anio", String.valueOf(cal.get(Calendar.YEAR)));
    	parameters.put("nroPedido", Utils.generarNroRemito(pedido));
    	parameters.put("destinatario",Parametro.NOMBRE_PROVEEDOR);
    	parameters.put("domicilio","");
    	
    	List<RemitoDTO> items = new ArrayList<RemitoDTO>();
    	for (RelPedidoProducto rpp : relPedProdList) {
			RemitoDTO remito = new RemitoDTO();
			remito.setCantidad(rpp.getCantidad()/rpp.getProductoCosto().getProducto().getPesoCaja());
			remito.setProducto(rpp.getProductoCosto().getProducto().getCodigo().concat("-").concat(rpp.getProductoCosto().getProducto().getDescripcion()));
			remito.setPu(rpp.getCosto().multiply(new BigDecimal(rpp.getProductoCosto().getProducto().getPesoCaja())));
			remito.setImporte(rpp.getCosto().multiply(new BigDecimal(rpp.getCantidad().intValue())).setScale(2, RoundingMode.HALF_UP));
			items.add(remito);
		}
    	
    	//El remito permite hasta 21 items
    	while(items.size() % 21 != 0){
    		RemitoDTO remitos = new RemitoDTO();
    		items.add(remitos);
    	}
    	String archivoReporte = "remitoConfirmado";
    	if(pedido.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO))){
    		archivoReporte = "remitoEntregado";
    	}else if(pedido.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ANULADO))){
    		archivoReporte = "remitoAnulado";
    	}
    	
    	ByteArrayOutputStream bytes = null;
		try {
			bytes = buildReportToByteArrayOutputStream(archivoReporte, parameters, "ireport/", items);
		} catch (Exception e) {
			throw new ReporteException("pedido.generar.remito.error");
		}
		return bytes;
	}
	
	public ByteArrayOutputStream generarRemitoVenta(Venta pedido) throws ReporteException{
		Map<String, Object> parameters = new HashMap<String, Object>();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(pedido.getFecha());
    	parameters.put("dia", Utils.aTextoConCeroIzqSegunCantDigitos(cal.get(Calendar.DAY_OF_MONTH), 2));
    	parameters.put("mes", Utils.aTextoConCeroIzqSegunCantDigitos(cal.get(Calendar.MONTH), 2));
    	parameters.put("anio", String.valueOf(cal.get(Calendar.YEAR)));
    	parameters.put("nroPedido", Utils.generarNroRemito(pedido));
    	parameters.put("destinatario",Parametro.NOMBRE_PROVEEDOR);
    	parameters.put("domicilio","");
    	
    	List<RemitoDTO> itemsRemito = new ArrayList<RemitoDTO>();
    	for (ItemVentaDTO item : pedido.getItems()) {
			RemitoDTO remito = new RemitoDTO();
			remito.setCantidad(item.getCantidad());
			remito.setProducto(item.getProducto().getCodigo().concat("-").concat(item.getProducto().getDescripcion()));
			remito.setPu(item.getCostoVenta());
			remito.setImporte(item.getImporteVenta());
			itemsRemito.add(remito);
		}
    	
    	//El remito permite hasta 21 items
    	while(itemsRemito.size() % 21 != 0){
    		RemitoDTO remitos = new RemitoDTO();
    		itemsRemito.add(remitos);
    	}
    	String archivoReporte = "remitoConfirmado";
    	if(pedido.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO))){
    		archivoReporte = "remitoEntregado";
    	}else if(pedido.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ANULADO))){
    		archivoReporte = "remitoAnulado";
    	}
    	
    	ByteArrayOutputStream bytes = null;
		try {
			bytes = buildReportToByteArrayOutputStream(archivoReporte, parameters, "ireport/", itemsRemito);
		} catch (Exception e) {
			throw new ReporteException("pedido.generar.remito.error");
		}
		return bytes;
	}
  
    
    /**
     * @param reportName - nombre del archivo de reporte sin extension
     * @param parameters - map con los parametros a escribir
     * @param response  
     * @throws IOException
     */
    private ByteArrayOutputStream buildReportToByteArrayOutputStream(String reportName, Map<String, Object> parameters, 
    		String reportsPath, List<RemitoDTO> items) throws Exception{
    	
    	logger.info("iniciando la creacion del reporte: " + reportName );  
    	ByteArrayOutputStream out = new ByteArrayOutputStream();;
    	try {
    		String fileName = reportsPath + reportName + ".jasper" ;	
    		
    		String path = this.getClass().getClassLoader().getResource(fileName).getPath();
    		File reportFile = new File(path);
    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(reportFile);
    		
    		logger.info("iniciando la exportacion");
			JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(items);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrbcds);
			logger.info("fin de la escritura del reporte" );
			
			if(parameters.get("newFileName")!= null){
				reportName = (String)parameters.get("newFileName");
			}
			
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);    
			exporter.exportReport();
    		
    		logger.info("fin de exportacion - reporte generado con exito.");  
    		
    	}catch (Exception ex){
    		logger.error("Error en la creacion del reporte: "+ PrinterStack.getStackTraceAsString(ex), ex.getCause());
    		throw ex;
    	}
    	
    	return out ;
    }
    
}

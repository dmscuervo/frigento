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
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.soutech.frigento.dto.ItemVentaDTO;
import com.soutech.frigento.exception.ReporteException;
import com.soutech.frigento.model.Parametro;
import com.soutech.frigento.model.Pedido;
import com.soutech.frigento.model.RelPedidoProducto;
import com.soutech.frigento.model.RelVentaProducto;
import com.soutech.frigento.model.Venta;
import com.soutech.frigento.util.Constantes;
import com.soutech.frigento.util.PrinterStack;
import com.soutech.frigento.util.Utils;
import com.soutech.frigento.web.dto.RemitoDTO;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class ReportManager{
	
	static Logger logger = Logger.getLogger(ReportManager.class);
	protected static ResourceBundle reportsResourceBundle;
	protected static Locale locale;
	static{
		locale = Locale.getDefault();
		reportsResourceBundle = ResourceBundle.getBundle("i18n.reports.ReportesResource", locale);
	}
	@Autowired
	private MessageSource messageSource;
	
	public ByteArrayOutputStream generarRemitoPedido(List<RelPedidoProducto> relPedProdList) throws ReporteException{
		Pedido pedido = relPedProdList.get(0).getPedido();
		Map<String, Object> parameters = new HashMap<String, Object>();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(pedido.getFecha());
    	parameters.put("header_cantidad", messageSource.getMessage("pedido.cantidad.caja", null, locale));
    	parameters.put("dia", Utils.aTextoConCeroIzqSegunCantDigitos(cal.get(Calendar.DAY_OF_MONTH), 2));
    	parameters.put("mes", Utils.aTextoConCeroIzqSegunCantDigitos(cal.get(Calendar.MONTH), 2));
    	parameters.put("anio", String.valueOf(cal.get(Calendar.YEAR)));
    	parameters.put("nroPedido", Utils.generarNroRemito(pedido));
    	parameters.put("destinatario",Parametro.NOMBRE_PROVEEDOR);
    	parameters.put("domicilio","");
    	parameters.put(JRParameter.REPORT_LOCALE, locale);
    	parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, reportsResourceBundle);
    	
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
	
	public ByteArrayOutputStream generarRemitoVenta(Venta venta) throws ReporteException{
		List<RemitoDTO> itemsRemito = new ArrayList<RemitoDTO>();
    	for (ItemVentaDTO item : venta.getItems()) {
			RemitoDTO remito = new RemitoDTO();
			remito.setCantidad(item.getCantidad());
			remito.setProducto(item.getProducto().getCodigo().concat("-").concat(item.getProducto().getDescripcion()));
			remito.setPu(item.getImporteVenta());
			remito.setImporte(item.getImporteVenta().multiply(new BigDecimal(item.getCantidad()).setScale(2, RoundingMode.HALF_UP)));
			itemsRemito.add(remito);
		}
    	
    	return generarRemitoVenta(venta , itemsRemito);
	}
	
	public ByteArrayOutputStream generarRemitoVenta(List<RelVentaProducto> relVtaProdList) throws ReporteException{
		List<RemitoDTO> itemsRemito = new ArrayList<RemitoDTO>();
    	for (RelVentaProducto rvp : relVtaProdList) {
			RemitoDTO remito = new RemitoDTO();
			remito.setCantidad(rvp.getCantidad());
			String descripcion = "";
			if(rvp.getPromocion() != null){
				descripcion = messageSource.getMessage("venta.promocion", new Object[]{rvp.getPromocion().getCantidadMinima()}, locale);
			}
			descripcion = descripcion.concat(rvp.getRelProductoCategoria().getProducto().getDescripcion());
			remito.setProducto(descripcion);
			remito.setPu(rvp.getPrecioVenta());
			remito.setImporte(new BigDecimal(rvp.getCantidad()).multiply(rvp.getPrecioVenta()).setScale(2, RoundingMode.HALF_UP));
			itemsRemito.add(remito);
		}
    	
    	Venta venta = relVtaProdList.get(0).getVenta();
		return generarRemitoVenta(venta , itemsRemito);
	}
	
	private ByteArrayOutputStream generarRemitoVenta(Venta venta, List<RemitoDTO> itemsRemito) throws ReporteException{
		Map<String, Object> parameters = new HashMap<String, Object>();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(venta.getFecha());
    	parameters.put("header_cantidad", messageSource.getMessage("venta.cantidad.kg", null, locale));
    	parameters.put("dia", Utils.aTextoConCeroIzqSegunCantDigitos(cal.get(Calendar.DAY_OF_MONTH), 2));
    	parameters.put("mes", Utils.aTextoConCeroIzqSegunCantDigitos(cal.get(Calendar.MONTH), 2));
    	parameters.put("anio", String.valueOf(cal.get(Calendar.YEAR)));
    	parameters.put("nroPedido", Utils.generarNroRemito(venta));
    	parameters.put("destinatario",venta.getUsuario().getNombre().concat(venta.getUsuario().getApellido() == null ? "" : " ".concat(venta.getUsuario().getApellido())));
    	parameters.put("domicilio","");
    	parameters.put(JRParameter.REPORT_LOCALE, locale);
    	parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, reportsResourceBundle);
    	
    	//El remito permite hasta 21 items
    	while(itemsRemito.size() % 21 != 0){
    		RemitoDTO remitos = new RemitoDTO();
    		itemsRemito.add(remitos);
    	}
    	String archivoReporte = "remitoConfirmado";
    	if(venta.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO))){
    		archivoReporte = "remitoEntregado";
    	}else if(venta.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ANULADO))){
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

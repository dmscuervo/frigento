package com.soutech.frigento.web.reports;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.soutech.frigento.model.RelPedidoProducto;
import com.soutech.frigento.util.PrinterStack;

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
///**
// * @param reportName - nombre del archivo de reporte sin extension
// * @param parameters - map con los parametros a escribir
// * @param response  
// * @throws IOException
// */
//  public static void buildReportToStream(String reportName, Map<String, Object> parameters, 
//		  HttpServletResponse response, String reportsPath)throws Exception{
//	  
//	logger.info("iniciando la creacion del reporte: " + reportName );  
//	
//	Calendar cal;
//	String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
//	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
//	
//	ByteArrayOutputStream out = new ByteArrayOutputStream();
//	sdf.setTimeZone(TimeZone.getDefault());
//	
//	if(!reportsPath.endsWith(File.separator)){
//		reportsPath = reportsPath.concat(File.separator);
//	}
//
//	parameters.put("subreporte", reportsPath);
//	
//	try {
//		String fileName = reportsPath + reportName ;	
//		
//	    File reportFile = new File(fileName + ".jasper");
//	
//	    @SuppressWarnings("deprecation")
//		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(reportFile);
//
//	    logger.info("iniciando la escritura del reporte" );
//	    Usuario usuario =(Usuario)parameters.get("usuario");
//	    Collection<Usuario> col = new ArrayList<Usuario>();
//	    	col.add(usuario);
//	   
//
//	    logger.info("iniciando la exportacion");
//    	JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(col);
// 	    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,jrbcds);
// 	    logger.info("fin de la escritura del reporte" );
//    	
// 	    if(parameters.get("newFileName")!= null){
//			reportName = (String)parameters.get("newFileName");
//		}
// 	    
//	    response.setHeader("Content-Disposition", "attachment;filename=" + reportName + ".pdf");
//    	JRPdfExporter exporter = new JRPdfExporter();
//        response.setContentType( "application/pdf" );
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);    
//        exporter.exportReport();
//	    
//	    logger.info("fin de exportacion - reporte generado con exito.");  
//
//	}catch (Exception ex){
//    	cal = Calendar.getInstance(TimeZone.getDefault());
//        logger.error("Fecha: "+sdf.format(cal.getTime())+" Nombre Clase: ReportManager");
//        logger.error("error en la creacion del reporte: "+ PrinterStack.getStackTraceAsString(ex), ex.getCause());
//        throw ex;
//    }
//	response.getOutputStream().write(out.toByteArray());
//  }
  
    
    /**
     * @param reportName - nombre del archivo de reporte sin extension
     * @param parameters - map con los parametros a escribir
     * @param response  
     * @throws IOException
     */
    public ByteArrayOutputStream buildReportToByteArrayOutputStream(String reportName, Map<String, Object> parameters, 
    		String reportsPath, List<RelPedidoProducto> items)throws Exception{
    	
    	logger.info("iniciando la creacion del reporte: " + reportName );  
    	
    	Calendar cal;
    	String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
    	
    	ByteArrayOutputStream out = new ByteArrayOutputStream();;
    	sdf.setTimeZone(TimeZone.getDefault());
    	
    	//parameters.put("subreporte", reportsPath);
    	
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
    		cal = Calendar.getInstance(TimeZone.getDefault());
    		logger.error("Fecha: "+sdf.format(cal.getTime())+" Nombre Clase: ReportManager");
    		logger.error("error en la creacion del reporte: "+ PrinterStack.getStackTraceAsString(ex), ex.getCause());
    		throw ex;
    	}
    	
    	return out ;
    }
    
}

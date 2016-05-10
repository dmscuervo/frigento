package com.soutech.frigento.web.reports;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.soutech.frigento.util.PrinterStack;
import com.soutech.frigento.util.Utils;
import com.soutech.frigento.web.dto.reports.ColumnExpressionReporteDTO;
import com.soutech.frigento.web.dto.reports.ColumnReporteDTO;
import com.soutech.frigento.web.dto.reports.PlanillaClienteDTO;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

@Component
public class ReportePresupuestoManager {

	private final Logger log = Logger.getLogger(this.getClass());
	
	public ByteArrayOutputStream generarReporte(PlanillaClienteDTO contenido, Integer[] indicesSel){
		ByteArrayOutputStream os = null;
		try {
	
			FastReportBuilder drb = new FastReportBuilder();
			
			Style headerStyle = new Style();
			headerStyle.setBackgroundColor(new Color(230,230,230));
			headerStyle.setBorder(new Border(1, Border.BORDER_STYLE_SOLID, Color.BLACK));
			//headerStyle.setBorderColor(Color.black);
			headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
			headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
			headerStyle.setTransparency(Transparency.OPAQUE);
			
			Style rowStyle = new Style();
			rowStyle.setBorder(new Border(1, Border.BORDER_STYLE_SOLID, Color.BLACK));
			rowStyle.setHorizontalAlign(HorizontalAlign.CENTER);
			rowStyle.setVerticalAlign(VerticalAlign.MIDDLE);
			rowStyle.setTransparency(Transparency.TRANSPARENT);
			
			drb
			//.setDefaultStyles(headerStyle, headerStyle, headerStyle, rowStyle)
			//.setMargins(20, 20, 20, 20)
			//.setPrintBackgroundOnOddRows(true)
			.setUseFullPageWidth(true);
			
			//Agrego las columnas
			AbstractColumn abstactColumn;
			//En caso de que no sean seleccionados por el usuario, agregao estos 
			//campos xq son obligatorios para poder calcular precios con IVA.
			drb.addField("importeVenta", BigDecimal.class);
			drb.addField("pesoCaja", Float.class);
			for (int i = 0; i < indicesSel.length; i++) {
				Integer indice = indicesSel[i];
				ColumnReporteDTO column = contenido.getColumns().get(indice);
				if(column instanceof ColumnExpressionReporteDTO){
					abstactColumn = ColumnBuilder.getNew().setCustomExpression((ColumnExpressionReporteDTO)column).build();
					abstactColumn.setTitle(column.getNombreElegido());
					abstactColumn.setWidth(column.getAncho());
					abstactColumn.setFixedWidth(column.isAjustarAncho());
					abstactColumn.setPattern(column.getPattern());
					abstactColumn.setStyle(column.generarStyle(rowStyle));
					abstactColumn.setHeaderStyle(headerStyle);
					drb.addColumn(abstactColumn);
					
				}else{
					abstactColumn = ColumnBuilder.getNew().setColumnProperty(column.getProperty(), column.getClassName()).build();
					abstactColumn.setTitle(column.getNombreElegido());
					abstactColumn.setWidth(column.getAncho());
					abstactColumn.setFixedWidth(column.isAjustarAncho());
					abstactColumn.setPattern(column.getPattern());
					abstactColumn.setStyle(column.generarStyle(rowStyle));
					abstactColumn.setHeaderStyle(headerStyle);
					drb.addColumn(abstactColumn);
				}
			}
			Set<Object> keySet = System.getProperties().keySet();
			for (Object object : keySet) {
				String property = System.getProperty((String)object);
				System.out.println(((String)object).concat(": ").concat(property));
			}
			String fileName = null;
			if(contenido.getOrientacion().equals(PlanillaClienteDTO.ORIENTACION_VERTICAL)){
				fileName = "ireport/presupuesto.jrxml" ;
			}else if(contenido.getOrientacion().equals(PlanillaClienteDTO.ORIENTACION_HORIZONTAL)){
				fileName = "ireport/presupuestoHorizontal.jrxml" ;	
			}
			if(fileName != null){
	    		String path = this.getClass().getClassLoader().getResource(fileName).getPath();
				drb.setTemplateFile(path);
			}
			
			DynamicReport dr = drb.build();
	
			JRDataSource ds = new JRBeanCollectionDataSource(contenido.getRows());
			
			Map<String, Object> params = new HashMap<String, Object>();
			
			String path = this.getClass().getClassLoader().getResource("ireport/frigento.png").getPath();
			byte[] bytes = Utils.getByteArray(path);
			params.put("LOGO", bytes);
			params.put("FECHA", Utils.formatDate(contenido.getFecha(), Utils.SDF_DDMMYYYY));
			JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), params);
			JasperPrint jp = JasperFillManager.fillReport(jr, params, ds);
			
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8"); //CP1252
			exporter.setParameter(JRPdfExporterParameter.CHARACTER_ENCODING, "UTF-8");
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
	  		os = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
	  		exporter.exportReport();
			
		} catch (Exception e) {
			log.error(PrinterStack.getStackTraceAsString(e));
		}
		
		return os;
	}
}

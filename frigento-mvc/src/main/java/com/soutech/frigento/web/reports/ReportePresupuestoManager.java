package com.soutech.frigento.web.reports;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.soutech.frigento.util.PrinterStack;
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
			headerStyle.setTransparency(Transparency.OPAQUE);
			
			Style rowStyle = new Style();
			rowStyle.setBorder(new Border(1, Border.BORDER_STYLE_SOLID, Color.BLACK));
			rowStyle.setHorizontalAlign(HorizontalAlign.CENTER);
			rowStyle.setTransparency(Transparency.TRANSPARENT);
			
			drb
			//.setPrintBackgroundOnOddRows(true)
			.setUseFullPageWidth(true);
			
			//Agrego las columnas
			for (int i = 0; i < indicesSel.length; i++) {
				Integer indice = indicesSel[i];
				ColumnReporteDTO column = contenido.getColumns().get(indice);
				if(column instanceof ColumnExpressionReporteDTO){
					//drb.addColumn(ColumnBuilder.getNew().setCustomExpression((ColumnExpressionReporteDTO)column).build());
					drb.addColumn(column.getNombre(), (ColumnExpressionReporteDTO)column, column.getAncho(), column.isAjustarAncho(), column.getPattern(), rowStyle);
				}else{
					drb.addColumn(ColumnBuilder.getNew().setColumnProperty(column.getProperty(), column.getClassName()).setTitle(column.getNombre()).setWidth(column.getAncho()).build());
					//drb.addColumn(column.getNombre(), column.getProperty(), column.getClassName(), column.getAncho(), column.isAjustarAncho());
				}
			}
			
			String fileName = "ireport/presupuesto.jasper" ;	
    		String path = this.getClass().getClassLoader().getResource(fileName).getPath();
			drb.setTemplateFile(path);
			
			DynamicReport dr = drb.build();
	
			JRDataSource ds = new JRBeanCollectionDataSource(contenido.getRows());
			
			Map<String, Object> params = new HashMap<String, Object>();
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

package com.soutech.frigento.web.reports;

import java.util.HashMap;
import java.util.Map;

import com.soutech.frigento.util.PrinterStack;
import com.soutech.frigento.web.dto.reports.ColumnReporteDTO;
import com.soutech.frigento.web.dto.reports.PlanillaClienteDTO;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class ReportePresupuestoManager {

	public ByteOutputStream generarReporte(PlanillaClienteDTO contenido){
		ByteOutputStream os = null;
		try {
	
			FastReportBuilder drb = new FastReportBuilder();
			
			drb.setPrintBackgroundOnOddRows(true)			
			.setUseFullPageWidth(true);
			//Agrego las columnas
			for (ColumnReporteDTO column : contenido.getColumns()) {
					drb.addColumn(column.getNombre(), column.getProperty(), column.getClassName(), column.getAncho(), column.isAjustarAncho());
			}
			
			AbstractColumn columnaCustomExpression = ColumnBuilder.getNew()
					.setCustomExpression(new CustomExpression() {
											private static final long serialVersionUID = -2085748425686769967L;

											@Override
											@SuppressWarnings("rawtypes")
					                        public Object evaluate(Map fields, Map variables, Map parameters) {
					                                String state = (String) fields.get("state");
					                                String branch = (String) fields.get("branch");
					                                String productLine = (String) fields.get("productLine");
					                                Integer count = (Integer) variables.get("REPORT_COUNT");
					                                return count + ": " +state.toUpperCase() + " / " + branch.toUpperCase() + " / " + productLine;
					                        }

											@Override
					                        public String getClassName() {
					                                return String.class.getName();
					                        }

					                }
					).build();
			drb.addColumn(columnaCustomExpression);
			
			
			DynamicReport dr = drb.build();
	
			JRDataSource ds = new JRBeanCollectionDataSource(contenido.getRows());
			
			Map<String, Object> params = new HashMap<String, Object>();
			JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), params);
			JasperPrint jp = JasperFillManager.fillReport(jr, params, ds);
			
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
	  		os = new ByteOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
	  		exporter.exportReport();
			
		} catch (Exception e) {
			PrinterStack.getStackTraceAsString(e);
		}
		
		return os;
	}
}

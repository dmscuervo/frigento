<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	var dataTablePlanillaProductos;

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaGananAmbos')){
			dataTablePlanillaProductos = $('#idGrillaGananAmbos').DataTable({
	    		order: [[ 0, "asc" ],
	    		        [ 1, "asc" ],
	    		        [ 2, "asc" ]],
   		        scrollX: 		true,
   		        scrollCollapse: true,
	    	});
		}
	    
	});
	
	
			
</script>
<table id="idGrillaGananAmbos" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
        <thead>
            <tr>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.fecha" /></th>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.venta" /></th>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.producto" /></th>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.costo" /></th>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.cantidad" /></th>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.importe" /></th>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.ganancia" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="reg" items="${registros}">
        	<tr>
        		<td style="white-space: nowrap;">${reg.venta.fecha}</td>
        		<td style="white-space: nowrap;">
        			<a href="#" onclick="loadInBody('venta/${reg.venta.id}?detalle')">${reg.nroVenta}</a>
        		</td>
        		<td style="white-space: nowrap;">${reg.producto.codigo} - ${reg.producto.descripcion}</td>
        		<td style="white-space: nowrap;"><fmt:formatNumber currencySymbol="$" type="currency" value="${reg.costo}" /></td>
        		<td style="white-space: nowrap;"><fmt:formatNumber value="${reg.cantidad}" minFractionDigits="2" maxFractionDigits="2" /></td>
        		<td style="white-space: nowrap;"><fmt:formatNumber currencySymbol="$" type="currency" value="${reg.importeVenta}" /></td>
        		<td style="white-space: nowrap;"><fmt:formatNumber currencySymbol="$" type="currency" value="${reg.ganancia}" /></td>
        	</tr>
        </c:forEach>
        </tbody>
</table>
<%-- <fmt:formatDate type="date" value="${reg.mes}" pattern="MM"/> --%>
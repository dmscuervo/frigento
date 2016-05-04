<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	var dataTablePlanillaProductos;

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaGananProd')){
			dataTablePlanillaProductos = $('#idGrillaGananProd').DataTable({
	    		order: [[ 0, "asc" ]]
	    	});
		}
	    
	});
	
	
			
</script>
<table id="idGrillaGananProd" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
        <thead>
            <tr>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.producto" /></th>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.esPromo" /></th>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.costo" /></th>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.cantidad" /></th>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.importe" /></th>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.ganancia" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="reg" items="${registros}">
        	<tr>
        		<td style="white-space: nowrap;">${reg.producto.codigo} - ${reg.producto.descripcion}</td>
        		<td style="white-space: nowrap;">${reg.promo}</td>
        		<td style="white-space: nowrap;"><fmt:formatNumber currencySymbol="$" type="currency" value="${reg.costo}" /></td>
        		<td style="white-space: nowrap;"><fmt:formatNumber value="${reg.cantidad}" minFractionDigits="2" maxFractionDigits="2" /></td>
        		<td style="white-space: nowrap;"><fmt:formatNumber currencySymbol="$" type="currency" value="${reg.importeVenta}" /></td>
        		<td style="white-space: nowrap;"><fmt:formatNumber currencySymbol="$" type="currency" value="${reg.ganancia}" /></td>
        	</tr>
        </c:forEach>
        </tbody>
</table>
<%-- <fmt:formatDate type="date" value="${reg.mes}" pattern="MM"/> --%>
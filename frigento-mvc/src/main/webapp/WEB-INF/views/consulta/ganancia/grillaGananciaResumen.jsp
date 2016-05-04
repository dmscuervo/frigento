<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	var dataTablePlanillaProductos;

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaResumen')){
			dataTablePlanillaProductos = $('#idGrillaResumen').DataTable({
	    		order: [[ 0, "asc" ]]
	    	});
		}
	    
	});
	
	
			
</script>
<table id="idGrillaResumen" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
        <thead>
            <tr>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.mes" /></th>
                <th style="white-space: nowrap;"><fmt:message key="consulta.ganancia.grilla.ganancia" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="reg" items="${registros}">
        	<tr>
        		<td style="white-space: nowrap;"><fmt:formatNumber value="${reg.mes}" /></td>
        		<td style="white-space: nowrap;"><fmt:formatNumber currencySymbol="$" type="currency" value="${reg.ganancia}" /></td>
        	</tr>
        </c:forEach>
        </tbody>
</table>
<%-- <fmt:formatDate type="date" value="${reg.mes}" pattern="MM"/> --%>
<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaColumnas')){
	    	$('#idGrillaColumnas').DataTable({
	    		scrollY:        400,
		    	scrollX: 		false,
		        scrollCollapse: true,
		        paging: false,
	    		rowReorder: true,
		        columnDefs: [
		            { orderable: true, className: 'reorder', targets: 0 },
		            { orderable: false, targets: '_all' }
		        ],
		        select: {
	                style: 'multi'
	            }
	    	});
		}
	    
	    if('${informar}' != null){
	    	$('#idModalMensaje').attr('style', 'visible');
	    }
	});
	
	
			
</script>
<span class="messageBlue">
	<fmt:message key="planilla.cliente.seleccionar.producto" /><input type="button" value='<fmt:message key="boton.generar" />' onclick="generar()">
</span>
<table id="idGrillaColumnas" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
        <thead>
            <tr>
            	<th style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna0" /></th>
                <th style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna1" /></th>
                <th style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna2" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${planillaDTO.columns}" var="columna" varStatus="i">
        	<tr>
        		<td style="white-space: nowrap;">${i.index+1}</td>
        		<td style="white-space: nowrap;">
        			${columna.nombre}
        			<input type="hidden" id="idRow-${i.index}" value="${i.index}">
        		</td>
        		<td style="white-space: nowrap;"><input type="text" value="${columna.nombre}"></td>
        	</tr>
        </c:forEach>
        </tbody>
</table>
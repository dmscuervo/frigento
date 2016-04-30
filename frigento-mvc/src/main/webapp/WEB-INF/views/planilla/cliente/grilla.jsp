<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaPlanilla')){
	    	$('#idGrillaPlanilla').DataTable({
	    		scrollY:        400,
		    	scrollX: 		false,
		        scrollCollapse: true,
		        paging: false,
	    		order: [[ 0, "asc" ]],
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
	<fmt:message key="planilla.cliente.seleccionar.producto" /><input type="button" class="btn btn-default btn-primary" value='<fmt:message key="boton.listo" />' onclick="cargarColumnas()">
</span>
<table id="idGrillaPlanilla" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
        <thead>
            <tr>
                <th style="white-space: nowrap;"><fmt:message key="planilla.cliente.prod.codigo" /></th>
                <th style="white-space: nowrap;"><fmt:message key="planilla.cliente.prod.descripcion" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="rpc" items="${rpcList}">
        	<tr>
        		<td style="white-space: nowrap;">${rpc.producto.codigo}</td>
        		<td style="white-space: nowrap;">${rpc.producto.descripcion}</td>
        	</tr>
        </c:forEach>
        </tbody>
</table>
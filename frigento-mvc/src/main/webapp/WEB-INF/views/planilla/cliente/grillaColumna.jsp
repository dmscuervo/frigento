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
	<fmt:message key="planilla.cliente.seleccionar.producto" /><input type="button" value='<fmt:message key="boton.listo" />' onclick="">
</span>
<table id="idGrillaColumnas" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
        <thead>
            <tr>
            	<th>Secuencia</th>
                <th style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna1" /></th>
                <th style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna2" /></th>
            </tr>
        </thead>
        <tbody>
        	<tr>
        		<td style="white-space: nowrap;">1</td>
        		<td style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna.producto.codigo" /></td>
        		<td style="white-space: nowrap;"><input type="text" value='<fmt:message key="planilla.cliente.columna.producto.codigo" />'></td>
        	</tr>
        	<tr>
        		<td style="white-space: nowrap;">2</td>
        		<td style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna.producto.descripcion" /></td>
        		<td style="white-space: nowrap;"><input type="text" value='<fmt:message key="planilla.cliente.columna.producto.descripcion" />'></td>
        	</tr>
        	<tr>
        		<td style="white-space: nowrap;">3</td>
        		<td style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna.producto.descripcionCliente" /></td>
        		<td style="white-space: nowrap;"><input type="text" value='<fmt:message key="planilla.cliente.columna.producto.descripcionCliente" />'></td>
        	</tr>
        	<tr>
        		<td style="white-space: nowrap;">4</td>
        		<td style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna.producto.peso.caja" /></td>
        		<td style="white-space: nowrap;"><input type="text" value='<fmt:message key="planilla.cliente.columna.producto.peso.caja" />'></td>
        	</tr>
        	<tr>
        		<td style="white-space: nowrap;">5</td>
        		<td style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna.producto.precio.kg" /></td>
        		<td style="white-space: nowrap;"><input type="text" value='<fmt:message key="planilla.cliente.columna.producto.precio.kg" />'></td>
        	</tr>
        	<tr>
        		<td style="white-space: nowrap;">6</td>
        		<td style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna.producto.precio.kg.iva" /></td>
        		<td style="white-space: nowrap;"><input type="text" value='<fmt:message key="planilla.cliente.columna.producto.precio.kg.iva" />'></td>
        	</tr>
        	<tr>
        		<td style="white-space: nowrap;">7</td>
        		<td style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna.producto.precio.caja" /></td>
        		<td style="white-space: nowrap;"><input type="text" value='<fmt:message key="planilla.cliente.columna.producto.precio.caja" />'></td>
        	</tr>
        	<tr>
        		<td style="white-space: nowrap;">8</td>
        		<td style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna.producto.precio.caja.iva" /></td>
        		<td style="white-space: nowrap;"><input type="text" value='<fmt:message key="planilla.cliente.columna.producto.precio.caja.iva" />'></td>
        	</tr>
        </tbody>
</table>
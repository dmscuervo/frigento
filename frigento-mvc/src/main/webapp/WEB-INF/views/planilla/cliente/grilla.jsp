<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	var dataTablePlanillaProductos;

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaPlanilla')){
			dataTablePlanillaProductos = $('#idGrillaPlanilla').DataTable({
	    		scrollY:        400,
		    	scrollX: 		false,
		        scrollCollapse: true,
		        paging: false,
		        rowReorder: true,
		        columnDefs: [
		            { orderable: true, className: 'reorder', targets: 0 },
		            { orderable: false, targets: '_all' }
		        ],
	    		//order: [[ 0, "asc" ]],
		    	dom: 'Bfrtip',
	    		select: {
	    			style: 'multi'
	            },
		        //select: true,
		        buttons: [
		            {
		                text: '<fmt:message key="database.grilla.button.select.all" />',
		                action: function () {
		                	dataTablePlanillaProductos.rows().select();
		                }
		            },
		            {
		                text: '<fmt:message key="database.grilla.button.select.none" />',
		                action: function () {
		                	dataTablePlanillaProductos.rows().deselect();
		                }
		            }
		        ]
	    	});
		}
	    
	    if('${informar}' != null){
	    	$('#idModalMensaje').attr('style', 'visible');
	    }
	});
	
	
			
</script>

<div class='row'>
	<div class='col-sm-8'>
		<div class="form-group">
			<label class="col-sm-2 control-label" style="white-space: nowrap;">
				<fmt:message key="planilla.cliente.seleccionar.producto" />&nbsp;<input type="button" class="btn btn-default btn-primary" value='<fmt:message key="boton.listo" />' onclick="cargarColumnas()">
			</label>
		</div>
	</div>
</div>
<div id="idMsgError" class="form-validate" style="display: none;">
	<label style="white-space: nowrap;"></label>
</div>
<table id="idGrillaPlanilla" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
        <thead>
            <tr>
            	<th style="white-space: nowrap;"><fmt:message key="planilla.cliente.columna0" /></th>
                <th style="white-space: nowrap;"><fmt:message key="planilla.cliente.prod.codigo" /></th>
                <th style="white-space: nowrap;"><fmt:message key="planilla.cliente.prod.descripcion" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="rpc" items="${rpcList}" varStatus="index">
        	<tr>
        		<td style="white-space: nowrap;">
        			${index.index+1}
        		</td>
        		<td style="white-space: nowrap;">
        			${rpc.producto.codigo}
        			<input type="hidden" id="idRow-${index.index}" value="${index.index}">
        		</td>
        		<td style="white-space: nowrap;">${rpc.producto.descripcion}</td>
        	</tr>
        </c:forEach>
        </tbody>
</table>
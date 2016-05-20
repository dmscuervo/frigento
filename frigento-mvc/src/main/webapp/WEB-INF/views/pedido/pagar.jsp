<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">

	var dataTablePagarPedidos;

	$(document).ready(function(){
		$('#datetimepickerVentaFechaPagado').datetimepicker({
			ignoreReadonly: true,
			defaultDate: ${pedidoAPagar.fecha.time},
			maxDate: moment(),
			minDate: ${pedidoAPagar.fecha.time},
			locale: 'es'
	    });
		if(!$.fn.DataTable.isDataTable('#idGrillaPagar')){
			dataTablePagarPedidos = $('#idGrillaPagar').DataTable({
	    		scrollY:        200,
		    	scrollX: 		false,
		        scrollCollapse: true,
		        paging: false,
		        select: {
	                style: 'multi'
	            },
				searching: false
	    	});
		}
	});

	function confirmar(){
		var indices = '';
		$('.selected').find('td input:hidden').each(function(){
			indices = indices + $(this).val() + ",";
		});
		
		//Elimino el ultimo ;
		indices = indices.substring(0, indices.length-1);
		
		//Bloqueo contenido
		blockControl($('#wrapperContenidoGrilla'));
		
		$('#idModalAccion').on('hidden.bs.modal', function () {
			$('#idModalAccion').modal('hide');
			var url = '${pathBase}' + 'pedido/pagar/'+moment($('#idFechaPago').val(),'DD/MM/YYYY HH:mm')+'/'+indices;
			$.post(url, function(result){
				//Cargo contenido
            	$('#page-wrapper').html(result);
			});
		});
		$('#idModalAccion').modal('hide');
	}

</script>

<div class="modal admin" id="idModalAccion" tabindex="-1" role="dialog" style="width: 100%">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">
					<fmt:message key="pedido.pagar.title">
						<fmt:param value='${pedidoAPagar.id}'/>
					</fmt:message>
				</h4>
			</div>
			<div class="modal-body">
				<div class='row'>
			        <div class='col-sm-6'>    
						<div class="form-group" >
							<label class="col-sm-2 control-label" for="idFechaPago" style="white-space: nowrap;">
								<fmt:message key="pedido.pagar.fecha" />
							</label>
						</div>
			        </div>
			        <div class='col-sm-6'>
			        	<div class="form-group">
			        		<div class='input-group date' id='datetimepickerVentaFechaPagado'>
			       				<input type="text" class="form-control" id="idFechaPago" readonly="readonly"/>
			          				<span class="input-group-addon">
			               			<span class="glyphicon glyphicon-calendar"></span>
			           			</span>
			       			</div>
						</div>
			        </div>
		    	</div>
			</div>
			<c:if test="${not empty otrosPedidos }">
			<div class="modal-body">
				<p>
					<fmt:message key="pedido.pagar.otros" />
				</p>
				<div class='row'>
			        <div class='col-sm-12'>    
						<div class="form-group" >
							<table id="idGrillaPagar" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
						        <thead>
						            <tr>
						            	<th style="white-space: nowrap;"><fmt:message key="pedido.id" /></th>
                						<th style="white-space: nowrap;"><fmt:message key="pedido.fecha" /></th>
                						<th style="white-space: nowrap;"><fmt:message key="pedido.costo" /></th>
						            </tr>
						        </thead>
						        <tbody>
						        <c:forEach var="ped" items="${otrosPedidos}" varStatus="i">
						        	<tr>
						        		<td style="white-space: nowrap;">
						        			${ped.id}
						        			<input type="hidden" id="idRow-${i.index}" value="${i.index}">
						        		</td>
						        		<td style="white-space: nowrap;"><fmt:formatDate value="${ped.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
						        		<td style="white-space: nowrap;">${ped.costo}</td>
						        	</tr>
						        </c:forEach>
						        </tbody>
						</table>
						</div>
			        </div>
		    	</div>
			</div>
			</c:if>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					onclick="confirmar()"><fmt:message key="boton.aceptar"/></button>
				<button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="boton.cancelar"/></button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">

	$(document).ready(function(){
		$('#datetimepickerVentaFechaEntregado').datetimepicker({
			ignoreReadonly: true,
			defaultDate: moment(),
			maxDate: moment(),
			locale: 'es'
	    });
	});

	function confirmar(){
		$('#idModalAccion').on('hidden.bs.modal', function () {
			$('#idModalAccion').modal('hide');
			submitInBody($('#idForm'));
		});
		$('#idModalAccion').modal('hide');
	}

</script>

<div class="modal fade" id="idModalAccion" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<c:url var="urlCumplir" value="/venta/cumplir" />
			<form:form action="${urlCumplir}" method="post"
				class="form-horizontal" commandName="ventaForm" id="idForm">
			<form:hidden path="id" />
			<form:hidden path="usuario.email" />
			<form:hidden path="fecha" />
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title"><fmt:message key="venta.cumplir.title"/></h4>
			</div>
			<div class="modal-body">
				<p>
					<fmt:message key="venta.cumplir.confirm">
						<fmt:param value='${ventaForm.id}'/>
					</fmt:message>
				</p>
				<div class='row'>
			        <div class='col-sm-4'>    
						<div class="form-group" >
							<label class="col-sm-2 control-label" for="idFechaEntrega" style="white-space: nowrap;">
								<fmt:message key="venta.fecha.entregado" />
							</label>
						</div>
			        </div>
			        <div class='col-sm-4'>
			        	<div class="form-group">
	             			<div class='input-group date' id='datetimepickerVentaFechaEntregado'>
	             				<form:input path="fechaEntregado" cssClass="form-control" id="idFechaEntrega" readonly="true" />
	                				<span class="input-group-addon">
	                     			<span class="glyphicon glyphicon-calendar"></span>
	                 			</span>
	             			</div>
						</div>
			        </div>
		    	</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					onclick="confirmar()"><fmt:message key="boton.aceptar"/></button>
				<button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="boton.cancelar"/></button>
			</div>
			</form:form>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
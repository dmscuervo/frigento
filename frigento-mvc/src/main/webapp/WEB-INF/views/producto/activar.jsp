<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">

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
			<c:url var="urlActivar" value="/producto/activar" />
			<form:form action="${urlActivar}" method="post"
				class="form-horizontal" commandName="productoForm" id="idForm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title"><fmt:message key="producto.activar.title"/></h4>
			</div>
			<div class="modal-body">
				<p>
					<fmt:message key="producto.activar.confirm">
						<fmt:param value='${productoForm.descripcion}'/>
					</fmt:message>
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					onclick="confirmar()"><fmt:message key="boton.aceptar"/></button>
				<button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="boton.cancelar"/></button>
			</div>
				<form:hidden path="id" />
			</form:form>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
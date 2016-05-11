<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!--CONFIRM NO QUITAR. ESTA ETIQUETA PERMITE SABER QUE EL CONTENIDO SE CARGA EN UN DIV INTERNO-->
<script type="text/javascript">

	function confirmar(){
		$('#idModalAccion').on('hidden.bs.modal', function () {
			$('#idModalAccion').modal('hide');
			submitInBody($('#idFormConfirmar'));
		});
		$('#idModalAccion').modal('hide');
	}

	function cancelar(){
		$('#idModalAccion').on('hidden.bs.modal', function () {
			$('#idModalAccion').modal('hide');
			loadInBody('producto?estado=A&sortFieldName=descripcion&sortOrder=asc');
		});
		$('#idModalAccion').modal('hide');
		
	}
	
	function cambiarPrecio(){
		$('#idModalAccion').on('hidden.bs.modal', function () {
			$('#idModalAccion').modal('hide');
			loadInBody('prodCosto/${productoForm.id}?listar=&estado=V');
		});
		$('#idModalAccion').modal('hide');
		
	}
</script>

<div class="modal fade" id="idModalAccion" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<c:url var="urlEditar" value="/producto/editar" />
			<form:form action="${urlEditar}" method="post"
				class="form-horizontal" commandName="productoForm" id="idFormConfirmar">
			<form:hidden path="id" />
			<form:hidden path="stockPrevio"/>
			<form:hidden path="costoPrevio"/>
			<form:hidden path="fechaAlta"/>
			<form:hidden path="codigo"/>
			<form:hidden path="descripcion"/>
			<form:hidden path="costoActual"/>
			<form:hidden path="iva"/>
			<form:hidden path="stock"/>
			<form:hidden path="stockMinimo"/>
			<form:hidden path="pesoCaja"/>
			<form:hidden path="pesoEnvase"/>
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title"><fmt:message key="producto.editar.confirm.title"/></h4>
			</div>
			<div class="modal-body">
				<p>
					<fmt:message key="producto.editar.confirm">
						<fmt:param value="javascript: cambiarPrecio()"/>
					</fmt:message>
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					onclick="confirmar()"><fmt:message key="boton.aceptar"/></button>
				<button type="button" class="btn btn-default" data-dismiss="modal" onclick="cancelar()"><fmt:message key="boton.cancelar"/></button>
			</div>
			</form:form>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<%@ include file="/WEB-INF/views/include.jsp"%>

<div class="modal fade" id="myModalMessage" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<c:url var="urlBorrarCategoria" value="/categoria/borrar" />
			<form:form action="${urlBorrarCategoria}" method="post"
				class="form-horizontal" commandName="categoriaForm" id="idFormCat">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title"><fmt:message key="categoria.borrar.title"/></h4>
			</div>
			<div class="modal-body">
				<p><fmt:message key="categoria.borrar.confirm"/></p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					onclick="javascript:submitInBody($('#idFormCat'))"><fmt:message key="boton.aceptar"/></button>
				<button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="boton.cancelar"/></button>
			</div>
				<form:hidden path="id" />
			</form:form>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
	    $('#idGrillaCat').DataTable(
	    );
	});
	
/* 	function confirmDelete(idCat){
		var msg = '#msg-'+idCat;
		$('#pMsg').html($(msg).val());
		$("#btBorrar").on('click', function(){
			loadInBody('categoria/{'+idCat+'}?borrar');	
		});
		$('#idModalBorrar').modal('show');
	} */
			
</script>

<h3>
	<fmt:message key="categoria.alta.title" />
</h3>
<br />
<table id="idGrillaCat" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 70%">
        <thead>
            <tr>
                <th><fmt:message key="categoria.descripcion" /></th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="cat" items="${categorias}">
        	<tr>
        		<td>${cat.descripcion}</td>
        		<td colspan="2">
        			<i class="fa fa-edit" onclick="loadInBody('categoria/${cat.id}?editar')"></i>
        			<i class="fa fa-trash" onclick="confirmDelete('categoria/${cat.id}?borrar')"></i>
        			<%-- 
        			<i class="fa fa-trash" onclick="confirmDelete('${cat.id}')"></i>
			        <input type="hidden" id="msg-${cat.id}" value="<fmt:message key='categoria.borrar.confirm'><fmt:param value='${cat.descripcion}'/></fmt:message>" />
			        --%>
				</td>
        	</tr>
        </c:forEach>
        </tbody>
</table>

<div id="divVentanaGrilla">
</div>
<%--
<div class="modal fade" id="idModalBorrar" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title"><fmt:message key="categoria.borrar.title"/></h4>
			</div>
			<div class="modal-body">
				<p id="pMsg">
				</p>
			</div>
			<div class="modal-footer">
				<button id="btBorrar" type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="boton.aceptar"/></button>
				<button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="boton.cancelar"/></button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
--%>
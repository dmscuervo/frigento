<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
	    $('#idGrillaCat').DataTable();
	    if('${informar}' != null){
	    	$('#idModalMensaje').attr('style', 'visible');
	    }
	});
			
</script>

<h3>
	<fmt:message key="categoria.grilla.title" />
</h3>
<p class="form-validate">
	${msgError}
</p>
<table id="idGrillaCat" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 80%">
        <thead>
            <tr>
                <th><fmt:message key="categoria.descripcion" /></th>
                <th><fmt:message key="categoria.grilla.acciones" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="cat" items="${categorias}">
        	<tr>
        		<td>${cat.descripcion}</td>
        		<td>
        			<i class="fa fa-edit" onclick="loadInBody('categoria/${cat.id}?editar')"></i>
        			<i class="fa fa-trash" onclick="confirmDelete('categoria/${cat.id}?borrar')"></i>
        			<i class="fa fa-stack-overflow" onclick="loadInBody('relProdCat/${cat.id}?listar')"></i>
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


<div class="modal fade" id="idModalMensaje" tabindex="-1" role="dialog" style="visibility: hidden;">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><fmt:message key="mensaje.title" /></h4>
      </div>
      <div class="modal-body">
        <p>${informar}</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="boton.aceptar"/></button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div>
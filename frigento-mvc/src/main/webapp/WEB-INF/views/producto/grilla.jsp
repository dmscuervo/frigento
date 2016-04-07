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
	<fmt:message key="producto.grilla.title" />
</h3>
<br />
<table id="idGrillaCat" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 70%">
        <thead>
            <tr>
                <th><fmt:message key="producto.codigo" /></th>
                <th><fmt:message key="producto.descripcion" /></th>
                <th><fmt:message key="producto.costoActual" /></th>
                <th><fmt:message key="producto.stock" /></th>
                <th><fmt:message key="producto.pesoCaja" /></th>
                <th><fmt:message key="producto.grilla.acciones" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="prod" items="${productos}">
        	<tr>
        		<td>${prod.codigo}</td>
        		<td>${prod.descripcion}</td>
        		<td>${prod.costoActual}</td>
        		<td>${prod.stock}</td>
        		<td>${prod.pesoCaja}</td>
        		<td colspan="2">
        			<i class="fa fa-edit" onclick="loadInBody('producto/${prod.id}?editar')"></i>
        			<i class="fa fa-trash" onclick="confirmDelete('producto/${prod.id}?borrar')"></i>
        			<%-- 
        			<i class="fa fa-trash" onclick="confirmDelete('${prod.id}')"></i>
			        <input type="hidden" id="msg-${prod.id}" value="<fmt:message key='producto.borrar.confirm'><fmt:param value='${prod.descripcion}'/></fmt:message>" />
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
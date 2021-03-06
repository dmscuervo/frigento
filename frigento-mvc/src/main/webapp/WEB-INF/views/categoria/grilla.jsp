<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaCat')){
		    $('#idGrillaCat').DataTable({
	    		"columnDefs": [
		                       { "orderable": false, "targets": -1 }
		                     ]
		    });
		}
	    if('${informar}' != null){
	    	$('#idModalMensaje').attr('style', 'visible');
	    }
	});
			
</script>

<div style="float: left; min-width: 300px">
<h3>
	<fmt:message key="categoria.grilla.title" />
</h3>
<p class="form-validate">
	${msgError}
</p>
<table id="idGrillaCat" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 80%">
        <thead>
            <tr>
                <th style="white-space: nowrap;"><fmt:message key="categoria.descripcion" /></th>
                <th style="white-space: nowrap;"><fmt:message key="categoria.cant.productos" /></th>
                <th style="white-space: nowrap;"><fmt:message key="categoria.grilla.acciones" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="cat" items="${categorias}">
        	<tr>
        		<td style="white-space: nowrap;">${cat.descripcion}</td>
        		<td style="white-space: nowrap;">
        			<c:choose>
        				<c:when test="${empty mapaCant[cat.id]}">0</c:when>
        				<c:otherwise>${mapaCant[cat.id]}</c:otherwise>
        			</c:choose>
        		</td>
        		<td style="white-space: nowrap;">
        			<i class="fa fa-edit" onclick="loadInBody('categoria/${cat.id}?editar')"></i>
        			&nbsp;&nbsp;<i class="fa fa-trash" onclick="confirmarAccion('categoria/${cat.id}?borrar')"></i>
        			&nbsp;&nbsp;<i class="fa fa-stack-overflow" onclick="loadInBody('relProdCat/${cat.id}?listar&estado=V')"></i>
				</td>
        	</tr>
        </c:forEach>
        </tbody>
</table>
</div>

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

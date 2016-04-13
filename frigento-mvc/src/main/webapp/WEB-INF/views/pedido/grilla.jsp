<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaPed')){
	    	$('#idGrillaPed').DataTable({
	    		columnDefs: [
		                       { orderable: false, targets: -1 }
		                     ]
	    	});
		}
	    
	    if('${informar}' != null){
	    	$('#idModalMensaje').attr('style', 'visible');
	    }
	});
			
</script>

<h3>
	<fmt:message key="pedido.grilla.title" />
</h3>
<p class="form-validate">
	${msgError}
</p>
<table id="idGrillaPed" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 80%">
        <thead>
            <tr>
                <th><fmt:message key="pedido.id" /></th>
                <th><fmt:message key="pedido.costo" /></th>
                <th><fmt:message key="pedido.estado" /></th>
                <th><fmt:message key="pedido.fecha.entregar" /></th>
                <th><fmt:message key="pedido.grilla.acciones" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="ped" items="${pedidos}">
        	<tr>
        		<td style="white-space: nowrap;">${ped.id}</td>
        		<td style="white-space: nowrap;">${ped.costo}</td>
        		<td style="white-space: nowrap;">${ped.estado.descripcion}</td>
        		<td style="white-space: nowrap;"><fmt:formatDate value="${ped.fechaAEntregar}" pattern="dd/MM/yyyy HH:mm"/></td>
        		<td style="white-space: nowrap;">
        			<i class="fa fa-edit" onclick="loadInBody('pedido/${ped.id}/detalle')"></i>
        			<c:if test="${ped.estado.id eq 1 or ped.estado.id eq 2}">
        				<i class="fa fa-edit" onclick="loadInBody('pedido/${ped.id}?editar')"></i>
        				<i class="fa fa-trash" onclick="confirmAccion('pedido/${ped.id}?anular')"></i>
        				<i class="fa fa-trash" onclick="loadInBody('pedido/${ped.id}?cumplir')"></i>
        			</c:if>
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
<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaPed')){
	    	$('#idGrillaPed').DataTable({
	    		order: [[ 0, "desc" ]],
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

<div style="width: 80%; float: left; min-width: 300px">
<h3>
	<fmt:message key="pedido.grilla.title" />
</h3>
${msgError}
<table id="idGrillaPed" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
        <thead>
            <tr>
                <th style="white-space: nowrap;"><fmt:message key="pedido.id" /></th>
                <th style="white-space: nowrap;"><fmt:message key="pedido.fecha" /></th>
                <th style="white-space: nowrap;"><fmt:message key="pedido.costo" /></th>
                <th style="white-space: nowrap;"><fmt:message key="pedido.estado" /></th>
                <th style="white-space: nowrap;"><fmt:message key="pedido.fecha.entregar" /></th>
                <th style="white-space: nowrap;"><fmt:message key="pedido.grilla.acciones" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="ped" items="${pedidos}">
        	<tr>
        		<td style="white-space: nowrap;">${ped.id}</td>
        		<td style="white-space: nowrap;"><fmt:formatDate value="${ped.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
        		<td style="white-space: nowrap;">${ped.costo}</td>
        		<td style="white-space: nowrap;">${ped.estado.descripcion}</td>
        		<td style="white-space: nowrap;"><fmt:formatDate value="${ped.fechaPagado}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
        		<td style="white-space: nowrap;">
        			<i class="fa fa-list-ol" onclick="loadInBody('pedido/${ped.id}?detalle')"></i>
        			<c:if test="${ped.estado.id eq 1 or ped.estado.id eq 2}">
        				&nbsp;&nbsp;<i class="fa fa-edit" onclick="loadInBody('pedido/${ped.id}?editar')"></i>
        				&nbsp;&nbsp;<i class="fa fa-check" onclick="loadInBody('pedido/${ped.id}?cumplir')"></i>
        				&nbsp;&nbsp;<i class="fa fa-times" onclick="confirmarAccion('pedido/${ped.id}?anular')"></i>
        			</c:if>
        			<c:if test="${ped.estado.id eq 3 and empty ped.fechaPagado}">
        				&nbsp;&nbsp;<i class="fa fa-usd" onclick="confirmarAccion('pedido/${ped.id}?pagar')"></i>
        			</c:if>
        			<c:if test="${ped.estado.id gt 1}">
        				&nbsp;&nbsp;<a href="pedido/${ped.id}?descargar"><i class="fa fa-arrow-circle-down" ></i></a>
        			</c:if>
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
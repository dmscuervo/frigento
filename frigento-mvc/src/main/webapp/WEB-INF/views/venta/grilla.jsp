<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaVta')){
	    	$('#idGrillaVta').DataTable({
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
	<fmt:message key="venta.grilla.title" />
</h3>
${msgError}
<table id="idGrillaVta" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
        <thead>
            <tr>
                <th style="white-space: nowrap;"><fmt:message key="venta.id" /></th>
                <th style="white-space: nowrap;"><fmt:message key="venta.fecha" /></th>
                <th style="white-space: nowrap;"><fmt:message key="venta.importe" /></th>
                <th style="white-space: nowrap;"><fmt:message key="venta.estado" /></th>
                <th style="white-space: nowrap;"><fmt:message key="venta.fecha.entregar" /></th>
                <th style="white-space: nowrap;"><fmt:message key="venta.grilla.acciones" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="vta" items="${ventas}">
        	<tr>
        		<td style="white-space: nowrap;">${vta.id}</td>
        		<td style="white-space: nowrap;"><fmt:formatDate value="${vta.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
        		<td style="white-space: nowrap;">${vta.importe}</td>
        		<td style="white-space: nowrap;">${vta.estado.descripcion}</td>
        		<td style="white-space: nowrap;"><fmt:formatDate value="${vta.fechaAEntregar}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
        		<td style="white-space: nowrap;">
        			<i class="fa fa-list-ol" onclick="loadInBody('venta/${vta.id}?detalle')"></i>
        			<c:if test="${vta.estado.id eq 1 or vta.estado.id eq 2}">
        				&nbsp;&nbsp;<i class="fa fa-edit" onclick="loadInBody('venta/${vta.id}?editar')"></i>
        				&nbsp;&nbsp;<i class="fa fa-check" onclick="loadInBody('venta/${vta.id}?cumplir')"></i>
        				&nbsp;&nbsp;<i class="fa fa-times" onclick="confirmarAccion('venta/${vta.id}?anular')"></i>
        			</c:if>
        			<c:if test="${vta.estado.id gt 1}">
        				&nbsp;&nbsp;<a href="venta/${vta.id}?descargar"><i class="fa fa-arrow-circle-down" ></i></a>
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
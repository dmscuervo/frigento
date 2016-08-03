<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaPromo')){
		    $('#idGrillaPromo').DataTable({
		    	scrollX: 		true,
   		        scrollCollapse: true,
	    		"columnDefs": [
		                       { "orderable": false, "targets": -1 }
		                     ]
		    });
		}
	    if('${informar}' != null){
	    	$('#idModalMensaje').attr('style', 'visible');
	    }
	    
	    $('#idSelEstado').on('change', function(){
	    	var estado = $("#idSelEstado").val();
	    	loadInBody('promocion?vigente='+estado+'&sortFieldName=fechaDesde&sortOrder=desc');
	    });
	    
	    if('${estadoSel}'==''){
	    	$('#idSelEstado option[value="V"]').prop('selected', false);
	    	$('#idSelEstado option[value="NV"]').prop('selected', false);
	    	$('#idSelEstado option[value=""]').prop('selected', true);	
	    }else if('${estadoSel}'=='true'){
	    	$('#idSelEstado option[value="V"]').prop('selected', true);
	    	$('#idSelEstado option[value="NV"]').prop('selected', false);
	    	$('#idSelEstado option[value=""]').prop('selected', false);
	    }else {
	    	$('#idSelEstado option[value="V"]').prop('selected', false);
	    	$('#idSelEstado option[value="NV"]').prop('selected', true);
	    	$('#idSelEstado option[value=""]').prop('selected', false);
	    }
	});
			
</script>

<div style="width: 100%; float: left; min-width: 300px">
<h3>
	<fmt:message key="promocion.grilla.title" />
</h3>
<div class='row'>
	<div class='col-sm-4'>
     	<div class="form-group">
			<label class="form-validate">${msgError}</label>
		</div>
	</div>
	<div class='col-sm-4'>
     	<div class="form-group">
			<label class="col-sm-6 control-label" for="idCod" style="white-space: nowrap">
				<fmt:message key="relProdCat.estado" />
			</label>
     		<select id="idSelEstado">
				<option value="true" ><fmt:message key="estado.rel.vigente" /></option>
				<option value="false" ><fmt:message key="estado.rel.no.vigente" /></option>
				<option value="" ><fmt:message key="combos.todos" /></option>
        	</select>
		</div>
	</div>
</div>
<table id="idGrillaPromo" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
        <thead>
            <tr>
                <th style="white-space: nowrap;"><fmt:message key="promocion.id" /></th>
                <th style="white-space: nowrap;"><fmt:message key="promocion.producto" /></th>
                <th style="white-space: nowrap;"><fmt:message key="promocion.descuento" /></th>
                <th style="white-space: nowrap;"><fmt:message key="promocion.cant.minima" /></th>
                <th style="white-space: nowrap;"><fmt:message key="promocion.fecha.desde" /></th>
                <th style="white-space: nowrap;"><fmt:message key="promocion.fecha.hasta" /></th>
                <th style="white-space: nowrap;"><fmt:message key="promocion.grilla.acciones" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="promo" items="${promociones}">
        	<tr>
        		<td style="white-space: nowrap;">${promo.id}</td>
        		<td style="white-space: nowrap;">${promo.relProdCat.producto.descripcionCompuesta}</td>
        		<td style="white-space: nowrap;"><fmt:formatNumber value="${promo.descuento}" minFractionDigits="2" maxFractionDigits="2" /></td>
        		<td style="white-space: nowrap;"><fmt:formatNumber value="${promo.cantidadMinima}" maxFractionDigits="2" minFractionDigits="2" /></td>
        		<td style="white-space: nowrap;"><fmt:formatDate value="${promo.fechaDesde}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
			    <td style="white-space: nowrap;">
			    	<c:choose>
			    		<c:when test="${not empty promo.fechaHasta}"><fmt:formatDate value="${promo.fechaHasta}" pattern="dd/MM/yyyy HH:mm:ss"/></c:when>
			    		<c:otherwise><fmt:message key="estado.rel.vigente" /></c:otherwise>
			    	</c:choose>
	    		</td>
        		<td style="white-space: nowrap;">
        			<i class="fa fa-edit" onclick="loadInBody('promocion/${promo.id}?editar')"></i>&nbsp;&nbsp;
        			<c:if test="${empty promo.fechaHasta}">
        				<i class="fa fa-trash" onclick="confirmarAccion('promocion/${promo.id}?borrar')"></i>
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

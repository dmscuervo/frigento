<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
	    $('#idGrillaCat').DataTable();
	    if('${informar}' != null){
	    	$('#idModalMensaje').attr('style', 'visible');
	    }
	    $('#idSelEstado').on('change', function(){
	    	var estado = $("#idSelEstado").val();
	    	loadInBody('producto?estado='+estado+'&sortFieldName=descripcion&sortOrder=asc');
	    });
	    
	    if('${estadoSel}'=='A'){
	    	$('#idSelEstado option[value="A"]').prop('selected', true);
	    	$('#idSelEstado option[value="I"]').prop('selected', false);
	    }else if('${estadoSel}'=='I'){
	    	$('#idSelEstado option[value="A"]').prop('selected', false);
	    	$('#idSelEstado option[value="I"]').prop('selected', true);	
	    }
	    
	});
			
</script>

<h3>
	<fmt:message key="producto.grilla.title" />
</h3>
<br />
<div class='row'>
	<div class='col-sm-6' >    
		<div class="form-group" style="float: right;">
			<label class="col-sm-6 control-label" for="idCod" style="white-space: nowrap">
			<fmt:message key="producto.estado" />
			</label>
		</div>
     </div>
     <div class='col-sm-4'>
     	<div class="form-group">
     		<select id="idSelEstado">
				<option value="A" ><fmt:message key="estado.activos" /></option>
				<option value="I" ><fmt:message key="estado.inactivos" /></option>
        	</select>
		</div>
	</div>
</div>
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
        			<c:if test="${estadoSel eq 'A'}">
        				<i class="fa fa-trash" onclick="confirmDelete('producto/${prod.id}?borrar')"></i>
        			</c:if>
        			<c:if test="${estadoSel eq 'I'}">
        				<i class="fa fa-check-square-o" onclick="confirmActivar('producto/${prod.id}?activar')"></i>
        			</c:if>
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
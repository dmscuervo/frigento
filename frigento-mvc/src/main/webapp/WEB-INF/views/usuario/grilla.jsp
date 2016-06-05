<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaUsu')){
		    $('#idGrillaUsu').DataTable({
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
	    	if(estado == ''){
	    		loadInBody('usuario?sortFieldName=nombre,apellido&sortOrder=asc');
	    	}else{
		    	loadInBody('usuario?estado='+estado+'&sortFieldName=nombre,apellido&sortOrder=asc');
	    	}
	    });
	    
	    if('${estadoSel}'=='true'){
	    	$('#idSelEstado option[value=""]').prop('selected', false);
	    	$('#idSelEstado option[value="true"]').prop('selected', true);
	    	$('#idSelEstado option[value="false"]').prop('selected', false);
	    }else if('${estadoSel}'=='false'){
	    	$('#idSelEstado option[value=""]').prop('selected', false);
	    	$('#idSelEstado option[value="true"]').prop('selected', false);
	    	$('#idSelEstado option[value="false"]').prop('selected', true);	
	    }else{
	    	$('#idSelEstado option[value=""]').prop('selected', true);
	    	$('#idSelEstado option[value="true"]').prop('selected', false);
	    	$('#idSelEstado option[value="false"]').prop('selected', false);
	    }
	});
			
</script>

<div style="width: 100%; float: left; min-width: 300px">
<h3>
	<fmt:message key="usuario.grilla.title" />
</h3>
<p class="form-validate">
	${msgError}
</p>
<div class='row'>
	<div class='col-sm-6' >    
		<div class="form-group" style="float: right;">
			<label class="col-sm-6 control-label" for="idSelEstado" style="white-space: nowrap">
			<fmt:message key="usuario.estado" />
			</label>
		</div>
     </div>
     <div class='col-sm-4'>
     	<div class="form-group">
     		<select id="idSelEstado">
     			<option value="" ><fmt:message key="combos.todos" /></option>
				<option value="true" ><fmt:message key="estado.habilitados" /></option>
				<option value="false" ><fmt:message key="estado.inhabilitados" /></option>
        	</select>
		</div>
	</div>
</div>
<table id="idGrillaUsu" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
        <thead>
            <tr>
                <th style="white-space: nowrap;"><fmt:message key="usuario.nombre" /></th>
                <th style="white-space: nowrap;"><fmt:message key="usuario.apellido" /></th>
                <th style="white-space: nowrap;"><fmt:message key="usuario.username" /></th>
                <th style="white-space: nowrap;"><fmt:message key="prodCosto.categoria" /></th>
                <th style="white-space: nowrap;"><fmt:message key="usuario.telefono" /></th>
                <th style="white-space: nowrap;"><fmt:message key="usuario.celular" /></th>
                <th style="white-space: nowrap;"><fmt:message key="usuario.calle" /></th>
                <th style="white-space: nowrap;"><fmt:message key="usuario.altura" /></th>
                <th style="white-space: nowrap;"><fmt:message key="usuario.depto" /></th>
                <th style="white-space: nowrap;"><fmt:message key="usuario.grilla.acciones" /></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="usu" items="${usuarios}">
        	<tr>
        		<td style="white-space: nowrap;">${usu.nombre}</td>
        		<td style="white-space: nowrap;">${usu.apellido}</td>
        		<td style="white-space: nowrap;">${usu.username}</td>
        		<td style="white-space: nowrap;">${usu.categoriaProducto.descripcion}</td>
        		<td style="white-space: nowrap;">${usu.telefono}</td>
        		<td style="white-space: nowrap;">${usu.celular}</td>
        		<td style="white-space: nowrap;">${usu.calle}</td>
        		<td style="white-space: nowrap;">${usu.altura}</td>
        		<td style="white-space: nowrap;">${usu.depto}</td>
        		<td style="white-space: nowrap;">
        			<i class="fa fa-list-ol" onclick="loadInBody('usuario/${usu.id}?detalle')"></i>
        			<c:if test="${usu.habilitado eq 'true' }">
	        			&nbsp;&nbsp;<i class="fa fa-edit" onclick="loadInBody('usuario/${usu.id}?editar')"></i>
        				&nbsp;&nbsp;<i class="fa fa-trash" onclick="confirmarAccion('usuario/${usu.id}?borrar')"></i>
        			</c:if>
        			<c:if test="${usu.habilitado eq 'false' }">
        				&nbsp;&nbsp;<i class="fa fa-edit" onclick="loadInBody('usuario/${usu.id}?editar')"></i>
        				&nbsp;&nbsp;<i class="fa fa-check-square-o" onclick="confirmarAccion('usuario/${usu.id}?activar')"></i>
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

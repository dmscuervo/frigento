<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaRPCa')){
		    $('#idGrillaRPCa').DataTable({
		    	"paging": false,
		    	ordering: false
		    }); 
		}
		
		$('#idBtConfirmar').hide();
		$('#idBtAnular').hide();
		
		$('#idSelEstado').on('change', function(){
	    	var estado = $("#idSelEstado").val();
	    	loadInBody('relProdCat/${categoria.id}?listar=&estado='+estado);
	    });
	    
	    if('${estadoSel}'=='V'){
	    	$('#idSelEstado option[value="V"]').prop('selected', true);
	    	$('#idSelEstado option[value="NV"]').prop('selected', false);
	    	$('#idSelEstado option[value=""]').prop('selected', false);
	    }else if('${estadoSel}'=='NV'){
	    	$('#idSelEstado option[value="V"]').prop('selected', false);
	    	$('#idSelEstado option[value="NV"]').prop('selected', true);
	    	$('#idSelEstado option[value=""]').prop('selected', false);
	    }else if('${estadoSel}'==''){
	    	$('#idSelEstado option[value="V"]').prop('selected', false);
	    	$('#idSelEstado option[value="NV"]').prop('selected', false);
	    	$('#idSelEstado option[value=""]').prop('selected', true);	
	    }
	    
	    //Defino que registro se pueden editar
	    
	});
	
	$('#idGrillaRPCa tbody').on( 'click', 'tr', function () {
        if ( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        }
        else {
            table.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
    } );
			
	function generar(path){
		var url = '${pathBase}' + path;
		$('#divVentanaGrilla').load(url, function(data){
			$('#idModalAlta').modal('show');
		});
	}
	
	function eliminar(path){
		var url = '${pathBase}' + path;
		$.ajax({
            url: url,
            type: 'POST',
            success: function(result) {
            	$('#page-wrapper').html(result);
            	//Controles
            	$('#idSelEstado').attr('disabled', true);
            	$('#idBtConfirmar').show();
            	$('#idBtAnular').show();
            }
        });
	}
</script>

<h3>
	<fmt:message key="relProdCat.grilla.title" >
		<fmt:param value="${categoria.descripcion}" />
	</fmt:message>
</h3>
<p class="form-validate">
	${msgRespuesta}
</p>
<div class='row'>
	<div class='col-sm-3'>
		<div class="form-group">
		<c:if test="${not empty codProductosMap}">
			<fmt:message key="relProdCat.grilla.agregar.producto" />
			<i class="fa fa-plus-square fa-lg" onclick="generar('relProdCat?alta')"></i>
		</c:if>			
		</div>
	</div>
	<div class='col-sm-4'>
		<div class="form-group">
			<input type="button" id="idBtConfirmar" class="btn btn-default btn-primary" value='<fmt:message key="boton.confirmar" />' onclick="loadInBody('relProdCat?confirmar')">
			<input type="button" id="idBtAnular" class="btn btn-default btn-primary" value='<fmt:message key="boton.cancelar" />' onclick="loadInBody('relProdCat/${categoria.id}?listar=&estado=V')">
		</div>
	</div>
     <div class='col-sm-4'>
     	<div class="form-group">
			<label class="col-sm-6 control-label" for="idCod" style="white-space: nowrap">
				<fmt:message key="relProdCat.estado" />
			</label>
     		<select id="idSelEstado">
				<option value="V" ><fmt:message key="estado.rel.vigente" /></option>
				<option value="NV" ><fmt:message key="estado.rel.no.vigente" /></option>
				<option value="" ><fmt:message key="combos.todos" /></option>
        	</select>
		</div>
	</div>
</div>
<table id="idGrillaRPCa" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 80%">
	<thead>
        <tr>
            <th style="white-space: nowrap;"><fmt:message key="relProdCat.producto" /></th>
            <th style="white-space: nowrap;"><fmt:message key="relProdCat.incremento" /></th>
            <th style="white-space: nowrap;"><fmt:message key="relProdCat.precio" /></th>
            <th style="white-space: nowrap;"><fmt:message key="relProdCat.fechaDesde" /></th>
            <th style="white-space: nowrap;"><fmt:message key="relProdCat.fechaHasta" /></th>
            <th></th>
        </tr>
    </thead>
    <tbody id="idBodyContenido">
    	<c:forEach var="prodCat" items="${productosCategoria}" varStatus="status">
		<tr>
			<td style="white-space: nowrap;" id="idCodigo-${status.index}" >${prodCat.producto.codigo} - ${prodCat.producto.descripcion}</td>
		    <td style="white-space: nowrap;">${prodCat.incremento}</td>
		    <td style="white-space: nowrap;">${prodCat.precioCalculado}</td>
		    <td style="white-space: nowrap;"><fmt:formatDate value="${prodCat.fechaDesde}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
		    <td style="white-space: nowrap;" id="idFechaHasta-${status.index}">
		    	<c:choose>
		    		<c:when test="${not empty prodCat.fechaHasta}"><fmt:formatDate value="${prodCat.fechaHasta}" pattern="dd/MM/yyyy HH:mm:ss"/></c:when>
		    		<c:otherwise><fmt:message key="estado.rel.vigente" /></c:otherwise>
		    	</c:choose>
    		</td>
		    <td style="white-space: nowrap;" id="idAccion-${status.index}">
		    	<c:if test="${empty prodCat.id}">
		    		<i class="fa fa-pencil-square" onclick="generar('relProdCat/${status.index}?editar')" ></i>
		    	</c:if>
		    	<c:if test="${empty prodCat.fechaHasta}">
		    		<i class="fa fa-minus-square" onclick="eliminar('relProdCat/${status.index}?borrar')" ></i>
		    	</c:if>
		    	<c:if test="${prodCat.esEditable}">
		    		<i class="fa fa-check" onclick="eliminar('relProdCat/${status.index}?ponerVigente')" ></i>
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
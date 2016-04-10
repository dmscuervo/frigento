<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrilla')){
		    $('#idGrilla').DataTable({
		    	"paging": false,
		        "columnDefs": [
		                       { "orderable": false, "targets": -1 }
		                     ]
		    }); 
		}
	});
	
	$('#idGrilla tbody').on( 'click', 'tr', function () {
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
	${msgError}
</p>
<c:if test="${not empty codProductosMap}">
	<p>
	
	</p>
</c:if>
<div class='row'>
	<div class='col-sm-4'>
		<div class="form-group">
			<fmt:message key="relProdCat.grilla.agregar.producto" />
			<i class="fa fa-plus-square" onclick="generar('relProdCat?alta')"></i>
		</div>
	</div>
	<div class='col-sm-4'>
		<div class="form-group">
			<input type="button" value='<fmt:message key="boton.confirmar" />' onclick="loadInBody('relProdCat?confirmar')">
		</div>
	</div>
</div>
<table id="idGrilla" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 80%">
	<thead>
        <tr>
            <th>Producto</th>
            <th>Incremento (%)</th>
            <th>Precio</th>
            <th>F. Desde</th>
            <th></th>
        </tr>
    </thead>
    <tbody id="idBodyContenido">
    	<c:forEach var="prodCat" items="${productosCategoria}" varStatus="status">
		<tr>
			<td style="white-space: nowrap;">${prodCat.producto.codigo} - ${prodCat.producto.descripcion}</td>
		    <td style="white-space: nowrap;">${prodCat.incremento}</td>
		    <td style="white-space: nowrap;">${prodCat.precioCalculado}</td>
		    <td style="white-space: nowrap;"><fmt:formatDate value="${prodCat.fechaDesde}" pattern="dd/MM/yyyy HH:mm"/></td>
		    <td style="white-space: nowrap;">
		    	<c:if test="${empty prodCat.id}">
		    		<i class="fa fa-pencil-square" onclick="generar('relProdCat/${status.index}?editar')" ></i>
		    	</c:if>
		    	<i class="fa fa-minus-square" onclick="eliminar('relProdCat/${status.index}?borrar')" ></i>
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
<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
	    $('#idGrilla').DataTable({
	        "columnDefs": [
	                       { "orderable": false, "targets": -1 }
	                     ]
	    }); 
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
</script>

<h3>
	<fmt:message key="relProdCat.grilla.title" />
</h3>
<p class="form-validate">
	${msgError}
</p>
<p>
<i class="fa fa-plus-square" onclick="generar('relProdCat/${idCat}?alta')"></i>
</p>
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
    	<c:forEach var="prodCat" items="${productosCategoria}">
		<tr>
			<td style="white-space: nowrap;">${prodCat.producto.codigo} - ${prodCat.producto.descripcion}</td>
		    <td style="white-space: nowrap;">${prodCat.incremento}</td>
		    <td style="white-space: nowrap;">${prodCat.precioCalculado}</td>
		    <td style="white-space: nowrap;"><fmt:formatDate value="${prodCat.fechaDesde}" pattern="dd/MM/yyyy HH:mm"/></td>
		    <td style="white-space: nowrap;">
		    	<i class="fa fa-pencil-square" onclick="generar('relProdCat/${idCat}?edita')"></i>
		    	<i class="fa fa-minus-square" onclick="generar('relProdCat/${idCat}?borrar')"></i>
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
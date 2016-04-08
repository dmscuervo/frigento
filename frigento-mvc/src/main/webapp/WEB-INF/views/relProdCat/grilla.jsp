<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
	    $('#idGrilla').DataTable(); 
	});
			
	function generar(path){
		var url = '${pathBase}' + path;
		$('#divVentanaGrilla').load(url, function(data){
			$('#idModalAlta').modal('show');
		});
	}
</script>

<h3>
	<fmt:message key="categoria.grilla.title" />
</h3>
<p class="form-validate">
	${msgError}
</p>
<p>
	<input type="button" value="Nuevo fila" onclick="generar('relProdCat?alta')" />
</p>
<table id="idGrilla" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 70%">
    <thead>
        <tr>
            <th>Producto</th>
            <th>Incremento (%)</th>
            <th>Precio</th>
            <th>Desde</th>
        </tr>
    </thead>
    <tbody id="idBodyContenido">
    	<c:forEach var="prodCat" items="${productosCategoria}">
		<tr>
			<td>${prodCat.producto.codigo}</td>
		    <td>${prodCat.producto.descripcion}</td>
		    <td>${prodCat.incremento}</td>
		    <td>${prodCat.precioCalculado}</td>
		    <td></td>
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
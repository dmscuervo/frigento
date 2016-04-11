<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrilla')){
		    $('#idGrilla').DataTable({
		    	"paging": false
		    }); 
		}
		
		$('#datetimepickerPC').datetimepicker({
			maxDate: moment(),
			locale: 'es'
        });
		//Aplico restricciones
		$('#idCosto').keyup(function(){
			var value=$(this).val();
			 value=value.replace(/([^0-9.]*)/g, "");
			$(this).val(value);
		});
	});

</script>

<h3>
	<fmt:message key="prodCosto.grilla.title" >
		<fmt:param value="${producto.codigo} - ${producto.descripcion}" />
	</fmt:message>
</h3>
<p class="form-validate">
	${msgRespuesta}
</p>
<div class='row'>
	<div class='col-sm-1'>
		<div class="form-group">
			<input type="button" id="idBtConfirmar" value='<fmt:message key="boton.confirmar" />' onclick="loadInBody('prodCosto?confirmar')">
		</div>
	</div>
	<div class='col-sm-1'>
		<div class="form-group">
			<input type="button" id="idBtAnular" value='<fmt:message key="boton.cancelar" />' onclick="loadInBody('prodCosto/${producto.id}?listar=&estado=V')">
		</div>
	</div>
	<div class='col-sm-3'>
		<div class="form-group">
			<input type="text" id="idCosto" class="form-control" />
		</div>
	</div>
	<div class='col-sm-3'>
		<div class="form-group">
			<div class='input-group date' id='datetimepickerPC'>
				<input type="text" class="form-control" id="idFechaDesde" />
   				<span class="input-group-addon">
        			<span class="glyphicon glyphicon-calendar"></span>
    			</span>
			</div>
		</div>
	</div>
</div>
<table id="idGrilla" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 80%">
	<thead>
        <tr>
            <th><fmt:message key="categoria.decripcion" /></th>
            <th><fmt:message key="relProdCat.incremento" /></th>
            <th><fmt:message key="prodCosto.precio.venta" /></th>
        </tr>
    </thead>
    <tbody id="idBodyContenido">
    	<c:forEach var="prodCat" items="${productosCategoria}" varStatus="status">
		<tr>
			<td style="white-space: nowrap;">${prodCat.categoria.descripcion}</td>
		    <td style="white-space: nowrap;">${prodCat.incremento}</td>
		    <td style="white-space: nowrap;">${prodCat.producto.precioVenta}</td>
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
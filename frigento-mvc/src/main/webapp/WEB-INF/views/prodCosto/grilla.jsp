<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function() {
		if(!$.fn.DataTable.isDataTable('#idGrillaRPCo')){
		    $('#idGrillaRPCo').DataTable({
		    	"paging": false
		    }); 
		}
		
		if('${informar}' != null){
	    	$('#idModalMensaje').attr('style', 'visible');
	    }
		
		$('#datetimepickerPC').datetimepicker({
			//defaultDate: new Date(),
			ignoreReadonly: true,
			maxDate: moment(),
			locale: 'es'
        });
		//Aplico restricciones
		$('#idCosto').keyup(function(){
			var value=$(this).val();
			 value=value.replace(/([^0-9.]*)/g, "");
			$(this).val(value);
			calcularPrecio(null);
		});
		//Aplico precios calculados
		/* var count = '${fn:length(productosCategoria)}';
		for(i = 0; i < count; i++){
			var incremento = $('#idIncremento-'+i).prop('placeholder');
			var costo = '${producto.costoActual}';
			var factor = incremento/100 + 1;
			$('#idPrecioCalc-'+i).html(Math.round(costo*factor * 100) / 100);
		} */
	});
	
	function calcularPrecio(indice){
		var costo = $('#idCosto').val();
		if(costo == ''){
			costo = '${producto.costoActual}';
		}
		if(indice == null){
			var count = '${fn:length(productosCategoria)}';
			for(i = 0; i < count; i++){
				var incremento = $('#idIncremento-'+i).val();
				if(incremento == ''){
					incremento = $('#idIncremento-'+i).prop('placeholder');
				}
				var factor = incremento/100 + 1;
				$('#idPrecioCalc-'+i).val(Math.round(costo*factor * 100) / 100);
			}
		}else{
			var incremento = $('#idIncremento-'+indice).val();
			if(incremento == ''){
				incremento = $('#idIncremento-'+indice).prop('placeholder');
			}
			var factor = incremento/100 + 1;
			$('#idPrecioCalc-'+indice).val(Math.round(costo*factor * 100) / 100);
		}
	}

	function procesarPrecios(){
		if($('#idCosto').val() == ''){
			$('#idCosto').val($('#idCosto').prop('placeholder'));
		}
		var count = '${fn:length(productosCategoria)}';
		for(i = 0; i < count; i++){
			if($('#idIncremento-'+i).val() == ''){
				$('#idIncremento-'+i).val($('#idIncremento-'+i).prop('placeholder'));
			}
		}
		submitInBody($('#idForm'));
	}
</script>

<h3>
	<fmt:message key="prodCosto.grilla.title" >
		<fmt:param value="${producto.codigo} - ${producto.descripcion}" />
	</fmt:message>
</h3>
<br/>
<c:url var="urlConfirmar" value="/prodCosto/confirmar" />
<form:form action="${urlConfirmar}" method="post" class="form-horizontal" commandName="prodCostoForm" id="idForm" autocomplete="off">
<form:hidden path="prodId"/>
<div class='row' style="width: 80%">
	<div class='col-sm-1'>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="idCosto" style="white-space: nowrap;">
				<fmt:message key="producto.costoActual" />
			</label>
		</div>
	</div>
	<div class='col-sm-2'>
		<div class="form-group">
			<form:input path="costo" id="idCosto" cssClass="form-control" placeholder="${producto.costoActual}"/>
		</div>
	</div>
	<div class='col-sm-2'>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="datetimepickerPC" style="white-space: nowrap;">
				<fmt:message key="relProdCat.fechaDesde" />
			</label>
		</div>
	</div>
	<div class='col-sm-3'>
		<div class="form-group">
			<div class='input-group date' id='datetimepickerPC'>
				<form:input readonly="true" path="fechaDesde" cssClass="form-control" id="idFechaDesde" />
   				<span class="input-group-addon">
        			<span class="glyphicon glyphicon-calendar"></span>
    			</span>
			</div>
		</div>
	</div>
	<div class='col-sm' style="float: right;">
		<div class="form-group">
			<input type="button" id="idBtConfirmar" value='<fmt:message key="boton.confirmar" />' onclick="procesarPrecios()">
			<input type="button" id="idBtAnular" value='<fmt:message key="boton.cancelar" />' onclick="loadInBody('producto?estado=A&sortFieldName=descripcion&sortOrder=asc')">
		</div>
	</div>
</div>
<table id="idGrillaRPCo" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 80%">
	<thead>
        <tr>
            <th><fmt:message key="prodCosto.categoria" /></th>
            <th><fmt:message key="relProdCat.incremento" /></th>
            <th><fmt:message key="prodCosto.precio.venta" /></th>
        </tr>
    </thead>
    <tbody id="idBodyContenido">
    	<c:forEach var="prodCat" items="${productosCategoria}" varStatus="status">
		<tr>
			<td style="white-space: nowrap;">${prodCat.categoria.descripcion}</td>
		    <td style="white-space: nowrap;">
		    	<form:input path="incrementos[${status.index}]" id="idIncremento-${status.index}" cssClass="form-control" placeholder="${prodCat.incremento}" onkeyup="calcularPrecio(${status.index})"/>
		    </td>
		    <td style="white-space: nowrap;">
		    	<form:input path="precioCalculado[${status.index}]" id="idPrecioCalc-${status.index}" cssClass="form-control" placeholder="${prodCostoForm.precioCalculado[status.index]}" />
		    </td>
		</tr>
		</c:forEach>
    </tbody>
</table>
</form:form>

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
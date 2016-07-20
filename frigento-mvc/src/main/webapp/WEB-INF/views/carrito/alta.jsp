<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">

	$(document).ready(function(){
		//Aumento el ancho del modal 
		$('.modal-dialog').css('width', '70%');
		$('.modal-dialog').css('margin', '30px auto');
		
		//Solo en caso de que no sea una carga de pantalla por validacion de errores
		var tieneError = '${msgError}';
		$('#msgError').text(tieneError);
		
		$('#datetimepickerVentaFechaEntrega').datetimepicker({
			ignoreReadonly: true,
			locale: 'es'
	    });
		
		$("[id^=idCantidad-]").change(function(){
			//Tengo que hacerlo asi por estar montado en un modal. Sino trae lo que primero cargo al parsear el html
			var cantPedida = $(this).val();
			$('#'+$(this).attr('id')+' option').each(function(){
				if(cantPedida == $(this).val()){
					$(this).attr('selected', 'selected');
				}else{
					$(this).removeAttr('selected');
				}
			});
			actualizarFechaEntrega();
		});

		actualizarFechaEntrega();
	});
	
	function actualizarFechaEntrega(){
		var entregaRapida = 1; //24hs
		var entregaLenta = 3; //72hs
		var cantProd = '${carritoSize}';
		var incremento = entregaRapida;
		for(i = 0; i < cantProd; i++){
			var cantPedida;
			//Tengo que hacerlo asi por estar montado en un modal. Sino trae lo que primero cargo al parsear el html
			$('#idCantidad-'+i+' option').each(function(){
				if($(this).attr('selected') == 'selected'){
					cantPedida = $(this).val();
				}
			});
			
			var stock = $('#idStock-'+i).val();
			if(cantPedida >= stock){
				incremento = entregaLenta;
			}
		}
		var ahora = moment();
		console.log(ahora);
		console.log(incremento);
		$('#datetimepickerVentaFechaEntrega').data("DateTimePicker").minDate(ahora.add(incremento, 'days'));
		$('#datetimepickerVentaFechaEntrega').datetimepicker('setDate', ahora);
		//$('#datetimepickerVentaFechaEntrega').datetimepicker("setDate", ahora);
		console.log(ahora);
	}
	
	function cargarForm(form){
		//Si la grilla esta filtrada por alguna busqueda la quito, sino el submit no se lleva todos los valores
		if(dataTableVentaAlta != undefined){
			dataTableVentaAlta.search('').draw();
		}
		
		$("input[placeholder]").each( function () {
			if($(this).val() == ''){
			    $(this).val( $(this).attr("placeholder") );
			}
		});
		
		submitInMain(form);
		
	}
</script>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h3 class="modal-title"><fmt:message key="online.carrito.ver.title" /></h3>
</div>
<div class="modal-body" style="height: 60%; overflow: auto" >
	<c:url var="urlAlta" value="/carrito/alta" />
	<form:form action="${urlAlta}" method="post" class="form-horizontal" commandName="ventaForm" id="idForm" autocomplete="off">
	<form:hidden path="fecha"/>
	<form:hidden path="importe"/>
	<form:hidden path="incrementoIva"/>
	<form:hidden path="version"/>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idFechaEntregar" style="white-space: nowrap;">
					<fmt:message key="venta.fecha.entregar" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
             			<div class='input-group date' id='datetimepickerVentaFechaEntrega'>
             				<form:input path="fechaAEntregar" cssClass="form-control" id="idFechaEntregar" readonly="true" />
                				<span class="input-group-addon">
                     			<span class="glyphicon glyphicon-calendar"></span>
                 			</span>
             			</div>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="fechaAEntregar" cssClass="form-validate" />
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-12'>
			<table id="idGrillaItems" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 100%">
			        <thead>
			            <tr>
			            	<th style="white-space: nowrap;">&nbsp;</th>
			                <th style="white-space: nowrap;"><fmt:message key="venta.item.cantidad.kg" /></th>
			                <th style="white-space: nowrap;"><fmt:message key="venta.item.producto" /></th>
			                <th style="white-space: nowrap;"><fmt:message key="online.carrito.item.fechaEntrega" /></th>
			            </tr>
			        </thead>
			        <tbody id="contenidoCarrito">
			        	<jsp:include page="grilla.jsp" />
			        </tbody>
			</table>
		</div>
	</div>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idObservacion" style="white-space: nowrap;">
					<fmt:message key="online.carrito.observacion" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
             			<form:textarea path="observacion" cssClass="form-control" id="idObservacion" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="observacion" cssClass="form-validate" />
			</div>
        </div>
    </div>
	<div class='row'>
		<div class='col-sm-12'>
			<form:errors path="*" cssClass="form-validate" />
		</div>
	</div>
	</form:form>
</div>
<div class="modal-footer" style="text-align: right;" id="contentFooter">
	<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.aceptar"/>'
						onclick="javascript:cargarForm($('#idForm'))">
	<button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="boton.cancelar"/></button>
</div>
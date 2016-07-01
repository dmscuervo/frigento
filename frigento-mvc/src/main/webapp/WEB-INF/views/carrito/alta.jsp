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
		
		if(!$.fn.DataTable.isDataTable('#idGrillaItems')){
			dataTableVentaAlta = $('#idGrillaItems').DataTable({
		    	scrollY:        300,
		    	scrollX: 		false,
		        scrollCollapse: true,
		        paging: false,
		    	order: [[ 1, "asc" ]],
		    	searching: false,
		    	columnDefs: [
		                       { "orderable": false, "targets": 0 }
		                     ]
		    }); 
		}
		
		$('#datetimepickerVentaFechaEntrega').datetimepicker({
			ignoreReadonly: true,
			locale: 'es'
	    });
		
		//Aplico restricciones
		$("[id^=idCantidad-]").keyup(function(){
			var value=$(this).val();
			 value=value.replace(/([^0-9.]*)/g, "");
			$(this).val(value);
		});
		
		//Ajusto los anchos de la tabla
		dataTableVentaAlta.columns.adjust().draw();
	});
	
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
			                <th style="white-space: nowrap;"><fmt:message key="venta.item.cantidad.kg" /></th>
			                <th style="white-space: nowrap;"><fmt:message key="venta.item.producto" /></th>
			            </tr>
			        </thead>
			        <tbody>
			        <c:forEach var="item" items="${ventaForm.items}" varStatus="status">
			        	<tr>
			        		<td style="white-space: nowrap;">
			                	<form:select path="items[${status.index}].cantidad" cssClass="form-control" id="idCantidad-${status.index}" >
			                		<option value="-1" >0&nbsp;<fmt:message key="venta.cantidad.kg" /></option>
			                		<c:forEach begin="1" end="50" step="1" var="canti">
			                			<c:if test="${item.cantidad eq (canti/2)}">
				                			<option value="${canti/2}" selected="selected">${canti/2}&nbsp;<fmt:message key="venta.cantidad.kg" /></option>
			                			</c:if>
			                			<c:if test="${item.cantidad ne (canti/2)}">
			                				<option value="${canti/2}">${canti/2}&nbsp;<fmt:message key="venta.cantidad.kg" /></option>
			                			</c:if>
			                		</c:forEach>
				        		</form:select>
			        			<form:hidden path="items[${status.index}].producto.id"/>
			        			<form:hidden path="items[${status.index}].producto.codigo"/>
			        			<form:hidden path="items[${status.index}].producto.descripcion"/>
			        			<form:hidden path="items[${status.index}].producto.iva"/>
			        			<form:hidden path="items[${status.index}].importeVenta"/>
								<form:hidden path="items[${status.index}].relProductoCategoriaId"/>
								<c:if test="${ not empty item.promocion }">
									<form:hidden path="items[${status.index}].promocion.id"/>
									<form:hidden path="items[${status.index}].promocion.cantidadMinima"/>
									<form:hidden path="items[${status.index}].promocion.fechaDesde"/>
								</c:if>
			        		</td>
			        		<td style="white-space: nowrap;">
			        			<c:if test="${ not empty item.promocion }">
			        				<fmt:message key="venta.promocion">
			        					<fmt:param value="${item.promocion.cantidadMinima}" />
			        				</fmt:message>
			        			</c:if>
			        			${item.producto.codigo} - ${item.producto.descripcion}
			        		</td>
			        	</tr>
			        </c:forEach>
			        </tbody>
			</table>
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
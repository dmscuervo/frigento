<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">

	var dataTablePedidoAlta;
	
	$(document).ready(function(){
		
		visualizarEnvioMail();
		//Si la pantalla se carga con el pedido en estado confirmado, no dejo el tilde de enviar correo por default
		//Solo en caso de que no sea una carga de pantalla por validacion de errores
		var tieneError = '${msgError}';
		if($('#idEstado').val() == 2 && tieneError == ''){
			$('#idEnvioMail').prop( "checked", false );
		}
		
		if(!$.fn.DataTable.isDataTable('#idGrillaItems')){
			dataTablePedidoAlta = $('#idGrillaItems').DataTable({
		    	scrollY:        200,
		    	scrollX: 		false,
		        scrollCollapse: true,
		        paging: false,
		    	order: [[ 2, "asc" ]],
		    	columnDefs: [
		                       { "orderable": false, "targets": 0 }
		                     ]
		    }); 
		}
		
		$('#datetimepickerPedidoFecha').datetimepicker({
			ignoreReadonly: true,
			maxDate: moment(),
			locale: 'es'
	    });
		
		$('#datetimepickerPedidoFechaEntrega').datetimepicker({
			ignoreReadonly: true,
			locale: 'es'
	    });
		
		//Aplico restricciones
		$("[id^=idCantidad-]").keyup(function(){
			var value=$(this).val();
			 value=value.replace(/([^0-9.]*)/g, "");
			$(this).val(value);
		});
		
		/* $('#idCod').on('change', function(){
			calcularPrecio();
		});
		
		$('#idIncremento').on('keyup', function(){
			calcularPrecio();
		}); */
		
	});
	
	function visualizarEnvioMail(){
		if($('#idEstado').val() == 2){
			$('#idConfirmar').slideDown( "slow" );
		}else{
			$('#idConfirmar').hide();
		}
	}
	
	function generarPedido(form){
		//Si la grilla esta filtrada por alguna busqueda la quito, sino el submit no se lleva todos los valores
		if(dataTablePedidoAlta != undefined){
			dataTablePedidoAlta.search('').draw();
		}
		submitInBody(form);
	}
	
</script>

<div style="width: 80%; float: left; min-width: 300px">
	<h3>
		<fmt:message key="pedido.alta.title" />
	</h3>
	<p class="form-validate">
		${msgError}
	</p>
	<c:url var="urlAlta" value="/pedido/alta" />
	<form:form action="${urlAlta}" method="post" class="form-horizontal" commandName="pedidoForm" id="idForm" autocomplete="off">
	<form:hidden path="costo"/>
	<form:hidden path="version"/>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idFecha" style="white-space: nowrap;">
					<fmt:message key="pedido.fecha" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
       			<div class='input-group date' id='datetimepickerPedidoFecha'>
       				<form:input path="fecha" cssClass="form-control" id="idFecha" readonly="true"/>
          				<span class="input-group-addon">
               			<span class="glyphicon glyphicon-calendar"></span>
           			</span>
       			</div>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="fecha" cssClass="form-validate" />
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idEstado" style="white-space: nowrap;">
					<fmt:message key="pedido.estado" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
       			<form:select path="estado.id" cssClass="form-control" id="idEstado" onchange="visualizarEnvioMail()">
       				<form:options items="${estadoList}" itemValue="id" itemLabel="descripcion" />
        		</form:select>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="estado.id" cssClass="form-validate" />
			</div>
        </div>
    </div>
    <div class='row' id="idConfirmar">
    	<div class='col-sm-4'>&nbsp;</div>
        <div class='col-sm-8'>    
			<div class="form-group" >
				<form:checkbox path="envioMail" value="true" id="idEnvioMail"/><fmt:message key="pedido.enviar.mail" />
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idFechaEntregar" style="white-space: nowrap;">
					<fmt:message key="pedido.fecha.entregar" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
             			<div class='input-group date' id='datetimepickerPedidoFechaEntrega'>
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
    <div class='row' style="">
        <div class='col-sm-12'>
			<table id="idGrillaItems" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 70%">
			        <thead>
			            <tr>
			                <th style="white-space: nowrap;"><fmt:message key="pedido.item.cantidad.caja" /></th>
			                <th style="white-space: nowrap;"><fmt:message key="producto.pesoCaja" /></th>
			                <th style="white-space: nowrap;"><fmt:message key="pedido.item.producto" /></th>
			            </tr>
			        </thead>
			        <tbody>
			        <c:forEach var="item" items="${pedidoForm.items}" varStatus="status">
			        	<tr>
			        		<td style="white-space: nowrap;">
			        			<form:input path="items[${status.index}].cantidad" cssClass="form-control" id="idCantidad-${status.index}" placeholder="${item.cantidad}" />
			        			<form:hidden path="items[${status.index}].producto.id"/>
			        			<form:hidden path="items[${status.index}].producto.codigo"/>
			        			<form:hidden path="items[${status.index}].producto.descripcion"/>
			        		</td>
			        		<td style="white-space: nowrap;">${item.producto.pesoCaja}</td>
			        		<td style="white-space: nowrap;">${item.producto.codigo} - ${item.producto.descripcion}</td>
			        	</tr>
			        </c:forEach>
			        </tbody>
			</table>
		</div>
	</div>
	<br/>
	<div class='row'>
		<div class='col-sm-4'>&nbsp;</div>
        <div class='col-sm-8'> 
			<div class="form-group">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.aceptar"/>'
						onclick="javascript:generarPedido($('#idForm'))">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.cancelar"/>'
						onclick="javascript:loadInBody('pedido?sortFieldName=id&sortOrder=desc')">
			</div>
        </div>
    </div>
	</form:form>
</div>
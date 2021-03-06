<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	
	var dataTablePedidoCumplir;
	
	$(document).ready(function(){
		
		if(!$.fn.DataTable.isDataTable('#idGrillaCumplir')){
			dataTablePedidoCumplir = $('#idGrillaCumplir').DataTable({
		    	scrollY:        200,
		    	scrollX: 		false,
		        scrollCollapse: true,
		        paging: false,
		    	order: [[ 1, "asc" ]],
		    	columnDefs: [
		                       { "orderable": false, "targets": 0 }
		                     ]
		    }); 
		}
		
		$('#datetimepickerPedidoFechaEntregado').datetimepicker({
			ignoreReadonly: true,
			defaultDate: ${pedidoForm.fecha.time},
			maxDate: moment(),
			locale: 'es'
	    });
		
		//Aplico restricciones
		$("[id^=idCantidad-]").keyup(function(){
			var value=$(this).val();
			 value=value.replace(/([^0-9.]*)/g, "");
			$(this).val(value);
		});
		
		$("[id^=idCostoCumplir-]").keyup(function(){
			var value=$(this).val();
			 value=value.replace(/([^0-9.]*)/g, "");
			$(this).val(value);
		});
		
	});
	
	function cumplirPedido(form){
		//Si la grilla esta filtrada por alguna busqueda la quito, sino el submit no se lleva todos los valores
		if(dataTablePedidoCumplir != undefined){
			dataTablePedidoCumplir.search('').draw();
		}
		submitInBody(form);
	}
	
</script>

<div style="width: 80%; float: left; min-width: 300px">
	<h3>
		<fmt:message key="pedido.editar.title" />&nbsp;<fmt:message key="pedido.id" />:&nbsp;${pedidoForm.id}
	</h3>
	<p class="form-validate">
		${msgError}
	</p>
	<c:url var="urlCumplir" value="/pedido/cumplir" />
	<form:form action="${urlCumplir}" method="post" class="form-horizontal" commandName="pedidoForm" id="idForm">
	<form:hidden path="costo"/>
	<form:hidden path="version"/>
	<form:hidden path="id"/>
	<form:hidden path="fecha"/>
	<form:hidden path="fechaAEntregar"/>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idFechaEntrega" style="white-space: nowrap;">
					<fmt:message key="pedido.fecha.entregado" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
           			<div class='input-group date' id='datetimepickerPedidoFechaEntregado'>
           				<form:input path="fechaEntregado" cssClass="form-control" id="idFechaEntrega" readonly="true" />
              				<span class="input-group-addon">
                   			<span class="glyphicon glyphicon-calendar"></span>
               			</span>
           			</div>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="fechaEntregado" cssClass="form-validate" />
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
       			<form:select path="estado.id" cssClass="form-control" id="idEstado">
       				<form:options items="${estadoList}" itemValue="id" itemLabel="descripcion"/>
        		</form:select>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="estado.id" cssClass="form-validate" />
			</div>
        </div>
    </div>
    <div class='row'>
    	<div class='col-sm-4'>&nbsp;</div>
        <div class='col-sm-8'>    
			<div class="form-group" >
				<form:checkbox path="envioMail" value="true" id="idEnvioMail"/><fmt:message key="pedido.enviar.mail" />
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-12'>
			<table id="idGrillaCumplir" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 70%">
			        <thead>
			            <tr>
			                <th style="white-space: nowrap;"><fmt:message key="pedido.item.cantidad.caja" /></th>
			                <th style="white-space: nowrap;"><fmt:message key="pedido.item.producto" /></th>
			                <th style="white-space: nowrap;"><fmt:message key="pedido.item.pu.peso" /></th>
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
			        		<td style="white-space: nowrap;">${item.producto.codigo} - ${item.producto.descripcion}</td>
			        		<td style="white-space: nowrap;">
			        			<form:input path="items[${status.index}].costoCumplir" cssClass="form-control" id="idCostoCumplir-${status.index}" placeholder="${item.costoCumplir}" />
			        		</td>
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
						value='<fmt:message key="boton.cumplir.pedido"/>'
						onclick="javascript:cumplirPedido($('#idForm'))">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.cancelar"/>'
						onclick="javascript:loadInBody('pedido?sortFieldName=id&sortOrder=desc')">
			</div>
        </div>
    </div>
	</form:form>
</div>
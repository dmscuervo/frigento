<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	
	$(document).ready(function(){
		
		if(!$.fn.DataTable.isDataTable('#idGrillaItems')){
		    $('#idGrillaItems').DataTable({
		    	scrollY:        200,
		        scrollCollapse: true,
		        paging: false,
		    	order: [[ 1, "asc" ]],
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
			maxDate: moment(),
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
	
</script>

<div style="width: 80%; float: left; min-width: 300px">
	<h3>
		<fmt:message key="pedido.alta.title" />
	</h3>
	<br />
	<c:url var="urlAlta" value="/pedido/alta" />
	<form:form action="${urlAlta}" method="post" class="form-horizontal" commandName="pedidoForm" id="idForm">
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
       			<form:select path="estadoId" cssClass="form-control" id="idEstado" >
       				<form:options items="${estadoList}" itemValue="id" itemLabel="descripcion" />
        		</form:select>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="estadoId" cssClass="form-validate" />
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
             			<div class='input-group date' id='datetimepickerPedidoFecha'>
             				<form:input path="fechaEntregar" cssClass="form-control" id="idFechaEntregar" readonly="true" />
                				<span class="input-group-addon">
                     			<span class="glyphicon glyphicon-calendar"></span>
                 			</span>
             			</div>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="fechaEntregar" cssClass="form-validate" />
			</div>
        </div>
    </div>
    <div class='row' style="">
        <div class='col-sm-12'>
			<table id="idGrillaItems" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 70%">
			        <thead>
			            <tr>
			                <th><fmt:message key="pedido.item.cantidad.caja" /></th>
			                <th><fmt:message key="pedido.item.producto" /></th>
			            </tr>
			        </thead>
			        <tbody>
			        <c:forEach var="item" items="${pedidoForm.items}" varStatus="status">
			        	<tr>
			        		<td>
			        			<form:input path="items[${status.index}].cantidad" cssClass="form-control" id="idCantidad-${status.index}" placeholder="${item.cantidad}" /><form:hidden path="items[${status.index}].productoCosto.id"/><form:hidden path="items[${status.index}].productoCosto.costo"/>
			        		</td>
			        		<td>${item.productoCosto.producto.codigo} - ${item.productoCosto.producto.descripcion}</td>
			        	</tr>
			        </c:forEach>
			        </tbody>
			</table>
		</div>
	</div>
	<div class='row'>
        <div class='col-sm-12'> 
			<div class="form-group">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.generar"/>'
						onclick="javascript:submitInBody($('#idForm'))">
			</div>
        </div>
    </div>
	</form:form>
</div>
<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	
	$(document).ready(function(){
		
		if(!$.fn.DataTable.isDataTable('#idGrillaItems')){
		    $('#idGrillaItems').DataTable({
		    	"paging": false
		    }); 
		}
		
		$('#datetimepickerPedidoFecha').datetimepicker({
			maxDate: moment(),
			locale: 'es'
	    });
		
		$('#datetimepickerPedidoFechaEntrega').datetimepicker({
			maxDate: moment(),
			locale: 'es'
	    });
		
		//Aplico restricciones
		/* $('#idIncremento').keyup(function(){
			var value=$(this).val();
			 value=value.replace(/([^0-9.]*)/g, "");
			$(this).val(value);
		}); */
		
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
       				<form:input path="fecha" cssClass="form-control" id="idFecha" />
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
       			<form:select path="estadoId" items="${estadoList}" cssClass="form-control" id="idEstado">
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
             				<form:input path="fechaEntregar" cssClass="form-control" id="idFechaEntregar" />
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
			                <th><fmt:message key="pedido.item.cantidad.kilo" /></th>
			                <th><fmt:message key="pedido.item.producto" /></th>
			            </tr>
			        </thead>
			        <tbody>
			        <c:forEach var="item" items="${pedidoForm.items}" varStatus="status">
			        	<tr>
			        		<td><form:input path="items[${status.index}].cantidad" cssClass="form-control" id="idCantidad" placeholder="${item.cantidad}" /></td>
			        		<td>${item.producto}</td>
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
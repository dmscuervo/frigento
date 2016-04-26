<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!--PRE-ALTA NO QUITAR. ESTA ETIQUETA PERMITE SABER QUE EL CONTENIDO SE CARGA EN UN DIV INTERNO-->
<script type="text/javascript">
	
	$(document).ready(function(){
		
		visualizarEnvioMail();
		//Si la pantalla se carga con la venta en estado confirmado, no dejo el tilde de enviar correo por default
		//Solo en caso de que no sea una carga de pantalla por validacion de errores
		var tieneError = '${msgError}';
		$('#msgError').text(tieneError);
		
		if($('#idEstado').val() == 2 && tieneError == ''){
			if('${not empty ventaForm.usuario.email}'){
				$('#idEnvioMail').prop( "checked", false );
			}
		}
		
		if(!$.fn.DataTable.isDataTable('#idGrillaItems')){
		    $('#idGrillaItems').DataTable({
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
		//Si el usuario no dispone de email no permito el envio de correo
		
	});
	
	function visualizarEnvioMail(){
		if($('#idEstado').val() == 2){
			if('${empty ventaForm.usuario.email}'){
				$('#idConfirmarContenido').html('<fmt:message key="venta.usuario.sin.email" />');
			}
			$('#idConfirmar').slideDown( "slow" );
		}else{
			$('#idConfirmar').hide();
		}
	}
	
</script>

	<c:url var="urlAlta" value="/venta/alta" />
	<form:form action="${urlAlta}" method="post" class="form-horizontal" commandName="ventaForm" id="idForm" autocomplete="off">
	<form:hidden path="fecha"/>
	<form:hidden path="usuario.id"/>
	<form:hidden path="usuario.email"/>
	<form:hidden path="importe"/>
	<form:hidden path="version"/>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idEstado" style="white-space: nowrap;">
					<fmt:message key="venta.estado" />
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
			<div class="form-group" id="idConfirmarContenido">
				<form:checkbox path="envioMail" value="true" id="idEnvioMail"/><fmt:message key="venta.enviar.mail" />
			</div>
        </div>
    </div>
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
			<table id="idGrillaItems" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 70%">
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
			        			<form:input path="items[${status.index}].cantidad" cssClass="form-control" id="idCantidad-${status.index}" placeholder="0" />
			        			<form:hidden path="items[${status.index}].producto.id"/>
			        			<form:hidden path="items[${status.index}].producto.codigo"/>
			        			<form:hidden path="items[${status.index}].producto.descripcion"/>
			        			<form:hidden path="items[${status.index}].importeVenta"/>
								<form:hidden path="items[${status.index}].relProductoCategoriaId"/>
								<c:if test="${ not empty item.promocion }">
									<form:hidden path="items[${status.index}].promocion.id"/>
									<form:hidden path="items[${status.index}].promocion.cantidadMinima"/>
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
	<div class='row'>
		<div class='col-sm-4'>&nbsp;</div>
        <div class='col-sm-8'> 
			<div class="form-group">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.aceptar"/>'
						onclick="javascript:cargarForm($('#idForm'), 'POST')">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.cancelar"/>'
						onclick="javascript:loadInBody('venta?sortFieldName=id&sortOrder=desc')">
			</div>
        </div>
    </div>
	</form:form>

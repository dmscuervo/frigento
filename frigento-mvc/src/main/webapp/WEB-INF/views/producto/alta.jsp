<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	
	$(document).ready(function(){
		$('#datetimepickerProdAlta').datetimepicker({
			ignoreReadonly: true,
			maxDate: moment(),
			locale: 'es'
        });
	});
	
	function submitInBodyUploadFile(form){
		if(!bodyBlock){
			blockControl($('#wrapper'));
			bodyBlock = true;				
		}
		var formData = new FormData(document.getElementById("idForm"));
		$.ajax({
            url: form.attr('action'),
            type: 'POST',
            data: formData,
            dataType: "html",
            cache: false,
            contentType: false,
            processData: false,
            success: function(result) {
            	//Desbloqueo pantalla
            	$('#wrapper').unblock();
    			bodyBlock = false;
    			//Cargo contenido
            	$('#page-wrapper').html(result);
    			//Levanto Modal
    			$('#idModalMensaje').modal('show');
            }
        });
	}
</script>
		
<div style="width: 80%; float: left; min-width: 300px">
	<h3>
		<fmt:message key="producto.alta.title" />
	</h3>
	<br />
	<c:url var="urlAltaProducto" value="/producto/alta" />
	<form:form action="${urlAltaProducto}" method="post" class="form-horizontal" commandName="productoForm" id="idForm" enctype="multipart/form-data">
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idFechaAlta" style="white-space: nowrap;">
					<fmt:message key="producto.fecha.alta" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
        		<div class='input-group date' id='datetimepickerProdAlta'>
     				<form:input path="fechaAlta" cssClass="form-control" id="idFechaAlta" readonly="true" />
        				<span class="input-group-addon">
             			<span class="glyphicon glyphicon-calendar"></span>
         			</span>
     			</div>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="fechaAlta" cssClass="form-validate" />
			</div>
        </div>
    </div>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idCod" style="white-space: nowrap;">
					<fmt:message key="producto.codigo" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="codigo" cssClass="form-control" id="idCod" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="codigo" cssClass="form-validate" />
			</div>
        </div>
    </div>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idDesc" style="white-space: nowrap;">
					<fmt:message key="producto.descripcion" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="descripcion" cssClass="form-control" id="idDesc" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="descripcion" cssClass="form-validate" />
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idDescRemito" style="white-space: nowrap;">
					<fmt:message key="producto.descripcion.venta" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="descripcionVenta" cssClass="form-control" id="idDescRemito" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="descripcionVenta" cssClass="form-validate" />
			</div>
        </div>
    </div>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idCosto" style="white-space: nowrap;">
					<fmt:message key="producto.costoActual" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="costoActual" cssClass="form-control" id="idCosto" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="costoActual" cssClass="form-validate" />
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idIVA" style="white-space: nowrap;">
					<fmt:message key="producto.iva" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
        		<form:select path="iva" cssClass="form-control" id="idIVA" onchange="visualizarEnvioMail()">
        			<form:option value="VENTIUNO" >21%</form:option>
        			<form:option value="DIEZYMEDIO" >10.5%</form:option>
        		</form:select>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="iva" cssClass="form-validate" />
			</div>
        </div>
    </div>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idStock" style="white-space: nowrap;">
					<fmt:message key="producto.stock" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="stock" cssClass="form-control" id="idStock" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="stock" cssClass="form-validate" />
			</div>
        </div>
    </div>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idStockMinimo" style="white-space: nowrap;">
					<fmt:message key="producto.stockMinimo" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="stockMinimo" cssClass="form-control" id="idStockMinimo" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="stockMinimo" cssClass="form-validate" />
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idImagen" style="white-space: nowrap;">
					<fmt:message key="producto.imagen" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
        		<input type="file" name="idImagen" id="idImagen" >
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
        		<span class="form-validate">
        			${mensajeImagen}
				</span>
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idPesoCaja" style="white-space: nowrap;">
					<fmt:message key="producto.pesoCaja" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="pesoCaja" cssClass="form-control" id="idPesoCaja" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="pesoCaja" cssClass="form-validate" />
			</div>
        </div>
    </div>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idPesoEnvase" style="white-space: nowrap;">
					<fmt:message key="producto.pesoEnvase" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="pesoEnvase" cssClass="form-control" id="idPesoEnvase" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="pesoEnvase" cssClass="form-validate" />
			</div>
        </div>
    </div>
	<br/>
	<div class='row'>
		<div class='col-sm-4'>&nbsp;</div>
        <div class='col-sm-8'> 
			<div class="form-group">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.aceptar"/>'
						onclick="javascript:submitInBodyUploadFile($('#idForm'))">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.cancelar"/>'
						onclick="javascript:loadInBody('producto?estado=A&sortFieldName=descripcion&sortOrder=asc')">
			</div>
        </div>
    </div>
	</form:form>
</div>
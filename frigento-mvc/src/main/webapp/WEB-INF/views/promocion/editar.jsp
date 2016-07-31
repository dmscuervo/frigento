<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	
	$(document).ready(function(){
		
		calcularPrecioPromo($('#idDesc').val());
		
		//Se evita el submit del form al apretar la tecla ENTER
		$('#idForm').on('keyup keypress', function(e) {
			  var keyCode = e.keyCode || e.which;
			  if (keyCode === 13) { 
			    e.preventDefault();
			    return false;
			  }
		});
		
		//Aplico restricciones
		$("#idCantMin").keyup(function(){
			var value=$(this).val();
			 value=value.replace(/([^0-9.]*)/g, "");
			$(this).val(value);
		});
		$("#idPrecioPromo").keyup(function(){
			var value=$(this).val();
			 value=value.replace(/([^0-9.]*)/g, "");
			$(this).val(value);
			//Calculo precio
			calcularDescuento(value);
		});
	})
	
	function calcularDescuento(precioPromo){
		console.log(precioPromo);
		if(precioPromo == ''){
			precioPromo = $("#idPrecioPromo").attr('placeholder');
		}
		console.log('${idRpcPrecioVtaJson}');
		var rpcPrecioVtaJsonMap = JSON.parse('${idRpcPrecioVtaJson}');
		console.log(rpcPrecioVtaJsonMap);
		var idRPC = $('#idRelProdCat').val();
		console.log(idRPC);
		var precio = rpcPrecioVtaJsonMap[idRPC];
		console.log(precio);
		var descuento = (precio - precioPromo) * 100 / precio;
		console.log(descuento);
		$('#idDesc').val(Math.round(descuento * 100) / 100);
		
	}
	
	function calcularPrecioPromo(porcDesc){
		var rpcPrecioVtaJsonMap = JSON.parse('${idRpcPrecioVtaJson}');
		var idRPC = $('#idRelProdCat').val();
		var precio = rpcPrecioVtaJsonMap[idRPC];
		var precioPromo = precio - precio * porcDesc / 100;
		$('#idPrecioPromo').val(Math.round(precioPromo * 100) / 100);
		$("#idPrecioPromo").attr('placeholder', $('#idPrecioPromo').val());
		
	}
	
	function editar(){
		var cantMin = $("#idCantMin").val();
		if(cantMin == ''){
			$("#idCantMinError").text('<fmt:message key="NotNull.promocionForm.cantidadMinima" />');
			return;
		}
		submitInBody($('#idForm'))
	}
	
</script>

<div style="width: 100%; float: left; min-width: 300px">
	<h3>
		<fmt:message key="promocion.editar.title" />
	</h3>
	<div id="idMsgError" class="form-validate" style="display: none;">
		<label style="white-space: nowrap;"></label>
	</div>
<c:url var="urlEdit" value="/promocion/editar" />
<form:form action="${urlEdit}" method="post" class="form-horizontal" commandName="promoForm" id="idForm">
<form:hidden path="id"/>
<form:hidden path="relProdCat.id" id="idRelProdCat"/>
<form:hidden path="fechaDesde"/>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idCat" style="white-space: nowrap;">
					<fmt:message key="promocion.categoria" />
				</label>
			</div>
        </div>
        <div class='col-sm-8'>
        	<div class="form-group">
        		${promoForm.relProdCat.categoria.descripcion}
			</div>
        </div>
    </div>
    <div class='row' id="idDivProd">
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idRelProdCat" style="white-space: nowrap;">
					<fmt:message key="promocion.producto" />
				</label>
			</div>
        </div>
        <div class='col-sm-8'>
        	<div class="form-group">
        		${promoForm.relProdCat.producto.descripcionCompuesta}
			</div>
        </div>
    </div>
    <div class='row'>
    	<div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idCantMin">
					<fmt:message key="promocion.cant.minima" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="cantidadMinima" cssClass="form-control" id="idCantMin" for="idNumError" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="cantidadMinima" cssClass="form-validate" id="idNumError" />
			</div>
        </div>
    </div>
    <div class='row'>
    	<div class='col-sm-4'>    
			<div class="form-group" style="white-space: nowrap;">
				<label class="col-sm-2 control-label" for="idPrecioPromo">
					<fmt:message key="promocion.precio.por.kilo" />
				</label>
			</div>
        </div>
        <div class='col-sm-8'>
        	<div class="form-group">
				<input type="text" class="form-validate" id="idPrecioPromo" />
			</div>
        </div>
    </div>
    <div class='row'>
    	<div class='col-sm-4'>    
			<div class="form-group" style="white-space: nowrap;">
				<label class="col-sm-2 control-label" for="idDesc">
					<fmt:message key="promocion.descuento" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="descuento" cssClass="form-control" id="idDesc" for="idDescError" readonly="true"/>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="descuento" cssClass="form-validate" id="idDescError"/>
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
						onclick="javascript:editar()">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.cancelar"/>'
						onclick="javascript:loadInBody('promocion?vigente=V&sortFieldName=fechaDesde&sortOrder=desc')">
			</div>
        </div>
    </div>
</form:form>
</div>
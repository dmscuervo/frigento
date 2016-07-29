<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">

	$(document).ready(function(){
		
		var rpcPrecioVtaJsonMap = JSON.parse('${idRpcPrecioVtaJson}');
		var idRPC = $('#idRelProdCat').val();
		var precio = rpcPrecioVtaJsonMap[idRPC];
		$("#idPrecioPromo").val(precio);
		$("#idPrecioPromo").attr('placeholder', precio)
		
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
			calcularPrecioPromo(value);
		});
	})
	
	function calcularPrecioPromo(precioPromo){
		console.log(precioPromo);
		if(precioPromo == ''){
			$('#idDesc').val('');
			return
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
</script>

<c:url var="urlAlta" value="/promocion/alta" />
<form:form action="${urlAlta}" method="post" class="form-horizontal" commandName="promoForm" id="idForm">
<form:hidden path="fechaDesde"/>
    <div class='row' id="idDivProd">
        <div class='col-sm-4'>    
			<div class="form-group" style="white-space: nowrap;">
				<label class="col-sm-2 control-label" for="idRelProdCat" style="white-space: nowrap;">
					<fmt:message key="promocion.producto" />
				</label>
			</div>
        </div>
        <div class='col-sm-8'>
        	<div class="form-group">
        		<form:select path="relProdCat.id" items="${relProdCatList}" itemLabel="producto.descripcionCompuesta" itemValue="id" cssClass="form-control" id="idRelProdCat" />
			</div>
        </div>
    </div>
    <div class='row'>
    	<div class='col-sm-4'>    
			<div class="form-group" style="white-space: nowrap;">
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
						onclick="javascript:submitInBody($('#idForm'))">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.cancelar"/>'
						onclick="javascript:loadInBody('promocion?estado=A&sortFieldName=fechaDesde&sortOrder=desc')">
			</div>
        </div>
    </div>
</form:form>
<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">

	var dataTableVentaAlta;
	
	$(document).ready(function(){
		
		$('#datetimepickerVentaFecha').datetimepicker({
			ignoreReadonly: true,
			maxDate: moment(),
			locale: 'es'
	    });
		
	});
	
	function cargarForm(form, type){
		//Si la grilla esta filtrada por alguna busqueda la quito, sino el submit no se lleva todos los valores
		if(dataTableVentaAlta != undefined){
			dataTableVentaAlta.search('').draw();
		}
		
		if(!bodyBlock){
			blockControl($('#wrapper'));
			bodyBlock = true;				
		}
		
		$("input[placeholder]").each( function () {
			if($(this).val() == ''){
			    $(this).val( $(this).attr("placeholder") );
			}
		});
		
		$.ajax({
            url: form.attr('action'),
            type: type,
            data: form.serialize(),
            success: function(result) {
            	//Desbloqueo pantalla
            	$('#wrapper').unblock();
    			bodyBlock = false;
    			//Cargo contenido
    			//La pantalla de preAlta comienza con el tag definido a continuación. 
    			//Caso contrario es porque finalizo el alta y cargo una nueva pantalla.
    			if(result.trim().startsWith('<!--PRE-ALTA')){
	    			$('#divButton').slideUp();
	            	$('#contenidoAlta').html(result);
	    			$('#divPreAlta').prop('disabled', true);
    			}else{
    				//Finaliza el proceso
    				$('#page-wrapper').html(result);
        			//Levanto Modal
        			$('#idModalMensaje').modal('show');
    			}
            }
        });
	}
	
	function visualizarEnvioMail(){
		if($('#idEstado').val() == 2){
			$('#idConfirmar').slideDown( "slow" );
		}else{
			$('#idConfirmar').hide();
		}
	}
	
</script>

<div style="width: 80%; float: left; min-width: 300px">
	<h3>
		<fmt:message key="venta.alta.title" />
	</h3>
	<p class="form-validate" >
		<label id="msgError">&nbsp;</label>
	</p>
	<c:url var="urlPreAlta" value="/venta?alta" />
	<form:form action="${urlPreAlta}" method="post" class="form-horizontal" commandName="ventaForm" id="idFormPreAlta">
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idFecha" style="white-space: nowrap;">
					<fmt:message key="venta.fecha" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
       			<div class='input-group date' id='datetimepickerVentaFecha'>
       				<form:input path="fecha" cssClass="form-control" id="idFecha" readonly="true"/>
          				<span class="input-group-addon">
               			<span class="glyphicon glyphicon-calendar"></span>
           			</span>
       			</div>
			</div>
        </div>
    </div>
    <div class='row' id="divPreAlta">
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idUsu" style="white-space: nowrap;">
					<fmt:message key="venta.usuario" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
       			<form:select path="usuario.id" cssClass="form-control" id="idUsu">
       				<form:options items="${usuarioList}" itemValue="id" itemLabel="identificadoWeb" />
        		</form:select>
			</div>
        </div>
   </div>
    <div class='row' id="divButton">
    <br/>
        <div class='col-sm-4'>    
			<div class="form-group" >
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.aceptar"/>'
						onclick="javascript:cargarForm($('#idFormPreAlta'), 'GET')">
				<input type="button" class="btn btn-default btn-primary"
					value='<fmt:message key="boton.cancelar"/>'
					onclick="javascript:loadInBody('venta?estado=A&sortFieldName=id&sortOrder=desc')">
			</div>
        </div>
    </div>
	</form:form>
<div id="contenidoAlta"></div>
</div>
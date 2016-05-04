<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:url var="pathBase" value="/" />

<script type="text/javascript">

	var dataTableVentaAlta;
	
	$(document).ready(function(){
		
		$('#datetimepickerPeriodo').datetimepicker({
			ignoreReadonly: true,
			defaultDate: moment(),
			minDate: 1451699700926,
			format: 'YYYY',
			locale: 'es'
	    });
		
		$('#divAgrupamiento').hide();
		
		$('#idTipo').change(function(){
			if($(this).val() == 1){
				$('#datetimepickerPeriodo').data("DateTimePicker").format('YYYY');
				$('#divAgrupamiento').slideUp();
			}else{
				$('#datetimepickerPeriodo').data("DateTimePicker").format('MM-YYYY');
				$('#divAgrupamiento').slideDown();
			}
		});
		
		
		
	});
	
	function buscar(){
		$('#idBtBuscar').prop('disabled', true);
		
		var url = '${pathBase}' + 'consulta/ganancia/buscar/'+$('#idTipo').val()+'/'+$('#idPeriodo').val()+'/'+$('#idAgrupamiento').val();
		
		$('#contenidoGrilla').load(url, function(){
			$('#idBtBuscar').prop('disabled', false);
		});
	}
	
	function cargarColumnas(){
		//Si la grilla esta filtrada por alguna busqueda la quito, sino el submit no se lleva todos los valores
		if(dataTablePlanillaProductos != undefined){
			dataTablePlanillaProductos.search('').draw();
		}
		
		$('#idMsgError').text('');
		$('#idMsgError').hide();
		
		var codigos = '';
		$('.selected td:first-child').each(function(){
			codigos = codigos + $(this).text() + ",";
		});
		
		if(codigos == ''){
			$('#idMsgError').text('<fmt:message key="planilla.cliente.sin.producto"/>');
			$('#idMsgError').slideDown("slow");
			return;
		}
		
		codigos = codigos.substring(0, codigos.length-1);
		
		//Bloqueo contenido
		blockControl($('#wrapperContenidoGrilla'));
		
		var url = '${pathBase}' + 'planilla/cliente/'+moment($('#idFecha').val(),'DD/MM/YYYY HH:mm')+'/'+$('#idCat').val()+'/'+codigos;
		console.log(url);
		$('#contenidoGrilla').load(url, function(){
			//Desbloqueo contenido
        	$('#wrapperContenidoGrilla').unblock();
			//Ya no puedo cambiar la fecha o categoria
			$('#idCat').prop('disabled',true);
			$('.input-group-addon').remove();
		});
	}
	
	function generar(form){
		//Si la grilla esta filtrada por alguna busqueda la quito, sino el submit no se lleva todos los valores
		if(dataTablePlanillaColumnas != undefined){
			dataTablePlanillaColumnas.search('').draw();
		}
		
		$('#idMsgError').text('');
		$('#idMsgError').hide();
		
		var indices = '';
		$('.selected').find('td input:hidden').each(function(){
			indices = indices + $(this).val() + ",";
		});
		
		if(indices == ''){
			$('#idMsgError').text('<fmt:message key="planilla.cliente.sin.columna"/>');
			$('#idMsgError').slideDown("slow");
			return;
		}
		
		indices = indices.substring(0, indices.length-1);
		console.log(indices);
		
		if(!bodyBlock){
			blockControl($('#wrapper'));
			bodyBlock = true;				
		}
		
		$.ajax({
            url: form.attr('action'),
            type: 'POST',
            data: form.serialize(),
            success: function(result) {
            	//Si tengo respuesta JSON es porque contiene errores 
            	try {
            		var obj = JSON.parse(result);
            		$('#msgError').html(obj.mensajeGenerico);
	            	//Desbloqueo pantalla
	            	$('#wrapper').unblock();
	    			bodyBlock = false;
            		return;
            	}
            	catch(err) {
					var url = '${pathBase}' + 'planilla/cliente/generar/'+indices;
					window.location=url;
					//Desbloqueo pantalla
	            	$('#wrapper').unblock();
	    			bodyBlock = false;
	    			//Cargo contenido
                	//$('#page-wrapper').html(result);
            	}
            }
        });
	}

</script>

<div style="width: 80%; float: left; min-width: 300px">
	<h3>
		<fmt:message key="consulta.ganancia.title" />
	</h3>
	<p class="form-validate" >
		<label id="msgError">&nbsp;</label>
	</p>
<div id="contenidoFiltro">
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idTipo" style="white-space: nowrap;">
					<fmt:message key="consulta.ganancia.tipo" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
       			<select class="form-control" id="idTipo" >
       				<option value="1"><fmt:message key="consulta.ganancia.tipo.resumen" /></option>
       				<option value="2"><fmt:message key="consulta.ganancia.tipo.detalle" /></option>
        		</select>
			</div>
        </div>
   </div>	
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idPeriodo" style="white-space: nowrap;">
					<fmt:message key="consulta.ganancia.periodo" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
       			<div class='input-group date' id='datetimepickerPeriodo'>
       				<input type="text" class="form-control" id="idPeriodo" readonly="readonly"/>
          				<span class="input-group-addon">
               			<span class="glyphicon glyphicon-calendar"></span>
           			</span>
       			</div>
			</div>
        </div>
    </div>
    <div class='row' id="divAgrupamiento">
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idAgrupamiento" style="white-space: nowrap;">
					<fmt:message key="consulta.ganancia.agrupamiento" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
       			<select class="form-control" id="idAgrupamiento" >
       				<option value="1"><fmt:message key="consulta.ganancia.agrupamiento.producto" /></option>
       				<option value="2"><fmt:message key="consulta.ganancia.agrupamiento.venta" /></option>
       				<option value="3"><fmt:message key="consulta.ganancia.agrupamiento.ambos" /></option>
        		</select>
			</div>
        </div>
   </div>
   <div class='row'>
   		<div class='col-sm-4'>    
			<div class="form-group" >
				&nbsp;
			</div>
        </div>
        <div class='col-sm-8'>
        	<div class="form-group">
       			<input id="idBtBuscar" type="button" class="btn btn-default btn-primary" value='<fmt:message key="boton.buscar" />' onclick="buscar()">
			</div>
        </div>
   </div>
</div>
<div id="wrapperContenidoGrilla">
<div id="contenidoGrilla" style="text-align: center;">
</div>
</div>
</div>
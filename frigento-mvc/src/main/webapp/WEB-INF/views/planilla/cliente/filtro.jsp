<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:url var="pathBase" value="/" />

<script type="text/javascript">

	var dataTableVentaAlta;
	
	$(document).ready(function(){
		
		$('#datetimepickerPlanilla').datetimepicker({
			ignoreReadonly: true,
			defaultDate: moment(),
			maxDate: moment(),
			format: 'DD/MM/YYYY',
			locale: 'es'
	    });
		
		$("#datetimepickerPlanilla").on("dp.change", function (e) {
			cargarGrilla();
        });
	});
	
	function cargarGrilla(){
		//Bloqueo contenido
		blockControl($('#wrapperContenidoGrilla'));
		
		var url = '${pathBase}' + 'planilla/cliente/'+moment($('#idFecha').val(),'DD/MM/YYYY HH:mm')+'/'+$('#idCat').val();
		console.log(url);
		$('#contenidoGrilla').load(url, function(){
			//Desbloqueo contenido
        	$('#wrapperContenidoGrilla').unblock();
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
                	$('#page-wrapper').html(result);
            	}
            }
        });
	}

</script>

<div style="width: 80%; float: left; min-width: 300px">
	<h3>
		<fmt:message key="planilla.cliente.title" />
	</h3>
	<p class="form-validate" >
		<label id="msgError">&nbsp;</label>
	</p>
<div id="contenidoFiltro">	
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idFecha" style="white-space: nowrap;">
					<fmt:message key="planilla.cliente.fecha" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
       			<div class='input-group date' id='datetimepickerPlanilla'>
       				<input type="text" class="form-control" id="idFecha" readonly="readonly"/>
          				<span class="input-group-addon">
               			<span class="glyphicon glyphicon-calendar"></span>
           			</span>
       			</div>
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idCat" style="white-space: nowrap;">
					<fmt:message key="planilla.cliente.categoria" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
       			<select class="form-control" id="idCat" onchange="cargarGrilla()">
       				<c:forEach items="${categoriaList}" var="cat">
       					<option value="${cat.id}">${cat.descripcion}</option>
       				</c:forEach>
        		</select>
			</div>
        </div>
   </div>
</div>
<div id="wrapperContenidoGrilla">
<div id="contenidoGrilla" style="text-align: center;">
	<jsp:include page="grilla.jsp"></jsp:include>
</div>
</div>
</div>
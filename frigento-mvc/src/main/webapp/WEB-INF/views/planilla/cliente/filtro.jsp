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
		var codigos = '';
		$('.selected td:first-child').each(function(){
			codigos = codigos + $(this).text() + ",";
		});
		codigos = codigos.substring(0, codigos.length-1);
		console.log(codigos);
		
		//Bloqueo contenido
		blockControl($('#wrapperContenidoGrilla'));
		
		var url = '${pathBase}' + 'planilla/cliente/'+moment($('#idFecha').val(),'DD/MM/YYYY HH:mm')+'/'+$('#idCat').val()+'/'+codigos;
		console.log(url);
		$('#contenidoGrilla').load(url, function(){
			//Desbloqueo contenido
        	$('#wrapperContenidoGrilla').unblock();
			//Ya no puedo cambiar la fecha o categoria
			$('#contenidoFiltro').prop('disabled', true);
		});
	}
	
	function generar(){
		var indices = '';
		$('.selected').find('td input:hidden').each(function(){
			indices = indices + $(this).val() + ",";
		});
		indices = indices.substring(0, indices.length-1);
		console.log(indices);
		
		var url = '${pathBase}' + 'planilla/cliente/generar/'+indices;
		window.location=url;
		/*
		$.ajax({
            url: url,
            type: 'GET',
            success: function(result) {
            	console.log(result);
            	//Desbloqueo pantalla
            	$('#wrapper').unblock();
    			bodyBlock = false;
    			//Cargo contenido
            	$('#page-wrapper').html(result);
            }
        });
		*/
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
<div id="contenidoGrilla" style="text-align: center;"></div>
</div>
</div>
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
		//Caso de un retorno de ver detalle
		if('${regresoDetalle}'){
			$('#idTipo').val('${tipoBack}');
			if($('#idTipo').val() == 1){
				$('#datetimepickerPeriodo').data("DateTimePicker").format('YYYY');
				$('#divAgrupamiento').slideUp();
			}else{
				$('#datetimepickerPeriodo').data("DateTimePicker").format('MM-YYYY');
				$('#divAgrupamiento').slideDown();
			}
			$("#datetimepickerPeriodo input").val('${periodoBack}');
			$('#idAgrupamiento').val('${agrupamientoBack}');
			buscar();
		}
		
	});
	
	function buscar(){
		$('#idBtBuscar').prop('disabled', true);
		
		var url = '${pathBase}' + 'consulta/ganancia/buscar/'+$('#idTipo').val()+'/'+$('#idPeriodo').val()+'/'+$('#idAgrupamiento').val();
		
		$('#contenidoGrilla').load(url, function(){
			$('#idBtBuscar').prop('disabled', false);
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
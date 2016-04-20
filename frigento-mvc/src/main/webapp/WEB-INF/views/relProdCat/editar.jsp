<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	

	$(document).ready(function(){
		$('#datetimepicker1').datetimepicker({
			ignoreReadonly: true,
			maxDate: moment().add(-1, 'minutes'),
			locale: 'es'
        });
		//20/04/2016 16:45
		//var day = moment($("#datetimepicker1").find("input").val(), "DD/MM/YYYY HH:mm");
		//console.log(day);
		/*
		var fechaHora = $("#datetimepicker1").find("input").val().split(" ");
		console.log(fechaHora[0]);
		var day = moment(fechaHora[0], "DD/MM/YYYY");
		console.log(day);
		*/
		//day.toDate();
		
		$('#datetimepicker2').datetimepicker({
			ignoreReadonly: true,
			minDate: moment($("#datetimepicker1").find("input").val(), "DD/MM/YYYY HH:mm"),
			maxDate: moment(),
			locale: 'es'
        });
		
		$("#datetimepicker2").find("input").val('');
		//Establezco el minimo de fechaHasta con el valor de fechaDesde
		//$('#datetimepicker2').data("DateTimePicker").minDate($('#datetimepicker1').data('date'));
		//$('#datetimepicker2').datetimepicker('method', 'setStartDate', $('#datetimepicker1').data('date'));
		
		/* $("#datetimepicker2").on("dp.show", function (e) {
			console.log('asdasdasd');
        }); */
		
		$("#datetimepicker1").on("dp.change", function (e) {
            $('#datetimepicker2').data("DateTimePicker").minDate(e.date);
        });
		$("#datetimepicker2").on("dp.change", function (e) {
            $('#datetimepicker1').data("DateTimePicker").maxDate(e.date);
        });
		
		//Aplico restricciones
		$('#idIncremento').keyup(function(){
			var value=$(this).val();
			 value=value.replace(/([^0-9.]*)/g, "");
			$(this).val(value);
		});
		
		$('#idCod').on('change', function(){
			calcularPrecio();
		});
		
		$('#idIncremento').on('keyup', function(){
			calcularPrecio();
		});
		
	});

	function calcularPrecio(){
		var costosMap = JSON.parse('${codCostoJson}');
		var codProd = $('#idCod').val();
		var incremento = $('#idIncremento').val();
		if(codProd != '-1' && incremento != ''){
			var costo = costosMap[codProd];
			var factor = incremento/100 + 1;
			$('#idPrecio').val(Math.round(costo*factor * 100) / 100);
		}else{
			$('#idPrecio').val('');
		}
	}

	function modificar(){
		$.ajax({
            url: $('#idForm').attr('action'),
            type: 'POST',
            data: $('#idForm').serialize(),
            success: function(result) {
            	//Si tengo respuesta JSON es porque contiene errores 
            	try {
            		var obj = JSON.parse(result);
            		$('#errorCodigo').html(obj.codigo);
            		$('#errorIncremento').html(obj.incremento);
            		$('#errorPrecioCalculado').html(obj.precioCalculado);
            		$('#errorFechaDesde').html(obj.fechaDesde);
            		return;
            	}
            	catch(err) {
            		console.log("No JSON");
	            	//Hago que se refresque el contenido al ocultar el modal
	            	$('#idModalAlta').on('hidden.bs.modal', function () {
	            		//Cargo contenido
	                	$('#page-wrapper').html(result);
	                	//Controles
	                	$('#idSelEstado').attr('disabled', true);
	                	$('#idBtConfirmar').show();
	                	$('#idBtAnular').show();
	    			});
	    			$('#idModalAlta').modal('hide');
            	}
            	
            }
        });
	}

</script>
<div class="modal fade" id="idModalAlta" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 ><fmt:message key="relProdCat.asignar.title"/></h4>
			</div>
			<div class="modal-body">
				<c:url var="urlEditar" value="/relProdCat/editar" />
				<form:form action="${urlEditar}" method="post" class="form-horizontal" commandName="relProdCatForm" id="idForm" autocomplete="off">
				<form:hidden path="categoria.id"/>
				<form:hidden path="id"/>
				<form:hidden path="indiceLista"/>
				<div class='row'>
			        <div class='col-sm-4'>    
						<div class="form-group" >
							<label class="col-sm-2 control-label" for="idCod" style="white-space: nowrap;">
								<fmt:message key="relProdCat.producto" />
							</label>
						</div>
			        </div>
			        <div class='col-sm-4'>
			        	<div class="form-group">
			        		<form:select disabled="disabled" path="producto.codigo" items="${codProductosMap}" cssClass="form-control" id="idCod">
			        			<form:option value="-1" label='asdasdas'/>
			        		</form:select>
						</div>
			        </div>
			        <div class='col-sm-4'>
			        	<div class="form-group" >
							<label id="errorCodigo" class="form-validate" style="white-space: nowrap;"></label>
						</div>
			        </div>
			    </div>
				<div class='row'>
			        <div class='col-sm-4'>    
						<div class="form-group" >
							<label class="col-sm-2 control-label" for="idIncremento" style="white-space: nowrap;">
								<fmt:message key="relProdCat.incremento" />
							</label>
						</div>
			        </div>
			        <div class='col-sm-4'>
			        	<div class="form-group">
							<form:input path="incremento" cssClass="form-control" id="idIncremento" />
						</div>
			        </div>
			        <div class='col-sm-4'>
			        	<div class="form-group" >
							<label id="errorIncremento" class="form-validate" style="white-space: nowrap;"></label>
						</div>
			        </div>
			    </div>
			    <div class='row'>
			        <div class='col-sm-4'>    
						<div class="form-group" >
							<label class="col-sm-2 control-label" for="idPrecio" style="white-space: nowrap;">
								<fmt:message key="relProdCat.precio" />
							</label>
						</div>
			        </div>
			        <div class='col-sm-4'>
			        	<div class="form-group">
							<form:input readonly="true" path="precioCalculado" cssClass="form-control" id="idPrecio" />
						</div>
			        </div>
			        <div class='col-sm-4'>
			        	<div class="form-group" >
							<label id="errorPrecioCalculado" class="form-validate" style="white-space: nowrap;"></label>
						</div>
			        </div>
			    </div>
			    <div class='row'>
			        <div class='col-sm-4'>    
						<div class="form-group" >
							<label class="col-sm-2 control-label" for="idFechaDesde" style="white-space: nowrap;">
								<fmt:message key="relProdCat.fechaDesde" />
							</label>
						</div>
			        </div>
			        <div class='col-sm-4'>
			        	<div class="form-group">
                			<div class='input-group date' id='datetimepicker1'>
                				<form:input path="fechaDesde" cssClass="form-control" id="idFechaDesde" readonly="true"/>
                   				<span class="input-group-addon">
                        			<span class="glyphicon glyphicon-calendar"></span>
                    			</span>
                			</div>
						</div>
			        </div>
			        <div class='col-sm-4'>
			        	<div class="form-group" >
							<label id="errorFechaDesde" class="form-validate" style="white-space: nowrap;"></label>
						</div>
			        </div>
			    </div>
			    <div class='row'>
			        <div class='col-sm-4'>    
						<div class="form-group" >
							<label class="col-sm-2 control-label" for="idFechaHasta" style="white-space: nowrap;">
								<fmt:message key="relProdCat.fechaHasta" />
							</label>
						</div>
			        </div>
			        <div class='col-sm-4'>
			        	<div class="form-group">
                			<div class='input-group date' id='datetimepicker2'>
                				<form:input path="fechaHasta" cssClass="form-control" id="idFechaHasta" readonly="true"/>
                   				<span class="input-group-addon">
                        			<span class="glyphicon glyphicon-calendar"></span>
                    			</span>
                			</div>
						</div>
			        </div>
			        <div class='col-sm-4'>
			        	<div class="form-group" >
							<label id="errorFechaHasta" class="form-validate" style="white-space: nowrap;"></label>
						</div>
			        </div>
			    </div>
				</form:form>
			</div>
			<div class="modal-footer">
			<div class='row'>
		        <div class='col-sm-12'> 
					<div class="form-group">
							<label id="idMessageError" class="form-validate" ></label>
							<button type="button" class="btn btn-default" onclick="modificar()"><fmt:message key="boton.agregar"/></button>
					</div>
		        </div>
		    </div>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	

	$(document).ready(function(){
		$('#datetimepickerRPCAlta').datetimepicker({
			ignoreReadonly: true,
			maxDate: moment(),
			locale: 'es'
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
		
		$("#datetimepickerRPCAlta").on("dp.change", function (e) {
			calcularPrecio();
        });
		
	});

	function calcularPrecio(){
		var costosMap = JSON.parse('${codCostoJson}');
		var codProd = $('#idCod').val();
		var rpcFechaDesde = $("#datetimepickerRPCAlta").find("input").val();
		var incremento = $('#idIncremento').val();
		if(codProd != '-1' && incremento != ''){
			var costo = '';
			//Recorro todas las relaciones productoCosto
			$.each(costosMap[codProd], function(i,item){
				var fechaDesde = costosMap[codProd][i].fechaDesde;
				var fechaHasta = costosMap[codProd][i].fechaHasta;
				var costoFecha = costosMap[codProd][i].costo;
				var mFechaDesde = moment(fechaDesde);
				var mFechaHasta = moment(fechaHasta);
				var mRpcFechaDesde = moment(rpcFechaDesde,'DD/MM/YYYY HH:mm');
				if(mFechaDesde.isSameOrBefore(moment(mRpcFechaDesde))
						&& (fechaHasta == null || mFechaHasta.isAfter(moment(mRpcFechaDesde)))){
					costo = costoFecha;
				}
			});
			if(costo === ''){
				//No hay un costo segun la fecha elegida
				$('#idMessageError').text('No hay un costo para la fecha seleccionada');
				$('#btAgregar').prop('disabled', true);
			}else{
				//Existe un costo
				$('#idMessageError').text('');
				$('#btAgregar').prop('disabled', false);
			}

			var factor = incremento/100 + 1;
			$('#idPrecio').val(Math.round(costo*factor * 100) / 100);
			
		}else{
			$('#idPrecio').val('');
		}
	}

	function agregar(){
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
				<c:url var="urlAlta" value="/relProdCat/alta" />
				<form:form action="${urlAlta}" method="post" class="form-horizontal" commandName="relProdCatForm" id="idForm" autocomplete="off">
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
			        		<form:select path="producto.codigo" items="${codProductosMap}" cssClass="form-control" id="idCod" />
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
                			<div class='input-group date' id='datetimepickerRPCAlta'>
                				<form:input path="fechaDesde" cssClass="form-control" id="idFechaDesde" readonly="true" />
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
				</form:form>
			</div>
			<div class="modal-footer">
			<div class='row'>
		        <div class='col-sm-12'> 
					<div class="form-group">
							<label id="idMessageError" class="form-validate" ></label>
							<button type="button" id="btAgregar" class="btn btn-default" onclick="agregar()"><fmt:message key="boton.agregar"/></button>
					</div>
		        </div>
		    </div>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
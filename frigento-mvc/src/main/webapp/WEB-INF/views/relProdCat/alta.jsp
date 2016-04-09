<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	

	$(document).ready(function(){
		$('#datetimepicker1').datetimepicker({
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

	function agregar(){
		$.ajax({
            url: $('#idForm').attr('action'),
            type: 'POST',
            data: $('#idForm').serialize(),
            success: function(result) {
            	if(result == 'ERROR'){
            		$('#idMessageError').text('<fmt:message key="relProdCat.asignar.error"/>'); 
            		return;
            	}
            	//Hago que se refresque el contenido al ocultar el modal
            	$('#idModalAlta').on('hidden.bs.modal', function () {
            		//Cargo contenido
                	$('#page-wrapper').html(result);
    			});
    			$('#idModalAlta').modal('hide');
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
				<div class='row'>
			        <div class='col-sm-4'>    
						<div class="form-group" >
							<label class="col-sm-2 control-label" for="idCod" style="white-space: nowrap;">
								<fmt:message key="relProdCat.producto.codigo" />
							</label>
						</div>
			        </div>
			        <div class='col-sm-4'>
			        	<div class="form-group">
			        		<form:select path="producto.codigo" items="${codProductosMap}" cssClass="form-control" id="idCod">
			        			<form:option value="-1" label='asdasdas'/>
			        		</form:select>
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
                				<form:input path="fechaDesde" cssClass="form-control" id="idFechaDesde" />
                   				<span class="input-group-addon">
                        			<span class="glyphicon glyphicon-calendar"></span>
                    			</span>
                			</div>
						</div>
			        </div>
			    </div>
				</form:form>
			</div>
			<div class="modal-footer">
			<div class='row'>
		        <div class='col-sm-8'> 
					<div class="form-group">
							<label id="idMessageError" class="form-validate" ></label>
							<button type="button" class="btn btn-default" onclick="agregar()"><fmt:message key="boton.agregar"/></button>
					</div>
		        </div>
		    </div>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
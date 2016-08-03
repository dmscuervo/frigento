<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">

	$(document).ready(function(){
		
		$('#idCat').change(function(){
			getContenido();
		});
	});
	
	function getContenido(){
		$('#idMsgError').text('');
		$('#idMsgError').hide();
		
		if($('#idCat').val() == -1){
			$('#idDivPromo').html('');
			return;
		}
		
		var url = '${pathBase}' + 'promocion/preAltaContenido';
		$.ajax({
	        url: url,
	        type: 'GET',
	        data: {idCat: $('#idCat').val()},
	        success: function(result) {
	        	try {
            		//Si hay json es porque tengo un mensaje
            		var obj = JSON.parse(result);
            		$('#idDivPromo').html('');
                	$('#idMsgError').text(obj.mensajeGenerico);
        			$('#idMsgError').slideDown("slow");
            	} catch(err) {
                	$('#idDivPromo').html(result);
            	}
	        }
	    });
	}
	
</script>

<div style="width: 100%; float: left; min-width: 300px">
	<h3>
		<fmt:message key="promocion.alta.title" />
	</h3>
	<div id="idMsgError" class="form-validate" style="display: none;">
		<label style="white-space: nowrap;"></label>
	</div>
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
        		<select id="idCat" class="form-control">
        			<option value="-1"><fmt:message key="combos.seleccione"/></option>
        			<c:forEach var="cat" items="${categoriaList}">
        				<option value="${cat.id}">${cat.descripcion}</option>
        			</c:forEach>
        		</select>
			</div>
        </div>
    </div>
	<div style="width: 100%; float: left; min-width: 300px" id="idDivPromo" ></div>
    <br/>
	<div class='row'>
		<div class='col-sm-4'>&nbsp;</div>
        <div class='col-sm-8'> 
			<div class="form-group">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.aceptar"/>'
						onclick="javascript:generar()">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.cancelar"/>'
						onclick="javascript:loadInBody('promocion?vigente=${estadoSel}&sortFieldName=fechaDesde&sortOrder=desc')">
			</div>
        </div>
    </div>
</div>
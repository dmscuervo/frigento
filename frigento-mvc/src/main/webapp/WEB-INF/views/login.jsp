<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">
	$(document).ready(function(){
		$('#msgLogin').slideUp(1);
		$('#username').focus();
	});
	
	function submitLogin(form){
		if(!bodyBlock){
			blockControl($('#idMain'));
			bodyBlock = true;				
		}
		$('#msgLogin').slideUp(1);
		$.ajax({
            url: form.attr('action'),
            type: 'POST',
            data: form.serialize(),
            success: function(result) {
            	//Si tengo respuesta JSON es porque contiene errores 
            	try {
            		var obj = JSON.parse(result);
            		$('#msgLogin').html(obj.mensajeGenerico);
            		$('#msgLogin').slideDown(400);
            		//Desbloqueo pantalla
	            	$('#idMain').unblock();
	    			bodyBlock = false;
            		return;
            	}
            	catch(err) {
                	//Desbloqueo pantalla
                	$('#idMain').unblock();
        			bodyBlock = false;
        			//Cargo contenido
                	$('#idMain').html(result);
        			//Levanto Modal
        			$('#idModalMensaje').modal('show');
            	}
            }
        });
	}
	
</script>

<div class="modal-header">
  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
  <h4><fmt:message key="login.subtitle" /></h4>
</div>
<div class="modal-body">
	<div style="width: 30%; margin: 0 auto; min-width: 300px">
		<c:url var="loginUrl" value="/login" />
		<form action="${loginUrl}" method="post" class="form-horizontal" autocomplete="off" id="idForm">
			<div class="input-group input-sm">
				<label class="input-group-addon" for="username">
					<i class="fa fa-user"></i>
				</label>
				<input type="text" class="form-control" id="username" name="username"
					placeholder='<fmt:message key="login.username"/>' required>
			</div>
			<div class="input-group input-sm">
				<label class="input-group-addon" for="password">
					<i class="fa fa-lock"></i>
				</label> 
				<input type="password" class="form-control" id="password" name="password"
					placeholder='<fmt:message key="login.password"/>' required>
			</div>
			<div id="msgLogin" class="alert alert-danger">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
			</div>
		</form>
	</div>
</div>
<div class="modal-footer">
	<input type="button" class="btn btn-block btn-primary btn-default" tabindex="-1"
		value="<fmt:message key="boton.ingresar"/>" onclick="javascript:submitLogin($('#idForm'))">
</div>
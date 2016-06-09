<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="botDetect" uri="botDetect"%>
<c:if test="${not empty captchaCodeError}">
<!--Captcha NO QUITAR. ESTA ETIQUETA PERMITE SABER QUE EL CONTENIDO SE CARGA EN UN DIV INTERNO-->
</c:if>
<script type="text/javascript">
	$(document).ready(function(){
		if($('#idHayErrores').val() > 0){
			$('#idMsgErrorGral').text('<fmt:message key="registracion.error.mensaje.generico" />');
		}
		
		//Aplico restricciones
		$('#idAltura').keyup(function(){
			var value=$(this).val();
			 value=value.replace(/([^0-9]*)/g, "");
			$(this).val(value);
		});
		
		verificarCaptcha();
		
		$('#idCaptchaCode').on('keyup', function(){
			verificarCaptcha();
		});
	});
	
	function verificarCaptcha(){
		if($('#idCaptchaCode').val() == ''){
			$('#idBtSubmit').prop('disabled', true);
		}else{
			$('#idBtSubmit').prop('disabled', false);
		}
	}
</script>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h3 class="modal-title"><fmt:message key="usuario.registrar.title" /></h3>
</div>
<div class="modal-body" style="height: 60%; overflow: auto;" id="contentBody">
	<jsp:include page="registrarForm.jsp"></jsp:include>
</div>
<div class="modal-footer" style="text-align: right;" id="contentFooter">
	<fieldset>
		<legend style="font-size: 12px; font-weight: bold; color: #337ab7; text-align: left;"><fmt:message key="usuario.registrar.captcha" /></legend>
		<div class="row">
	        <div class="col-sm-4"><botDetect:captcha id="exampleCaptcha"/></div>
			<div class="col-sm-4 col-md-offset-4"><input type="text" name="captchaCode" class="form-control" id="idCaptchaCode" /></div>
		</div>
	</fieldset>
	<label style="font-size: 16px; font-weight: bold; color: #a94442">${captchaCodeError}</label>
	<input type="button" class="btn btn-default btn-primary" id="idBtSubmit"
		value='<fmt:message key="boton.aceptar"/>'
		onclick="javascript:submitInPopUpRegistrar($('#idForm').attr('action'), $('#idForm').serialize()+'&captchaCode='+$('#idCaptchaCode').val())">
	<button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="boton.cancelar"/></button>
</div>


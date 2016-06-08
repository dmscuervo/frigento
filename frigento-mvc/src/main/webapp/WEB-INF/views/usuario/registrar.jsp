<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="botDetect" uri="botDetect"%>

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
<c:url var="urlRegistrar" value="/usuario/registrar" />
<form:form action="${urlRegistrar}" method="post" commandName="registracionForm" id="idForm">
<div class="modal-body" style="height: 60%; overflow: auto;">
	<form:hidden path="esAdmin"/>
	<form:hidden path="habilitado"/>
	<form:hidden path="localidad.id"/>
	<form:hidden path="categoriaProducto.id"/>
	<form:hidden path="distancia"/>
		<spring:hasBindErrors name="registracionForm">
			<input type="hidden" id="idHayErrores" value="${fn:length(errors.allErrors)}"/>
		</spring:hasBindErrors>
		<fieldset>
			<legend style="font-size: 16px; font-weight: bold; color: #a94442"><fmt:message key="datos.obligatorios" />&nbsp;<label id="idMsgErrorGral"></label></legend>
			<spring:bind path="username">
			<c:set var="usernameHasBindError">
				<form:errors path="username"/>
			</c:set>
			<div class="form-group ${status.error ? 'has-error' : ''}" style="margin-bottom: 10px; line-height: inherit;">
				<label class="control-label" for="idUserName">
					<fmt:message key="usuario.username" />&nbsp;${usernameHasBindError}
				</label>
				<form:input path="username" cssClass="form-control" id="idUserName" />
			</div>
			</spring:bind>
			<spring:bind path="password">
			<c:set var="passwordHasBindError">
				<form:errors path="password"/>
			</c:set>
			<div class="form-group ${status.error ? 'has-error' : ''}">
				<label class="control-label" for="idPassword">
					<fmt:message key="usuario.password" />&nbsp;${passwordHasBindError}
				</label>
				<form:password path="password" cssClass="form-control" id="idPassword" />
			</div>
			</spring:bind>
			<spring:bind path="passwordReingresada">
			<c:set var="passwordReingresadaHasBindError">
				<form:errors path="passwordReingresada"/>
			</c:set>
			<div class="form-group ${status.error ? 'has-error' : ''}">
				<label class="control-label" for="idPasswordReingresada">
					<fmt:message key="usuario.password.confirm" />&nbsp;${passwordReingresadaHasBindError}
				</label>
				<form:password path="passwordReingresada" cssClass="form-control" id="idPasswordReingresada" />
			</div>
			</spring:bind>
			<spring:bind path="nombre">
			<c:set var="nombreHasBindError">
				<form:errors path="nombre"/>
			</c:set>
			<div class="form-group ${status.error ? 'has-error' : ''}">
				<label class="control-label" for="idNombre">
					<fmt:message key="usuario.nombre" />&nbsp;${nombreHasBindError}
				</label>
				<form:input path="nombre" cssClass="form-control" id="idNombre" />
			</div>
			</spring:bind>
			<spring:bind path="email">
			<c:set var="emailHasBindError">
				<form:errors path="email"/>
			</c:set>
			<div class="form-group ${status.error ? 'has-error' : ''}">
				<label class="control-label" for="idEmail">
					<fmt:message key="usuario.email" />&nbsp;${emailHasBindError}
				</label>
				<form:input path="email" cssClass="form-control" id="idEmail" />
			</div>
			</spring:bind>
			<spring:bind path="calle">
			<c:set var="calleHasBindError">
				<form:errors path="calle"/>
			</c:set>
			<div class="form-group ${status.error ? 'has-error' : ''}">
				<label class="control-label" for="idCalle">
					<fmt:message key="usuario.calle" />&nbsp;${calleHasBindError}
				</label>
				<form:input path="calle" cssClass="form-control" id="idCalle" />
			</div>
			</spring:bind>
			<spring:bind path="altura">
			<c:set var="alturaHasBindError">
				<form:errors path="altura"/>
			</c:set>
			<div class="form-group ${status.error ? 'has-error' : ''}">
				<label class="control-label" for="idAltura">
					<fmt:message key="usuario.altura" />&nbsp;${alturaHasBindError}
				</label>
				<form:input path="altura" cssClass="form-control" id="idAltura" />
			</div>
			</spring:bind>
		</fieldset>
		<fieldset>
			<legend style="font-size: 16px; font-weight: bold; color: #337ab7;"><fmt:message key="datos.opcionales" /></legend>
			<div class="form-group">
				<label class="control-label" for="idDepto" style="font-weight: normal;">
					<fmt:message key="usuario.depto" />
				</label>
				<form:input path="depto" cssClass="form-control" id="idDepto" />
			</div>
			<div class="form-group ${status.error ? 'has-error' : ''}">
				<label class="control-label" for="idApellido" style="font-weight: normal;">
					<fmt:message key="usuario.apellido" />
				</label>
				<form:input path="apellido" cssClass="form-control" id="idApellido" />
			</div>
			<div class="form-group">
				<label class="control-label" for="idTelefono" style="font-weight: normal;">
					<fmt:message key="usuario.telefono" />
				</label>
				<form:input path="telefono" cssClass="form-control" id="idTelefono" />
			</div>
			<div class="form-group">
				<label class="control-label" for="idCelular" style="font-weight: normal;">
					<fmt:message key="usuario.celular" />
				</label>
				<form:input path="celular" cssClass="form-control" id="idCelular" />
			</div>
			<div class="form-group">
				<label class="control-label" for="idCuitCuil" style="font-weight: normal;">
					<fmt:message key="usuario.cuitCuil" />
				</label>
				<form:input path="cuitCuil" cssClass="form-control" id="idCuitCuil" />
			</div>
		</fieldset>
</div>
<div class="modal-footer" style="text-align: right;">
	<fieldset>
		<legend style="font-size: 12px; font-weight: bold; color: #337ab7; text-align: left;"><fmt:message key="usuario.registrar.captcha" /></legend>
		<div class="row">
	        <div class="col-sm-4"><botDetect:captcha id="exampleCaptcha"/></div>
			<spring:bind path="captchaCode">
				<c:set var="captchaCodeHasBindError">
					<form:errors path="captchaCode"/>
				</c:set>
	        <div class="col-sm-4 col-md-offset-4"><form:input path="captchaCode" cssClass="form-control" id="idCaptchaCode" /></div>
	        </spring:bind>
		</div>
	</fieldset>
	<label style="font-size: 16px; font-weight: bold; color: #a94442">${captchaCodeHasBindError}</label>
	<input type="button" class="btn btn-default btn-primary" id="idBtSubmit"
		value='<fmt:message key="boton.aceptar"/>'
		onclick="javascript:submitInPopUp($('#idForm'))">
	<button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="boton.cancelar"/></button>
</div>
</form:form>


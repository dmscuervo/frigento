<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div style="float: left; width: 100%; min-width: 300px">
	<div style="min-width: 50%">
		<h3>
			<fmt:message key="usuario.detalle.title" />
		</h3>
		<br/>
		<div class='row'>
	        <div class='col-md-6'>    
				<label class="control-label" for="idNick" style="white-space: nowrap;">
					<fmt:message key="usuario.username" />:
				</label>
	        	<span id="idNick" style="white-space: nowrap;">${usuario.username}</span>
	        </div>
	        <div class='col-md-6'>    
				<label class="control-label" for="idEstado" style="white-space: nowrap;">
					<fmt:message key="usuario.estado" />:
				</label>
	        	<span id="idEstado" style="white-space: nowrap;">
					<c:if test="${usuario.habilitado}">
						<fmt:message key="usuario.estado.habilitado"/>
					</c:if>
					<c:if test="${ !usuario.habilitado}">
						<fmt:message key="usuario.estado.inhabilitado"/>
					</c:if>
				</span>
	        </div>
	    </div>
	    <div class='row'>
	        <div class='col-md-6'>    
				<label class="control-label" for="idNombre" style="white-space: nowrap;">
					<fmt:message key="usuario.nombre" />:
				</label>
	        	<span id="idNombre" style="white-space: nowrap;">${usuario.nombre}</span>
	        </div>
	        <div class='col-md-6'>    
				<label class="control-label" for="idApellido" style="white-space: nowrap;">
					<fmt:message key="usuario.apellido" />:
				</label>
	        	<span id="idApellido" style="white-space: nowrap;">${usuario.apellido}</span>
	        </div>
	    </div>
	    <div class='row'>
	        <div class='col-md-6'>    
				<label class="control-label" for="idCuitCuil" style="white-space: nowrap;">
					<fmt:message key="usuario.cuitCuil" />:
				</label>
	        	<span id="idCuitCuil" style="white-space: nowrap;">${usuario.cuitCuil}</span>
	        </div>
	        <div class='col-md-6'>    
				<label class="control-label" for="idEmail" style="white-space: nowrap;">
					<fmt:message key="usuario.email" />:
				</label>
	        	<span id="idEmail" style="white-space: nowrap;">${usuario.email}</span>
	        </div>
	    </div>
	    <div class='row'>
	        <div class='col-md-6'>    
				<label class="control-label" for="idTel" style="white-space: nowrap;">
					<fmt:message key="usuario.telefono" />:
				</label>
	        	<span id="idTel" style="white-space: nowrap;">${usuario.telefono}</span>
	        </div>
	        <div class='col-md-6'>    
				<label class="control-label" for="idApellido" style="white-space: nowrap;">
					<fmt:message key="usuario.celular" />:
				</label>
	        	<span id="idApellido" style="white-space: nowrap;">${usuario.celular}</span>
	        </div>
	    </div>
	    <div class='row'>
	        <div class='col-md-12'>    
				<label class="control-label" for="idTel" style="white-space: nowrap;">
					<fmt:message key="usuario.calle" />:
				</label>
	        	<span id="idCalle" style="white-space: nowrap;">${usuario.calle}</span>
	        	<span id="idAlt" style="white-space: nowrap;">${usuario.altura}</span>
	        	<span id="idDepto" style="white-space: nowrap;">${usuario.depto}</span>
	        </div>
	    </div>
    </div>
	<br/>
	<div class='row'>
		<div class='col-sm-4'>&nbsp;</div>
        <div class='col-sm-8'> 
			<div class="form-group">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.volver"/>'
						onclick="javascript:loadInBody('usuario?sortFieldName=nombre,apellido&sortOrder=asc')">
			</div>
        </div>
    </div>
</div>
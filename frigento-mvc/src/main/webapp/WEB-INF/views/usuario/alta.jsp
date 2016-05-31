<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div style="width: 100%; float: left; min-width: 300px">
	<h3>
		<fmt:message key="usuario.alta.title" />
	</h3>
	<br />
	<c:url var="urlAlta" value="/usuario/alta" />
	<form:form action="${urlAlta}" method="post" class="form-horizontal" commandName="usuarioForm" id="idForm">
	<form:hidden path="username"/>
	<form:hidden path="password"/>
	<form:hidden path="esAdmin"/>
	<form:hidden path="habilitado"/>
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idUsuCat" style="white-space: nowrap;">
					<fmt:message key="usuario.categoria" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
       			<form:select path="categoriaProducto.id" cssClass="form-control" id="idUsuCat" >
       				<form:option value="" ><fmt:message key="combos.ninguna" /></form:option>
       				<form:options items="${categoriaList}" itemValue="id" itemLabel="descripcion" />
        		</form:select>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="categoriaProducto.id" cssClass="form-validate" />
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idNombre">
					<fmt:message key="usuario.nombre" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="nombre" cssClass="form-control" id="idNombre" for="idNombreError" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="nombre" cssClass="form-validate" id="idNombreError"/>
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idApellido">
					<fmt:message key="usuario.apellido" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="apellido" cssClass="form-control" id="idApellido" for="idApellidoError" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="apellido" cssClass="form-validate" id="idApellidoError"/>
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idCuitCuil">
					<fmt:message key="usuario.cuitCuil" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="cuitCuil" cssClass="form-control" id="idCuitCuil" for="idCuitCuilError" maxlength="11" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="cuitCuil" cssClass="form-validate" id="idCuitCuilError"/>
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4' style="white-space: nowrap;">    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idEmail">
					<fmt:message key="usuario.email" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="email" cssClass="form-control" id="idEmail" for="idEmailError" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="email" cssClass="form-validate" id="idEmailError"/>
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idTelefono">
					<fmt:message key="usuario.telefono" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="telefono" cssClass="form-control" id="idTelefono" for="idTelefonoError" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="telefono" cssClass="form-validate" id="idTelefonoError"/>
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idCelular">
					<fmt:message key="usuario.celular" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="celular" cssClass="form-control" id="idCelular" for="idCelularError" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="celular" cssClass="form-validate" id="idCelularError"/>
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idCalle">
					<fmt:message key="usuario.calle" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="calle" cssClass="form-control" id="idCalle" for="idCalleError" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="calle" cssClass="form-validate" id="idCalleError"/>
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idAltura">
					<fmt:message key="usuario.altura" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="altura" cssClass="form-control" id="idAltura" for="idAlturaError" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="altura" cssClass="form-validate" id="idAlturaError"/>
			</div>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idDepto">
					<fmt:message key="usuario.depto" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="depto" cssClass="form-control" id="idDepto" for="idDeptoError" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="depto" cssClass="form-validate" id="idDeptoError"/>
			</div>
        </div>
    </div>
    <br/>
	<div class='row'>
		<div class='col-sm-4'>&nbsp;</div>
        <div class='col-sm-8'> 
			<div class="form-group">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.aceptar"/>'
						onclick="javascript:submitInBody($('#idForm'))">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.cancelar"/>'
						onclick="javascript:loadInBody('usuario?estado=true&sortFieldName=nombre,apellido&sortOrder=asc')">
			</div>
        </div>
    </div>
	</form:form>
</div>
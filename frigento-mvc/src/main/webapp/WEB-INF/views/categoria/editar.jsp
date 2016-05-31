<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div style="width: 100%; float: left; min-width: 300px">
	<h3>
		<fmt:message key="categoria.editar.title" />
	</h3>
	<br />
	<c:url var="urlEditar" value="/categoria/editar" />
	<form:form action="${urlEditar}" method="post" class="form-horizontal" commandName="categoriaForm" id="idForm">
	<form:hidden path="id" />
	<div class='row'>
        <div class='col-sm-4'>    
			<div class="form-group" >
				<label class="col-sm-2 control-label" for="idDesc">
					<fmt:message key="categoria.descripcion" />
				</label>
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group">
				<form:input path="descripcion" cssClass="form-control" id="idDesc" for="idDescError" />
			</div>
        </div>
        <div class='col-sm-4'>
        	<div class="form-group" >
				<form:errors path="descripcion" cssClass="form-validate" />
			</div>
        </div>
    </div>
    <br/>
	<div class='row'>
		<div class='col-sm-4'>&nbsp;</div>
        <div class='col-sm-8'> 
			<div class="form-group">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.aplicar.cambios"/>'
						onclick="javascript:submitInBody($('#idForm'))">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.cancelar"/>'
						onclick="javascript:loadInBody('categoria?sortFieldName=descripcion&sortOrder=asc')">
			</div>
        </div>
    </div>
	</form:form>
</div>
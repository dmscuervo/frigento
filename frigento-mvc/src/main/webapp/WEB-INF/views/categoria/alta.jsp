<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">

	$(document).ready(function(){
		//Se evita el submit del form al apretar la tecla ENTER
		$('#idForm').on('keyup keypress', function(e) {
			  var keyCode = e.keyCode || e.which;
			  if (keyCode === 13) { 
			    e.preventDefault();
			    return false;
			  }
			});
	})
</script>


<div style="width: 100%; float: left; min-width: 300px">
	<h3>
		<fmt:message key="categoria.alta.title" />
	</h3>
	<br />
	<c:url var="urlAlta" value="/categoria/alta" />
	<form:form action="${urlAlta}" method="post" class="form-horizontal" commandName="categoriaForm" id="idForm">
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
						value='<fmt:message key="boton.aceptar"/>'
						onclick="javascript:submitInBody($('#idForm'))">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.cancelar"/>'
						onclick="javascript:loadInBody('categoria?sortFieldName=descripcion&sortOrder=asc')">
			</div>
        </div>
    </div>
	</form:form>
</div>
<%@ include file="/WEB-INF/views/include.jsp" %>

<div id="mainWrapper">
    <div class="login-container">
        <div class="login-card">
            <div class="login-form" style="width: 50%; float: left; min-width: 300px">
                <c:url var="urlAltaCategoria" value="/categoria" />
                <form:form action="${urlAltaCategoria}" method="post" class="form-horizontal" commandName="categoriaForm" id="idFormCat">
                	<p>
                    <c:if test="${param.error != null}">
                        <div class="alert alert-danger">
                            ${param.msg}
                        </div>
                    </c:if>
                    </p>
                    <p><span style="navbar-brand"><fmt:message key="categoria.alta.title"/></span></p>
                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="descripcion"><fmt:message key="categoria.descripcion"/></label>
                        <form:input path="descripcion" cssClass="form-control" id="idDesc" />
                        <form:errors path="descripcion" cssClass="error"/>
                        <%-- <form:input class="form-control" path="descripcion" name="username" placeholder='<fmt:message key="categoria.descripcion.ingrese"/>' required> --%>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <div class="form-actions">
                        <input type="button"
                            class="btn btn-block btn-primary btn-default" value="<fmt:message key="boton.ingresar"/>" onclick="javascript:submitInBody($('#idFormCat'))">
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

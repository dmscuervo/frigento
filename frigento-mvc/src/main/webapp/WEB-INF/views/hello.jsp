<%@ include file="/WEB-INF/views/include.jsp" %>

<html>
  <head><title><fmt:message key="abmUsuario.detalle.title"/></title></head>
  <body>
    <h1><fmt:message key="abmUsuario.alta.title"/></h1>
    <p><fmt:message key="abmUsuario.alta.nombre"/> <c:out value="${model.now}"/></p>
    <h3>Products</h3>
    <c:forEach items="${model.categorias}" var="cat">
      <c:out value="${cat.descripcion}"/>
    </c:forEach>
    <a href="<c:url value="categoria/alta.htm"/>">Increase Prices</a>
  </body>
</html>
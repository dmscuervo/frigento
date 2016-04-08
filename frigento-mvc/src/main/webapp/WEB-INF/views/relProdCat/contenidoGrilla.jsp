<%@ include file="/WEB-INF/views/include.jsp"%>

<c:forEach var="prodCat" items="${productosCategoria}">
<tr>
	<td>${prodCat.producto.codigo}</td>
    <td>${prodCat.producto.descripcion}</td>
    <td>${prodCat.incremento}</td>
    <td>${prodCat.precioCalculado}</td>
    <td></td>
</tr>
</c:forEach>
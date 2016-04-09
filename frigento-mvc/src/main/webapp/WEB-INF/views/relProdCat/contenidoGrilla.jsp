<%@ include file="/WEB-INF/views/include.jsp"%>

<c:forEach var="prodCat" items="${productosCategoria}">
<tr>
	<td style="white-space: nowrap;">${prodCat.producto.codigo} - ${prodCat.producto.descripcion}</td>
    <td style="white-space: nowrap;">${prodCat.incremento}</td>
    <td style="white-space: nowrap;">${prodCat.precioCalculado}</td>
    <td style="white-space: nowrap;">${prodCat.fechaDesde}</td>
</tr>
</c:forEach>
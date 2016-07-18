<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">

	$(document).ready(function (){
		$('#idCantCarrito').html('${carritoSize}');
	});
	
</script>

<c:forEach var="item" items="${ventaForm.items}" varStatus="status">
	<tr>
		<td style="white-space: nowrap; vertical-align: middle">
			<a href="javascript: quitarCarrito('${item.producto.id}')">
			<i class="fa fa-remove" style="color: red"></i>
			</a>
		</td>
		<td style="white-space: nowrap;"><form:select
				path="items[${status.index}].cantidad" cssClass="form-control"
				id="idCantidad-${status.index}">
				<option value="-1">0&nbsp;
					<fmt:message key="venta.cantidad.kg" /></option>
				<c:forEach begin="1" end="50" step="1" var="canti">
					<c:if test="${item.cantidad eq (canti/2)}">
						<option value="${canti/2}" selected="selected">${canti/2}&nbsp;<fmt:message
								key="venta.cantidad.kg" /></option>
					</c:if>
					<c:if test="${item.cantidad ne (canti/2)}">
						<option value="${canti/2}">${canti/2}&nbsp;<fmt:message
								key="venta.cantidad.kg" /></option>
					</c:if>
				</c:forEach>
			</form:select> <form:hidden path="items[${status.index}].producto.id" /> <form:hidden
				path="items[${status.index}].producto.codigo" /> <form:hidden
				path="items[${status.index}].producto.descripcion" /> <form:hidden
				id="idStock-${status.index}"
				path="items[${status.index}].producto.stock" /> <form:hidden
				path="items[${status.index}].producto.iva" /> <form:hidden
				path="items[${status.index}].importeVenta" /> <form:hidden
				path="items[${status.index}].relProductoCategoriaId" /> <c:if
				test="${ not empty item.promocion }">
				<form:hidden path="items[${status.index}].promocion.id" />
				<form:hidden path="items[${status.index}].promocion.cantidadMinima" />
				<form:hidden path="items[${status.index}].promocion.fechaDesde" />
			</c:if></td>
		<td style="white-space: nowrap; vertical-align: middle;"><c:if
				test="${ not empty item.promocion }">
				<fmt:message key="venta.promocion">
					<fmt:param value="${item.promocion.cantidadMinima}" />
				</fmt:message>
			</c:if> ${item.producto.codigo} - ${item.producto.descripcion}</td>
		<td
			style="white-space: nowrap; vertical-align: middle; font-size: 0.75em;">
			<fmt:message key="online.carrito.item.entrega1">
				<fmt:param value="${item.entregaArgMessage}" />
			</fmt:message><br> <fmt:message key="online.carrito.item.entrega2">
				<fmt:param value="${item.entregaArgMessage}" />
			</fmt:message>
		</td>
	</tr>
</c:forEach>
<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	
	$(document).ready(function(){
		
		if(!$.fn.DataTable.isDataTable('#idGrillaItems')){
		    $('#idGrillaItems').DataTable({
		    	scrollY:        300,
		    	scrollCollapse: true,
		        paging: false,
		    	order: [[ 1, "asc" ]]
		    }); 
		}
		
	});
	
</script>

<div style="width: 80%; min-width: 300px">
	<div style="display: table; float: left; margin: 0 auto; min-width: 50%">
		<h3>
			<fmt:message key="pedido.detalle.title" />
		</h3>
		<br/>
		<div class='row'>
	        <div class='col-md-3'>    
				<label class="control-label" for="idDesc" style="white-space: nowrap;">
					<fmt:message key="pedido.id" />:
				</label>
	        </div>
	        <div class='col-md-3'>
				<span id="idDesc" style="white-space: nowrap;">${relPedProdList[0].pedido.id}</span>
	        </div>
	        <div class='col-md-3'>    
				<label class="control-label" for="idDesc" style="white-space: nowrap;">
					<fmt:message key="pedido.fecha" />:
				</label>
	        </div>
	        <div class='col-md-3'>
				<span id="idDesc2" style="white-space: nowrap;"><fmt:formatDate value="${relPedProdList[0].pedido.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
	        </div>
	    </div>
	    <div class='row'>
	        <div class='col-md-3'>    
				<label class="control-label" for="idCosto" style="white-space: nowrap;">
					<fmt:message key="pedido.costo" />:
				</label>
	        </div>
	        <div class='col-md-3'>
				<span id="idCosto" style="white-space: nowrap;"><fmt:formatNumber currencySymbol="$" type="currency" value="${relPedProdList[0].pedido.costo}" /></span>
	        </div>
	        <div class='col-md-3'>    
				<label class="control-label" for="idCosto" style="white-space: nowrap;">
					<fmt:message key="pedido.fecha.entregar" />:
				</label>
	        </div>
	        <div class='col-md-3'>
				<span id="idDesc2" style="white-space: nowrap;"><fmt:formatDate value="${relPedProdList[0].pedido.fechaAEntregar}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
	        </div>
	    </div>
	    <div class='row'>
	        <div class='col-md-3'>    
				<label class="control-label" for="idEstado" style="white-space: nowrap;">
					<fmt:message key="pedido.estado" />:
				</label>
	        </div>
	        <div class='col-md-3'>
				<span id="idEstado" style="white-space: nowrap;">${relPedProdList[0].pedido.estado.descripcion}</span>
	        </div>
	        <div class='col-md-3'>    
				<c:if test="${relPedProdList[0].pedido.estado.id eq 3}">
					<label class="control-label" for="idEstado" style="white-space: nowrap;">
						<fmt:message key="pedido.fecha.entregador" />:
					</label>
				</c:if>
				<c:if test="${relPedProdList[0].pedido.estado.id eq 4}">
					<label class="control-label" for="idDesc2" style="white-space: nowrap;">
						<fmt:message key="pedido.fecha.anulado" />:
					</label>
				</c:if>
	        </div>
	        <div class='col-md-3'>
        		<c:if test="${relPedProdList[0].pedido.estado.id eq 3}">
					<span style="white-space: nowrap;"><fmt:formatDate value="${relPedProdList[0].pedido.fechaEntregado}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
				</c:if>
				<c:if test="${relPedProdList[0].pedido.estado.id eq 4}">
					<span style="white-space: nowrap;"><fmt:formatDate value="${relPedProdList[0].pedido.fechaAnulado}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
				</c:if>
	        </div>
	    </div>
	</div>
	<table id="idGrillaItems" class="order-column table table-striped table-bordered" >
		<thead>
			<tr>
				<th style="white-space: nowrap;"><fmt:message
						key="pedido.item.cantidad.caja" /></th>
				<th style="white-space: nowrap;"><fmt:message
						key="pedido.item.producto" /></th>
				<th style="white-space: nowrap;"><fmt:message
						key="producto.pesoCaja" /></th>
				<th style="white-space: nowrap;"><fmt:message
						key="pedido.item.pu" /></th>
				<th style="white-space: nowrap;"><fmt:message
						key="pedido.item.costo" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${relPedProdList}" varStatus="status">
				<tr>
					<td style="white-space: nowrap;"><fmt:formatNumber
							value="${item.cantidad / item.productoCosto.producto.pesoCaja}" maxFractionDigits="0"
							minFractionDigits="0" /></td>
					<td style="white-space: nowrap;">${item.productoCosto.producto.codigo}
						- ${item.productoCosto.producto.descripcion}</td>
					<td style="white-space: nowrap;">${item.productoCosto.producto.pesoCaja}</td>
					<td style="white-space: nowrap;"><fmt:formatNumber
							currencySymbol="$" type="currency" value="${item.costo * item.productoCosto.producto.pesoCaja}" /></td>
					<td style="white-space: nowrap;"><fmt:formatNumber
							currencySymbol="$" type="currency" value="${item.cantidad * item.costo}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br/>
	<div class='row'>
		<div class='col-sm-4'>&nbsp;</div>
        <div class='col-sm-8'> 
			<div class="form-group">
					<input type="button" class="btn btn-default btn-primary"
						value='<fmt:message key="boton.volver"/>'
						onclick="javascript:loadInBody('pedido?sortFieldName=id&sortOrder=asc')">
			</div>
        </div>
    </div>
</div>
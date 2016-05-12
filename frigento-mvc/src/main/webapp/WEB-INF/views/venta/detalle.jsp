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
	<div style="display: table; float: left; margin: 0 auto; min-width: 80%">
		<h3>
			<fmt:message key="venta.detalle.title" />
		</h3>
		<br/>
		<div class='row'>
	        <div class='col-md-3'>    
				<label class="control-label" for="idDesc" style="white-space: nowrap;">
					<fmt:message key="venta.id" />:
				</label>
	        </div>
	        <div class='col-md-3'>
				<span id="idDesc" style="white-space: nowrap;">${relVtaProdList[0].venta.id}</span>
	        </div>
	        <div class='col-md-3'>    
				<label class="control-label" for="idDesc" style="white-space: nowrap;">
					<fmt:message key="venta.fecha" />:
				</label>
	        </div>
	        <div class='col-md-3'>
				<span id="idDesc2" style="white-space: nowrap;"><fmt:formatDate value="${relVtaProdList[0].venta.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
	        </div>
	    </div>
	    <div class='row'>
	        <div class='col-md-3'>    
				<label class="control-label" for="idDesc" style="white-space: nowrap;">
					<fmt:message key="venta.usuario" />:
				</label>
	        </div>
	        <div class='col-md-9'>
				<span id="idDesc" style="white-space: nowrap;">${relVtaProdList[0].venta.usuario.identificadoWeb}</span>
	        </div>
	    </div>
	    <div class='row'>
	    	<div class='col-md-3'>    
				<label class="control-label" for="idEstado" style="white-space: nowrap;">
					<fmt:message key="venta.estado" />:
				</label>
	        </div>
	        <div class='col-md-3'>
				<span id="idEstado" style="white-space: nowrap;">${relVtaProdList[0].venta.estado.descripcion}</span>
	        </div>
	        <div class='col-md-3'>    
				<label class="control-label" for="idCosto" style="white-space: nowrap;">
					<fmt:message key="venta.importe" />:
				</label>
	        </div>
	        <div class='col-md-3'>
				<span id="idCosto" style="white-space: nowrap;"><fmt:formatNumber currencySymbol="$" type="currency" value="${relVtaProdList[0].venta.importe}" /></span>
	        </div>
	    </div>
	    <div class='row'>
	    	<div class='col-md-3'>    
				&nbsp;
	        </div>
	    	<div class='col-md-3'>    
				<label class="control-label" for="idEstado" style="white-space: nowrap;">
					<fmt:message key="venta.iva" >
						<c:if test="${relVtaProdList[0].venta.incrementoIva.unscaledValue() != 0}">
							<fmt:param >
								<fmt:message key="venta.iva.con" />
							</fmt:param>
						</c:if>
						<c:if test="${relVtaProdList[0].venta.incrementoIva.unscaledValue() == 0}">
							<fmt:param >
								<fmt:message key="venta.iva.sin" />
							</fmt:param>
						</c:if>
					</fmt:message>
				</label>
	        </div>
	        <div class='col-md-3'>    
				<label class="control-label" for="idCosto" style="white-space: nowrap;">
					<fmt:message key="venta.importe.con.iva" />
				</label>
	        </div>
	        <div class='col-md-3'>
	        	<c:if test="${relVtaProdList[0].venta.incrementoIva.unscaledValue() != 0}">
					<span id="idCosto" style="white-space: nowrap;"><fmt:formatNumber currencySymbol="$" type="currency" value="${relVtaProdList[0].venta.importe + relVtaProdList[0].venta.incrementoIva}" /></span>
	        	</c:if>
	        </div>
	    </div>
	    <div class='row'>
	        <div class='col-md-3'>    
				<label class="control-label" for="idCosto" style="white-space: nowrap;">
					<fmt:message key="venta.fecha.entregar" />:
				</label>
	        </div>
	        <div class='col-md-3'>
				<span id="idDesc2" style="white-space: nowrap;"><fmt:formatDate value="${relVtaProdList[0].venta.fechaAEntregar}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
	        </div>
	        <div class='col-md-3'>    
				<c:if test="${relVtaProdList[0].venta.estado.id eq 3}">
					<label class="control-label" for="idEstado" style="white-space: nowrap;">
						<fmt:message key="venta.fecha.entregado" />:
					</label>
				</c:if>
				<c:if test="${relVtaProdList[0].venta.estado.id eq 4}">
					<label class="control-label" for="idDesc2" style="white-space: nowrap;">
						<fmt:message key="venta.fecha.anulado" />:
					</label>
				</c:if>
	        </div>
	        <div class='col-md-3'>
        		<c:if test="${relVtaProdList[0].venta.estado.id eq 3}">
					<span style="white-space: nowrap;"><fmt:formatDate value="${relVtaProdList[0].venta.fechaEntregado}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
				</c:if>
				<c:if test="${relVtaProdList[0].venta.estado.id eq 4}">
					<span style="white-space: nowrap;"><fmt:formatDate value="${relVtaProdList[0].venta.fechaAnulado}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
				</c:if>
	        </div>
	    </div>
	</div>
	<table id="idGrillaItems" class="order-column table table-striped table-bordered" >
		<thead>
			<tr>
				<th style="white-space: nowrap;"><fmt:message
						key="venta.item.cantidad.kg" /></th>
				<th style="white-space: nowrap;"><fmt:message
						key="venta.item.producto" /></th>
				<th style="white-space: nowrap;"><fmt:message
						key="venta.item.pu.peso" /></th>
				<th style="white-space: nowrap;"><fmt:message
						key="venta.item.precio" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${relVtaProdList}" varStatus="status">
				<tr>
					<td style="white-space: nowrap;"><fmt:formatNumber
							value="${item.cantidad}" maxFractionDigits="3"
							minFractionDigits="3" /></td>
					<td style="white-space: nowrap;">
						<c:if test="${ not empty item.promocion }">
	        				<fmt:message key="venta.promocion">
	        					<fmt:param value="${item.promocion.cantidadMinima}" />
	        				</fmt:message>
	        			</c:if>
						${item.relProductoCategoria.producto.codigo} - ${item.relProductoCategoria.producto.descripcion}
					</td>
					<td style="white-space: nowrap;"><fmt:formatNumber
							currencySymbol="$" type="currency" value="${item.precioVenta}" /></td>
					<td style="white-space: nowrap;"><fmt:formatNumber
							currencySymbol="$" type="currency" value="${item.precioVenta * item.cantidad}" /></td>
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
						onclick="javascript:loadInBody('${urlVolver}')">
			</div>
        </div>
    </div>
</div>
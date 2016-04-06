<%@ include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
	    $('#idGrillaCat').DataTable(
	    );
	    
/* 	    $('#idGrillaCat tbody tr').each(function( index ) {
	    	  console.log($( this ) );
	    	  $(this).on( 'click', function () {
	    		  console.log( $(this).find('td:last-child').text());	
	    	  });
	    	}); */
	    console.log('Aplicado');
	});
</script>

<h3>
	<fmt:message key="categoria.alta.title" />
</h3>
<br />
<table id="idGrillaCat" class="order-column table table-striped table-bordered" style="border-spacing: 0; width: 70%">
        <thead>
            <tr>
                <th><fmt:message key="categoria.descripcion" /></th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="cat" items="${categorias}">
        	<tr>
        		<td>${cat.descripcion}</td>
        		<td colspan="2">
        			<i class="fa fa-edit" onclick="loadInBody('categoria/${cat.id}?editar')"></i>
        			<i class="fa fa-trash" onclick="loadInBody('categoria/${cat.id}?borrar')"></i>
				</td>
        	</tr>
        </c:forEach>
        </tbody>
</table>
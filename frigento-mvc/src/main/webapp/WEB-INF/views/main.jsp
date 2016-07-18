<%@ include file="/WEB-INF/views/include.jsp" %>
<c:url var="pathBase" value="/" />
<html>
<head>
    <title>
    	<fmt:message key="app.title" />&nbsp;${applicationScope.VERSION_APP}
    </title>
    <link href="${pathBase}resources/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
    
    <%@ include file="/WEB-INF/views/sources.jsp" %>

    <script type="text/javascript">
    	var bodyBlock=false;
    	
    	function loadInBody(path){
    		if(!bodyBlock){
				blockControl($('#page'));
				bodyBlock = true;				
			}
    		var url = '${pathBase}' + path;
    		$('#page-content').load(url, function(data){
    			$('#page-content').html(data);
    			$('#idModalAccion').modal('show');
    			//Desbloqueo pantalla
            	$('#page').unblock();
    			bodyBlock = false;
    			//En caso mobile, collapsa el menu luego de elegir una opcion
    			$('.sidebar-nav').attr('class', 'sidebar-nav navbar-collapse collapse');
    		});
    	}
    	
    	function loadInMain(path){
    		if(!bodyBlock){
				blockControl($('#idMain'));
				bodyBlock = true;				
			}
    		var url = '${pathBase}' + path;
    		$('#idMain').load(url, function(data){
    			$('#idMain').html(data);
    			//Desbloqueo pantalla
            	$('#idMain').unblock();
    			bodyBlock = false;
    			//En caso mobile, collapsa el menu luego de elegir una opcion
    			$('.sidebar-nav').attr('class', 'sidebar-nav navbar-collapse collapse');
    		});
    	}
    	
    	function confirmarAccion(path){
    		var url = '${pathBase}' + path;
    		$('#divVentanaGrilla').load(url, function(data){
    			$('#idModalAccion').modal('show');
    		});
    	}
    	
    	function submitInBody(form){
    		if(!bodyBlock){
				blockControl($('#page'));
				bodyBlock = true;				
			}
    		
    		$.ajax({
                url: form.attr('action'),
                type: 'POST',
                data: form.serialize(),
                success: function(result) {
                	//Desbloqueo pantalla
                	$('#page').unblock();
        			bodyBlock = false;
        			//Cargo contenido
                	$('#page-content').html(result);
        			//Levanto Modal
        			$('#idModalMensaje').modal('show');
                }
            });
    	}
    	
    	function submitInMain(form){
    		if(!bodyBlock){
				blockControl($('#idMain'));
				bodyBlock = true;				
			}
    		
    		$.ajax({
                url: form.attr('action'),
                type: 'POST',
                data: form.serialize(),
                success: function(result) {
                	//Desbloqueo pantalla
                	$('#idMain').unblock();
        			bodyBlock = false;
        			//Cargo contenido
                	$('#idMain').html(result);
        			//Levanto Modal
        			$('#idModalMensaje').modal('show');
                }
            });
    	}
    	
    	function loadInPopUp(path){
    		var url = '${pathBase}' + path;
    		$('#idModalPopUpContent').load(url, function(data){
    			$('#idModalPopUp').modal('show');
    		});
    	}
    	
    	function submitInPopUp(form){
    		blockControl($('#idModalPopUpContent'));
    		$.ajax({
                url: form.attr('action'),
                type: 'POST',
                data: form.serialize(),
                success: function(result) {
                	//Desbloqueo pantalla
                	$('#idModalPopUpContent').unblock();
        			//Cargo contenido
                	$('#idModalPopUpContent').html(result);
        			//Levanto Modal
        			//$('#idModalPopUp').modal('show');
                }
            });
    	}
    	
    	function submitInPopUpRegistrar(action, data){
    		blockControl($('#idModalPopUpContent'));
			$.ajax({
                url: action,
                type: 'POST',
                data: data,
                success: function(result) {
                	//Desbloqueo pantalla
                	$('#idModalPopUpContent').unblock();
                	if(result.trim().startsWith('<!--Captcha')){
                		$('#idModalPopUpContent').html(result);
                	}else{
	                	//Cargo contenido
	                	$('#contentBody').html(result);
                	}
                }
            });
    	}
    	
    	function agregarCarrito(idProd, indice){
    		var cant = $('#idCantidad-'+indice).val();
    		var url = '${pathBase}' + 'carrito/agregar';
    		$.ajax({
                url: url,
                type: 'POST',
                data: {idProd: idProd, cantidad: cant},
                success: function(result) {
                	var obj = JSON.parse(result);
                	$('#idLinkVaciar').slideDown(0);
                	$('#idCantCarrito').html(obj.mensajeGenerico);
                }
            });
    	}
    	
    	function quitarCarrito(idProd){
    		var r = confirm('<fmt:message key="online.carrito.quitar.confirm"/>');
    		if (r != true) {
    		    return;
    		}
    		var url = '${pathBase}' + 'carrito/quitar';
    		$.ajax({
                url: url,
                type: 'POST',
                data: {idProd: idProd},
                success: function(result) {
                	try {
                		//Si hay json es porque no quedan productos en el carrito
                		var obj = JSON.parse(result);
                		$('#idLinkVaciar').slideUp(0);
                    	$('#idCantCarrito').html('0');
                    	$('#idModalPopUpContent').html(obj.mensajeGenerico);
                	} catch(err) {
	                	$('#idModalPopUpContent').html(result);
                	}
                }
            });
    	}
    	
    	function vaciarCarrito(){
    		var url = '${pathBase}' + 'carrito/vaciar';
    		
    		$.ajax({
                url: url,
                type: 'GET',
                success: function(result) {
                	var obj = JSON.parse(result);
               		$('#idLinkVaciar').slideUp(0);
                	$('#idCantCarrito').html(obj.mensajeGenerico);
                }
            });
    	}
    	
    	function verCarrito(){
    		var url = '${pathBase}' + 'carrito/ver';
    		
    		var huboError = false;
    		$('#idModalPopUpContent').load(url, function(result){
    			//Si tengo respuesta JSON es porque contiene errores 
    			try {
            		var obj = JSON.parse(result);
            		$('#idModalPopUpContent').html(obj.mensajeGenerico);
        			$('#idModalPopUp').modal('show');
            		huboError = true;
        			return;
            	}
            	catch(err) {
		    		$('#idModalPopUp').modal('show');
            		//$('#idModalPopUpContent').html(result);
            	}
    			
    		});
       		//El datatable se carga despues de cargar el popup porque sino no ajusta los anchos
    		if(!huboError){
	       		if(!$.fn.DataTable.isDataTable('#idGrillaItems')){
	    			dataTableVentaAlta = $('#idGrillaItems').DataTable({
	    		    	scrollY:        300,
	    		    	scrollX: 		false,
	    		        scrollCollapse: true,
	    		        paging: false,
	    		    	order: [[ 2, "asc" ]],
	    		    	searching: false,
	    		    	columnDefs: [
	    		                       { "orderable": false, "targets": [0, 1] }
	    		                     ]
	    		    }); 
	    		}
    		}
    		
    	}
    	
    </script>

</head>

<body style="overflow: hidden;" id="idMain">
	<jsp:include page="home.jsp" />
</body>
</html>

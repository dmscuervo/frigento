<%@ include file="/WEB-INF/views/include.jsp" %>
<c:url var="pathBase" value="/" />
<html>
<head>
    <title>
    	<fmt:message key="app.title" />&nbsp;${applicationScope.VERSION_APP}
    </title>
    
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
    		
    		$('#idModalPopUpContent').load(url, function(result){
    			//Si tengo respuesta JSON es porque contiene errores 
    			try {
            		var obj = JSON.parse(result);
            		//Desbloqueo pantalla
	            	$('#page').unblock();
	    			bodyBlock = false;
	    			$('#idModalPopUpContent').html(obj.mensajeGenerico);
	    			$('#idModalPopUp').modal('show');
	    			return;
            	}
            	catch(err) {
            		$('#idModalPopUp').modal('show');
            	}
    			
    		});
    	}
    	
    </script>

</head>

<body style="overflow: hidden;" id="idMain">
	<jsp:include page="home.jsp" />
</body>
</html>

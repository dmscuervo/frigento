/**
 * Soluciona el problema que existe en IE8 con la funcion trim()
*/ 
if(typeof String.prototype.trim !== 'function') {
	String.prototype.trim = function() {
		return this.replace(/^\s+|\s+$/g, ''); 
	};
}

/**
* Funcion para agregar una pantalla que bloquee el control
*/
function blockControl(control){
	control.block({message: jQuery('#loading-img'), css:{border:'none',backgroundColor:'#fff',opacity: '.8', width:'120px'}});
}

function showPopup(control, width, left){
	$('#page').block({
		message: control, 
		css:{border:'none',
		backgroundColor:'#fff',
		width: width,
		left: left,
		focusInput: true, 
		cursor:'default'}}
	);
}

function closePopup(){
	$('#page').unblock();
}

function aplicarRestriccionesInputs(divId){
	var idDeDivsContenedores = '';
	if( divId != undefined){
		idDeDivsContenedores = divId;
	}
	//alert(idDeDivsContenedores);
	$(idDeDivsContenedores + ' input[numeric=entero]').keyup(function(){
      	var value=$(this).val();
	    value=value.replace(/([^0-9])/g, "");
		$(this).val(value);
		});

	$(idDeDivsContenedores + ' input[numeric=decimal]').keyup(function(){
		var value=$(this).val();
		 value=value.replace(/([^0-9.]*)/g, "");
		$(this).val(value);
	}); 
	
	$(idDeDivsContenedores + ' input[alfaNumeric]').keyup(function(){
		var value=$(this).val();
		value=value.replace(/[^a-zA-Z0-9]/g, '');
		$(this).val(value);
	});

	$(idDeDivsContenedores + ' input[alfaNumericCEspacio]').keyup(function(){
		var value=$(this).val();
		value=value.replace(/[^a-zA-Z0-9 ]/g, '');
		$(this).val(value);
	});
	
	$(idDeDivsContenedores + ' input[alfaNumericCEspEnie]').keyup(function(){
		var value=$(this).val();
		value=value.replace(/[^a-zA-Z0-9ñÑ ]/g, '');
		$(this).val(value);
	});

	$(idDeDivsContenedores + ' input[numeric=hexa]').keyup(function(){
		var value=$(this).val();
		value=value.replace(/([^A-Fa-f0-9])/g, "");
		$(this).val(value.toUpperCase());
	});
}
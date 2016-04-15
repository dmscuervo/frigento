<%@ include file="/WEB-INF/views/include.jsp" %>

<script type="text/javascript">

	$(document).ready(function(){
		$('#idModalMensaje').on('hidden.bs.modal', function () {
			cerrar();
		});
		$('#idModalMensaje').modal('show');
	});
	
	function cerrar(){
		$('#idModalMensaje').modal('hide');
		window.location.replace('${pathBase}' + "home");
	}

</script>

<c:url var="urlBack" value="/home" />
<div class="modal fade" id="idModalMensaje" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">${msgTitle}</h4>
      </div>
      <div class="modal-body">
        <p>${msgResult}</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" onclick="cerrar()"><fmt:message key="boton.aceptar"/></button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div>
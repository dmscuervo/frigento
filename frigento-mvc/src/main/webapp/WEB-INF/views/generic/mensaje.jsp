<%@ include file="/WEB-INF/views/include.jsp" %>

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
        <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="boton.aceptar"/></button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
  <input type="hidden" id="idUrlOk" value="${urlOk}" />
</div>
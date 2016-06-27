<%@ include file="/WEB-INF/views/include.jsp" %>

<script type="text/javascript">

	$(document).ready(function (){
		//Fue necesario invocar a metisMenu porque sino quedaba el menu estatico luego del login
		$('#side-menu').metisMenu();
	});
</script>


<div class="navbar-default sidebar" style="margin-top: 0px; width: 85%">
	<div class="sidebar-nav navbar-collapse" id="bs-example-navbar-collapse-2">
	    <ul class="nav" id="side-menu">
	    	<!-- 
	        <li class="sidebar-search">
	            <div class="input-group custom-search-form">
	                <input type="text" class="form-control" placeholder="<fmt:message key="menu.sidebar.search"/>">
	                <span class="input-group-btn">
	                <button class="btn btn-default" type="button">
	                    <i class="fa fa-search"></i>
	                </button>
	            </span>
	            </div>
	        </li>
	        -->
	        <li>
	            <a href="#"><i class="fa fa-book fa-fw"></i> <fmt:message key="menu.categoria"/><span class="fa arrow"></span></a>
	            <ul class="nav nav-second-level">
	                <li>
	                    <a href="javascript:loadInBody('categoria?alta')"><fmt:message key="menu.categoria.alta"/></a>
	                </li>
	                <li>
	                    <a href="javascript:loadInBody('categoria?sortFieldName=descripcion&sortOrder=asc')"><fmt:message key="menu.categoria.listar"/></a>
	                </li>
	            </ul>
	            <!-- /.nav-second-level -->
	        </li>
	        <li>
	            <a href="#"><i class="fa fa-asterisk fa-fw"></i> <fmt:message key="menu.producto"/><span class="fa arrow"></span></a>
	            <ul class="nav nav-second-level">
	                <li>
	                    <a href="javascript:loadInBody('producto?alta')"><fmt:message key="menu.producto.alta"/></a>
	                </li>
	                <li>
	                    <a href="javascript:loadInBody('producto?estado=A&sortFieldName=descripcion&sortOrder=asc')"><fmt:message key="menu.producto.listar"/></a>
	                </li>
	            </ul>
	            <!-- /.nav-second-level -->
	        </li>
	        <li>
	            <a href="forms.html"><i class="fa fa-truck fa-fw"></i> <fmt:message key="menu.pedido"/><span class="fa arrow"></span></a>
	            <ul class="nav nav-second-level">
	                <li>
	                    <a href="javascript:loadInBody('pedido?alta')"><fmt:message key="menu.pedido.generar"/></a>
	                </li>
	                <li>
	                    <a href="javascript:loadInBody('pedido?sortFieldName=id&sortOrder=desc')"><fmt:message key="menu.pedido.listar"/></a>
	                </li>
	            </ul>
	        </li>
	        <li>
	            <a href="#"><i class="fa fa-shopping-cart fa-fw"></i> <fmt:message key="menu.venta"/><span class="fa arrow"></span></a>
	            <ul class="nav nav-second-level">
	                <li>
	                    <a href="javascript:loadInBody('venta?preAlta')"><fmt:message key="menu.venta.generar"/></a>
	                </li>
	                <li>
	                    <a href="javascript:loadInBody('venta?estado=A&sortFieldName=id&sortOrder=desc')"><fmt:message key="menu.venta.listar"/></a>
	                </li>
	            </ul>
	            <!-- /.nav-second-level -->
	        </li>
	        <li>
	            <a href="#"><i class="fa fa-users fa-fw"></i> <fmt:message key="menu.usuario"/><span class="fa arrow"></span></a>
	            <ul class="nav nav-second-level">
	                <li>
	                    <a href="javascript:loadInBody('usuario?alta')"><fmt:message key="menu.usuario.alta"/></a>
	                </li>
	                <li>
	                    <a href="javascript:loadInBody('usuario?sortFieldName=nombre,apellido&sortOrder=asc')"><fmt:message key="menu.usuario.listar"/></a>
	                </li>
	            </ul>
	            <!-- /.nav-second-level -->
	        </li>
	        <li>
	            <a href="#"><i class="fa fa-table fa-fw"></i> <fmt:message key="menu.planilla"/><span class="fa arrow"></span></a>
	            <ul class="nav nav-second-level">
	                <li>
	                    <a href="javascript:loadInBody('planilla/cliente?filtro')"><fmt:message key="menu.planilla.cliente"/></a>
	                </li>
	            </ul>
	            <!-- /.nav-second-level -->
	        </li>
	        <li>
	            <a href="#"><i class="fa fa-table fa-fw"></i> <fmt:message key="menu.consulta"/><span class="fa arrow"></span></a>
	            <ul class="nav nav-second-level">
	                <li>
	                    <a href="javascript:loadInBody('consulta/ganancia?filtro')"><fmt:message key="menu.consulta.ganancia"/></a>
	                </li>
	            </ul>
	            <!-- /.nav-second-level -->
	        </li>
	    </ul>
    </div>
</div>
<!-- /.sidebar-collapse -->
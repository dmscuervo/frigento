<%@ include file="/WEB-INF/views/include.jsp" %>
<c:url var="pathBase" value="/" />
<html >
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>
    	<fmt:message key="app.title" />&nbsp;${applicationScope.VERSION_APP}
    </title>
    
    <link href="<c:url value="/resources/css/frigento.css" />" rel="stylesheet" />
    <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet" />
    <link href="<c:url value="/resources/css/metisMenu.min.css" />" rel="stylesheet" />
    <link href="<c:url value="/resources/css/timeline.css" />" rel="stylesheet" />
    <link href="<c:url value="/resources/css/sb-admin-2.css" />" rel="stylesheet" />
    <link href="<c:url value="/resources/css/morris.css" />" rel="stylesheet" />
    <link href="<c:url value="/resources/css/font-awesome.min.css" />" rel="stylesheet" />
	<!-- <link href="<c:url value="/resources/css/bootstrap-datetimepicker.min.css" />" rel="stylesheet" /> -->
    <%-- DataTable cs --%>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.11/css/dataTables.bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/autofill/2.1.1/css/autoFill.bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/colreorder/1.3.1/css/colReorder.bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/rowreorder/1.1.1/css/rowReorder.dataTables.min.css"/>
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/fixedcolumns/3.2.1/css/fixedColumns.bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/fixedheader/3.1.1/css/fixedHeader.bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/responsive/2.0.2/css/responsive.bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/scroller/1.4.1/css/scroller.bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.11/css/jquery.dataTables.min.css"/>
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/buttons/1.1.2/css/buttons.dataTables.min.css"/>
	
	<link href="//cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/e8bddc60e73c1ec2475f827be36e1957af72e2ea/build/css/bootstrap-datetimepicker.css" rel="stylesheet">
    
    <script src="<c:url value="/resources/js/jquery.min.js" />"></script>
    <script src="<c:url value="/resources/js/jquery.blockUI.js" />"></script>
    <script src="<c:url value="/resources/js/frigento.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
    <script src="<c:url value="/resources/js/metisMenu.min.js" />"></script>
    <script src="<c:url value="/resources/js/raphael-min.js" />"></script>
    <script src="<c:url value="/resources/js/morris.min.js" />"></script>
    <!-- script src="<c:url value="/resources/js/morris-data.js" />"></script-->
    <script src="<c:url value="/resources/js/sb-admin-2.js" />"></script>
    <!-- <script src="<c:url value="/resources/js/bootstrap-datetimepicker.min.js" />"></script> -->
    <%-- DataTable js --%>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.11/js/jquery.dataTables.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/1.10.11/js/dataTables.bootstrap.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/autofill/2.1.1/js/dataTables.autoFill.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/autofill/2.1.1/js/autoFill.bootstrap.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/colreorder/1.3.1/js/dataTables.colReorder.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/rowreorder/1.1.1/js/dataTables.rowReorder.min.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/fixedcolumns/3.2.1/js/dataTables.fixedColumns.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/fixedheader/3.1.1/js/dataTables.fixedHeader.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/responsive/2.0.2/js/dataTables.responsive.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/scroller/1.4.1/js/dataTables.scroller.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/select/1.1.2/js/dataTables.select.min.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.1.2/js/dataTables.buttons.min.js"></script>
	
	<script src="//cdnjs.cloudflare.com/ajax/libs/moment.js/2.13.0/moment-with-locales.js"></script>
	<script src="//cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/e8bddc60e73c1ec2475f827be36e1957af72e2ea/src/js/bootstrap-datetimepicker.js"></script>
	
	<%-- Ubicar a lo ultimo. Sobre escribe los mensajes de datatable --%> 
	<script src="<c:url value="/resources/js/datatable.language.js" />"></script>
	

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script type="text/javascript">
    	var bodyBlock=false;
    	
    	function loadInBody(path){
    		if(!bodyBlock){
				blockControl($('#wrapper'));
				bodyBlock = true;				
			}
    		var url = '${pathBase}' + path;
    		$('#page-wrapper').load(url, function(data){
    			$('#page-wrapper').html(data);
    			$('#idModalAccion').modal('show');
    			//Desbloqueo pantalla
            	$('#wrapper').unblock();
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
				blockControl($('#wrapper'));
				bodyBlock = true;				
			}
    		
    		$.ajax({
                url: form.attr('action'),
                type: 'POST',
                data: form.serialize(),
                success: function(result) {
                	//Desbloqueo pantalla
                	$('#wrapper').unblock();
        			bodyBlock = false;
        			//Cargo contenido
                	$('#page-wrapper').html(result);
        			//Levanto Modal
        			$('#idModalMensaje').modal('show');
                }
            });
    	}
    	
    </script>

</head>

<body>


    <div id="wrapper">
<sec:authorize access="isAuthenticated()">
        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="home">
                	<fmt:message key="app.logo" />&nbsp;${applicationScope.VERSION_APP}
                </a>
            </div>
            <!-- /.navbar-header -->

            <ul class="nav navbar-top-links navbar-right">
            <%-- 
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-bell fa-fw"></i>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-alerts">
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-comment fa-fw"></i> New Comment
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                                    <span class="pull-right text-muted small">12 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-envelope fa-fw"></i> Message Sent
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-tasks fa-fw"></i> New Task
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-upload fa-fw"></i> Server Rebooted
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a class="text-center" href="#">
                                <strong>See All Alerts</strong>
                                <i class="fa fa-angle-right"></i>
                            </a>
                        </li>
                    </ul>
                    <!-- /.dropdown-alerts -->
                </li>
				--%>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-user fa-fw"></i>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                        <li><a href="#"><i class="fa fa-user fa-fw"></i><fmt:message key="menu.header.user.profile"/></a>
                        </li>
                        <li class="divider"></li>
                        <li><a href="logout"><i class="fa fa-sign-out fa-fw"></i><fmt:message key="menu.header.user.logout"/></a>
                        </li>
                    </ul>
                    <!-- /.dropdown-user -->
                </li>
                <!-- /.dropdown -->
            </ul>
            <!-- /.navbar-top-links -->
            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li class="sidebar-search">
                            <div class="input-group custom-search-form">
                                <input type="text" class="form-control" placeholder="<fmt:message key="menu.sidebar.search"/>">
                                <span class="input-group-btn">
                                <button class="btn btn-default" type="button">
                                    <i class="fa fa-search"></i>
                                </button>
                            </span>
                            </div>
                            <!-- /input-group -->
                        </li>
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
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>
		<div id="page-wrapper">
</sec:authorize>
        	<jsp:include page="body.jsp" />
<sec:authorize access="hasRole('ROLE_ADMIN')">        	
        </div>
        <!-- /#page-wrapper -->
</sec:authorize>
    </div>
    <!-- /#wrapper -->
    <!-- Div para blockControl -->
    <div id="loadingDiv" style="display: none;overflow: auto; height: auto;width: 99%;">
		<table id="loading-img" cellpadding="0" cellspacing="0">
			<tr>
				<td><img src="<c:url value="/resources/images/ajax-loader.gif"/> "></td>
				<td style="width: 5px;">&nbsp;</td>
				<td style="FONT-FAMILY: Arial,Verdana;font-size: 12px; font-weight: bold; vertical-align: middle;">Procesando ...</td>
			</tr>
		</table>
	</div>
</body>
</html>

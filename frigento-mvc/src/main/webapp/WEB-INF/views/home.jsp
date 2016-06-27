<%@ include file="/WEB-INF/views/include.jsp" %>

<!-- Navigation -->
<!-- <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0"> -->
 <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
     <div class="container">
         <!-- Brand and toggle get grouped for better mobile display -->
         <div class="navbar-header">
             <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                 <span class="sr-only">Toggle navigation</span>
                 <span class="icon-bar"></span>
                 <span class="icon-bar"></span>
                 <span class="icon-bar"></span>
             </button>
             <button type="button" id="btToggle2" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-2">
                 <span class="sr-only">Toggle navigation</span>
                 <span class="icon-bar"></span>
                 <span class="icon-bar"></span>
                 <span class="icon-bar"></span>
             </button>
             <a class="navbar-brand" href="#">Start Bootstrap</a>
         </div>
         <!-- Collect the nav links, forms, and other content for toggling -->
         <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
             <ul class="nav navbar-nav">
                 <li>
                     <a href="#">About</a>
                 </li>
                 <li>
                     <a href="javascript:loadInPopUp('usuario?registrar')">Registrarse</a>
                 </li>
    <sec:authorize access="!isAuthenticated()">
                 <li>
                     <a href="javascript:loadInPopUp('login')">Ingresar</a>
                 </li>
       </sec:authorize>
	<sec:authorize access="isAuthenticated()">
	            <li class="dropdown">
	                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
	                    <i class="fa fa-user fa-fw"></i>  <i class="fa fa-caret-down"></i>
	                </a>
	                <ul class="dropdown-menu dropdown-user">
	                    <li><a href="#"><i class="fa fa-user fa-fw"></i><fmt:message key="menu.header.user.profile"/></a>
	                    </li>
	                    <li class="divider"></li>
	                    <li>
	                    	<a href="javascript:loadInMain('logout')"><i class="fa fa-sign-out fa-fw"></i><fmt:message key="menu.header.user.logout"/></a>
	                    </li>
	                </ul>
	            </li>
	</sec:authorize>
             </ul>
         </div>
         <!-- /.navbar-collapse -->
     </div>
     <!-- /.container -->
</nav>
<div id="page" class="container" style="width: 70%">
 	<jsp:include page="body.jsp" />
 	</div>
   <!-- /#page-content -->
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
<div class="modal fade" id="idModalPopUp" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content" id="idModalPopUpContent"></div>
	</div>
</div>

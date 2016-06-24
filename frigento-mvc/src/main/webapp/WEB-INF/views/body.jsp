<%@ include file="/WEB-INF/views/include.jsp" %>
<c:url var="pathBase" value="/" />
<!-- /.row -->
<sec:authorize access="hasRole('ROLE_USER')">
<div class="row">
    <div class="col-lg-4 col-md-6">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <div class="row">
                    <div class="col-xs-3">
                        <i class="fa fa-usd fa-5x"></i>
                    </div>
                    <div class="col-xs-9 text-right">
                        <div class="huge">26</div>
                        <div>Productos</div>
                    </div>
                </div>
            </div>
            <a href="#">
                <div class="panel-footer">
                    <span class="pull-left">Ver detalle</span>
                    <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                    <div class="clearfix"></div>
                </div>
            </a>
        </div>
    </div>
    <div class="col-lg-4 col-md-6">
        <div class="panel panel-green">
            <div class="panel-heading">
                <div class="row">
                    <div class="col-xs-3">
                        <i class="fa fa-shopping-cart fa-5x"></i>
                    </div>
                    <div class="col-xs-9 text-right">
                        <div class="huge">12</div>
                        <div>Pedidos</div>
                    </div>
                </div>
            </div>
            <a href="#">
                <div class="panel-footer">
                    <span class="pull-left">Ver Detalle</span>
                    <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                    <div class="clearfix"></div>
                </div>
            </a>
        </div>
    </div>
    <div class="col-lg-4 col-md-6">
        <div class="panel panel-yellow">
            <div class="panel-heading">
                <div class="row">
                    <div class="col-xs-3">
                        <i class="fa fa-user fa-5x"></i>
                    </div>
                    <div class="col-xs-9 text-right">
                        <div class="huge">124</div>
                        <div>Mis datos</div>
                    </div>
                </div>
            </div>
            <a href="login">
                <div class="panel-footer">
                    <span class="pull-left">Ver Detalle</span>
                    <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                    <div class="clearfix"></div>
                </div>
            </a>
        </div>
    </div>
</div>
</sec:authorize>
<!-- /.row -->
<div class="row">

<sec:authorize access="isAuthenticated()">
	<div class="col-md-3" >
		<%@ include file="/WEB-INF/views/menuSideBar.jsp" %>
    </div>
	<sec:authorize access="hasRole('ROLE_USER')">
		<c:set var="altoPantallaMax" value="80%"/>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN')">
		<c:set var="altoPantallaMax" value="100%"/>
	</sec:authorize>
    <c:set var="col" value="col-md-9" />
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
	<c:set var="altoPantallaMax" value="100%"/>
	<c:set var="col" value="col-md-12" />
</sec:authorize>
    <div class="${col}" id="page-content" style="height: ${altoPantallaMax}; overflow: auto">
	    	<div class="row carousel-holder">
			    <div class="col-md-12">
			        <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
			            <ol class="carousel-indicators">
			                <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
			                <li data-target="#carousel-example-generic" data-slide-to="1"></li>
			                <li data-target="#carousel-example-generic" data-slide-to="2"></li>
			            </ol>
			            <div class="carousel-inner">
			                <div class="item active">
			                    <img class="slide-image" src="<c:url value="/resources/images/Banner_1.jpg"/>" alt="">
			                </div>
			                <div class="item">
			                    <img class="slide-image" src="<c:url value="/resources/images/Banner_2.jpg"/>" alt="">
			                </div>
			                <div class="item">
			                    <img class="slide-image" src="<c:url value="/resources/images/Banner_3.jpg"/>" alt="">
			                </div>
			            </div>
			            <a class="left carousel-control" href="#carousel-example-generic" data-slide="prev">
			                <span class="glyphicon glyphicon-chevron-left"></span>
			            </a>
			            <a class="right carousel-control" href="#carousel-example-generic" data-slide="next">
			                <span class="glyphicon glyphicon-chevron-right"></span>
			            </a>
			        </div>
			    </div>
			</div>
			
			<div class="row">
			<c:forEach var="rpc" items="${rpcListOnline}" varStatus="i">
				<div class="col-sm-4 col-lg-4 col-md-4">
			        <div class="thumbnail">
			        	<c:if test="${not empty rpc.producto.imagen}">
			        		<c:set var="urlImagen" value="${pathBase}${rpc.producto.id}?imagen"/>
			        		<img src="${urlImagen}" alt="">
			        	</c:if>
			        	<c:if test="${empty rpc.producto.imagen}">
				        	<img src="<c:url value="/resources/images/productoSinFoto_chica.png"/>" alt="">
			        	</c:if>
			            <div class="caption">
			                <h5 class="pull-right" style="font-weight: bold;"><fmt:formatNumber currencySymbol="$" type="currency" value="${rpc.producto.costoActual + rpc.producto.costoActual*rpc.incremento/100 }" minFractionDigits="2" maxFractionDigits="2"/></h5>
			                <h5><a href="#">${rpc.producto.descripcionVenta }</a>
			                </h5>
			                <p>Acá prodría ir una descripción de elaboración quizás</p>
			            </div>
			            <div class="ratings">
			                <p class="pull-right">15 reviews</p>
			                <p>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                </p>
			            </div>
			        </div>
			    </div>
				
			</c:forEach>
			<%--
			    <div class="col-sm-4 col-lg-4 col-md-4">
			        <div class="thumbnail">
			            <img src="<c:url value="/resources/images/productoSinFoto_chica.png"/>" alt="">
			            <div class="caption">
			                <h4 class="pull-right">$24.99</h4>
			                <h4><a href="#">First Product</a>
			                </h4>
			                <p>See more snippets like this online store item at <a target="_blank" href="http://www.bootsnipp.com">Bootsnipp - http://bootsnipp.com</a>.</p>
			            </div>
			            <div class="ratings">
			                <p class="pull-right">15 reviews</p>
			                <p>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                </p>
			            </div>
			        </div>
			    </div>
			
			    <div class="col-sm-4 col-lg-4 col-md-4">
			        <div class="thumbnail">
			            <img src="<c:url value="/resources/images/productoSinFoto_chica.png"/>" alt="">
			            <div class="caption">
			                <h4 class="pull-right">$64.99</h4>
			                <h4><a href="#">Second Product</a>
			                </h4>
			                <p>This is a short description. Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
			            </div>
			            <div class="ratings">
			                <p class="pull-right">12 reviews</p>
			                <p>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star-empty"></span>
			                </p>
			            </div>
			        </div>
			    </div>
			
			    <div class="col-sm-4 col-lg-4 col-md-4">
			        <div class="thumbnail">
			            <img src="<c:url value="/resources/images/productoSinFoto_chica.png"/>" alt="">
			            <div class="caption">
			                <h4 class="pull-right">$74.99</h4>
			                <h4><a href="#">Third Product</a>
			                </h4>
			                <p>This is a short description. Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
			            </div>
			            <div class="ratings">
			                <p class="pull-right">31 reviews</p>
			                <p>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star-empty"></span>
			                </p>
			            </div>
			        </div>
			    </div>
			
			    <div class="col-sm-4 col-lg-4 col-md-4">
			        <div class="thumbnail">
			            <img src="<c:url value="/resources/images/productoSinFoto_chica.png"/>" alt="">
			            <div class="caption">
			                <h4 class="pull-right">$84.99</h4>
			                <h4><a href="#">Fourth Product</a>
			                </h4>
			                <p>This is a short description. Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
			            </div>
			            <div class="ratings">
			                <p class="pull-right">6 reviews</p>
			                <p>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star-empty"></span>
			                    <span class="glyphicon glyphicon-star-empty"></span>
			                </p>
			            </div>
			        </div>
			    </div>
			
			    <div class="col-sm-4 col-lg-4 col-md-4">
			        <div class="thumbnail">
			            <img src="<c:url value="/resources/images/productoSinFoto_chica.png"/>" alt="">
			            <div class="caption">
			                <h4 class="pull-right">$94.99</h4>
			                <h4><a href="#">Fifth Product</a>
			                </h4>
			                <p>This is a short description. Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
			            </div>
			            <div class="ratings">
			                <p class="pull-right">18 reviews</p>
			                <p>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star"></span>
			                    <span class="glyphicon glyphicon-star-empty"></span>
			                </p>
			            </div>
			        </div>
			    </div>
			
			    <div class="col-sm-4 col-lg-4 col-md-4">
			        <h4><a href="#">Like this template?</a>
			        </h4>
			        <p>If you like this template, then check out <a target="_blank" href="http://maxoffsky.com/code-blog/laravel-shop-tutorial-1-building-a-review-system/">this tutorial</a> on how to build a working review system for your online store!</p>
			        <a class="btn btn-primary" target="_blank" href="http://maxoffsky.com/code-blog/laravel-shop-tutorial-1-building-a-review-system/">View Tutorial</a>
			    </div>
				 --%>
			</div>
	</div>
</div>
<!-- /.row -->

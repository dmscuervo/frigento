<%@ include file="/WEB-INF/views/include.jsp" %>
<html >
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title><fmt:message key="app.title"/></title>
    
    <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/metisMenu.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/timeline.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/sb-admin-2.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/morris.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/font-awesome.min.css" />" rel="stylesheet">
    
    <script src="<c:url value="/resources/js/jquery.min.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
    <script src="<c:url value="/resources/js/metisMenu.min.js" />"></script>
    <script src="<c:url value="/resources/js/raphael-min.js" />"></script>
    <script src="<c:url value="/resources/js/morris.min.js" />"></script>
    <script src="<c:url value="/resources/js/morris-data.js" />"></script>
    <script src="<c:url value="/resources/js/sb-admin-2.js" />"></script>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <div id="wrapper">
			<jsp:include page="body.jsp" />
        <!-- /#page-content -->
    </div>
    <!-- /#wrapper -->
</body>
</html>

<%@ include file="/WEB-INF/views/include.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><fmt:message key="login.title" /></title>
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet" />
<script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<link href="<c:url value="/resources/css/font-awesome.min.css" />"
	rel="stylesheet" />
</head>

<body>
	<div style="width: 30%; margin: 0 auto; min-width: 300px">
		<p>
			<h2><fmt:message key="login.subtitle" /></h2>
		</p>
		<c:url var="loginUrl" value="/login" />
		<form action="${loginUrl}" method="post" class="form-horizontal">
			<p>
				<c:if test="${param.error != null}">
					<div class="alert alert-danger">${param.msg}</div>
				</c:if>
				<c:if test="${param.logout != null}">
					<div class="alert alert-success">
						<fmt:message key="login.logout.ok" />
					</div>
				</c:if>
			</p>
			<div class="input-group input-sm">
				<label class="input-group-addon" for="username"><i
					class="fa fa-user"></i></label> <input type="text" class="form-control"
					id="username" name="username"
					placeholder='<fmt:message key="login.username"/>' required>
			</div>
			<div class="input-group input-sm">
				<label class="input-group-addon" for="password"><i
					class="fa fa-lock"></i></label> <input type="password" class="form-control"
					id="password" name="password"
					placeholder='<fmt:message key="login.password"/>' required>
			</div>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />

			<div class="form-actions">
				<input type="submit" class="btn btn-block btn-primary btn-default"
					value="<fmt:message key="boton.ingresar"/>">
			</div>
		</form>
	</div>
</body>
</html>
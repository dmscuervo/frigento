<%@ include file="/WEB-INF/views/include.jsp" %>
<html>
<head>
<title>Login Page</title>
<style>
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}

#login-box {
	width: 300px;
	padding: 20px;
	margin: 100px auto;
	background: #fff;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 1px solid #000;
}
</style>
</head>
<body onload='document.loginForm.username.focus();'>

	<h1>Spring Security Custom Login Form (Annotation)</h1>

	<div id="login-box">

		<h2>Login with Username and Password</h2>

		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>

		<c:if test="${param.error != null}"> 
			<p>Invalid username / password</p>
		</c:if>
		<c:url var="loginUrl" value="/login"/>
		
		<form action="${loginUrl}" method="post">
			<p><label for="username">User:</label></p>
			<input type="text" id="username" name="username"/> 
		
			<p><label for="password">Password:</label></p>
			<input type="password" id="password" name="password"> 
		
			<div>
				<input name="submit" type="submit"/>
			</div>
		</form>
	</div>

</body>
</html>
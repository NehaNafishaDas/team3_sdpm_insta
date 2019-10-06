<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
	<title>Login</title>
	<link href="<c:url value="/WebContent/WEB-INF/jsp/login" />" rel="stylesheet">	
</head>
<body>

	<div id="body">
		<form class="login-form">
			<div class="insta-logo-type"></div>
			<input type="text" name="username" class="text-field username" placeholder="Username"><br/>
			<input type="password" name="password" class="text-field password" placeholder="Password"><br/>
			<input type="submit" name="login" class="def-button login" value="Login"><br/>
		</form>
		<div class="form-pointer">
			<p class="msg">Don't have an account? <a href="signUp" class="link">Sign up</a></p>
		</div>
	</div>
</body>
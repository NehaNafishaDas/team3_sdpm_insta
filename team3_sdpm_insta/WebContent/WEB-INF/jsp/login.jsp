<head>
	<title>Login</title>
	<link href="${pageContext.request.contextPath}/resources/style/login.css" rel="stylesheet" >
</head>
<body>
	
	<div id="logo">
		<img src="${pageContext.request.contextPath}/resources/images/insta_logo.png"/>
	</div>
	<div id="body">
		<form class="login-form" action="home_page" method="post">
			<div class="insta-logo-type"></div>
			<input type="hidden" name="err"></div>
			<input type="text" name="userName" class="text-field username" required="required" placeholder="Username or Mobile Number"><br/>
			<input type="password" name="password" class="text-field password" required="required" placeholder="Password"><br/>
			<button type="submit">Login</button>
		</form>
		<div class="form-pointer">
			<p class="msg">Don't have an account? <a href="signUp" class="link">Sign up</a></p>
		</div>
	</div>
</body>

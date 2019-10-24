<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign up to see photos and videos from your friends.</title>
<link href="${pageContext.request.contextPath}/resources/style/signup.css" rel="stylesheet" >
</head>
<body>
	<div id="body">
		<label>Please enter the following details:</label>
		<form class="signup-form" action="addUsers" method="post">
			<div class="insta-logo-type"></div>
			<input type="number" name="mobileNum" class="text-field mobile" placeholder="Mobile Number"><br/>
			<input type="text" name="email" class="text-field email" placeholder="Email"><br/>
			<input type="text" name="fullName" class="text-field Name" placeholder="Full Name"><br/>
			<input type="text" name="userName" class="text-field Username" placeholder="Username"><br/>
			<input type="password" name="password" class="text-field Password" placeholder="Password"><br/>
			<button type="submit">Sign Up</button>
		</form>
	</div>
</body>
</html>
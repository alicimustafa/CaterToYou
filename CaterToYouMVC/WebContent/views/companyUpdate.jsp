<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="../../../../favicon.ico">
<link href="css/bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Update Company Info</title>
</head>
<style>
ul {
	list-style-type: none;
	margin: 0;
	padding: 0;
	overflow: hidden;
	background-color: #333;
}

li {
	float: left;
	border-right: 1px solid #bbb;
}

li a {
	display: block;
	color: white;
	text-align: center;
	padding: 14px 16px;
	text-decoration: none;
}

li a:hover {
	background-color: #111;
}

html {
	background-image: url("css/food2.jpg");
	background-repeat: no-repeat;
	background-origin: inheret;
	background-size: 100%;
	background-color: blue;
}

body {
	background-color: darkblue;
	border: 2px solid white;
	color: white;
	
}

.stuff{
	min-width: 100px;
}
</style>
<body>
	<form action="editCompany.do" method="POST">

		<input type="hidden" name="id" value="${company.id}" /> <input
			type="hidden" name="addId" value="${address.id}" /><label class ="stuff">Name:</label> <input
			type="text" name="name" value="${company.name}" /><br /> <label class = "stuff">New
		Street: </label><input type="text" name="street" value="${address.street}" /><br />
		<label class ="stuff" >New Street2: </label><input type="text" name="street2"
			value="${address.street2}" /><br /><label class="stuff">New City: </label><input type="text"
			name="city" value="${address.city}" /><br /><label class = "stuff">New State:</label> <input
			type="text" name="state" value="${address.state}" /><br /> <label class="stuff">New Zip:</label>
		<input type="text" name="zip" value="${address.zip}" /><br /> <label class = "stuff">Image
		URL: </label>l<input type="text" name="url" value="${image.imageUrl}" /><br />
		<input type="submit" value="Edit Company Profile" />
	</form>
	<form action="UpdateMenuItem.do" method="POST">
		Update Menu Item: <select name="itemId">
			<c:forEach items="${menu}" var="item">
				<option value="${item.id}">${item.name}</option>
			</c:forEach>
		</select> <input type="submit" value="Update Menu Item" />
	</form>
	<form action="UpdateStaff.do" method="POST">
		Update Staff: <select name="staffId">
			<c:forEach items="${staff}" var="user">
				<option value="${user.id}">${user.lastName} ${user.firstName}</option>
			</c:forEach>
		</select> <input type="submit" value="Update Employee" />
	</form>
	<form action="ActivateEmployee.do" method="POST">
	Inactive Employees: <select name="inactiveId">
			<c:forEach items="${inactiveStaff}" var="user">
				<option value="${user.id}">${user.lastName}, ${user.firstName} </option>
			</c:forEach></select>
		<input type="submit" value="Make Employee Active" />
	</form>

	<form action="company.do" method="GET">
		<input type="submit" value="Return Home" />
	</form>

	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
		integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
		crossorigin="anonymous"></script>
	<script>
		window.jQuery
				|| document.write('<script src="js/jquery.min.js"><\/script>')
	</script>

	<script src="js/holder.js"></script>

	<script src="js/bootstrap.min.js"></script>
</body>
</html>



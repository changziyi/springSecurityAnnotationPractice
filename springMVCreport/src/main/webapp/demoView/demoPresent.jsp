<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>springMVC demo</title>
</head>
<link rel="stylesheet" href="<c:url value='/css/formCss.css'/>">
<!-- -------------------------------jquery------------------------------- -->
<script type="text/JavaScript"
	src='<c:url value="/js/jQuery/jquery-3.2.1.min.js"/>'></script>
<!-- ----------------------------end jquery------------------------------ -->
<body>
	<!-- 	____________________________form____________________________ -->
		<table class="table2">
			<tr>
				<td>my name</td>
				<td>${myName}</td>
			</tr>
			<tr>
				<td>my job</td>
				<td>${myJob}</td>
			</tr>
		</table>
		<img class="image" alt="defalut" src="<c:url value='/com/formDemo/handler/DemoHandler/getImage.do?name=${myName}'/>">
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
	<form id='formDemo' method="post"
		action="<c:url value='/com/formDemo/handler/DemoHandler/getFormData.do'/>"
		enctype="multipart/form-data">
		<table class="table1">
			<tr>
				<td >name</td>
				<td><input id="name" type="text" name="name"></td>
			</tr>
			<tr>
				<td >job</td>
				<td><input id="job" type="text" name="job"></td>
			</tr>
		</table>
		<div>image upload</div>
		<input type="file" name="file" id="imgFile"> <input
			class="submit" type="button" value="1111111">
	</form>
	<div id="ajaxSubmit">ajax submit</div>
</body>
<script>
	var data;
	$(function() {
		$('#imgFile').on('change', fileChange);
		$('.submit').on('click', uploadImage);
		$('#ajaxSubmit').on('click', ajaxSubmit);
	})
	function ajaxSubmit() {
		$.ajax({
			type : "POST",
			// 					enctype : 'multipart/form-data',
			url : "${pageContext.request.contextPath}/com/formDemo/handler/DemoHandler/getFormData.do",
			data:
			{name : $('#name').text(),
			job:$('#job').text()},
			success : function(data) {
// 				console.log(data);
// 				location.href = "${pageContext.request.contextPath}/demoView/demoPresent.jsp"
			}
		});//end post
	}
	function fileChange() {
		// 		files = e.target.files;
		data = new FormData($('#formDemo')[0]);
	}
	function uploadImage() {
		$
				.ajax({
					type : "POST",
					// 					enctype : 'multipart/form-data',
					url : "${pageContext.request.contextPath}/com/formDemo/handler/DemoHandler/processUpload.do",
					name : $(':input[name="name"]').val(),
					data : data,
					processData : false,
					contentType : false,
					cache : false,
					// 					timeout : 600000,
					success : function(data) {
						alert(data);
					}
				});//end post
		$('#formDemo').submit();
	}
</script>
</html>
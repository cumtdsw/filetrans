<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form method="get" action="<c:url value="/task/query"/>">
		<table>
			<tr>
				<td>id：</td>
				<td><input type="text" name="id" /></td>
			</tr>
			<tr>
				<td>status：</td>
				<td><input type="text" name="status" /></td>
			</tr>
			<tr>
				<td>dataType：</td>
				<td><input type="text" name="dataType"/></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" name="查询" /></td>
			</tr>
		</table>
	</form>
</body>
</html>
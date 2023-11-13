<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Client Portal</title>
</head>
<body>



<div align="center">
    <div class="center">
        <h2>Client Portal</h2>
    </div>

	<div class="center">
	    <h2>Submit a Quote Request</h2>
	    <form action="registerQuote" method="post">
	        <table border="1">
	            <tr>
	                <th>Initial Price:</th>
	                <td align="center">
	                    <input type="text" name="initialPrice" size="45" value="" onfocus="this.value='';">
	                </td>
	            </tr>
	            <tr>
	                <th>Time Window:</th>
	                <td align="center">
	                    <input type="text" name="timeWindow" size="45" value="" onfocus="this.value='';">
	                </td>
	            </tr>
	            <tr>
	                <th>Note:</th>
	                <td align="center">
	                    <input type="text" name="note" size="45" value="" onfocus="this.value='';">
	                </td>
	            </tr>
	            <tr>
	                <td align="center" colspan="2">
	                    <input type="submit" value="Register a Quote">
	                </td>
	            </tr>
	        </table>
	    </form>
	</div>
<center>
        <a href="login.jsp" target="_self">Logout</a><br><br>
    </center>
	<center>
        <a href="current.jsp" target="_self">View Quote Requests</a><br><br>
    </center>
</div>

</body>
</html>
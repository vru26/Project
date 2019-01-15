<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>EMI Calculator</title>
</head>
<body>
	<form:form method="POST" action="calculator" modelAttribute="calculatorValues" autocomplete="off">
		<table>
			<tr>
			<!-- To avoid confusion as to whether a radio button group is required or not, authors are encouraged to
			 specify the attribute on all the radio buttons in a group. (Source w3.org) Otherwise specifying "required"
			 for one buttongroup member is sufficient.-->
				<td><form:label path="vehicleType" >Vehicle Type:</form:label></td>
				<td>
					<input type="radio" id = "vehicleType1" name="vehicleType" value="Bike" required>Bike
					<input type="radio" name="vehicleType" id = "vehicleType2" value="Car" required>Car
				</td>
			</tr>
			<tr>
				<td><form:label path="loanAmount">Loan Amount:</form:label></td>
				<td><input type="number" name="loanAmount" id="loanAmount" required onfocusout="validateAmount();"
				placeholder="Please Enter Loan Amount">
			</tr>
			<tr>
				<td><form:label path="exShowRoomPrice">ex-ShowRoom Price:</form:label></td>
				<td><input type="number" name="exShowRoomPrice" required placeholder="Please Enter ex Showroom Price">
			</tr>
			<tr>
				<td><form:label path="rateOfInterest">Rate Of Interest:</form:label></td>
				<td><input type="number" name="rateOfInterest" required placeholder="Please Enter Rate of Interest">
			</tr>
			<tr>
				<td><form:label path="tenure">Tenure (in months):</form:label></td>
				<td><input type="number" name="tenure" required min="24" placeholder="Please Enter the Loan Duration">
			</tr>
			<tr>
				<td colspan="1" align="center"><input type="submit" value="Calculate" onclick="check();"/></td>
				<td colspan="1" align="center"><input type="reset" value="Clear" /></td>
			</tr>
		</table>
	</form:form>
	<script>
			function validateAmount() {
				
				//fetching the current value of loan amount textbox
				var loanAmt = document.getElementById('loanAmount').value;
				
				//checking if its null.
				if(loanAmt != "") {
					
					//fetching all the form elements having the specified name in this case radio buttons
					var type = document.getElementsByName('vehicleType');
					var value;
					
					//iterating over all the fetched radio button elememts to find out which radio button has been selected.
					for(var i = 0; i < type.length; i++){
					    if(type[i].checked){
					        value = type[i].value;
					        break;
					    }
					}
					if(value=='Bike') {
						if(loanAmt < 75000)
							console.log("less");
					}
					if(value=='Car') {
						if(loanAmt < 300000)
							console.log("less car");
					}
				}
			}
	</script>
</body>
</html>
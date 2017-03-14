<?php
	require_once("./configDatabase.php");

	$userID = mysqli_real_escape_string($mysqli, $_POST["userID"]);
	$userPW = mysqli_real_escape_string($mysqli, $_POST["userPW"]);
	$userName = mysqli_real_escape_string($mysqli, $_POST["userName"]);
	$userPhoneNumber = mysqli_real_escape_string($mysqli, $_POST["userPhoneNumber"]);
	$userMailAddress = mysqli_real_escape_string($mysqli, $_POST["userMailAddress"]);

	$statement = mysqli_prepare($mysqli, "UPDATE user SET userPW=?, userName=?, userPhoneNumber=?, userMailAddress=? WHERE userID=?");
	if(!$statement) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql statement error: '.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($statement, "sssss", $userPW, $userName, $userPhoneNumber, $userMailAddress, $userID);
	if(!mysqli_stmt_execute($statement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt statement execute err: '.mysqli_stmt_error($statement));
	}

	echo json_encode(array('resultCode'=>'1'));
	mysqli_stmt_close($statement);
	mysqli_close($mysqli);	
?>
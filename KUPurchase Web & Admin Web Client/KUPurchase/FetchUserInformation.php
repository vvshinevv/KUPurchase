<?php
	require_once("./configDatabase.php");

	if(!isset($_POST['userID'])) {
		echo json_encode(array('resultCode'=>'0'));
		die();
	}
	if(!isset($_POST['userPW'])) {
		echo json_encode(array('resultCode'=>'0'));
		die();
	}

	$userID = mysqli_real_escape_string($mysqli, $_POST['userID']);
	$userPW = mysqli_real_escape_string($mysqli, $_POST['userPW']);

	
	$statement = mysqli_prepare($mysqli, "SELECT * FROM user WHERE userID=? AND userPW=?");
	if(!$statement) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql statement error: '.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($statement, "ss", $userID, $userPW);
	if(!mysqli_stmt_execute($statement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt statement execute err: '.mysqli_stmt_error($statement));
	}

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $userManagementCode, $userID, $userPW, $userName, $userPhoneNumber, $userMailAddress);

	$user = array();
	$resultCode = "1";

	while(mysqli_stmt_fetch($statement)) {
		$user['resultCode'] = $resultCode;
		$user['userManagementCode'] = $userManagementCode;
		$user['userID'] = $userID;
		$user['userPW'] = $userPW;
		$user['userName'] = $userName;
		$user['userPhoneNumber'] = $userPhoneNumber;
		$user['userMailAddress'] = $userMailAddress;
	}


	echo json_encode($user);
	mysqli_stmt_close($statement);
	mysqli_close($mysqli);
?>
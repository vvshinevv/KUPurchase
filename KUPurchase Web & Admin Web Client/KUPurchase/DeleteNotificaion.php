<?php
	require_once("./configDatabase.php");


	$noticeManagementCode = mysqli_real_escape_string($mysqli, $_GET['id']);

	$statement = mysqli_prepare($mysqli, "DELETE FROM notification WHERE noticeManagementCode=?");
	if(!$statement) {
		echo json_encode(array('resultCode'=>'-1'));
		die('statement query err: '.mysqli_stmt_error($mysqli));
	}
	mysqli_stmt_bind_param($statement, "i", $noticeManagementCode);
	if(!mysqli_stmt_execute($statement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt statement execute err: '.mysqli_stmt_error($statement));
	}
	header("Location:"."notification.php");

	mysqli_stmt_close($statement);
	mysqli_close($mysqli);
	die();
?>
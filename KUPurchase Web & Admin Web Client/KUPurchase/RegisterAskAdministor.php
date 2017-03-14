<?php
	require_once("./configDatabase.php");

	$askTitle = mysqli_real_escape_string($mysqli, $_POST['askTitle']);
	$fromUserID = mysqli_real_escape_string($mysqli, $_POST['fromUserID']);
	$askContents = mysqli_real_escape_string($mysqli, $_POST['askContents']);

	$askDate = date('Y-m-d H:i:s', time());

	$statement = mysqli_prepare($mysqli, "INSERT INTO askAdministor (askDate, askTitle, askContents, fromUserID) VALUES (?, ?, ?, ?)");
	if( !$statement ) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysqli error: '.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($statement, "ssss", $askDate, $askTitle, $askContents, $fromUserID);
	if( !mysqli_stmt_execute($statement) ) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt err: '.mysqli_stmt_error($statement));
	}

	echo json_encode(array('resultCode'=>'1'));
	mysqli_stmt_close($statement);
	mysqli_close($mysqli);
	die();
?>
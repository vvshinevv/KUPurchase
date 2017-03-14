<?php
	require_once("./configDatabase.php");

	$noticeOfTitle = mysqli_real_escape_string($mysqli, $_POST['noticeName']);
	$noticeOfUploadDate = mysqli_real_escape_string($mysqli, $_POST['noticeOfUploadDate']);
	$noticeOfContents = mysqli_real_escape_string($mysqli, $_POST['noticeContent']);

	header("Location:"."Notification.php");

	$statement = mysqli_prepare($mysqli, "INSERT INTO notification (noticeOfTitle, noticeOfContents, noticeOfUploadDate) VALUES (?, ?, ?)");
	if( !$statement ) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysqli error: '.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($statement, "sss", $noticeOfTitle, $noticeOfContents, $noticeOfUploadDate);
	if( !mysqli_stmt_execute($statement) ) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt err: '.mysqli_stmt_error($statement));
	}

	echo json_encode(array('resultCode'=>'1'));
	mysqli_stmt_close($statement);
	mysqli_close($mysqli);
	die();
?>
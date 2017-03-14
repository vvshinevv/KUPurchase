<?php
	require_once("./configDatabase.php");

	$noticeManagementCode = mysqli_real_escape_string($mysqli, $_GET['id']);
	$noticeOfContents = mysqli_real_escape_string($mysqli, $_POST['noticeContent']);
	$noticeOfTitle = mysqli_real_escape_string($mysqli, $_POST['noticeName']);
	$noticeOfUploadDate = mysqli_real_escape_string($mysqli, $_POST['noticeOfUploadDate']);
	
	$statement = mysqli_prepare($mysqli, "UPDATE notification SET noticeOfContents=?, noticeOfTitle=?, noticeOfUploadDate=? WHERE noticeManagementCode=?");
	if(!$statement) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql statement error: '.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($statement, "sssi", $noticeOfContents, $noticeOfTitle, $noticeOfUploadDate, $noticeManagementCode);
	if(!mysqli_stmt_execute($statement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt statement execute err: '.mysqli_stmt_error($statement));
	}

	header("Location:"."contentsOfNotification.php?id=$noticeManagementCode");
	
	mysqli_stmt_close($statement);
	mysqli_close($mysqli);	
	
	die();
?>
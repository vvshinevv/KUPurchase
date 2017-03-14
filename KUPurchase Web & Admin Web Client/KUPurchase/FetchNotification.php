<?php
	require_once("./configDatabase.php");

	$statement = mysqli_prepare($mysqli, "SELECT * FROM notification");
	if(!$statement) {
		echo json_encode(array('resultCode'=>'-1'));
		die("statement query error: ".mysqli_error($mysqli));
	}

	if(!mysqli_stmt_execute($statement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die("statement execute error: ".mysqli_stmt_error($statement));
	}

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $noticeCode ,$noticeOfTitle, $noticeOfContents, $noticeOfUploadDate);

	$notice = array();
	$count = 0 ;

	while(mysqli_stmt_fetch($statement)) {
		$notice['resultCode'] = "1";
		$notice[$count] = array();
		$notice[$count]['noticeOfTitle'] = $noticeOfTitle;
		$notice[$count]['noticeOfContents'] = $noticeOfContents;
		$notice[$count]['noticeOfUploadDate'] = $noticeOfUploadDate;
		$count++;
	}
	echo json_encode($notice);
	mysqli_stmt_close($statement);
	mysqli_close($mysqli);




?>
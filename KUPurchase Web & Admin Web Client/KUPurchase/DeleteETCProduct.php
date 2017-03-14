<?php
	require_once("./configDatabase.php");

	$productManagementCode = mysqli_real_escape_string($mysqli, $_POST["productManagementCode"]); 

	$pictureStatement = mysqli_prepare($mysqli, "SELECT productImageURL FROM etcProductList WHERE productManagementCode=?");
	if(!$pictureStatement) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql pictureStatement error: '.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($pictureStatement, "i", $productManagementCode);

	if(!mysqli_stmt_execute($pictureStatement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql pictureStatement execute error: '.mysqli_stmt_error($pictureStatement));
	}

	mysqli_stmt_store_result($pictureStatement);
	mysqli_stmt_bind_result($pictureStatement, $productImageURL);

	while(mysqli_stmt_fetch($pictureStatement)) {
		$tempImageURL = $productImageURL;
	}

	$imageURL = substr($tempImageURL, 31);
	unlink($imageURL);

	mysqli_stmt_close($pictureStatement);


	$statement = mysqli_prepare($mysqli, "DELETE FROM etcProductList WHERE productManagementCode=?");

	if(!$statement) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql statement error: '.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($statement, "i", $productManagementCode);
	if(!mysqli_stmt_execute($statement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql statement execute error: '.mysqli_stmt_error($statement));
	}

	echo json_encode(array('resultCode'=>'1'));
	mysqli_stmt_close($statement);
	mysqli_close($mysqli);
?>
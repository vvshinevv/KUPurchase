<?php
	require_once("./configDatabase.php");

	$statement = mysqli_prepare($mysqli, "SELECT * FROM kuProductsList");
	if(!$statement) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql statement query error: '.mysqli_error($mysqli));
	}

	if(!mysqli_stmt_execute($statement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql statement execute error: '.mysqli_error($statement));
	}

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $productManagementCode, $productExpireDateOfPurchase, $productDepositStartDateOfPurchase, $productDepositDueDateOfPurchase, $productName, $productPrice, $productPictureUrlAddress);

	$kuProduct = array();
	$count = 0;

	while(mysqli_stmt_fetch($statement)) {
		$kuProduct['resultCode'] = "1";
		$kuProduct[$count] = array();
		$kuProduct[$count]['productManagementCode'] = $productManagementCode;
		$kuProduct[$count]['productExpireDateOfPurchase'] = $productExpireDateOfPurchase;
		$kuProduct[$count]['productDepositStartDateOfPurchase'] = $productDepositStartDateOfPurchase;
		$kuProduct[$count]['productDepositDueDateOfPurchase'] = $productDepositDueDateOfPurchase;
		$kuProduct[$count]['productName'] = $productName;
		$kuProduct[$count]['productPrice'] = $productPrice;
		$kuProduct[$count]['productPictureUrlAddress'] = $productPictureUrlAddress;
		$count++;
	}

	echo json_encode($kuProduct, true);
	mysqli_stmt_close($statement);
	mysqli_close($mysqli);
?>
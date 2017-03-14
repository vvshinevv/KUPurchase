<?php
	require_once("./configDatabase.php");

	$userManagementCode = mysqli_real_escape_string($mysqli, $_POST['userManagementCode']);

	$statement = mysqli_prepare($mysqli, "SELECT * FROM userWhoPurchaseKuProducts");
	if(!$statement) {
		echo json_encode(array('resultCode'=>'-1'));
		die("statement query error: ".mysqli_error($mysqli));
	}
	if(!mysqli_stmt_execute($statement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die("statement execute error: ".mysqli_stmt_error($statement));
	}

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $autoIncrement, $userCode, $productPurchaseDate, $productManagementCode, $productName, $productPrice, $productCount, $productDepositState);

	$purchasedProducts=array();
	$count = 0;
	while(mysqli_stmt_fetch($statement)) {
		$purchasedProducts['resultCode'] = "1";
		$purchasedProducts[$count] = array();
		$purchasedProducts[$count]['autoIncrement'] = $autoIncrement;
		$purchasedProducts[$count]['productPurchaseDate'] = $productPurchaseDate;
		$purchasedProducts[$count]['productName'] = $productName;
		$purchasedProducts[$count]['productPrice'] = $productPrice;
		$purchasedProducts[$count]['productCount'] = $productCount;
		$purchasedProducts[$count]['productDepositState'] = $productDepositState;
		$count++;
	}

	echo json_encode($purchasedProducts, true);
	mysqli_stmt_close($statement);
	mysqli_close($mysqli);
?>
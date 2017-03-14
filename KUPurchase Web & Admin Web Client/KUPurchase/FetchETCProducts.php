<?php
	require_once("./configDatabase.php");

	$statement = mysqli_prepare($mysqli, "SELECT * FROM etcProductList");
	if(!$statement) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql statement error: '.mysqli_error($mysqli));
	}
	if(!mysqli_stmt_execute($statement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt statement execute err: '.mysqli_stmt_error($statement));
	}
	
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $productManagementCode, $uploadProductsDate, $userManagementCode, $userID, $titleOfETCProduct, $productDetailInform, $productExpireDateOfPurchase, $productImageURL);
	
	$products = array();
	$resultCode = "1";
	$count = 0;

	while(mysqli_stmt_fetch($statement)) {
		$products['resultCode'] = $resultCode;
		$products[$count] = array();
		$products[$count]['productManagementCode'] = $productManagementCode;
		$products[$count]['uploadProductsDate'] = $uploadProductsDate;
		$products[$count]['userManagementCode'] = $userManagementCode;
		$products[$count]['userID'] = $userID;
		$products[$count]['titleOfETCProduct'] = $titleOfETCProduct;
		$products[$count]['productDetailInform'] = $productDetailInform;
		$products[$count]['productExpireDateOfPurchase'] = $productExpireDateOfPurchase;
		$products[$count]['productImageURL'] = $productImageURL;
		$count++;
	}

	echo json_encode($products);
	mysqli_stmt_close($statement);
	mysqli_close($mysqli);
?>
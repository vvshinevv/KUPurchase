<?php
	require_once("./configDatabase.php");

	$userID = mysqli_real_escape_string($mysqli, $_POST["userID"]);
	$titleOfETCProduct = mysqli_real_escape_string($mysqli, $_POST["titleOfETCProduct"]);
	$productDetailInform = mysqli_real_escape_string($mysqli, $_POST["productDetailInform"]);
	$productExpireDateOfPurchase = mysqli_real_escape_string($mysqli, $_POST["productExpireDateOfPurchase"]);
	$image = $_POST["image"];

	$time = strtotime("now");
	$decodedImage = base64_decode("$image");
	
	file_put_contents("ETCProductImages/".$userID.$time.".jpg", $decodedImage);

	$uploadProductsDate = date("Y-m-d");

	$productImageURL = SERVER_ADDRESS."ETCProductImages/".$userID.$time.".jpg";

	$userManagementCodeStatement = mysqli_prepare($mysqli, "SELECT userManagementCode FROM User WHERE userID=?");
	if(!$userManagementCodeStatement) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql userManagementCodeStatement error: '.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($userManagementCodeStatement, "s", $userID);
	if(!mysqli_stmt_execute($userManagementCodeStatement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt userManagementCodeStatement execute err: '.mysqli_stmt_error($userManagementCodeStatement));
	}
	mysqli_stmt_store_result($userManagementCodeStatement);
	mysqli_stmt_bind_result($userManagementCodeStatement, $userCode);

	$userManagementCode = "";
	while(mysqli_stmt_fetch($userManagementCodeStatement)) {
		$userManagementCode = $userCode;
	}
	mysqli_stmt_close($userManagementCodeStatement);

	$statement = mysqli_prepare($mysqli, "INSERT INTO etcProductList (uploadProductsDate, userManagementCode, userID, titleOfETCProduct, productDetailInform, productExpireDateOfPurchase, productImageURL) VALUES (?, ?, ?, ?, ?, ?, ?)");
	if(!$statement) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql statement error: '.mysqli_error($mysqli));
	}

	mysqli_stmt_bind_param($statement, "sisssss", $uploadProductsDate, $userManagementCode, $userID, $titleOfETCProduct, $productDetailInform, $productExpireDateOfPurchase, $productImageURL);
	if(!mysqli_stmt_execute($statement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt statement execute err: '.mysqli_stmt_error($statement));
	}

	echo json_encode(array('resultCode'=>'1'));
	mysqli_stmt_close($statement);
	mysqli_close($mysqli);
?>
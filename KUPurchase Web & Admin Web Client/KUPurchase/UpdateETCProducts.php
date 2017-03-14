<?php
	require_once("./configDatabase.php");

	$productManagementCode = mysqli_real_escape_string($mysqli, $_POST["productManagementCode"]); 
	$titleOfETCProduct = mysqli_real_escape_string($mysqli, $_POST["titleOfETCProduct"]);
	$productDetailInform = mysqli_real_escape_string($mysqli, $_POST["productDetailInform"]);
	$uploadProductsDate = mysqli_real_escape_string($mysqli, $_POST["uploadProductsDate"]);
	$productExpireDateOfPurchase = mysqli_real_escape_string($mysqli, $_POST["productExpireDateOfPurchase"]);
	$productImagePath = mysqli_real_escape_string($mysqli, $_POST["imagePath"]);

	$image = $_POST["image"];

	$decodedImage = base64_decode("$image");
	$temp = substr($productImagePath, 48);
	echo $temp;

	file_put_contents("ETCProductImages/".$temp, $decodedImage);

	$statement = mysqli_prepare($mysqli, "UPDATE etcProductList SET uploadProductsDate=?, titleOfETCProduct=?, productDetailInform=?, productExpireDateOfPurchase=? WHERE productManagementCode=?");

	if(!$statement) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysql statement error: '.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($statement, "sssss", $uploadProductsDate, $titleOfETCProduct, $productDetailInform, $productExpireDateOfPurchase, $productManagementCode);
	if(!mysqli_stmt_execute($statement)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt statement execute err: '.mysqli_stmt_error($statement));
	}

	echo json_encode(array('resultCode'=>'1'));
	mysqli_stmt_close($statement);
	mysqli_close($mysqli);	
?>
<?php
	require_once("./configDatabase.php");
?>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/earlyaccess/hanna.css">
	<link rel="stylesheet" type="text/css" href="CSSPage.css" >
	<title>KU공동구매 상품 수정중...</title>
</head>
<body bgcolor="#a9a9a9">	
<?php
	$productManagementCode = mysqli_real_escape_string($mysqli, $_POST['productManagementCode']);
	$expireDate = mysqli_real_escape_string($mysqli, $_POST['expireDate']);
	$startDepositDate = mysqli_real_escape_string($mysqli, $_POST['startDepositDate']);
	$EndDepositDate = mysqli_real_escape_string($mysqli, $_POST['EndDepositDate']);
	$productName = mysqli_real_escape_string($mysqli, $_POST['productName']);
	$productPrice = mysqli_real_escape_string($mysqli, $_POST['productPrice']);
	$fileToUpload = mysqli_real_escape_string($mysqli, $_FILES['fileToUpload']['name']);
	//////////사진 링크 DB로 부터 따오기
	$state = mysqli_prepare($mysqli, "SELECT productPictureUrlAddress FROM kuProductsList WHERE productManagementCode=?");

	if( !$state ) {
		die('mysqli error: '.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($state, "i", $productManagementCode);
	if(!mysqli_stmt_execute($state)) {
		die('stmt execute err: '.mysqli_stmt_error($state));
	}
	mysqli_stmt_store_result($state);
	mysqli_stmt_bind_result($state, $productCode);

	while(mysqli_stmt_fetch($state)) {
		$temp = $productCode;
	}
	//사진 링크 저장 장소 path; path = "SimpleTest/사진 주소"
	$path = "kuPurchase/".$temp;

	//현재 시간
	//$rtime = date("Y:m:d:H:i:s", time());

	$target_dir="Pictures/";
	$target_file = $target_dir . basename($_FILES["fileToUpload"]["name"]);
	
	$uploadOk = 1;
	$imageFileType = pathinfo($target_file,PATHINFO_EXTENSION);
	// Check if image file is a actual image or fake image
	if(isset($_POST["submit"])) {
    	$check = getimagesize($_FILES["fileToUpload"]["tmp_name"]);
    	if($check !== false) {
       		echo "File is an image - " . $check["mime"] . ".";
       		$uploadOk = 1;
    	} else {
    		header("Refresh: 3; UpdateFormKuProduct.php");
			echo "<p class='maintitle'>이미지 파일이 아닙니다.<br/>
				이전 페이지로 자동으로 이동합니다...</p>";
       		echo "File is not an image.";
        	$uploadOk = 0;
    	}
	}
	// Check if file already exists
	echo "<br/>";
	echo $target_file. "와 ". $temp;
	echo "<br/>";

	if (file_exists($target_file)) {
		if(!(strcmp($target_file, $temp))) {
			$uploadOk = 1;
		} else {
			header("Refresh: 5; UpdateFormKuProduct.php");
			echo "<p class='maintitle'>이미지 파일명을 바꿔주세요!<br/>
				잠시후 이전 페이지로 이동합니다...</p>";
	    	$uploadOk = 0;
    	}
	} else {
		unlink($temp);
	}
	// Check file size
	if ($_FILES["fileToUpload"]["size"] > 500000) {
		header("Refresh: 3; UpdateFormKuProduct.php");
    	echo "<h1>이미지 파일 크기가 너무 큽니다.<br/>
			잠시후 이전 페이지로 이동합니다...</h1>";
    	$uploadOk = 0;
	}
	// Allow certain file formats
	if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
	&& $imageFileType != "gif" ) {
		echo "<h1>JPG, JPEG, PNG, GIF 확장자만 가능합니다.<br/>
			이전 페이지로 자동으로 이동합니다...</h1>";
		header("Refresh: 3; UpdateFormKuProduct.php");

    	$uploadOk = 0;
	}
	// Check if $uploadOk is set to 0 by an error
	if ($uploadOk == 0) {
    	//echo "Sorry, your file was not uploaded.";
	// if everything is ok, try to upload file
	} else {
    	if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
    		header("Location:"."KuProductList.php");
    		echo $_FILES["fileToUpload"]["tmp_name"];
        	echo "The file ". basename( $_FILES["fileToUpload"]["name"]). " has been uploaded.";
        	$statement = mysqli_prepare($mysqli, "UPDATE kuProductsList SET productExpireDateOfPurchase=?, productDepositStartDateOfPurchase=?, productDepositDueDateOfPurchase=?, productName=?, productPrice=?, productPictureUrlAddress=? WHERE productManagementCode=?");
	
			if( !$statement ) {
				die('mysqli error: '.mysqli_error($mysqli));
			}
			mysqli_stmt_bind_param($statement, "ssssisi", $expireDate, $startDepositDate, $EndDepositDate, $productName, $productPrice, $target_file, $productManagementCode);

			if( !mysqli_stmt_execute($statement) ) {
				die('stmt err: '.mysqli_stmt_error($statement));
			}
			////계산서 수정
			$statementCaculSelect = mysqli_prepare($mysqli, "SELECT productTotalCount FROM caculateTotalPriceAndCount WHERE productManagementCode=?");
			if(!$statementCaculSelect) {
				die("statementCaculSelect query err: ".mysqli_error($mysqli));
			}
			mysqli_stmt_bind_param($statementCaculSelect, "i", $productManagementCode);

			if(!mysqli_stmt_execute($statementCaculSelect)) {
				die("statementCaculSelect execute err: ".mysqli_stmt_error($statementCaculSelect));
			}
			mysqli_stmt_store_result($statementCaculSelect);
			mysqli_stmt_bind_result($statementCaculSelect, $productTotalCount);

			while(mysqli_stmt_fetch($statementCaculSelect)) {
				$productTotalCount1 = $productTotalCount;
			}

			$productTotalPrice = $productPrice * $productTotalCount1;

			$statementCacul = mysqli_prepare($mysqli, "UPDATE caculateTotalPriceAndCount SET productName=?, productPrice=?, productTotalPrice=? WHERE productManagementCode=?");
			if(!$statementCacul){
				die('mysqli error: '.mysqli_error($mysqli));
			}
			mysqli_stmt_bind_param($statementCacul, "siii", $productName, $productPrice, $productTotalPrice, $productManagementCode);

			if( !mysqli_stmt_execute($statementCacul)) {
				die('stmt err: '.mysqli_stmt_error($statementCacul));
			}
			////계산서 수정

			////user 수정
			$statementUserPurchaseUpdate = mysqli_prepare($mysqli, "UPDATE userWhoPurchaseKuProducts SET productName=?, productPrice=? WHERE productManagementCode=?");
			if(!$statementUserPurchaseUpdate) {
				die('statementUserPurchaseUpdate query err: '.mysqli_error($mysqli));
			}
			mysqli_stmt_bind_param($statementUserPurchaseUpdate, "sii", $productName, $productPrice, $productManagementCode);
			if(!mysqli_stmt_execute($statementUserPurchaseUpdate)) {
				die('statementUserPurchaseUpdate execute err: '.mysqli_stmt_error($statementUserPurchaseUpdate));
			}
        	mysqli_stmt_close($statementUserPurchaseUpdate);
			mysqli_stmt_close($statement);
			mysqli_stmt_close($statementCacul);
    	} else {
    		//header("Location:"."RegisterFormKuProductList.php");
        	//echo "Sorry, there was an error uploading your file.";
    	}
	}
	
	
	////user 수정

	
	mysqli_close($mysqli);
	die();
?>
</body>
</html>
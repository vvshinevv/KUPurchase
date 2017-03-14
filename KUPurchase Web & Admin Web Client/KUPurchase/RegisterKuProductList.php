<?php
	require_once("./configDatabase.php");
?>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/earlyaccess/hanna.css">
	<link rel="stylesheet" type="text/css" href="CSSPage.css" >
	<title>KU공동구매 상품 등록중...</title>
</head>
<body bgcolor="#a9a9a9">	
<?php
	
	$expireDate = mysqli_real_escape_string($mysqli, $_POST['expireDate']);
	$startDepositDate = mysqli_real_escape_string($mysqli, $_POST['startDepositDate']);
	$EndDepositDate = mysqli_real_escape_string($mysqli, $_POST['EndDepositDate']);
	$productName = mysqli_real_escape_string($mysqli, $_POST['productName']);
	$productPrice = mysqli_real_escape_string($mysqli, $_POST['productPrice']);
	$fileToUpload = mysqli_real_escape_string($mysqli, $_FILES['fileToUpload']['name']);

	//echo $fileToUpload;
	//현재 시간
	$rtime = date("Y:m:d:H:i:s", time());

	$target_dir="Pictures/";
	$target_file = $target_dir . basename($_FILES["fileToUpload"]["name"]);
	echo "27:"; echo $target_file; echo "<br/>"; 
	$uploadOk = 1;
	$imageFileType = pathinfo($target_file,PATHINFO_EXTENSION);
	echo "30:"; echo $imageFileType; echo "<br/>"; //jpg
	// Check if image file is a actual image or fake image
	if(isset($_POST["submit"])) {
    	$check = getimagesize($_FILES["fileToUpload"]["tmp_name"]);
    	//echo $check;
    	if($check !== false) {
       		echo "File is an image - " . $check["mime"] . ".";
       		$uploadOk = 1;
    	} else {
    		header("Refresh: 3; RegisterFormKuProductList.php");
			echo "<p class='maintitle'>이미지 파일이 아닙니다.<br/>
				이전 페이지로 자동으로 이동합니다...</p>";
       		header("Location:"."RegisterFormKuProductList.php");
       		echo "File is not an image.";
        	$uploadOk = 0;
    	}
	}
	// Check if file already exists
	if (file_exists($target_file)) {
		header("Refresh: 5; RegisterFormKuProductList.php");
		echo "<p class='maintitle'>이미지 파일명을 바꿔주세요!<br/>
			잠시후 이전 페이지로 이동합니다...</p>";
    	$uploadOk = 0;
	}
	// Check file size
	if ($_FILES["fileToUpload"]["size"] > 500000) {
		header("Refresh: 3; RegisterFormKuProductList.php");
    	echo "<h1>이미지 파일 크기가 너무 큽니다.<br/>
			잠시후 이전 페이지로 이동합니다...</h1>";
    	$uploadOk = 0;
	}
	// Allow certain file formats
	if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
	&& $imageFileType != "gif" ) {
		echo "<h1>JPG, JPEG, PNG, GIF 확장자만 가능합니다.<br/>
			이전 페이지로 자동으로 이동합니다...</h1>";
		header("Refresh: 3; RegisterFormKuProductList.php");

    	$uploadOk = 0;
	}
	// Check if $uploadOk is set to 0 by an error
	if ($uploadOk == 0) {
		echo "$uploadOk=0";
    	//echo "Sorry, your file was not uploaded.";
	// if everything is ok, try to upload file
	} else {
		echo "aaaa";
		//$r = move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file);
		echo "<br/>"; echo $r; echo "<br/>";
    	if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
    		//echo "영구 왔다";
    		header("Location:"."KuProductList.php");
    		echo $_FILES["fileToUpload"]["tmp_name"];
        	echo "The file ". basename( $_FILES["fileToUpload"]["name"]). " has been uploaded.";
        	$statement = mysqli_prepare($mysqli, "INSERT INTO kuProductsList (productExpireDateOfPurchase, productDepositStartDateOfPurchase, productDepositDueDateOfPurchase, productName, productPrice, productPictureUrlAddress) VALUES (
			?, ?, ?, ?, ?, ?)");  
	
			if( !$statement ) {
				die('mysqli error: '.mysqli_error($mysqli));
			}

			mysqli_stmt_bind_param($statement, "ssssis", $expireDate, $startDepositDate, $EndDepositDate, $productName, $productPrice, $target_file);
	
			if( !mysqli_stmt_execute($statement) ) {
				echo json_encode(array('resultCode'=>'-1'));
				die('stmt err: '.mysqli_stmt_error($statement));
			}
			mysqli_stmt_close($statement);
			
			//계산서에 물품 등록하기
			$statementSelect = mysqli_prepare($mysqli, "SELECT productManagementCode FROM kuProductsList WHERE productName=?");
			if( !$statementSelect ) {
				die('mysqli statementSelect error: '.mysqli_error($mysqli));
			}
			mysqli_stmt_bind_param($statementSelect, "s", $productName);
			if(!mysqli_stmt_execute($statementSelect)) {
				die('stmt statementSelect execute err: '.mysqli_stmt_error($statementSelect));
			}
			mysqli_stmt_store_result($statementSelect);
			mysqli_stmt_bind_result($statementSelect, $productManagementCode);

			while(mysqli_stmt_fetch($statementSelect)) {
				$tempCode = $productManagementCode;
			}
			mysqli_stmt_close($statementSelect);

			
			$stateCacul = mysqli_prepare($mysqli, "INSERT INTO caculateTotalPriceAndCount (productManagementCode, productName, productTotalPrice, productTotalCount, productPrice) VALUES (
				?, ?, ?, ?, ?)");

			if( !$stateCacul ) {
				die('mysqli stateCalcul error: '.mysqli_error($mysqli));
			}
			$tmp1 = 0;
			$tmp2 = 0;
			mysqli_stmt_bind_param($stateCacul, "isiii", $tempCode, $productName, $tmp1, $tmp2, $productPrice);
			if(!mysqli_stmt_execute($stateCacul)) {
				die('stmt stateCacul execute err: '.mysqli_stmt_error($stateCacul));
			}
			mysqli_stmt_close($stateCacul);
    	} else {
    		echo "안돼 왜 !!!!!";
    		header("Location:"."RegisterFormKuProductList.php");
        	//echo "Sorry, there was an error uploading your file.";
    	}
	}
	mysqli_close($mysqli);
?>
</body>
</html>
<?php
	require_once("./configDatabase.php");

	$doit = 0;

	
	$jsonString = mysqli_real_escape_string($mysqli, $_POST['jsonString']);
	$tempString = str_replace('\"', '"', $jsonString);
	$purchaseInfo = json_decode($tempString);

	//유저 정보 변수에 저장
	$productPurchaseDate = $purchaseInfo->productPurchaseDate;
	$userManagementCode = $purchaseInfo->userManagementCode;
	$userID = $purchaseInfo->userID;
	$userName = $purchaseInfo->userName;
	$userPhoneNumber = $purchaseInfo->userPhoneNumber;
	$userMailAddress = $purchaseInfo->userMailAddress;

	//상품 정보 변수에 저장 
	$productsArray = array();
	$totalProductCount = 0;
	foreach($purchaseInfo->products as $products){
		$productsArray[$totalProductCount] = array();
		$productsArray[$totalProductCount]['productManagementCode'] = $products->productManagementCode;
		$productsArray[$totalProductCount]['productName'] = $products->productName;
		$productsArray[$totalProductCount]['productPrice'] = $products->productPrice;
		$productsArray[$totalProductCount]['productCount'] = $products->productCount;
		$totalProductCount++;
	}

	//userManagementCode 구하기... ㅅㅂ..


	//구매 목록이 맞는 정보인지 확인
	for($count=0 ; $count<$totalProductCount ; $count++) {
		$confirmKuproduct = mysqli_prepare($mysqli, "SELECT productName, productPrice FROM kuProductsList WHERE productManagementCode=?");
		if(!$confirmKuproduct) {
			echo json_encode(array('resultCode'=>'-1'));
			die("confirmKuproduct query error: ".mysqli_error($mysqli));
		}
		mysqli_stmt_bind_param($confirmKuproduct, "i", $productsArray[$count]['productManagementCode']); //클라에서 가지고온 정보
		echo "번호: ".$productsArray[$count]['productManagementCode']."<br/>";
		echo "클라가격: ".$productsArray[$count]['productPrice']."<br/>";
		if(!mysqli_stmt_execute($confirmKuproduct)) {
			echo json_encode(array('resultCode'=>'-1'));
			die("confirmKuproduct execute error: ".mysqli_stmt_error($confirmKuproduct));
		}
		mysqli_stmt_store_result($confirmKuproduct);
		mysqli_stmt_bind_result($confirmKuproduct , $productName, $productPrice);

		$conProducts = array();
		if(mysqli_stmt_fetch($confirmKuproduct)) {
			$conProducts['productName'] = $productName;
			$conProducts['productPrice'] = $productPrice;
		} else {
		//	echo "그딴 정보 없다";
			die();
		}
		//echo "서버가격: ".$conProducts['productPrice']."<br/>";
		//var_dump($conProducts['productPrice']); echo "<br/>";
		
		$temp = (int)$productsArray[$count]['productPrice'];
		//var_dump($temp); echo "<br/>";

		//var_dump($productsArray[$count]['productName']); echo "<br/>";
		//var_dump($conProducts['productName']); echo "<br/>";
		if(($productsArray[$count]['productName']==$conProducts['productName']) && ($temp == $conProducts['productPrice'])) {
			$doit = 1;
			echo $doit."<br/>";
		} else {
			echo json_encode(array('resultCode'=>'0'));
			$doit = 0;
			echo $doit."<br/>";
			die();
		}
		mysqli_stmt_close($confirmKuproduct);
	}
	

	//멤버 테이블에 데이터 저장 insert 쿼리
	$stateSelectMember = mysqli_prepare($mysqli, "SELECT * FROM memberWhoPurchaseKuProducts WHERE userManagementCode=?");
	if(!$stateSelectMember) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysqli error: '.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($stateSelectMember, "i", $userManagementCode);
	if(!mysqli_stmt_execute($stateSelectMember)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt statementSelect execute err: '.mysqli_stmt_error($stateSelectMember));
	}
	
	//테이블에 아이디가 없다면
	if(!(mysqli_stmt_fetch($stateSelectMember))) {
		$stateMember = mysqli_prepare($mysqli, "INSERT INTO memberWhoPurchaseKuProducts (userManagementCode, userID, userName, userPhoneNumber, userMailAddress, productStateOfDeposit) VALUES (
		?, ?, ?, ?, ?, ?)");

		if(!$stateMember) {
			echo json_encode(array('resultCode'=>'-1'));
			die('mysqli error: '.mysqli_error($mysqli));
		}
		$productStateOfDeposit = 0;
		mysqli_stmt_bind_param($stateMember, "issssi", $userManagementCode, $userID, $userName, $userPhoneNumber, $userMailAddress, $productStateOfDeposit);
		if(!mysqli_stmt_execute($stateMember)) {
			echo json_encode(array('resultCode'=>'-1'));
			die('stmt execute err: '.mysqli_stmt_error($stateMember));
		}
		mysqli_stmt_close($stateMember);
	}
	mysqli_stmt_close($stateSelectMember);

	$state = 0;
	//유저 테이블에 데이터 저장 insert 쿼리
	$stateUser = mysqli_prepare($mysqli, "INSERT INTO userWhoPurchaseKuProducts (userManagementCode, productPurchaseDate, productManagementCode, productName, productPrice, productCount, productDepositState) VALUES (
		?, ?, ?, ?, ?, ?, ?)");
	if(!$stateUser) {
		echo json_encode(array('resultCode'=>'-1'));
		die('mysqli error: '.mysqli_error($mysqli));
	}
	for($count=0 ; $count < $totalProductCount ; $count++) {
		mysqli_stmt_bind_param($stateUser, "isisiii", $userManagementCode, $productPurchaseDate, $productsArray[$count]['productManagementCode'], $productsArray[$count]['productName'],
			$productsArray[$count]['productPrice'], $productsArray[$count]['productCount'], $state);
		if(!mysqli_stmt_execute($stateUser)) {
			echo json_encode(array('resultCode'=>'-1'));
			die('stmt execute err1: '.mysqli_stmt_error($stateUser));
		}
	}
	mysqli_stmt_close($stateUser);



	$caculArray = array();
	for($count=0 ; $count < $totalProductCount ; $count++){
		$stateSelectCal = mysqli_prepare($mysqli, "SELECT * FROM caculateTotalPriceAndCount WHERE productManagementCode=?");
		if(!$stateSelectCal) {
			echo json_encode(array('resultCode'=>'-1'));
			die('mysqli error: '.mysqli_error($mysqli));
		}
		mysqli_stmt_bind_param($stateSelectCal, "i", $productsArray[$count]['productManagementCode']);
		if(!mysqli_stmt_execute($stateSelectCal)) {
			echo json_encode(array('resultCode'=>'-1'));
			die('stmt execute err: '.mysqli_stmt_error($stateSelectCal));
		}
		mysqli_stmt_store_result($stateSelectCal);
		mysqli_stmt_bind_result($stateSelectCal, $productID, $productCode, $productName, $productTotalPrice, $productTotalCount, $productPrice);

		while(mysqli_stmt_fetch($stateSelectCal)) {
			$caculArray[$count] = array();
			$caculArray[$count]['productManagementCode'] = $productID;
			$caculArray[$count]['productName'] = $productName;
			$caculArray[$count]['productTotalPrice'] = $productTotalPrice;
			$caculArray[$count]['productTotalCount'] = $productTotalCount;
			$caculArray[$count]['productPrice'] = $productPrice;
		}

		$caculatedTotalCount = $caculArray[$count]['productTotalCount'] + $productsArray[$count]['productCount'];
		$caculatedTotalPirce = $caculArray[$count]['productTotalPrice'] + ( $productsArray[$count]['productCount'] * $productsArray[$count]['productPrice'] );


		//계산서 테이블에 데이터 저장 update쿼리
		$stateCaculate = mysqli_prepare($mysqli, "UPDATE caculateTotalPriceAndCount SET productTotalPrice=?, productTotalCount=? WHERE productManagementCode=?");
		if(!$stateCaculate) {
			echo json_encode(array('resultCode'=>'-1'));
			die('mysqli stateCaculate error: '.mysqli_error($mysqli));		
		}

		mysqli_stmt_bind_param($stateCaculate, "iii", $caculatedTotalPirce, $caculatedTotalCount, $productsArray[$count]['productManagementCode']);
		if( !mysqli_stmt_execute($stateCaculate) ) {
			echo json_encode(array('resultCode'=>'-1'));
			die('stmt err: '.mysqli_stmt_error($stateCaculate));
		}
	}
	mysqli_stmt_close($stateCaculate);
	mysqli_stmt_close($stateSelectCal);
	mysqli_close($mysqli);

	echo json_encode(array('resultCode'=>'1'));
?>
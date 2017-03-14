<?php
	require_once("./configDatabase.php");

	$autoIncrementNum = mysqli_real_escape_string($mysqli, $_POST['autoIncrementNum']);
	$userManagementCode = mysqli_real_escape_string($mysqli, $_POST['userManagementCode']);

	//userwhopurchasekuproducts 테이블에 있는 데이터 삭제
	$stateUserTableSelectAuto = mysqli_prepare($mysqli, "SELECT productManagementCode, productcount FROM userWhoPurchaseKuProducts WHERE autoIncrementNum=?");
	if(!$stateUserTableSelectAuto) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stateUserTableSelectAuto query err: '.mysqli_stmt_error($mysqli));
	}
	mysqli_stmt_bind_param($stateUserTableSelectAuto, "i", $autoIncrementNum);
	if(!mysqli_stmt_execute($stateUserTableSelectAuto)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt stateUserTableSelectAuto execute err: '.mysqli_stmt_error($stateUserTableSelectAuto));
	}
	mysqli_stmt_store_result($stateUserTableSelectAuto);
	mysqli_stmt_bind_result($stateUserTableSelectAuto, $productManagementCode, $productCount); //59,2

	$userTable = array();
	while(mysqli_stmt_fetch($stateUserTableSelectAuto)) {
		$userTable['productManagementCode'] = $productManagementCode;//59
		$userTable['productCount'] = $productCount; //2
	}

	$stateCaculateSelect = mysqli_prepare($mysqli, "SELECT productTotalCount, productPrice FROM caculateTotalPriceAndCount WHERE productManagementCode=?");
	if(!$stateCaculateSelect) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stateCaculateSelect query error:'.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($stateCaculateSelect, "i", $userTable['productManagementCode']);
	if(!mysqli_stmt_execute($stateCaculateSelect)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt stateCaculateSelect execute err: '.mysqli_stmt_error($stateCaculateSelect));
	}
	mysqli_stmt_store_result($stateCaculateSelect);
	mysqli_stmt_bind_result($stateCaculateSelect, $productTotalCount, $productPrice);

	
	while(mysqli_stmt_fetch($stateCaculateSelect)) {
		$totalCount = $productTotalCount;
		$price = $productPrice;
	}
	

	$updatedProductCount = 0;
	$updatedProductCount = $totalCount - $userTable['productCount'];
	$updatedProductPrice = 0;
	$updatedProductPrice = $updatedProductCount * $price;

	//caculateTotalPriceAndCount 테이블에서 클라이언트가 삭제하여 파생되는 총액 총개수 수정
	$stateCaculate = mysqli_prepare($mysqli, "UPDATE caculateTotalPriceAndCount SET productTotalCount=?, productTotalPrice=? WHERE productManagementCode=?");
	if(!$stateCaculate) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stateCaculate query error:'.mysqli_error($mysqli));
	}

	mysqli_stmt_bind_param($stateCaculate, "iii", $updatedProductCount, $updatedProductPrice, $userTable['productManagementCode']);
	if( !mysqli_stmt_execute($stateCaculate) ) {
		die('stmt stateCaculate execute err: '.mysqli_stmt_error($stateCaculate));
	}


	$stateUserTable = mysqli_prepare($mysqli, "DELETE FROM userWhoPurchaseKuProducts WHERE autoIncrementNum=?");
	if(!$stateUserTable) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stateUserTable query error:'.mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($stateUserTable, "i", $autoIncrementNum);
	if(!mysqli_stmt_execute($stateUserTable)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt stateUserTable execute err: '.mysqli_stmt_error($stateUserTable));
	}

	$stateUserTableSelect = mysqli_prepare($mysqli, "SELECT * FROM userWhoPurchaseKuProducts WHERE userManagementCode=?");
	if(!$stateUserTableSelect) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stateUserTableSelect query err: '.mysqli_stmt_error($mysqli));
	}
	mysqli_stmt_bind_param($stateUserTableSelect, "i", $userManagementCode);
	if(!mysqli_stmt_execute($stateUserTableSelect)) {
		echo json_encode(array('resultCode'=>'-1'));
		die('stmt stateUserTableSelect execute err: '.mysqli_stmt_error($stateUserTableSelect));
	}
	//구매 내역이 없으면 memberwhopurchasekuproducts에 데이터도 삭제
	if(!(mysqli_stmt_fetch($stateUserTableSelect))) {
		$stateMemberTable = mysqli_prepare($mysqli, "DELETE FROM memberWhoPurchaseKuProducts WHERE userManagementCode=?");
		if(!$stateMemberTable) {
			echo json_encode(array('resultCode'=>'-1'));
			die('stateMemberTable query err: '.mysqli_stmt_error($mysqli));
		}
		mysqli_stmt_bind_param($stateMemberTable, "i", $userManagementCode);
		if(!mysqli_stmt_execute($stateMemberTable)) {
			echo json_encode(array('resultCode'=>'-1'));
			die('stmt stateMemberTable execute err: '.mysqli_stmt_error($stateMemberTable));
		}
		mysqli_stmt_close($stateMemberTable);
	}


	mysqli_stmt_close($stateCaculateSelect);
	mysqli_stmt_close($stateCaculate);
	mysqli_stmt_close($stateUserTableSelectAuto);
	mysqli_stmt_close($stateUserTable);
	mysqli_stmt_close($stateUserTableSelect);
	mysqli_close($mysqli);
	echo json_encode(array('resultCode'=>'1'));
?>
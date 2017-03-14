<!DOCTYPE html>
<?php
	require_once("./configDatabase.php");

	$userManagementCode = mysqli_real_escape_string($mysqli, $_GET['id']);

	$productManagementCode = array();
	$statmentDepositCode=1;
	$count = 0;

	if(!isset($_POST['deposit'])) {
		//이부분은 아무것도 체크가 안되어있는 상태일때 실행된다.
		//그러니가 productDepositState를 전부 0으로 밖면된다 where usermanagementCode 인것을...
		$stateDepositCode = 0;
		$stateUserState = mysqli_prepare($mysqli, "UPDATE userWhoPurchaseKuProducts SET productDepositState=? WHERE userManagementCode=?");
		if(!$stateUserState) {
			die("stateUserState query error: ".mysqli_error($mysqli));
		}
		mysqli_stmt_bind_param($stateUserState, "ii", $stateDepositCode, $userManagementCode);
		if(!mysqli_stmt_execute($stateUserState)) {
			die("stateUserState execute error: ".mysqli_stmt_error($stateUserState));
		}
		$productStateOfDeposit2 = 0;
		$stateMemberDeposit2 = mysqli_prepare($mysqli, "UPDATE memberWhoPurchaseKuProducts SET productStateOfDeposit=? WHERE userManagementCode=?");
		if(!$stateMemberDeposit2) {
			die("stateMemberDeposit2 query error :".mysqli_error($mysqli));
		}
		mysqli_stmt_bind_param($stateMemberDeposit2, "ii", $productStateOfDeposit2, $userManagementCode);
		if(!mysqli_stmt_execute($stateMemberDeposit2)) {
			die("stateMemberDeposit2 execute error:".mysqli_stmt_error($stateMemberDeposit2));
		}
		mysqli_stmt_close($stateMemberDeposit2);
		echo "<script>window.location='UserListPurchaseProductsBySuper.php';</script>";
		die();

	}
		
	$stateDepositCode = 0;
	$stateUserState = mysqli_prepare($mysqli, "UPDATE userWhoPurchaseKuProducts SET productDepositState=? WHERE userManagementCode=?");
	if(!$stateUserState) {
		die("stateUserState query error: ".mysqli_error($mysqli));
	}

	mysqli_stmt_bind_param($stateUserState, "ii", $stateDepositCode, $userManagementCode);
	if(!mysqli_stmt_execute($stateUserState)) {
		die("stateUserState execute error: ".mysqli_stmt_error($stateUserState));
	}

	foreach($_POST['deposit'] as $entry) {

		$productManagementCode[$count] = $entry;
		echo $productManagementCode[$count];
		
		$statement = mysqli_prepare($mysqli, "UPDATE userWhoPurchaseKuProducts SET productDepositState=? WHERE productManagementCode=? AND userManagementCode=?");
		if(!$statement) {
			die("$statement query error:".mysqli_error($statement));
		}

		mysqli_stmt_bind_param($statement, "iii", $statmentDepositCode, $productManagementCode[$count], $userManagementCode);
		if(!mysqli_stmt_execute($statement)) {
			die("$statment execute error:".mysqli_stmt_error($statement));
		}

		$count++;

		mysqli_stmt_close($statement);
	}
	// 여기까지가 선택한 것 별로 입금 상태를 0이냐 1이냐로 바꾸는 코드;

	// 아래부터는 memberwhopurchasekuproducts 테이블에 있는 입금 상태를 바꾸는 코드;
	$stateDepositCode = 0;

	$statementCheckDeposit = mysqli_prepare($mysqli, "SELECT productManagementCode, productDepositState FROM userWhoPurchaseKuProducts WHERE userManagementCode=?");
	if(!$statementCheckDeposit) {
		die("statementCheckDeposit query error : ".mysqli_error($mysqli));
	}
	mysqli_stmt_bind_param($statementCheckDeposit, "i", $userManagementCode);
	if(!mysqli_stmt_execute($statementCheckDeposit)) {
		die("statementCheckDeposit execute error : ".mysqli_stmt_error($statementCheckDeposit));
	}
	mysqli_stmt_store_result($statementCheckDeposit);
	mysqli_stmt_bind_result($statementCheckDeposit, $productManagementCode, $productDepositState);
	$count = 0;
	$depositStmt = array();
	while(mysqli_stmt_fetch($statementCheckDeposit)) {
		$depositStmt[$count] = array();
		$depositStmt[$count]['productManagementCode'] = $productManagementCode;
		$depositStmt[$count]['productDepositState'] = $productDepositState;
		//echo "입금 상태: ";
		//var_dump($depositStmt[$count]['productDepositState']); echo "<br/>";
		if($depositStmt[$count]['productDepositState'] == 1) {
			$stateDepositCode++;
		} 
		$count++;
	}
	//echo $stateDepositCode;
	if($stateDepositCode == $count) {
		$productStateOfDeposit = 1;
		
		$stateMemberDeposit = mysqli_prepare($mysqli, "UPDATE memberWhoPurchaseKuProducts SET productStateOfDeposit=? WHERE userManagementCode=?");
		if(!$stateMemberDeposit) {
			die("stateMemberDeposit query error :".mysqli_error($mysqli));
		}
		mysqli_stmt_bind_param($stateMemberDeposit, "ii", $productStateOfDeposit, $userManagementCode);
		if(!mysqli_stmt_execute($stateMemberDeposit)) {
			die("stateMemberDeposit execute error:".mysqli_stmt_error($stateMemberDeposit));
		}
		mysqli_stmt_close($stateMemberDeposit);
	} else {
		$productStateOfDeposit1 = 0;
		$stateMemberDeposit1 = mysqli_prepare($mysqli, "UPDATE memberWhoPurchaseKuProducts SET productStateOfDeposit=? WHERE userManagementCode=?");
		if(!$stateMemberDeposit1) {
			die("stateMemberDeposit1 query error :".mysqli_error($mysqli));
		}
		mysqli_stmt_bind_param($stateMemberDeposit1, "ii", $productStateOfDeposit1, $userManagementCode);
		if(!mysqli_stmt_execute($stateMemberDeposit1)) {
			die("stateMemberDeposit1 execute error:".mysqli_stmt_error($stateMemberDeposit1));
		}
		mysqli_stmt_close($stateMemberDeposit1);
	}
	mysqli_stmt_close($statementCheckDeposit);

	echo "<script>window.location='UserListPurchaseProductsBySuper.php';</script>";
?>
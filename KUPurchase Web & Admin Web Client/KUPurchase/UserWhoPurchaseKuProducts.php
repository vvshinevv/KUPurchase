<?php
	require_once("./configDatabase.php");
	$userManagementCode = mysqli_real_escape_string($mysqli, $_GET['id']);

?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/earlyaccess/hanna.css">
	<link rel="stylesheet" type="text/css" href="CSSPage.css" >
	<script type="text/javascript">
		function depositFunc() {
			var check = document.myform;
			
			//if( document.getElementById("deposit[]").checked) {
			//	document.getElementById("depositHidden[]").disabled = true;
			//}

			check.submit();
		}
		function checkDepositState() {
			var deposit = document.getElementsByName("deposit[]");
			<?php
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
					//if($depositStmt[$count]['productDepositState'] == 0) {
					//	$stateDepositCode = 0;
					//} else {
					//	$stateDepositCode = 1;
					//}
					$count++;
				}


				echo "var depositArray=".json_encode($depositStmt).";\n";
				echo "var count =".$count.";\n";
				mysqli_stmt_close($statementCheckDeposit);
			 ?>
			 for(i = 0 ; i < count ; i++ ){
			 	if(depositArray[i]['productDepositState'] == 1) {
			 		deposit[i].checked = true;
			 	} else {
			 		deposit[i].checked = false;
			 	}
			 }
		}
	</script>
	<title>사용자 세부 구매 내역</title>
</head>
<body onload="checkDepositState()" bgcolor="#a9a9a9">
	<div id="menu">
   		<a href="./MainPage.html" target="_self">메인 화면</a>
   		<a href="./KuProductList.php" target="_self">상품 목록</a>
    	<a href="./UserListPurchaseProductsBySuper.php">구매자 목록</a>
    	<a href="./TotalBills.php">계산서</a>
    	<a href="./AskAdministor.php">문의사항</a>
    	<a href="./Notification.php">공지사항</a>
  	</div>
	<?php
		
		//echo $userManagementCode;

		$statementMem = mysqli_prepare($mysqli, "SELECT userName FROM memberWhoPurchaseKuProducts WHERE userManagementCode=?");
		if( !$statementMem ) {
			echo json_encode(array('resultCode'=>'-1'));
			die('mysqli statementMem error: '.mysqli_error($mysqli));
		}
		mysqli_stmt_bind_param($statementMem, "i", $userManagementCode);
		if(!mysqli_stmt_execute($statementMem)) {
			echo json_encode(array('resultCode'=>'-1'));
			die('stmt statementMem execute err: '.mysqli_stmt_error($statementMem));
		}
		mysqli_stmt_store_result($statementMem);
		mysqli_stmt_bind_result($statementMem, $userName);
		while(mysqli_stmt_fetch($statementMem)) {
			$usertmpName = $userName;
		}
		mysqli_stmt_close($statementMem);
		echo "<p class=\"maintitle\">".$usertmpName."님의 구매 목록</p>";
	?>
	<form action="./depositState.php?id=<?=$userManagementCode?>" name="myform" method="post">
	<table class="table2">
		<thead>
			<tr>
				<th class="table2">구매 날짜</th>
				<th class="table2">상품 이름</th>
				<th class="table2">상품 가격</th>
				<th class="table2">상품 개수</th>
				<th class="table2">계산된 가격</th>
				<th class="table2">입금 상태</th>
			</tr>
		</thead>
		<tbody>
			<?php
				$statement = mysqli_prepare($mysqli, "SELECT * FROM userWhoPurchaseKuProducts WHERE userManagementCode=?");
				if( !$statement ) {
					echo json_encode(array('resultCode'=>'-1'));
					die('mysqli statement error: '.mysqli_error($mysqli));
				}
				mysqli_stmt_bind_param($statement, "i", $userManagementCode);
				if(!mysqli_stmt_execute($statement)) {
					echo json_encode(array('resultCode'=>'-1'));
					die('stmt statement execute err: '.mysqli_stmt_error($statement));
				}
				mysqli_stmt_store_result($statement);
				mysqli_stmt_bind_result($statement, $autoIncrement, $userManagementCode, $productpurchasedate, $productManagementCode, $productName, $productPrice, $productCount, $productDepositState);

				$userPurchaseList = array();
				$count = 0;
				$totalPrice = 0;
				while(mysqli_stmt_fetch($statement)) {
					$userPurchaseList[$count] = array();
					$userPurchaseList[$count]['productpurchasedate'] = $productpurchasedate;
					$userPurchaseList[$count]['productName'] = $productName;
					$userPurchaseList[$count]['productPrice'] = $productPrice;
					$userPurchaseList[$count]['productCount'] = $productCount;
					$userPurchaseList[$count]['PriceByCount'] = $productPrice * $productCount;
					$userPurchaseList[$count]['productManagementCode'] = $productManagementCode;	
			?>
			<tr>
				<td class="table2"><?php echo $userPurchaseList[$count]['productpurchasedate']?></td>
				<td class="table2"><?php echo $userPurchaseList[$count]['productName']?></td>
				<td class="table2"><?php echo $userPurchaseList[$count]['productPrice']?></td>
				<td class="table2"><?php echo $userPurchaseList[$count]['productCount']?></td>
				<td class="table2"><?php echo $userPurchaseList[$count]['PriceByCount']?></td>
				<td class="table2"><input type="checkbox" name="deposit[]" id="deposit[]" value="<?=$productManagementCode?>"></td>
				<!--<input type="hidden" name="depositHidden[]" id="depositHidden[]" value="<?=$productManagementCode?>">-->
			</tr>
			<?php
					$totalPrice += $userPurchaseList[$count]['PriceByCount'];
					$count++;
				}
			?>
			<tr>
				<td class="table2" colspan="5" align="right" >총 가격: <?php echo $totalPrice?></td>
				<td class="table2"><input type="button" name="deposit" value="선택 입금 완료" onclick="depositFunc()"></td>
			</tr>
			<?php
	  				mysqli_stmt_close($statement);
	  				mysqli_close($mysqli);
	  		?>
		</tbody>
	</table>
	</form>
</body>
</html>
<?php
	die();
?>
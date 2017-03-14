<?php
	require_once("./configDatabase.php");

?>
<!DOCTYPE html>
<html>
<head>	
	<meta charset="utf-8"/>
	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/earlyaccess/hanna.css">
	<link rel="stylesheet" type="text/css" href="CSSPage.css" >
	<title>KU공동구매 상품 목록</title>
</head>
<body bgcolor="#a9a9a9"> 
	<p class="maintitle">KU공동구매 상품 목록</p>

	<div id="menu">
   		<a href="./MainPage.html" target="_self">메인 화면</a>
   		<a href="./KuProductList.php" target="_self">상품 목록</a>
    	<a href="./UserListPurchaseProductsBySuper.php">구매자 목록</a>
    	<a href="./TotalBills.php">계산서</a>
    	<a href="./AskAdministor.php">문의사항</a>
    	<a href="./Notification.php">공지사항</a>
  	</div>
	
	<table class="table2">
		
			<?php

				$state = mysqli_prepare($mysqli, "SELECT * FROM kuProductsList");
				if(!$state) {
					die("state query error".mysqli_error($mysqli));
				}
				if(!mysqli_stmt_execute($state)) {
					die("state execute error".mysqli_stmt_error($state));
				}
				mysqli_stmt_store_result($state);
				mysqli_stmt_bind_result($state, $productManagementCode, $productExpireDateOfPurchase, $productDepositStartDateOfPurchase, $productDepositDueDateOfPurchase, $productName, $productPrice, $productPictureUrlAddress);

				$products = array();

				while(mysqli_stmt_fetch($state)) {
					$products['productManagementCode'] = $productManagementCode;
					$products['productExpireDateOfPurchase'] = $productExpireDateOfPurchase;
					$products['productDepositStartDateOfPurchase'] = $productDepositStartDateOfPurchase;
					$products['productDepositDueDateOfPurchase'] = $productDepositDueDateOfPurchase;
					$products['productName'] = $productName;
					$products['productPrice'] = $productPrice;
					$products['productPictureUrlAddress'] = $productPictureUrlAddress;
			?>
			<tr>
				<td class="table2" rowspan="5" align="center"><?php echo $products['productManagementCode']."."?></td>
				<td class="table2" rowspan="5" align="center"><?php echo "<img src=".$products['productPictureUrlAddress']." width=180 height=250>"; ?></td>	
				<td class="table2">구매 만료 날짜</td>
				<td class="table2"><?php echo $products['productExpireDateOfPurchase'] ?></td>
				<td class="table2" rowspan="5" align="center">
					<form method="POST" action="./UpdateFormKuProduct.php">
						<input type="hidden" name="productManagementCode" value="<?=$products['productManagementCode']?>">
						<input type="submit" value="수정하기">
					</form>
					<br/>
					<form method="POST" name="myform" action="./DeleteKuProduct.php">
						<input type="hidden" name="productManagementCode" id="productManagementCode" value="<?=$products['productManagementCode']?>">
						<input type="submit" value="삭제하기">
					</form>
				</td>			
			</tr>
			<tr>
				<td class="table2">입금 시작 날짜</td>
				<td class="table2"><?php echo $products['productDepositStartDateOfPurchase'] ?></td>
			</tr>
			<tr>
				<td class="table2">입금 종료 날짜</td>
				<td class="table2"><?php echo $products['productDepositDueDateOfPurchase'] ?></td>
			</tr>
			<tr>
				<td class="table2">상품 이름</td>
				<td class="table2"><?php echo $products['productName'] ?></td>
			</tr>
			<tr>
				<td class="table2">상품 가격</td>
				<td class="table2"><?php echo $products['productPrice'] ?> 원</td>
			</tr>
			<tr>
				<td colspan="4"><p></p></td>
			</tr>
			<?php
				}
			?>
			<tr>
				<td colspan="5" align="right">
					<a href="./RegisterFormKuProductList.php">
						<input type="button" value="상품 추가"/>
					</a>
				</td>
			</tr>
		
	</table>
</body>
</html>
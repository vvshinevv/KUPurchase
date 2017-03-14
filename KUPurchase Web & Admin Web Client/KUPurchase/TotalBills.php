<?php
	require_once("./configDatabase.php");
?>
<!DOCTYPE html>
<html>
<head>
	 <meta charset="utf-8"/>
	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/earlyaccess/hanna.css">
	<link rel="stylesheet" type="text/css" href="CSSPage.css" >
	<title>총액 계산서</title>
</head>
<body bgcolor="#a9a9a9">
	<p class="maintitle">총액 계산서</p>

	<div id="menu">
      <a href="./MainPage.html" target="_self">메인 화면</a>
      <a href="./KuProductList.php" target="_self">상품 목록</a>
      <a href="./UserListPurchaseProductsBySuper.php">구매자 목록</a>
      <a href="./TotalBills.php">계산서</a>
      <a href="./AskAdministor.php">문의사항</a>
      <a href="./Notification.php">공지사항</a>
    </div>
  	
  	<table class="table2">
  		<thead>
  			<tr>
  				<th class="table2">상품 이름</th>
  				<th class="table2">상품 가격</th>
  				<th class="table2">상푸 총 개수</th>
  				<th class="table2">계산된 가격</th>
  			</tr>
  		</thead>
  		<tbody>
  			<?php
  				$statement = mysqli_prepare($mysqli, "SELECT * FROM caculateTotalPriceAndCount");
  				if(!$statement) {
  					die('mysqli statement error: '.mysqli_error($mysqli));
  				}

		  		if(!mysqli_stmt_execute($statement)) {
 		 			  die('stmt statment execute err: '.mysqli_stmt_error($statement));
  				}
  				mysqli_stmt_store_result($statement);
  				mysqli_stmt_bind_result($statement, $autoNum, $productManagementCode, $productName, $productTotalPrice, $productTotalCount, $productPrice);

  				$totalBills = array();
  				$count = 0;
  				$totalPrice = 0;
  				while(mysqli_stmt_fetch($statement)) {
  					$totalBills[$count] = array();
  					$totalBills[$count]['productName'] = $productName;
  					$totalBills[$count]['productPrice'] = $productPrice;
  					$totalBills[$count]['productTotalCount'] = $productTotalCount;
  					$totalBills[$count]['productTotalPrice'] = $productTotalPrice;
	  			?>
	  			<tr>
	  				<td class="table2"><?php echo $totalBills[$count]['productName']?></td>
	  				<td class="table2"><?php echo $totalBills[$count]['productPrice']?></td>
	  				<td class="table2"><?php echo $totalBills[$count]['productTotalCount']?></td>
					<td class="table2"><?php echo $totalBills[$count]['productTotalPrice']?></td>
	  			</tr>
	  			<?php
	  				$totalPrice += $totalBills[$count]['productTotalPrice'];
	  				$count++;
            }
	  			?>
	  			<tr>
	  				<td class="table2" colspan="4" align="right">총 가격: <?php echo $totalPrice?></td>
	  			</tr>
	  			<?php
	  				mysqli_stmt_close($statement);
            mysqli_close($mysqli);
	  			?>
  		</tbody>
  	</table>
</body>
</html>
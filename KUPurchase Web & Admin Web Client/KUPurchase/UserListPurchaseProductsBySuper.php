<?php
	require_once("./configDatabase.php");
?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/earlyaccess/hanna.css">
	<link rel="stylesheet" type="text/css" href="CSSPage.css" >
	<title>상품 구매 유저 목록</title>

</head>
<body bgcolor="#a9a9a9">
	<p class="maintitle">KU공동구매 구매자 목록</p>
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

				<th class="table2">번호</th>
				<th class="table2">사용자 이름</th>
				<th class="table2">사용자 아이디</th>
				<th class="table2">사용자 전화번호</th>
				<th class="table2">사용자 이메일</th>
				<th class="table2">구매 상세 목록</th>
				<th class="table2">입금 상태</th>
			</tr>
		</thead>
		<tbody>
			<?php
				$state = mysqli_prepare($mysqli, "SELECT * FROM memberWhoPurchaseKuProducts");
				if(!$state) {
					die("state query error :".mysqli_error($mysqli));
				}
				if(!mysqli_stmt_execute($state)) {
					die("state execute error :".mysqli_error($state));
				}
				mysqli_stmt_store_result($state);
				mysqli_stmt_bind_result($state, $autoIncrementNum, $userManagementCode, $userID, $userName, $userPhoneNumber, $userMailAddress, $productStateOfDeposit);

				$member = array();
				while(mysqli_stmt_fetch($state)) {
					$member['autoIncrementNum'] = $autoIncrementNum;
					$member['userManagementCode'] = $userManagementCode;
					$member['userID'] = $userID;
					$member['userName'] = $userName;
					$member['userPhoneNumber'] = $userPhoneNumber;
					$member['userMailAddress'] = $userMailAddress;
					$member['productStateOfDeposit'] = $productDepositState;
				

				//$sql = 'select * from MemberwhopurchaseKuproducts';
				//$result = $mysqli->query($sql);
				//while($row = $result->fetch_assoc())
				//{
			?>
			<tr>
				<td class="table2"><?php echo $member['autoIncrementNum'] ?></td>
				<td class="table2"><?php echo $member['userName']?></td>
				<td class="table2"><?php echo $member['userID']?></td>
				<td class="table2"><?php echo $member['userPhoneNumber']?></td>
				<td class="table2"><?php echo $member['userMailAddress']?></td>
				<td class="table2"><a href="./UserWhoPurchaseKuProducts.php?id=<?=$member['userManagementCode']?>">상세 목록</a></td>
				<td class="table2">
					<?php
						$statement = mysqli_prepare($mysqli, "SELECT productStateOfDeposit FROM memberWhoPurchaseKuProducts where userManagementCode=?");
						if(!$statement) {
							die("statement query error: ".mysqli_error($mysqli));
						}
						mysqli_stmt_bind_param($statement, "i", $member['userManagementCode']);
						if(!mysqli_stmt_execute($statement)) {
							die("statement execute error".mysqli_stmt_error($statement));
						}
						mysqli_stmt_store_result($statement);
						mysqli_stmt_bind_result($statement, $productStateOfDeposit);
						while(mysqli_stmt_fetch($statement)) {
							$productDepositState = $productStateOfDeposit;
						}

						if($productDepositState == 1) {
							echo "<span class=\"paran\">입금 완료</span>";
						} else {
							echo "<span class=\"hong\">입금 미완료</span>";
						}
					?>
				</td>
			</tr>
			<?php
				}
			?>
		</tbody>
	</table>
</body>
</html>

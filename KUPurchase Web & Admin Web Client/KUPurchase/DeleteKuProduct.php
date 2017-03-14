
<!DOCTYPE html>
<?php
	require_once("./configDatabase.php");
	

	$productManagementCode = mysqli_real_escape_string($mysqli, $_POST['productManagementCode']);
	//var_dump($productManagementCode);

	echo $productManagementCode; echo "<br/>"; 
	$isThereAnyPurchasedProducts = mysqli_prepare($mysqli, "SELECT userManagementCode FROM userWhoPurchaseKuProducts WHERE productManagementCode=?");
	if(!$isThereAnyPurchasedProducts) {
		die("isThereAnyPurchasedProducts query err: ".mysqli_stmt_error($mysqli_));
	}
	mysqli_stmt_bind_param($isThereAnyPurchasedProducts, "i", $productManagementCode);
	if(!mysqli_stmt_execute($isThereAnyPurchasedProducts)) {
		die('stmt isThereAnyPurchasedProducts execute err: '.mysqli_stmt_error($isThereAnyPurchasedProducts));
	}

	mysqli_stmt_store_result($isThereAnyPurchasedProducts);
	mysqli_stmt_bind_result($isThereAnyPurchasedProducts, $userManagementCode);
	echo $userManagementCode;
	//물건 구매한 사람이 없다.
	if(!(mysqli_stmt_fetch($isThereAnyPurchasedProducts))) {
		echo "구매한 사람이 없다"; echo "<br/>";
		$statement = mysqli_prepare($mysqli, "SELECT productPictureUrlAddress FROM kuProductsList WHERE productManagementCode=?");
	
		if( !$statement ) {
			die('mysqli error: '.mysqli_error($mysqli));
		}

		mysqli_stmt_bind_param($statement, "i", $productManagementCode);
		if(!mysqli_stmt_execute($statement)) {
			die('stmt1 execute err: '.mysqli_stmt_error($statement));
		}

		mysqli_stmt_store_result($statement);
		mysqli_stmt_bind_result($statement, $productCode);
	
		while(mysqli_stmt_fetch($statement)) {
			$temp = $productCode;
		}
		$path = "SimpleTest/".$temp;
		echo $temp;

		unlink($temp); // 사진 삭제

		$res = mysqli_prepare($mysqli, "DELETE FROM kuProductsList WHERE productManagementCode=?");

		mysqli_stmt_bind_param($res, "i", $productManagementCode);
		if(!mysqli_stmt_execute($res)) {
			die('stmt2 execute err: '.mysqli_stmt_error($res));
		}
		
		mysqli_stmt_close($statement);

		$statement2 = mysqli_prepare($mysqli, "DELETE FROM caculateTotalPriceAndCount WHERE productManagementCode=?");
		if(!$statement2) {
			die("statement2 query error: ".mysqli_error($statement2));
		}
		mysqli_stmt_bind_param($statement2, "i", $productManagementCode);
		if(!mysqli_stmt_execute($statement2)) {
			die("statement2 execute err".mysqli_stmt_error($statement2));
		}
		mysqli_stmt_close($statement2);
		echo "<script>alert(\"삭제 되었습니다.\"); window.location='KuProductList.php';</script>";

	} else {

		$userManagementCode = $userManagementCode;
		//물건 구매한 사람이 있음
		echo "<script language=javascript>alert(\"해당 상품의 구매자가 있습니다. \\n구매자들에게 물건이 삭제되었다고 공지하세요.\");</script>";
		//echo "<script language=javascript>conf();</script>";

		
		//$dump1 = print_r($ccc);
		//var_dump($dump1);

		//trim($con);
		//var_dump(strpos($con, ";"));
		//var_dump($con);
		

		$int =0;
		//var_dump($int); echo "<br/>";
		//echo "<br/>";


		//var_dump($con); echo strlen($con);
		//echo $con;
		//var_dump($con);
		//var_dump("false"); echo "<br/>";
		//var_dump(strcmp($con, "false"));
		if(1) {
			$statement1 = mysqli_prepare($mysqli, "SELECT productPictureUrlAddress FROM kuProductsList WHERE productManagementCode=?");
	
			if( !$statement1 ) {
				die('mysqli error: '.mysqli_error($mysqli));
			}

			mysqli_stmt_bind_param($statement1, "i", $productManagementCode);
			if(!mysqli_stmt_execute($statement1)) {
				die('stmt3 execute err: '.mysqli_stmt_error($statement1));
			}

			mysqli_stmt_store_result($statement1);
			mysqli_stmt_bind_result($statement1, $productCode);
	
			while(mysqli_stmt_fetch($statement1)) {
				$temp = $productCode;
			}
			$path = "SimpleTest/".$temp;
			echo $temp;

			unlink($temp); // 사진 삭제

			$res = mysqli_prepare($mysqli, "DELETE FROM kuProductsList WHERE productManagementCode=?");

			mysqli_stmt_bind_param($res, "i", $productManagementCode);
			if(!mysqli_stmt_execute($res)) {
				die('stmt4 execute err: '.mysqli_stmt_error($res));
			}
			mysqli_stmt_close($statement1);

			$statement3 = mysqli_prepare($mysqli, "DELETE FROM caculateTotalPriceAndCount WHERE productManagementCode=?");
			if(!$statement3) {
				die("statement3 query error: ".mysqli_error($statement3));
			}
			mysqli_stmt_bind_param($statement3, "i", $productManagementCode);
			if(!mysqli_stmt_execute($statement3)) {
				die("statement3 execute err".mysqli_stmt_error($statement3));
			}
			mysqli_stmt_close($statement3);

			$stmtUserDelete = mysqli_prepare($mysqli, "DELETE FROM userWhoPurchaseKuProducts WHERE productManagementCode=?");
			if(!$stmtUserDelete) {
				die("stmtUserDelete query error: ".mysqli_error($stmtUserDelete));
			}
			mysqli_stmt_bind_param($stmtUserDelete, "i", $productManagementCode);
			if(!mysqli_stmt_execute($stmtUserDelete)) {
				die("stmtUserDelete execute err".mysqli_stmt_error($stmtUserDelete));
			}
			mysqli_stmt_close($stmtUserDelete);

			
			//header("Location:"."KuProductList.php");

			$stateUserTableSelect = mysqli_prepare($mysqli, "SELECT * FROM userWhoPurchaseKuProducts WHERE userManagementCode=?");
			if(!$stateUserTableSelect) {
				die("stateUserTableSelect query error: ".mysqli_error($mysqli));
			}
			mysqli_stmt_bind_param($stateUserTableSelect, "i", $userManagementCode);
			if(!mysqli_stmt_execute($stateUserTableSelect)) {
				die("stateUserTableSelect execute error: ".mysqli_stmt_error($stateUserTableSelect));
			}
			if(!(mysqli_stmt_fetch($stateUserTableSelect))) {
				$stmtDeleteMember = mysqli_prepare($mysqli, "DELETE FROM memberWhoPurchaseKuProducts WHERE userManagementCode=?");
				if(!$stmtDeleteMember) {
					die("stmtDeleteMember query err:".mysqli_error($mysqli));
				}
				mysqli_stmt_bind_param($stmtDeleteMember, "i", $userManagementCode);
				if(!mysqli_stmt_execute($stmtDeleteMember)) {
					die("stmtDeleteMember execute error : " .mysqli_stmt_error($stmtDeleteMember));
				}
				mysqli_stmt_close($stmtDeleteMember);
			}

			
			mysqli_stmt_close($stateUserTableSelect);

			echo "<script>alert(\"삭제 되었습니다.\"); window.location='KuProductList.php';</script>";
		} else {
			//echo "<script>alert(\"취소 되었습니다.\"); window.location='kuProductList.php';</script>";
			exit();
		}
	}

	//caculate에 있는 정보 삭제해야됨
	//header("Location:"."KuProductList.php");

	mysqli_stmt_close($isThereAnyPurchasedProducts);
	
	
	mysqli_close($mysqli);
	die();
?>

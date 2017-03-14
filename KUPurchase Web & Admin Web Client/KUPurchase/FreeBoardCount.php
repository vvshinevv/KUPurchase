<?php
	require_once("./configDatabase.php");

	$freeBoardManagementCode = mysqli_real_escape_string($mysqli, $_POST["freeBoardManagementCode"]);

	$statementCount = mysqli_prepare($mysqli, "SELECT freeBoardCount FROM freeBoard WHERE freeBoardManagementCode=?");
	mysqli_stmt_bind_param($statementCount, "i", $freeBoardManagementCode);
	if(!$statementCount) {
        echo json_encode(array('resultCode'=>'-1'));
        die("statementCount query error: ".mysqli_error($mysqli));
    }

    if(!mysqli_stmt_execute($statementCount)) {
        echo json_encode(array('resultCode'=>'-1'));
        die("statementCount execute error: ".mysqli_stmt_error($statementCount));
    }

    mysqli_stmt_store_result($statementCount);
    mysqli_stmt_bind_result($statementCount, $freeBoardCount);

    while(mysqli_stmt_fetch($statementCount)) {
         $freeTmpBoardCount = $freeBoardCount;
    }

    $freeTmpBoardCount += 1;


	$statement = mysqli_prepare($mysqli, "UPDATE freeBoard SET freeBoardCount=? WHERE freeBoardManagementCode=?");
	if(!$statement) {
        echo json_encode(array('resultCode'=>'-1'));
        die('mysql statement error: '.mysqli_error($mysqli));
    }
    mysqli_stmt_bind_param($statement, "ii", $freeTmpBoardCount, $freeBoardManagementCode);
    if(!mysqli_stmt_execute($statement)) {
        echo json_encode(array('resultCode'=>'-1'));
        die('stmt statement execute err: '.mysqli_stmt_error($statement));
    }

    echo json_encode(array('resultCode'=>'1'));
    mysqli_stmt_close($statementCount);
    mysqli_stmt_close($statement);
	mysqli_close($mysqli);


?>
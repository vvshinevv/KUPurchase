<?php
	require_once("./configDatabase.php");
    
    $userID = mysqli_real_escape_string($messageMysqli, $_POST["userID"]);

     $statement = mysqli_prepare($messageMysqli, "SELECT * FROM ".$userID."");

    if(!$statement) {
    	echo json_encode(array('resultCode'=>'-1'));
        die('messageMysqli statement error: '.mysqli_error($messageMysqli));
    }

    if(!mysqli_stmt_execute($statement)) {
    	echo json_encode(array('resultCode'=>'-1'));
        die('messageMysqli statement execute error: '.mysqli_error($statement));
    }

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $messageManagementCode, $toUserID, $fromUserID, $sendTime, $message);

    $dataMessages = array();
    $count = 0;
    $resultCode = 1;

    while(mysqli_stmt_fetch($statement)) {
    	$dataMessages["resultCode"] = $resultCode;
    	$dataMessages[$count] = array();
    	$dataMessages[$count]["toUserID"] = $toUserID;
    	$dataMessages[$count]["fromUserID"] = $fromUserID;
    	$dataMessages[$count]["sendTime"] = $sendTime;
    	$dataMessages[$count]["message"] = $message;
    	$count++;
    }

    echo json_encode($dataMessages);
    mysqli_stmt_close($statement);
	mysqli_close($messageMysqli);
?>
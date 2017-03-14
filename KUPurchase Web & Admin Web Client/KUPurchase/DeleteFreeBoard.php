<?php
        require_once("./configDatabase.php");

        $freeBoardManagementCode = mysqli_real_escape_string($mysqli, $_POST["freeBoardManagementCode"]);


        $statement = mysqli_prepare($mysqli, "DELETE FROM freeBoard WHERE freeBoardManagementCode=?");

        if(!$statement) {
                echo json_encode(array('resultCode'=>'-1'));
                die('mysql statement error: '.mysqli_error($mysqli));
        }
        mysqli_stmt_bind_param($statement, "i", $freeBoardManagementCode);
        if(!mysqli_stmt_execute($statement)) {
                echo json_encode(array('resultCode'=>'-1'));
                die('mysql statement execute error: '.mysqli_stmt_error($statement));
        }

        echo json_encode(array('resultCode'=>'1'));
        mysqli_stmt_close($statement);
        mysqli_close($mysqli);
?>

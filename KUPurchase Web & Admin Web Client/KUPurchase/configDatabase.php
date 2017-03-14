<?php
	date_default_timezone_set('Asia/Seoul');
	header('Content-Type:text/html; charset=utf-8');

	define("SERVER_ADDRESS", "http://52.79.93.255/KUPurchase/");

	$mysqli = new mysqli('localhost', 'root', 'y1271039396*', 'KUPurchase');
	$messageMysqli = new mysqli('localhost', 'root', 'y1271039396*', 'MessageRoom');

	if($mysqli->connect_errno) {
		die('Connection Error('.$mysqli->connect_errno.'): '.
			$mysqli->connect_error);
	}

	if($messageMysqli->connect_errno) {
		die('Connection Error('.$messageMysqli->connect_errno.'): '.
			$messageMysqli->connect_error);
	}

    $messageMysqli->set_charset('utf8');
	$mysqli->set_charset('utf8');
?>
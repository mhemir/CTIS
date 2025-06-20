<?php

$dsn  = "mysql:host=localhost;dbname=test;charset=utf8mb4";
$user = "root";
$pass = "";

try {
    $db = new PDO($dsn, $user, $pass,  [
        PDO::MYSQL_ATTR_LOCAL_INFILE => true,
    ]);
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (Exception $ex) {
   echo "DB Connection Error : " .  $ex->getMessage();
}

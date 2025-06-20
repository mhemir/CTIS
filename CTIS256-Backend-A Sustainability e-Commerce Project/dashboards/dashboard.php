<?php
session_start();
require "../db.php";

if(empty($_SESSION)){
    header("Location: ../index.php");
  }


if ($_SESSION['user']['type'] == 'market') {
    // echo "Hello " . $_SESSION["user"]["name"] . " this is your market management page";
    require "./market/market.php";
} 
else if ($_SESSION["user"]["type"] == "consumer"){
    // echo "Hello " . $_SESSION["user"]["name"] . " you are a consumer";
    
    
    
    require "./customer/customer.php";
}

?>

 




















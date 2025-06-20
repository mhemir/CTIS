<?php
require '../../db.php';
session_start();
if($_SESSION['user']['type'] != 'consumer') exit('Yetkisiz');

$id = $_POST['item_id'];

if($id){
  $db->prepare("DELETE FROM shopping_cart_items WHERE product_id = ?")
     ->execute([$id]);
  echo "removed";
  }

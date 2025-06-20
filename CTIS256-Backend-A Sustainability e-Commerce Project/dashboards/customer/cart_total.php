<?php
session_start();
require "../../db.php";



if (!isset($_SESSION['user']['id']) || $_SESSION['user']['type'] !== 'consumer') {
  http_response_code(403);
  echo json_encode(["error" => "Yetkisiz"]);
  exit;
}

$id = $_POST["id"] ?? $_SESSION["user"]["id"];

$stmt = $db->prepare("
  SELECT 
	count(*)
FROM
	shopping_cart_items
WHERE
	cart_id = ( SELECT id from shopping_cart where consumer_id = ?)
");
$stmt->execute([$id]);
$total = $stmt->fetchColumn();

echo $total;

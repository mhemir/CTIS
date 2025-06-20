<?php
session_start();
require '../../db.php';

// Security Check
if (!isset($_SESSION['user']['id']) || $_SESSION['user']['type'] !== 'market') {
  exit('Yetkisiz erişim');
}

$id = isset($_GET['id']) ? (int)$_GET['id'] : 0;


if ($id > 0) {
  //find the image path
  $stmt = $db->prepare("SELECT image_path FROM products WHERE id = ? AND market_id = ?");
  $stmt->execute([$id, $_SESSION['user']['id']]);
  $product = $stmt->fetch(PDO::FETCH_ASSOC);
  // delete the photo
  if ($product && !empty($product['image_path']) && file_exists($product['image_path'])) {
    unlink($product['image_path']);
  }
  // Delete the product
  $stmt = $db->prepare("DELETE FROM products WHERE id = ? AND market_id = ?");
  $stmt->execute([$id, $_SESSION['user']['id']]);

  // Check if it is deleted 
  if ($stmt->rowCount() > 0) {
    header('Location: ../dashboard.php');
    exit;
  } else {
    echo "Products couldn't find or this product does not belong to you.";
    
    exit;
  }
} else {
  echo "Invalid ID.";
  exit;
}

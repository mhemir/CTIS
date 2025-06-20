<?php
session_start();
require '../../db.php';

if ($_SESSION['user']['type'] !== 'consumer') {
  exit('Yetkisiz');
}

// Kullanıcının sepet ID'sini al
$stmt = $db->prepare("SELECT id FROM shopping_cart WHERE consumer_id = ?");
$stmt->execute([$_SESSION['user']['id']]);
$cart = $stmt->fetch(PDO::FETCH_ASSOC);

$stmt = $db->prepare("SELECT product_id, quantity FROM shopping_cart_items WHERE cart_id = ?");
$stmt->execute([$cart['id']]);
$items = $stmt->fetchAll(PDO::FETCH_ASSOC);

if ($cart) {
  // Sepet içeriğini sil
  $db->prepare("DELETE FROM shopping_cart_items WHERE cart_id = ?")
     ->execute([$cart['id']]);

  foreach ($items as $item) {
    $stmt = $db->prepare("UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?");
    $stmt->execute([$item['quantity'], $item['product_id'], $item['quantity']]);
}

}

// AJAX için sade çıktı:
echo "Satın alma başarılı!";



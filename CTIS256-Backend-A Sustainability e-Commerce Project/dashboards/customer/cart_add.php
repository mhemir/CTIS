<?php
session_start();
require '../../db.php';

if (!isset($_SESSION['user']['id']) || $_SESSION['user']['type'] !== 'consumer') {
    http_response_code(403);
    echo "Unauthorized access";
    exit;
}

$consumer_id = $_SESSION['user']['id'];
$product_id = $_POST['product_id'];

if (!$product_id) {
    http_response_code(400);
    echo "Invalid data";
    exit;
}


//Create card if doesn't exist
$stmt = $db->prepare("INSERT INTO shopping_cart (consumer_id)
SELECT ? 
WHERE NOT EXISTS (
    SELECT 1 FROM shopping_cart WHERE consumer_id = ?
);");

$stmt->execute([$consumer_id, $consumer_id]);


// Get card
$stmt = $db->prepare("SELECT id FROM shopping_cart WHERE consumer_id = ?");
$stmt->execute([$consumer_id]);
$cart = $stmt->fetch(PDO::FETCH_ASSOC);

$cart_id = $cart['id'];

// Add item to shopping_cart_items
$stmt = $db->prepare("INSERT INTO shopping_cart_items (cart_id, product_id, quantity)
VALUES (?, ?, 1)
ON DUPLICATE KEY UPDATE
quantity = quantity + 1;");

$stmt->execute([$cart_id, $product_id]);

$stmt = $db->prepare("
SELECT
    p.id AS product_id,
    p.title AS title,            
    p.discounted_price AS discounted_price, 
    i.quantity,
    p.expiration_date AS expiration_date, 
    p.image_path AS image
FROM 
    products p, 
    shopping_cart_items i,
    shopping_cart c
WHERE
    i.cart_id = ? 
AND
	p.id = i.product_id
AND 
    c.id = i.cart_id
");

$stmt->execute([$cart_id]);
$items = $stmt->fetchAll(PDO::FETCH_ASSOC);


echo count($items);

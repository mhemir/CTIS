<?php
require_once "../../db.php";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $item_id = $_POST["item_id"] ?? null;

    $quantity = $_POST["quantity"] ?? 1;
    $stmt = $db->prepare("UPDATE shopping_cart_items SET quantity = ? WHERE product_id = ?");
    $stmt->execute([$quantity, $item_id]);
    echo "updated";
}

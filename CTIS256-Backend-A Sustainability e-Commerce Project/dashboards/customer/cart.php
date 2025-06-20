<?php
session_start();
require_once "../../db.php"; // veritabanı bağlantısı

if(!isset($_SESSION["user"])){
    header("Location: ../../index.php");
    exit;
}

$consumer_id = $_SESSION['user']['id'];



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
$card = $stmt->fetch(PDO::FETCH_ASSOC);


$cart_id = $card['id'];

// Retrieve items from card
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
$count = count($items);
?>

<!DOCTYPE html>
<html>
<head>
    <title>Shopping Card</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT"
          crossorigin="anonymous">
</head>
<body class="bg-light">
<div class="container py-5">
    <h2 class="mb-4">Your Card</h2>
    <div class="table-responsive">
        <table class="table table-bordered align-middle bg-white">
        <thead class="table-light">
            <tr>
                <th>Product Title</th>
                <th>Discounted Price</th>
                <th>Quantity</th>
                <th>Expiration Date</th>
                <th>Image</th>
                <th>Operation</th>
            </tr>
        </thead>
            <tbody>
                <?php foreach ($items as $item): ?>
                <tr data-id="<?= $item['product_id'] ?>">
                    <td><?= $item['title'] ?></td>
                    <td class="subtotal"><?= $item['discounted_price'] ?> TL</td>
                    <td><input type="number" value="<?= $item['quantity'] ?>" class="form-control quantity"></td>                    
                    <td><?= $item['expiration_date'] ?></td>
                    </td>
                    <td>
<?php
      if (preg_match('/^https?:\/\//', $item['image'])) {
        $imgSrc = $item['image'];
      } else {
        $imgSrc = preg_replace('#^(\.\./)+#', '', $item['image']);
        $imgSrc = '/CTIS256-Project/' . $imgSrc;
      }
    ?>
    <img src="<?= htmlspecialchars($imgSrc) ?>" alt="Product Image" style="width:60px; height:60px; object-fit:cover;">
</td>  
                    <td><button class="btn btn-sm btn-danger remove">Remove</button></td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    </div>
    <h4 class="mt-4">Overall: <span id="total"></span> TL</h4>
    <button id="purchase" class="btn btn-success mt-3">Purchase</button>
    <a href="../dashboard.php" class="btn btn-outline-secondary">← Turn Back</a>
</div>

<script>
    
 function updateTotal() {
     let total = 0;
     $(".subtotal").each(function () {
        var quantity = $(this).closest('tr').find('.quantity').val();
        total += parseFloat($(this).text())*quantity;

   });
     $("#total").text(total.toFixed(2));
 }

updateTotal();

// if quantity changes
$(".quantity").on("change", function () {
    const tr = $(this).closest("tr");
    const itemId = tr.data("id")
    const quantity = $(this).val();
    $.ajax({
             url: './cart_update.php',
             type: 'POST',
             data : { item_id: itemId, quantity: quantity  },
             success: function (response) {
              updateTotal();
             }
       })
});

// Remove product
$(".remove").on("click", function () {
    const tr = $(this).closest("tr");
    const itemId = tr.data("id");

    $.ajax({
             url: './cart_remove.php',
             type: 'POST',
             data : { item_id: itemId},
             success: function (response) {
            //   console.log(response)
                tr.remove()
                updateTotal();

        }
       })
});

// Satın al
$("#purchase").on("click", function () {
    if (!confirm("Satın almak istediğine emin misin?")) return;

    $.post("checkout.php", {}, function (response) {
        console.log(response);

        // 🧼 Sepetteki tüm satırları DOM'dan sil
        $("tr[data-id]").remove();

        // 🧮 Genel toplamı sıfırla
        $("#total").text("0.00");

        // (isteğe bağlı) "Sepet boş" mesajı
        $("table").after("<p>Sepetiniz boş 😢</p>");
    });
});

    // updateCartTotal(); // sayfa yüklenince hemen çağır
</script>
</body>
</html>

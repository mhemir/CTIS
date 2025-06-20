<?php
session_start();
require '../../db.php';

// Yetkisiz erişim kontrolü
if (!isset($_SESSION['user']['id']) || $_SESSION['user']['type'] !== 'market') {
    exit('Unauthorized access');
}

$market_id = $_SESSION['user']['id'];
$product_id = $_GET['id'] ?? 0;

// Eğer form gönderildiyse
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $title = $_POST['title'];
    $stock = $_POST['stock'];
    $normal_price = $_POST['normal_price'];
    $discounted_price = $_POST['discounted_price'];
    $expiration_date = $_POST['expiration_date'];

     // update photo
    $imagePath = $product['image_path'];
    if (isset($_FILES['image']) && $_FILES['image']['error'] === UPLOAD_ERR_OK) {
        $ext = pathinfo($_FILES['image']['name'], PATHINFO_EXTENSION);
        $newName = uniqid('img_', true) . '.' . $ext;
        if (!is_dir('photos')) mkdir('photos');
        $target = 'photos/' . $newName;
        move_uploaded_file($_FILES['image']['tmp_name'], $target);
        $imagePath = $target;
    }

    $stmt = $db->prepare("
        UPDATE products SET 
            title = ?, stock = ?, normal_price = ?, discounted_price = ?, expiration_date = ?, image_path= ?
        WHERE id = ? AND market_id = ?
    ");
    $stmt->execute([$title, $stock, $normal_price, $discounted_price, $expiration_date,$imagePath, $product_id, $market_id]);

    header("Location: ../dashboard.php");
    exit;
}


$stmt = $db->prepare("SELECT * FROM products WHERE id = ? AND market_id = ?");
$stmt->execute([$product_id, $market_id]);
$product = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$product) {
    exit("Product couldn't be found or doesn't belong to you");
}
?>

<!DOCTYPE html>
<html lang="tr">
<head>
  <meta charset="UTF-8">
  <title>Ürünü Güncelle</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
  <div class="card shadow p-5" style="max-width: 600px; width: 100%;">
    <h2 class="text-center mb-4">Edit Product</h2>

    <form method="POST" enctype="multipart/form-data">
      <div class="mb-3">
        <input type="text" name="title" class="form-control" placeholder="Başlık" value="<?= htmlspecialchars($product['title']) ?>" required>
      </div>

      <div class="mb-3">
        <input type="number" name="stock" class="form-control" placeholder="Stok" value="<?= $product['stock'] ?>" required>
      </div>

      <div class="mb-3">
        <input type="text" name="normal_price" class="form-control" placeholder="Normal Fiyat" value="<?= $product['normal_price'] ?>" required>
      </div>

      <div class="mb-3">
        <input type="text" name="discounted_price" class="form-control" placeholder="İndirimli Fiyat" value="<?= $product['discounted_price'] ?>" required>
      </div>

      <div class="mb-3">
        <input type="date" name="expiration_date" class="form-control" value="<?= $product['expiration_date'] ?>" required>
      </div>

      <!-- Existing Photo -->
      <?php if ($product['image_path']): ?>
        <div class="mb-3 text-center">
          <img src="<?= htmlspecialchars($product['image_path']) ?>" width="100" class="img-thumbnail">
        </div>
      <?php endif; ?>

      <div class="mb-4">
        <label class="form-label">New Photo</label>
        <input type="file" name="image" class="form-control">
      </div>

      <button type="submit" class="btn btn-success w-100">Update</button>
    </form>

    <div class="text-center mt-3">
      <a href="../dashboard.php" class="btn btn-outline-secondary w-100">← Turn Back</a>
    </div>
  </div>
</div>

</body>
</html>

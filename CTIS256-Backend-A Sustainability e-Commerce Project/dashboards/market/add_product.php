<?php
require '../../db.php';
session_start();

if ($_SESSION['user']['type'] !== 'market') exit('Yetkisiz');
if ($_SERVER['REQUEST_METHOD'] === 'POST') {

  $title = $_POST['title'];
  $stock = (int)$_POST['stock'];
  $norm  = (float)$_POST['normal_price'];
  $disc  = (float)$_POST['discounted_price'];
  $exp   = $_POST['expiration_date'];
  // image upload
$imagePath = null;
if (isset($_FILES['image']) && $_FILES['image']['error'] === UPLOAD_ERR_OK) {
    $ext = pathinfo($_FILES['image']['name'], PATHINFO_EXTENSION);
    $newName = uniqid('img_', true) . '.' . $ext;
    $photosDir = '../../photos';
    if (!is_dir($photosDir)) mkdir($photosDir);
    $target = $photosDir . '/' . $newName;
    move_uploaded_file($_FILES['image']['tmp_name'], $target);
    $imagePath = $target;
}
  $stmt = $db->prepare("INSERT INTO products
    (market_id,title,stock,normal_price,discounted_price,expiration_date,image_path)
    VALUES (?,?,?,?,?,?,?)");
  $stmt->execute([$_SESSION['user']['id'], $title, $stock, $norm, $disc, $exp,$imagePath]);
  header('Location: ../dashboard.php');
  exit;
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Add Product</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css"
        rel="stylesheet"
        integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT"
        crossorigin="anonymous">
</head>
<body class="bg-light">

<div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
  <div class="card shadow p-5" style="max-width: 500px; width: 100%;">
    <h2 class="text-center mb-4">Add New Product</h2>

    <form method="POST" enctype="multipart/form-data">
      <div class="mb-3">
        <input name="title" class="form-control" placeholder="Product Name" required>
      </div>

      <div class="mb-3">
        <input name="stock" type="number" class="form-control" placeholder="Stock" required>
      </div>

      <div class="mb-3">
        <input name="normal_price" type="number" step="0.01" class="form-control" placeholder="Normal Price" required>
      </div>

      <div class="mb-3">
        <input name="discounted_price" type="number" step="0.01" class="form-control" placeholder="Discounted Price" required>
      </div>

      <div class="mb-3">
        <input name="expiration_date" type="date" class="form-control" required>
      </div>

      <div class="mb-4">
        <input type="file" name="image" class="form-control">
      </div>

      <button type="submit" class="btn btn-success w-100">Add Product</button>
    </form>

    <div class="text-center mt-3">
      <a href="../dashboard.php" class="btn btn-outline-secondary w-100">← Turn Back</a>
    </div>
  </div>
</div>

</body>
</html>


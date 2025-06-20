<?php
  //no need ../../db.php because this file is require in dashboard.php
  //no need session_start() because this file is required in dashboard.php
  if(empty($_SESSION)){
    exit("Unauthorized access!  <a href='../../login.php'>Go to login page</a>");

  }
  $stmt = $db->prepare("SELECT * FROM products WHERE market_id=? ORDER BY created_at DESC");
  $stmt->execute([$_SESSION['user']['id']]);
  $products = $stmt->fetchAll(PDO::FETCH_ASSOC);
  ?>
  
  <!DOCTYPE html>
<html lang="tr">
<head>
  <meta charset="UTF-8">
  <title>Market</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div style="margin: 50px" class="d-flex justify-content-between align-items-center mb-4">
    <a href="update_profile.php" class="btn btn-primary">Update Your Profile</a>
    <a href="../logout.php" class="btn btn-outline-danger">Log Out</a>
</div>

<div class="card shadow border-0 text-center" style="max-width: 1000px; margin: 10px auto">
    <div class="card-body p-4">
      <h1 class="h3 fw-bold text-primary">Hello, <?= $_SESSION["user"]["name"]?></h1>
      <p class="text-muted mt-3 mb-4">
        This is your market page. Start exploring and managing your products with ease.
      </p>
    </div>
 

<h3 style="text-align:center" class="mb-3">Product Management</h3>
<div class="container py-5">
 <a href="./market/add_product.php" class="btn btn-success mb-3">+ Add a new product</a>

 <div class="table-responsive">
 <table class="table table-bordered align-middle bg-white">
 <thead class="table-light">
 <tr>
     <th>Title</th>
     <th>Stock</th>
     <th>Price</th>
     <th>Expiration Date</th>
     <th>Photo</th>
     <th>Edit/Delete</th>
   </tr>
  </thead>
  <tbody>
<?php if(!empty($products)):?>
   <?php foreach ($products as $p):
      $expired = (new DateTime($p['expiration_date']) < new DateTime());
    ?>
     <tr class="<?= $expired ? 'table-danger' : '' ?>">
       <td><?= htmlspecialchars($p['title']) ?></td>
       <td><?= $p['stock'] ?></td>
       <td><?= $p['discounted_price'] ?>₺</td>
       <td><?= $p['expiration_date'] ?></td>
      <td>
  <?php if (!empty($p['image_path'])) : ?>
    <?php
      if (preg_match('/^https?:\/\//', $p['image_path'])) {
        // İnternetten gelen resim
        $imgSrc = $p['image_path'];
      } else {
        // Yerel dosya: baştaki ../ veya ../../ kaldırılır ve kökten erişilir
        $imgSrc = preg_replace('#^(\.\./)+#', '', $p['image_path']);
        $imgSrc = '/CTIS256-Project/' . $imgSrc;
      }
    ?>
    <img src="<?= htmlspecialchars($imgSrc) ?>" width="60" class="img-thumbnail">
  <?php else: ?>
    <span class="text-muted">No image</span>
  <?php endif; ?>
</td>
       <td>
         <a href="./market/edit_product.php?id=<?= $p['id'] ?>">Edit</a> |
         <a href="./market/delete_product.php?id=<?= $p['id'] ?>"
           onclick="return confirm('Are you sure to delete?')">Delete</a>
       </td>
     </tr>
   <?php endforeach; ?>
  <?php else : ?>
      <tr>
        <td colspan=6>You have no product</td>
      </tr>
  <?php endif ?>
 </table>

 </div>
 </div>
</body>
</html>

<?php
session_start();
require "../db.php";
// Profile updating
if(empty($_SESSION)){
  header("Location: ../index.php");
}


if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['update_profile'])) {
    $name = trim($_POST['name']);
    $city = trim($_POST['city']);
    $district = trim($_POST['district']);
    $params = [$name, $city, $district];
    $sql = "UPDATE user SET name=?, city=?, district=?";
    // If password will be changed
    if (!empty($_POST['password'])) {
      $password = password_hash($_POST['password'], PASSWORD_DEFAULT);
      $sql .= ", password=?";
      $params[] = $password;
    }
    $sql .= " WHERE id=?";
    $params[] = $_SESSION['user']['id'];
    $stmt = $db->prepare($sql);
    $stmt->execute($params);
    // Update session info
    $_SESSION['user']['name'] = $name;
    $_SESSION['user']['city'] = $city;
    $_SESSION['user']['district'] = $district;
    // $redirect= $_SESSION["user"]["type"]."php";
    header("Location: ./dashboard.php");
  }

?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Update Profile</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="card shadow p-4 mb-4">
      <h3 class="mb-4">Update Profile</h3>
      <form method="POST">
        <input type="hidden" name="update_profile" value="1">
        <div class="mb-3">
          <label>Email</label>
          <input name="email" class="form-control" value="<?= htmlspecialchars($_SESSION['user']['email']) ?>" disabled>
        </div>
        <div class="mb-3">
          <label>Name</label>
          <input name="name" class="form-control" value="<?= htmlspecialchars($_SESSION['user']['name']) ?>" required>
        </div>
        <div class="mb-3">
          <label>City</label>
          <input name="city" class="form-control" value="<?= htmlspecialchars($_SESSION['user']['city']) ?>" required>
        </div>
        <div class="mb-3">
          <label>District</label>
          <input name="district" class="form-control" value="<?= htmlspecialchars($_SESSION['user']['district']) ?>" required>
        </div>
        <div class="mb-4">
          <label>New Password (optional)</label>
          <input name="password" type="password" class="form-control" placeholder="New password">
        </div>
        <button class="btn btn-success w-100">Update</button>
      </form>
      <div class="text-center mt-3">
        <a href="./dashboard.php" class="btn btn-outline-secondary">← Turn back</a>
      </div>
  
</body>
</html>












<?php
require "./db.php";
session_start();

// Getting form values
$code = $_POST['code'];
$email = $_SESSION["email"];

// Fetch user data
$stmt = $db->prepare("SELECT * FROM user WHERE email = ?");
$stmt->execute([$email]);

$user = $stmt->fetch(PDO::FETCH_ASSOC);

if ($user && $user['verify_code'] === $code) {
    // Update user verification status
    $update = $db->prepare("UPDATE user SET verified = 1 WHERE email = ?");
    $update->execute([$email]);

    // Success message and redirect
    echo "✅ Verification is successful. Your account is active.";
    session_destroy();
    header("Location: ./login.php");
    exit;
} else {
    // Error message and redirect
    echo "❌ Wrong code. Please try again.";
    header("Location: ./verify.php");
    exit;
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Verify Your Email</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT"
          crossorigin="anonymous">
</head>
<body class="bg-light">

<div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
    <div class="card shadow p-5" style="max-width: 500px; width: 100%;">
        <h2 class="text-center mb-4">Email Verification</h2>

        <form action="verify_process.php" method="POST">
            <div class="mb-3">
                <input type="text" name="code" class="form-control" placeholder="Enter 6-digit code" maxlength="6" required>
            </div>
            <button type="submit" class="btn btn-success w-100">Verify</button>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO"
        crossorigin="anonymous"></script>
</body>
</html>

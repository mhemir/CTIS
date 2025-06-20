<?php
session_start();
require_once "db.php";

// Getting form values
$email = isset($_POST['email']) ? trim($_POST['email']) : '';
$password = isset($_POST['password']) ? $_POST['password'] : '';

$error_message = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Validate inputs
    if (filter_var($email, FILTER_VALIDATE_EMAIL) && !empty($password)) {
        // Find user
        $stmt = $db->prepare("SELECT * FROM user WHERE email = ?");
        $stmt->execute([$email]);
        $user = $stmt->fetch();

        if ($user) {
            // Check if password is correct
            if (password_verify($password, $user['password'])) {
                if ($user['verified'] == 1) {
                    // Successful login
                    $_SESSION['user'] = $user;
                    header("Location: dashboards/dashboard.php");
                    exit;
                } else {
                    $error_message = "⚠️ Your account has not been verified yet. Please check your email!";
                }
            } else {
                $error_message = "❌ Wrong email or password.";
            }
        } else {
            $error_message = "❌ User with this email was not found!";
        }
    } else {
        $error_message = "❌ Please enter a valid email and password.";
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT"
          crossorigin="anonymous">
</head>
<body class="bg-light">
<div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
    <div class="card shadow p-5" style="max-width: 500px; width: 100%;">
        <h2 class="text-center mb-4">Login</h2>

    <?php if (!empty($error_message)): ?>
        <p style="color: red;">
            <?php echo htmlspecialchars($error_message); ?>
        </p>
    <?php endif; ?>

    <form action="?" method="POST">
            <div class="mb-3">
                <label for="email" class="form-label">Email:</label>
                <input type="email" name="email" class="form-control" required>
            </div>

            <div class="mb-3">
                <label for="password" class="form-label">Password:</label>
                <input type="password" name="password" class="form-control" required>
            </div>

            <button type="submit" class="btn btn-success w-100">Login</button>
        </form>

        <div class="text-center mt-3">
            <a href="index.php" class="btn btn-link">← Back to main page</a>
        </div>
    </div>
</div>
</body>
</html>

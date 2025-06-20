<?php
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;
require './vendor/autoload.php';
require './db.php';
require './config.php';

session_start();
$error = null;
$step = $_SESSION['step'] ?? 'register'; 

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    if ($step === 'register') {
        // Kayıt işlemi
        $email    = trim($_POST['email']);
        $name     = trim($_POST['name']);
        $password = password_hash(trim($_POST['password']), PASSWORD_DEFAULT);
        $city     = trim($_POST['city']);
        $district = trim($_POST['district']);
        $type     = trim($_POST['type']);
        $code     = rand(100000, 999999); 

        try {
           
            $stmt = $db->prepare("INSERT INTO user (email, name, password, type, city, district, verify_code) 
                                  VALUES (?, ?, ?, ?, ?, ?, ?)");
            $stmt->execute([$email, $name, $password, $type, $city, $district, $code]);

          
            $mail = new PHPMailer(true);
            $mail->isSMTP();
            $mail->Host       = HOST;
            $mail->SMTPAuth   = true;
            $mail->Username   = MAIL;
            $mail->Password   = PASS;
            $mail->SMTPSecure = PHPMailer::ENCRYPTION_SMTPS;
            $mail->Port       = 465;

            $mail->setFrom(MAIL, FULLNAME);
            $mail->addAddress($email);
            $mail->isHTML(true);
            $mail->Subject = 'Your Verification Code';
            $mail->Body    = "Hello,<br><br>Your verification code is: <b>$code</b><br><br>Thanks!";

            $mail->send();

            $_SESSION['email'] = $email;
            $_SESSION['step']  = 'verify';
            header("Location: register.php");
            exit;

        } catch (Exception $e) {
            $error = "❌ Registration failed: " . $e->getMessage();
        }
    } elseif ($step === 'verify') {
    
        $code = trim($_POST['code']);
        $email = $_SESSION['email'];

       
        $stmt = $db->prepare("SELECT * FROM user WHERE email = ?");
        $stmt->execute([$email]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($user && $user['verify_code'] == $code) {
           
            $update = $db->prepare("UPDATE user SET verified = 1 WHERE email = ?");
            $update->execute([$email]);

         
            session_destroy();
            header("Location: login.php");
            exit;
        } else {
            $error = "❌ Wrong code. Please try again.";
        }
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT"
          crossorigin="anonymous">
</head>
<body class="bg-light">
    <div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
        <div class="card shadow p-5" style="max-width: 500px; width: 100%;">
    <?php if ($step === 'register'): ?>
        <h2 class="text-center mb-4">Register</h2>
        <?php if ($error): ?>
            <p style="color: red;"><?= htmlspecialchars($error) ?></p>
        <?php endif; ?>
        <form action="register.php" method="POST">
            <div class="mb-3">
                <input type="email" name="email" class="form-control" placeholder="Email" required>
            </div>

            <div class="mb-3">
                <input type="text" name="name" class="form-control" placeholder="Full Name / Market Name" required>
            </div>

            <div class="mb-3">
                <input type="password" name="password" class="form-control" placeholder="Password" required>
            </div>

            <div class="mb-3">
                <input type="text" name="city" class="form-control" placeholder="City" required>
            </div>

            <div class="mb-3">
                <input type="text" name="district" class="form-control" placeholder="District" required>
            </div>

            <div class="mb-4">
                <select name="type" class="form-select" required>
                    <option value="" disabled selected>Select user type</option>
                    <option value="market">Market</option>
                    <option value="consumer">Consumer</option>
                </select>
            </div>

            <button type="submit" class="btn btn-success w-100">Register</button>
    <?php elseif ($step === 'verify'): ?>
        <h2>Verify Your Email</h2>
        <?php if ($error): ?>
            <p style="color: red;"><?= htmlspecialchars($error) ?></p>
        <?php endif; ?>
        <div class="container d-flex justify-content-center align-items-center" style="min-height: 50vh;">
            <div class="card shadow p-5" style="max-width: 500px; width: 100%;">
                <h2 class="text-center mb-4">Email Verification</h2>

                <form action="register.php" method="POST">
                    <div class="mb-3">
                        <input type="text" name="code" class="form-control" placeholder="Enter 6-digit code" maxlength="6" required>
                    </div>
                    <button type="submit" class="btn btn-success w-100">Verify</button>
                </form>
        </div>
</div>
    <?php endif; ?>
</body>
</html>

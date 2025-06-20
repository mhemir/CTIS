<?php
 use PHPMailer\PHPMailer\PHPMailer;
    use PHPMailer\PHPMailer\Exception;

    require 'vendor/autoload.php';
include 'db.php';
include 'config.php';

// 1. Form verilerini al
$email    = $_POST['email'];
$name     = $_POST['name'];
$password = $_POST['password'];
//$password = password_hash($_POST['password'], PASSWORD_DEFAULT); // güvenli şifreleme$city     = $_POST['city'];
$city = $_POST['city'];
$district = $_POST['district'];
$type     = $_POST['type'];
$code     = rand(100000, 999999);

// 2. Hangi tabloya kaydedeceğimizi belirle
$table = ($type === 'market') ? 'markets' : 'consumers';

// 3. Veritabanına kaydet
try {
    $stmt = $db->prepare("INSERT INTO $table (email, name, password, city, district, verify_code, verified)
                          VALUES (:email, :name, :password, :city, :district, :code, 0)");
    $stmt->execute([
        ':email'    => $email,
        ':name'     => $name,
        ':password' => $password,
        ':city'     => $city,
        ':district' => $district,
        ':code'     => $code
    ]);

    

   

    $mail = new PHPMailer(true);

    try {
        $mail->isSMTP();
        $mail->Host       = 'smtp.gmail.com';
        $mail->SMTPAuth   = true;
        $mail->Username   = MAIL;
        $mail->Password   = PASSWORD;
        $mail->SMTPSecure = PHPMailer::ENCRYPTION_SMTPS;
        $mail->Port       = 465;

        $mail->setFrom(MAIL, 'Sustainability Market');
        $mail->addAddress($email);

        $mail->isHTML(true);
        $mail->Subject = 'Your Verification Code';
        $mail->Body    = "Hello,<br><br>Your verification code is: <b>$code</b><br><br>Thanks!";

        $mail->send();
        echo "✅ Verification email sent to $email";
        header("Location: verify.php");
exit;

    } catch (Exception $e) {
        echo "❌ Mail could not be sent. Error: {$mail->ErrorInfo}";
    }

} catch (PDOException $e) {
    echo "Registration failed: " . $e->getMessage();
}
?>

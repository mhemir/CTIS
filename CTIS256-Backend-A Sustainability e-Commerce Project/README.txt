- You have to setup a WAMP/XAMPP or that kind of server application that runs php engine to view this project. 
- Please check the file "db.php" and reconfigure your dbname and password according to your local account to log in your local phpmyadmin correctly.-
- After that, please drag the "db.sql" file into your database to see sample data.

- Bu projeyi görüntülemek için WAMP/XAMPP veya bu tür php motoru çalıştıran sunucu uygulaması kurmanız gerekmektedir.
- phpmyadmin hesabınıza doğru şekilde giriş yapabilmek için lütfen db.php dosyasındaki değerleri yerel hesabınıza göre güncelleyin.
- Bundan sonra lütfen "db.sql" dosyasını veritabanınızın içine örnek verileri görmek için yükleyin. 

Example: 
If your database is "deneme"
and your phpmyadmin credential is like
username = "std" password="",

please edit "db.php" with the code below:

Örnek: 
Eğer veritabanınızın ismi "deneme" 
ve phpmyadmin bilgileriniz aşağıdaki gibiyse

lütfen "db.php" dosyasını aşağıdaki kodla değiştirin:


<?php

$dsn  = "mysql:host=localhost;deneme=test;charset=utf8mb4";
$user = "std";
$pass = "";

try {
    $db = new PDO($dsn, $user, $pass,  [
        PDO::MYSQL_ATTR_LOCAL_INFILE => true,
    ]);
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (Exception $ex) {
   echo "DB Connection Error : " .  $ex->getMessage();
}



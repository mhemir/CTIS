<?php
session_start();

// Varsayılan olarak sayfa numarasını ayarla
if (!isset($_SESSION['page'])) {
    $_SESSION['page'] = 1;
}

// İstek türü kontrolü
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'];

    if ($action === 'prev' && $_SESSION['page'] > 1) {
        $_SESSION['page']--; // Sayfayı bir azalt
    } elseif ($action === 'next') {
        $_SESSION['page']++; // Sayfayı bir artır
    }

    // Yeni sayfa numarasını döndür
    echo $_SESSION['page'];
    exit;
}
?>
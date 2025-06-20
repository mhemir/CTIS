<?php

   require_once "db.php";
 
 function checkUser($email, $pass, &$user) {
     global $db ;

     $stmt = $db->prepare("select * from user where email=?") ;
     $stmt->execute([$email]) ;
     $user = $stmt->fetch() ;
     return $user ? password_verify($pass, $user["password"]) : false ;
 }

 // Remember me
 function getUserByToken($token) {
    global $db ;
    $stmt = $db->prepare("select * from user where remember = ?") ;
    $stmt->execute([$token]) ;
    return $stmt->fetch() ;
 }

 function setTokenByEmail($email, $token) {
    global $db ;
    $stmt = $db->prepare("update  set remember = ? where email = ?") ;
    $stmt->execute([$token, $email]) ;
 }

 
 
 


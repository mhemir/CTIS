<?php
   session_start() ;
   // delete session file
   session_destroy() ;
   // redirect to login page.
   header("Location: index.php") ;
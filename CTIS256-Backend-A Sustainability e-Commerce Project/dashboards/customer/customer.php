<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Customer</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT"
          crossorigin="anonymous">
  <script
    src="https://code.jquery.com/jquery-3.7.1.min.js"
    integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
    crossorigin="anonymous"
  ></script>
</head>
<body class="bg-light">

  <div class="container py-5">
    <div class="card shadow p-4 mb-4">
        <h3 class="mb-3">Search Product</h3>
        <form action="" method="GET" class="d-flex" id="search-form">
            <input type="text" name="q" id="search-input" class="form-control me-2" placeholder="Write a keyword" required>
            <button type="submit" class="btn btn-success">Search</button>
        </form>
    </div>

<div class="d-flex justify-content-between" id="search-results">
   
</div>

      <div class="pagination d-flex justify-content-center mt-4">
          <a onclick="updateSessionPage('prev')" id="prev-btn" class="btn btn-secondary me-2">Previous</a>
          <a onclick="updateSessionPage('next')" id="next-btn" class="btn btn-secondary">Next</a>
      </div>

  <p class="mt-4">
        <a href="./customer/cart.php" class="btn btn-primary">
            🛒 My Card (<span id="cart-count"></span>)
        </a>
    </p>
    
    <div class="d-flex justify-content-between align-items-center mb-4">
    <a href="update_profile.php" class="btn btn-primary">Update Your Profile</a>
    <a href="../logout.php" class="btn btn-outline-danger">Log Out</a>
</div>
</div>


</div>  
  <script>
    
    //render cart count
    $.ajax({
             url: './customer/cart_total.php',
             type: 'POST',
             data : { product_id: <?=$_SESSION["user"]["id"] ?>},
             success: function (response) {
             console.log(response)
              //update
               $("#cart-count").html(response)
             }
       })


      $("#search-form").on("submit", function(){
        event.preventDefault();
        loadProduct();
      });


      function loadProduct(page){
        const query = $("#search-input").val();
        // $("#search-results").append(query);
        $.ajax({
          url: "./customer/search.php",
          type: "GET",
          data: { q: query , page: page},
          success: function (response) {
              $("#search-results").html("")
              $("#search-results").append(response)
            }
        })
    
        
      }

      function updateSessionPage(action) {
        $.ajax({
        url: './customer/update_session.php',
        type: 'POST',
        data: { action: action },
        success: function (response) {
            const newPage = parseInt(response.trim(), 10);

            // Yeni ürünleri yükle
            loadProduct(newPage);
        },
        error: function (error) {
            console.error("Session update failed:", error);
        }
      });
    }

   function updateCard(id){
          console.log(id)
           
           $.ajax({
             url: './customer/cart_add.php',
             type: 'POST',
             data : { product_id: id},
             success: function (response) {
             console.log(response)
              //update
               $("#cart-count").html(response)
             }
       })
    }
    

    </script>
  <?php
    
  
  
  ?>
     
</body>
</html>

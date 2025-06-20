<?php
session_start();
require "../../db.php";

$q = $_GET['q'] ?? '';

$userId = $_SESSION["user"]["id"];

$_SESSION["page"] = $_GET["page"] ?? 1;

$page = $_GET["page"] ?? 1; 

if($_SESSION["page"] < $page){
    $_SESSION["page"]++;
}
else if($_SESSION["page"] > $page){
    $_SESSION["page"]--;
}
// $_SESSION["page"] = $page;



$perPage = 4; 
$offset = ($page - 1) * $perPage;


$sql = "
SELECT 
    p.id AS product_id,          
    p.market_id AS market_id,    
    u_market.city AS city,       
    u_market.district AS district, 
    p.title AS title,            
    p.stock AS stock,            
    p.normal_price AS normal_price, 
    p.discounted_price AS discounted_price, 
    p.expiration_date AS expiration_date, 
    p.image_path AS image
FROM 
    products p
JOIN 
    user u_market ON u_market.id = p.market_id
WHERE 
    u_market.city = (
        SELECT 
            city 
        FROM 
            user 
        WHERE 
            id = ?
    )
    AND p.expiration_date >= CURRENT_DATE
    AND p.title LIKE CONCAT('%', ?, '%') 
ORDER BY 
    u_market.district = (
        SELECT 
            district 
        FROM 
            user 
        WHERE 
            id = ?
    ) DESC, 
    u_market.district ASC, 
    p.title ASC
LIMIT ?, 4;
";

$stmt = $db->prepare($sql);
$stmt->bindValue(1, $userId, PDO::PARAM_INT);
$stmt->bindValue(2, $q, PDO::PARAM_STR);
$stmt->bindValue(3, $userId, PDO::PARAM_INT);
$stmt->bindValue(4, $offset, PDO::PARAM_INT);
$stmt->execute();
$results = $stmt->fetchAll(PDO::FETCH_ASSOC);


if (empty($results)) {
    echo "<p>Product not found.</p>";
    exit;
}


// echo "<tr>
// <th>Product ID</th>
// <th>Market ID</th>
// <th>City</th>
// <th>District</th>
// <th>Title</th>
// <th>Stock</th>
// <th>Normal Price</th>
// <th>Discounted Price</th>
// <th>Expiration Date</th>
// <th>Image</th>
// </tr>";
// foreach ($results as $product) {
//   echo "<tr class='product'>";
//     $keys = array_keys($product);
//     foreach($keys as $key){
//       echo "<td>";
//       echo htmlspecialchars($product[$key]);
//       echo "</td>";
//     }
//     echo "<td>";
//     echo "<form method='POST' action='./customer/cart_add.php' style='display:inline;'>";
//     echo "<input type='hidden' name='product_id' value='" . htmlspecialchars($product['product_id']) . "' />";
//     echo "<button type='submit' class='btn btn-secondary'>Add to Card</button>";
//     echo "</form>";
//     echo "</td>";
//     echo "</tr>";

?>

<?php foreach ($results as $product): ?>
<div class="card shadow-sm border-0" style="max-width: 400px; border-radius: 16px; transition: transform 0.3s ease, box-shadow 0.3s ease;">
 <?php
      if (preg_match('/^https?:\/\//', $product['image'])) {
        // İnternetten gelen resim
        $imgSrc = $product['image'];
      } else {
        // Yerel dosya: baştaki ../ veya ../../ kaldırılır ve kökten erişilir
        $imgSrc = preg_replace('#^(\.\./)+#', '', $product['image']);
        $imgSrc = '/CTIS256-Project/' . $imgSrc;
      }
    ?>
    <div class="card shadow-sm border-0" style="max-width: 400px; border-radius: 16px; transition: transform 0.3s ease, box-shadow 0.3s ease;">
        <img src="<?= htmlspecialchars($imgSrc) ?>" class="card-img-top" alt="Product Image" style="border-top-left-radius: 16px; border-top-right-radius: 16px; object-fit: cover; height: 200px;">
        
    </div>    <div class="card-body text-center">
        <h5 class="card-title fw-bold" style="color: #333;"><?=$product['title']?></h5>
        <div class="d-flex justify-content-between mt-3" style="font-size: 0.9rem; color: #777;">
            <span>City: <strong><?= $product["city"] ?></strong></span>
            <span>District: <strong><?=$product["district"]?></strong></span>
        </div>
        <div class="d-flex justify-content-between mt-3" style="font-size: 0.9rem; color: #777;">
            <span>Stock: <strong><?=$product["stock"]?></strong></span>
            <span>Expires: <strong><?=$product["expiration_date"]?></strong></span>
        </div>
        <div class="mt-4">
            <div class="text-center" style="color: #999; font-size: 1rem; text-decoration: line-through;"><?=$product["normal_price"]?></div>
            <div class="text-center" style="color: #28a745; font-size: 1.5rem; font-weight: bold;"><?=$product["discounted_price"]?></div>
        </div>
            <div id="addcart">
                <div onclick="updateCard(<?=$product['product_id'] ?>)" class="btn w-100" id="<?=$product["product_id"] ?>" style="background-color: #ff5722; color: white; border-radius: 8px; font-size: 0.9rem;">Add to Cart</div>
            </div>
    </div>    
</div>
<?php endforeach ?>








let profileCnt = 0;
let profiles = [];

class Profile {
  constructor(name) {
    this.wallet = {
      USD: 1000.0,
      coins: {},
    };
    this.name = name;
    this.id = profileCnt;
    this.currentDay = 0; // 1. Günden başlıyoruz.
    this.currentSelectionIndex = 2; //Başlangıç seçimi bitcoin
    this.minLimit =
      market[this.currentDay].coins[this.currentSelectionIndex].low -
      Math.floor(
        market[this.currentDay].coins[this.currentSelectionIndex].low
      ) /
        10;
    this.maxLimit =
      market[this.currentDay].coins[this.currentSelectionIndex].high +
      Math.floor(
        market[this.currentDay].coins[this.currentSelectionIndex].high
      ) /
        10;
  }
}

$(".overlay").click(function () {
  $(".newProfile").css("visibility", "hidden");
  $(".overlay").fadeOut();
});

$(document).ready(function () {
  loadProfilesFromLocalStorage();
  // Butona tıklandığında yeni profil ekranını göster
  $(".bottom button").click(function () {
    $(".newProfile").css("visibility", "visible");
    $(".overlay").fadeIn();
  });
  $(document).on("click", ".buy-btn", function () {
    $(this).css("background-color", "green").css("color", "white");
    $(this)
      .closest(".trading-buttons")
      .find(".sell-btn")
      .css("background-color", "white")
      .css("color", "black");

    const coinName = $(this)
      .closest(".profile-screen")
      .find("#currentCoinName")
      .text();

    $(this)
      .closest(".trading-section")
      .find(".coin-buy-btn")
      .text(`Buy ${coinName}`)
      .css("background-color", "green");
  });

  $(document).on("click", ".sell-btn", function () {
    $(this).css("background-color", "red").css("color", "white");
    $(this)
      .closest(".trading-buttons")
      .find(".buy-btn")
      .css("background-color", "white")
      .css("color", "black");

    const coinName = $(this)
      .closest(".profile-screen")
      .find("#currentCoinName")
      .text();

    $(this)
      .closest(".trading-section")
      .find(".coin-buy-btn")
      .text(`Sell ${coinName}`)
      .css("background-color", "red");
  });
  // Yeni profil ekle
  $(".newProfile .content button").click(function () {
    let profileName = $(".newProfile .content input").val();
    if (profileCnt === 0) {
      $("#empty").remove(); // İlk profil ekle, "Empty" kaldır
    }

    let profile = new Profile(profileName); // Yeni profile oluşturuluyor
    addNewProfile(profile); // Yeni profil ekleniyor
    $(`#profile-screen-${profile.id} .coin[alt="${coins[profile.currentSelectionIndex].name}"]`).addClass(
      "heartbeat"
    );
  });

  // Profil silme
  $(document).on("click", ".profile .delete-profile", function (e) {
    let profileElement = $(this).closest(".profile"); // Silinen profili buluyoruz
    deleteProfile(profileElement);
    e.stopPropagation();
  });

  // Profil tıklama, o profile ait profile-screen'i gösterme
  $(document).on("click", ".profile", function () {
    let profileId = $(this).data("id"); // Profilin id'sini alıyoruz
    showProfileScreen(profileId); // Profil ekranını gösteriyoruz
  });

  // Profil ekranından çıkma (logout)
  $(document).on("click", "#logout-btn", function () {
    $(".profile-screen").hide(); // Profil ekranını gizle
    $(".container").show(); // Ana ekranı göster
  });

  $(document).on("click", "#next", function () {
    let profileId = $(this).closest(".profile-screen").data("id"); // Profil ekranını buluyoruz
    let profile = profiles.find((p) => p.id === profileId);

    if (profile.currentDay+1 < 365) {
      next_day(profile); // next_day fonksiyonunu tetikleyerek güncellemeyi yapıyoruz
    }
  });

  let timer = null;
  $(document).on("click", "#play", function () {
    let profileId = $(this).closest(".profile-screen").data("id");
    let profile = profiles.find((p) => p.id === profileId);
    if (profile.currentDay < 365) {
      if (timer === null) {
        timer = setInterval(() => next_day(profile, timer), 100);
        $(this).text("Pause");
      } else {
        clearInterval(timer);
        $(this).text("Play");
        timer = null;
      }
    }
  });

  $(document).on("click", ".coin", function () {
    // Tıklanan coin'in image ve name verilerini al
    let coinImage = $(this).data("image");
    let coinName = $(this).data("name");
    let profileId = $(this).closest(".profile-screen").data("id");
    let profile = profiles.find((p) => p.id === profileId);
    profile.currentCoin = { name: coinName, image: coinImage }; // Coin bilgilerini profilin currentCoin'ine kaydet

    // 'currentCoin' bölümündeki img ve span öğelerini güncelle
    $(`#profile-screen-${profileId} #currentCoinImage`).attr(
      "src",
      "./images/" + coinImage
    );
    $(`#profile-screen-${profileId} #currentCoinName`).text(coinName);
    $(`#profile-screen-${profileId} .coin-buy-btn`).text(`Buy ${coinName}`);
    $(`#profile-screen-${profileId} .column`).remove(); // eski veriyi sil
    $(`#profile-screen-${profileId} .coin[alt="${coins[profile.currentSelectionIndex].name}"]`).removeClass(
      "heartbeat"
    ); // Bir önceki objeyi bulup animasyonunu kaldırdık
    profile.currentSelectionIndex = coins.map((e) => e.name).indexOf(coinName);
    let day = profile.currentDay;
    profile.currentDay = 0;
    profile.currentMonthIndex = 0;
    profile.minLimit =
      market[profile.currentDay].coins[profile.currentSelectionIndex].low -
      Math.floor(
        market[profile.currentDay].coins[profile.currentSelectionIndex].low
      ) /
        10;
    profile.maxLimit =
      market[profile.currentDay].coins[profile.currentSelectionIndex].high +
      Math.floor(
        market[profile.currentDay].coins[profile.currentSelectionIndex].high
      ) /
        10;
    for (let i = 0; i < day; i++) {
      next_day(profile);
    }

      $(this).addClass("heartbeat");
  });

  $(document).on("click", ".coin-buy-btn", function () {
    const profileScreen = $(this).closest(".profile-screen");
    const profileId = profileScreen.data("id");
    const profile = profiles.find((p) => p.id === profileId);
    const amount = parseFloat(profileScreen.find(".amount-input input").val());

    if (isNaN(amount) || amount <= 0) {
      alert("Please enter a valid amount!");
      return;
    }

    const tradeType =
      profileScreen.find(".buy-btn").css("background-color") ===
      "rgb(0, 128, 0)"
        ? "buy"
        : "sell";
    executeTrade(profile, tradeType, amount);
  });

  $(document).on("input", ".amount-input input", function () {
    let profileScreen = $(this).closest(".profile-screen");
    let profileId = profileScreen.data("id");
    let profile = profiles.find((p) => p.id === profileId);
    let coinAmount = $(this).val().trim();

    if (coinAmount === "" || isNaN(coinAmount)) {
      profileScreen.find(".dollar-amount").text("= $0.00");
      return;
    }

    const amount = parseFloat(coinAmount);
    const currentPrice =
      market[profile.currentDay].coins[profile.currentSelectionIndex].close;
    const dollarValue = (amount * currentPrice).toFixed(5);
    profileScreen.find(".dollar-amount").text(`= $${dollarValue}`);
  });

  $(".amount-input").html(`
    <input type="text" placeholder="Enter coin amount">
    <div class="dollar-amount"></div>
  `);
});

// Yeni profil ekle
function addNewProfile(profile) {
  profiles.push(profile); // Profili listeye ekle

  // Yeni profil HTML öğesini oluşturuyoruz
  $(".profiles").append(
    `<div class="profile" data-id="${profile.id}">
      <img src="./images/profile.png" alt="Profile Picture">
      <p>${profile.name}</p>
      <button class="delete-profile">X</button>
    </div>`
  );
  $("#empty").remove();
  
  profileCnt++;

  // Yeni profil ekranını da oluşturuyoruz (her profil için benzersiz)
  $("body").append(
    `<div class="profile-screen" id="profile-screen-${profile.id}" data-id="${
      profile.id
    }">
      <div class="heading_profile">
        <div class="left">
          <h2>CTIS</h2>
          <span style="font-size: 18px">Crypto Trading Information System</span>
        </div>
        <div class="right">
          <div class="userIcon">
          <img src="./images/profile.png">
          <span id="name">${profile.name}</span>
          </div>
          <div id="logout-btn">
          <img src="./images/logout.png">
          <button type="button" >Logout</button>
          </div>
        </div>
      </div>
      <div class="day">
        <h1 id="days">${profile.currentDay} Day</h1>
        <h2 id="month">${formatDate(
          profile
        )}</h2> <!-- Profilin tarihini buradan alıyoruz -->
        <div>
          <button type="button" id="next">Next day</button>
          <button type="button" id="play">Play</button>
        </div>
      </div>
      <div class="table">
        <div class="coinList">
          ${coins
            .map(
              (coin) =>
                `<img src="./images/${coin.image}" alt="${coin.name}" class="coin" data-name="${coin.name}" data-image="${coin.image}">`
            )
            .join("")}
        </div>
        <div class="currentCoin">
          <img id="currentCoinImage" src="./images/${
            coins[profile.currentSelectionIndex].image
          }">
          <span id="currentCoinName">${
            coins[profile.currentSelectionIndex].name
          }</span>
          <div class="currentCoinInfo"></div>
          </div>
        <div class="coinInfo">
        <div class="maxLimit"></div>
          <div class="chartContainer">
            
          </div>
          <div class="minLimit"></div>
        </div>
        <!-- Trading table -->
        <div class="balance">$1000.00</div>
        <div class="trading_table">
        
    <div class="trading-section">
        <div class="section-title">Trading</div>
        <div class="trading-buttons">
            <button class="buy-btn">Buy</button>
            <button class="sell-btn">Sell</button>
        </div>
        
        <div class="amount-input">
              <input type="text" placeholder="Enter coin amount">
              <div class="dollar-amount">= $0.00</div>
          </div>
        
        <button class="coin-buy-btn">Buy ${
          coins[profile.currentSelectionIndex].name
        }</button>
    </div>
    
    <div class="wallet-section">
        <div class="section-title">Wallet</div>
        <table class="wallet_table">
            <tr>
                <th>Coin</th>
                <th>Amount</th>
                <th>Subtotal</th>
                <th>Last Close</th>
            </tr>
            <tr>
                <td>Dolar</td>
                <td>$1000.00</td>
                <td></td>
                <td></td>
            </tr>
        </table>
    </div>
</div>
      </div>
    </div>`
  );

  $(".newProfile .content input").val(""); // Input alanını temizle
  $(".newProfile").css("visibility", "hidden");
  $(".overlay").fadeOut();
  next_day(profile);
  saveProfilesToLocalStorage();
  //first dashed bar
  // $(".chartContainer").append(`<div class="close" style="top:${
  //   ((profile.Maxlimit - profile.close) / profile.range) * 100
  // }%">
  //                          <div class="line"></div>
  //                          <div class="value">$${profile.close}</div>
  //                        </div>`);
  $(".chartContainer").on("mousemove", ".candleStickGroup", function (e) {
    $(".currentCoinInfo").html(
      `<span class="info">Date: ${market[profile.currentDay-1].date} Open: $${$(this).data(
        "open"
      )} Close: $${$(this).data("close")} High: $${$(this).data(
        "high"
      )} Low: $${$(this).data("open")} </span>`
    );
  });

  $(".chartContainer").on("mouseleave", ".candleStickGroup", function (e) {
    $(".currentCoinInfo").html("");
  });
}

// Profil sil
function deleteProfile(profileElement) {
  let profileName = profileElement.find("p").text();
  profiles = profiles.filter((profile) => profile.name !== profileName); // Listeden çıkar
  profileElement.remove();
  profileCnt--;

  // Eğer profil silindiyse ve başka profil yoksa, "Empty" mesajını ekleyelim
  if (profileCnt === 0) {
    $(".profiles").append(`<p id="empty">Empty</p>`); // Profil listesi boşsa "Empty" mesajı ekle
  }

  // Profil ekranı görünüyorsa, onu gizle
  $(".profile-screen").hide();

  // Eğer ana ekranda ise, ona geri dönüyoruz
  $(".container").show();
  saveProfilesToLocalStorage();
}

// Profil ekranını göster
function showProfileScreen(profileId) {
  // Tüm profile-screen'leri gizle
  $(".profile-screen").hide();

  // İlgili profile-screen'i göster
  $("#profile-screen-" + profileId).show();

  // Ana ekranı gizle
  $(".container").hide();
}

 let months = [
   { name: "January"},
   { name: "February"}, // Şubat için varsayılan olarak 28 gün
   { name: "March"},
   { name: "April"},
   { name: "May"},
   { name: "June"},
   { name: "July"},
   { name: "August"},
   { name: "September"},
   { name: "October"},
   { name: "November" },
   { name: "December" },
 ];

// Formatlanmış tarih string'i oluşturulması
// Formatlanmış tarih string'i oluşturulması
function formatDate(profile) {
  return `${parseInt(market[profile.currentDay].date.substring(0, 2))} ${months[parseInt((market[profile.currentDay].date).substring(3,5))-1].name} ${market[profile.currentDay].date.substring(6)}`;}

function formatDateInfo(profile) {
  return `${
    profile.monthDay + 1 < 10
      ? "0" + (profile.monthDay + 1)
      : profile.monthDay + 1
  }-${
    profile.currentMonthIndex + 1 < 10
      ? "0" + (profile.currentMonthIndex + 1)
      : profile.currentMonthIndex + 1
  }-${2021}`;
}

// Güncelleme fonksiyonu
// Güncelleme fonksiyonu
function next_day(profile, timer) {
  let profileScreen = $("#profile-screen-" + profile.id); // İlgili Profil ekranını buluyoruz

  // Eğer o ayın sonuna geldiyssek, ayı değiştir
 

  //Grafik Düzenleme (Emir)
  // profile.currentSelection = coins[currentDay];
  var info = market[profile.currentDay].coins[profile.currentSelectionIndex];
  var open = info.open;
  var close = info.close;
  var high = info.high;
  var low = info.low;
  var color = open < close ? "green" : "red";
  var minLimit = low - Math.floor(low) / 10; // Current MinLimit profilde eskisi var
  var maxLimit = high + Math.floor(high) / 10; // Current MaxLimit profilde eskisi var

  var temprange = profile.maxLimit - profile.minLimit; // Değişmeye aday range (değişebilir veya değişmeyebilir)

  if (minLimit < profile.minLimit) {
    profile.minLimit = minLimit; // Profilin minlimitini yeni değerle değiştirdik.
    console.log("minlim değişti");
    console.log(profile.maxLimit);
  }

  if (maxLimit > profile.maxLimit) {
    profile.maxLimit = maxLimit; // Profilin maxlimitini yeni değerle değiştirdik.
    console.log("maxlim değişti");
  }
  var currange = profile.maxLimit - profile.minLimit; // Değişen yeni range (değişmiştir veya değişmemiştir)

  console.log("SCALE ÇAĞIRILMADAN ÖNCEKİ RANGELER", temprange, currange);

  if (temprange != currange) {
    // Ekleme sonrası range'de değişim yaşandıysa scale'i değiştir
    changeScale(temprange, currange);
    console.log("çağırıldı");
  }

  profileScreen.find(".chartContainer .close").remove();

  console.log("Eski ve yeni range:", temprange, currange);
  
  profileScreen.find(".minLimit").html("$" + profile.minLimit.toFixed(1));
  profileScreen.find(".maxLimit").html("$" + profile.maxLimit.toFixed(1));
  
  profile.currentDay++; // Günün sayısını artırıyoruz
  
  profileScreen.find(".chartContainer").append(`
  <div class="column">
  <div data-date="${formatDateInfo(
    profile
  )}" data-open="${open}" data-close="${close}" data-high="${high}" data-low="${low}" class="candleStickGroup" style="height:${
    ((high - low) / currange) * 100
  }%;bottom:${((low - profile.minLimit) / currange) * 100}%;">
  <div class="stick"></div>
  <div class="candle" style="background:${color};height:${
    (Math.abs(open - close) / (high - low)) * 100
  }%; bottom:${
    ((Math.min(open, close) - low) / (high - low)) * 100
  }%;"></div>            
  </div>
  </div>
  `);

  //Gray dashed bar
  profileScreen.find(".chartContainer").append(`<div class="close" style="top:${
    ((profile.maxLimit - close) / currange) * 100
  }%">
    <div class="line"></div>
    <div class="value">$${close}</div>
    </div>`);

  if (profile.currentDay > 120) {
    profileScreen.find(".column").first().remove();
  }

  //Portföy değerini hesapla ve güncelle
  const portfolioValue = calculatePortfolioValue(profile);
  profileScreen.find(".balance").text(`$${portfolioValue.toFixed(2)}`);

  updateWalletDisplay(profile);


  // Gün ve ay bilgisini güncelle
  profileScreen.find("#days").text(`${profile.currentDay + 1} Day`); // Toplam gün sayısını güncelliyoruz
  profileScreen.find("#month").text(formatDate(profile)); // Profilin tarih bilgisini güncelliyoruz

  if (profile.currentDay === 364) {
    $("#play").text("Play");
    clearInterval(timer);
    timer = null;
    profileScreen.find(".trading-section").remove();
    profileScreen.find(".balance").addClass("heartbeat");
    return;
  }

  saveProfilesToLocalStorage();
}

function changeScale(temprange, currange) {
  $(".candleStickGroup").each(function () {
    let height = $(this).height();
    let heightPercentage = (height / $(this).parent().height()) * 100;
    let newPercentage = temprange / currange;
    let bottom = $(this).parent().height() - height - $(this).position().top;
    let bottomPercentage = (bottom / $(this).parent().height()) * 100;
    $(this).css("height", `${heightPercentage * newPercentage}%`);
    $(this).css("bottom", `${bottomPercentage * newPercentage}%`);
  });
}

function executeTrade(profile, type, coinAmount) {
  const coinPrice =
    market[profile.currentDay].coins[profile.currentSelectionIndex].close;
  const coinName = coins[profile.currentSelectionIndex].name;
  const dollarAmount = coinAmount * coinPrice;

  if (type === "buy") {
    if (profile.wallet.USD < dollarAmount) {
      alert("Insufficient balance!");
      return false;
    }

    // Update wallet
    profile.wallet.USD -= dollarAmount;
    profile.wallet.coins[coinName] =
      (profile.wallet.coins[coinName] || 0) + coinAmount;
  } else if (type === "sell") {
    if (
      !profile.wallet.coins[coinName] ||
      profile.wallet.coins[coinName] < coinAmount
    ) {
      alert("Insufficient coins!");
      return false;
    }

    // Update wallet
    profile.wallet.USD += dollarAmount;
    profile.wallet.coins[coinName] -= coinAmount;

    // Remove coin from wallet if amount is 0
    if (profile.wallet.coins[coinName] <= 0.00000001) {
      delete profile.wallet.coins[coinName];
    }
  }

  updateWalletDisplay(profile);
  saveProfilesToLocalStorage();
  return true;
}

function updateWalletDisplay(profile) {
  const profileScreen = $(`#profile-screen-${profile.id}`);
  const walletTable = profileScreen.find(".wallet_table");
  // const currentPrice = market[profile.currentDay].coins[profile.currentSelectionIndex].close;

  walletTable.find("tr:gt(0)").remove();

  walletTable.append(`
    <tr>
      <td>USD</td>
      <td>$${profile.wallet.USD.toFixed(2)}</td>
      <td>$${profile.wallet.USD.toFixed(2)}</td>
      <td>$1.00</td>
    </tr>
  `);

  Object.entries(profile.wallet.coins).forEach(([coin, amount]) => {
    const coinIndex = coins.findIndex((c) => c.name === coin);
    const price = market[profile.currentDay].coins[coinIndex].close;
    const subtotal = amount * price;

    walletTable.append(`
      <tr>
        <td>${coin}</td>
        <td>${amount.toFixed(8)}</td>
        <td>$${subtotal.toFixed(2)}</td>
        <td>$${price.toFixed(2)}</td>
      </tr>
    `);
  });

  const portfolioValue = calculatePortfolioValue(profile);
  profileScreen.find(".balance").text(`$${portfolioValue.toFixed(2)}`);
  // profileScreen.find('.balance').text(`$${profile.wallet.USD.toFixed(2)}`);
}

function calculatePortfolioValue(profile) {
  let totalValue = profile.wallet.USD;

  Object.entries(profile.wallet.coins).forEach(([coin, amount]) => {
    const coinIndex = coins.findIndex((c) => c.name === coin);
    const currentPrice = market[profile.currentDay].coins[coinIndex].close;
    totalValue += amount * currentPrice;
  });

  return totalValue;
}


function saveProfilesToLocalStorage() {
  localStorage.setItem("profiles", JSON.stringify(profiles));
}

function loadProfilesFromLocalStorage() {
  const savedData = localStorage.getItem("profiles");
  if (savedData) {
    const savedProfiles = JSON.parse(savedData);
    savedProfiles.forEach((savedObj) => {
      const loadedProfile = new Profile(savedObj.name);
      Object.assign(loadedProfile, savedObj);
      console.log(loadedProfile.currentDay);
      let day = loadedProfile.currentDay;
      loadedProfile.currentDay = 0;
      addNewProfile(loadedProfile);
      for (let i = 0; i < day-1; i++) {
        next_day(loadedProfile);
        console.log("asdasd");
      }
          $(
            `#profile-screen-${loadedProfile.id} .coin[alt="${
              coins[loadedProfile.currentSelectionIndex].name
            }"]`
          ).addClass("heartbeat");
    
    });
    // Update profileCnt
    // profileCnt = profiles.length;
  }
}

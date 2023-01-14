# Supermarket-Self-Checkout-App
This is an Android mobile application that allows users to self checkout at a supermarket. This is done by scanning the shopping items with the scanner on the app, and eventually paying for the scanned items using M-Pesa. This application was created to combat long queues in supermarkets and give users alternative checkout options in Kenya.

## Colaborators
This is an individual project with no other collaborators

## Languages and Tools used
- Java language
- Firebase Authentication
- Firebase Realtime Database
- Daraja Mpesa API

# Application Walk through
## Splash Screen
<p> This is the first page of the application upon startup</p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/splashscreen.jpeg" width="200" height="400"/>

## Login Page
<p> After startup, the user is directed to the Login page. Here, users who already have an account can simply put in their details and log in. Users who don't have an account can click "Signup" in order to create a new account.</p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/login.jpeg" width="200" height="400"/>

## Create Account
<p> Here, users who do not already have an account can create a new account by providing their relevant details.</p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/createAccount.jpeg" width="200" height="400"/>

## Home page- Shopping List Tab
<p>Once a user has succesfully logged in, they are directed to the Application Home page. Here there are two tabs: the Shoppig List tab and the Cart tab. The user can easily swipe between these two tabs. Lastly, the home page contains a camera button. This button allows the user to scan their shoppign item.</p>
<p>The Shopping List Tab allows the user to create their shopping list from items avaialable in the supermarket. The user can also increase or reduce the quantity of the item they have added as well as delete it all together. The shoppign list also provides the user with cost estimates of the items they have added. </p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/shoppinglist.jpeg" width="200" height="400"/>

### On Clicking "ADD"
<p>The add button allows the user to see their options and add them to their shopping list as shown below </p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/shoppingListOptions.jpeg" width="200" height="400"/>

## Home page- Cart Tab
<p>The Cart tab shows scanned items that have been added to the users cart while shopping. The user can also delete items in their cart from this page, or reduce the quantity of the items. </p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/cart.jpeg" width="200" height="400"/>

## Scanner
<p>The scanner is accessed by clickign the camera icon on the home page. This scanner allows users to scan the item bar codes in order to add them to their cart. </p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/cameraScanner.jpeg" width="200" height="400"/>

### On Scanning
<p>Once an item has been scanned, the items details pop up and the user is able to add it to their cart as shown below </p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/addingScannedItem.jpeg" width="200" height="400"/>

## Checkout
<p>A user is directed to the checkout page once they click the checkout button on the Cart Page. Here they can proceeed to pay for their items </p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/checkoutPage.jpeg" width="200" height="400"/>

### New user Checking out
<p>If this is the first time a user is checking out, they are first prompted to input their Mpesa number when they land on the Checkout page </p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/EnterMpesaNUmber.jpeg" width="200" height="400"/>

### On Clicking "Checkout"
<p>Once a User clicks Checkout, they are prompted for their Mpesa Pin</p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/MpesaMEssage1.jpeg" width="200" height="400"/>

### on Succesful Payment
<p>Once a User succesfully pays, they get an Mpesa Message confirming the same</p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/MpesaMessage2.jpeg" width="200" height="400"/>

<p>On top of this, they can view a receipt of the transaction at the Receipt page</p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/Receipts.jpeg" width="200" height="400"/>

## Additional Functionality
### Side Bar
<p>The application Side Bar found on the Homepage allows the user to access some additional functionality of the application such as seeing their Receipts, seeing their account information and serachign for the isle of particular products</p>
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/sidebar.jpeg" width="200" height="400"/>

### Finding Product Isle
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/checkForItemIsle.jpeg" width="200" height="400"/>


### Account Page
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/accountPage.jpeg" width="200" height="400"/>

### Receipts Page
<img src="https://github.com/Kendi42/Supermarket-Self-Checkout-App/blob/73df9f07ac6f829e1423cb359e240765ba22542e/Application%20Screenshots/Receipts.jpeg" width="200" height="400"/>


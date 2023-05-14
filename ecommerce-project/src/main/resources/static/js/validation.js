function validateProduct(){
	
	var productName = document.getElementById("productName").value;
	var productBrand = document.getElementById("productBrand").value;
	var productModel = document.getElementById("productModel").value;
	var productPrice = document.getElementById("productPrice").value;
	var unitStock = document.getElementById("unitStock").value;
	var productCategory = document.getElementById("productCategory").value;
	var productDescription = document.getElementById("productDescription").value;
	var discount = document.getElementById("discount").value;
	var imageUrl = document.getElementById("imageUrl").value;
	var c01 = document.getElementById("c01");
	var c02 = document.getElementById("c02");
	
	var productNameError = document.getElementById("productNameError");
	var productBrandError = document.getElementById("productBrandError");
	var productModelError = document.getElementById("productModelError");
	var productPriceError = document.getElementById("productPriceError");
	var unitStockError = document.getElementById("unitStockError");
	var productCategoryError = document.getElementById("productCategoryError");
	var productDescriptionError = document.getElementById("productDescriptionError");
	var discountError = document.getElementById("discountError");
	var imageUrlError = document.getElementById("imageUrlError");
	var productStatusError = document.getElementById("productStatusError");
	
	var productPriceNum = Number(productPrice);
	var unitStockNum = Number(unitStock);
	var discountNum = Number(discount);
	
	var returnValue = true;
	
	if(productName === "" || productName.length > 40){
		productNameError.style.visibility = "visible";
		returnValue = false;
	}else {
		productNameError.style.visibility = "hidden";
	}
	
	if(productBrand === "" || productBrand.length > 40){
		productBrandError.style.visibility = "visible";
		returnValue = false;
	}else {
		productBrandError.style.visibility = "hidden";
	}
	
	if(productModel === "" || productModel.length > 40){
		productModelError.style.visibility = "visible";
		returnValue = false;
	}else {
		productModelError.style.visibility = "hidden";
	}
	
	if(productPrice === "" || productPriceNum < 10){
		productPriceError.style.visibility = "visible";
		returnValue = false;
	}else {
		productPriceError.style.visibility = "hidden";
	}
	
	if(unitStock === "" || unitStockNum < 0){
		unitStockError.style.visibility = "visible";
		returnValue = false;
	}else {
		unitStockError.style.visibility = "hidden";
	}
	
	if(productCategory === "" || productCategory.length > 40){
		productCategoryError.style.visibility = "visible";
		returnValue = false;
	}else {
		productCategoryError.style.visibility = "hidden";
	}
	
	if(productDescription === "" || productDescription.length > 90){
		productDescriptionError.style.visibility = "visible";
		returnValue = false;
	}else {
		productDescriptionError.style.visibility = "hidden";
	}
	
	if(discount === "" || discountNum < 0 || discountNum > 50){
		discountError.style.visibility = "visible";
		returnValue = false;
	}else {
		discountError.style.visibility = "hidden";
	}
	
	if(imageUrl === "" || imageUrl.length > 255){
		imageUrlError.style.visibility = "visible";
		returnValue = false;
	}else {
		imageUrlError.style.visibility = "hidden";
	}
	
	if(c01.checked === false && c02.checked === false){
		productStatusError.style.visibility = "visible";
		returnValue = false;
	}else {
		productStatusError.style.visibility = "hidden";
	}
	
	
	return returnValue;
}



function validateKeyword(keyword){
	var returnValue = false;
	
	if(keyword.length < 3 || keyword.length > 10){
		alert("Minimum 3 and maximum 10 letters allowed!");
	}else{
		returnValue = true;
	}
	
	return returnValue;
}

function validateRegForm(){
	var firstName = document.getElementById("firstName").value;
	var lastName = document.getElementById("lastName").value;
	var email = document.getElementById("email").value;
	var password = document.getElementById("password").value;
	var billAddress = document.getElementById("billAddress").value;
	var billCity = document.getElementById("billCity").value;
	var billState = document.getElementById("billState").value;
	var billPostcode = document.getElementById("billPostcode").value;
	var billCountry = document.getElementById("billCountry").value;
	var shippAddress = document.getElementById("shippAddress").value;
	var shippCity = document.getElementById("shippCity").value;
	var shippState = document.getElementById("shippState").value;
	var shippPostcode = document.getElementById("shippPostcode").value;
	var shippCountry = document.getElementById("shippCountry").value;
	var customerPhone = document.getElementById("customerPhone").value;
	
	var firstNameError = document.getElementById("firstNameError");
	var lastNameError = document.getElementById("lastNameError");
	var emailError = document.getElementById("emailError");
	var passwordError = document.getElementById("passwordError");
	var billAddressError = document.getElementById("billAddressError");
	var billCityError = document.getElementById("billCityError");
	var billStateError = document.getElementById("billStateError");
	var billPostcodeError = document.getElementById("billPostcodeError");
	var billCountryError = document.getElementById("billCountryError");
	var shippAddressError = document.getElementById("shippAddressError");
	var shippCityError = document.getElementById("shippCityError");
	var shippStateError = document.getElementById("shippStateError");
	var shippPostcodeError = document.getElementById("shippPostcodeError");
	var shippCountryError = document.getElementById("shippCountryError");
	var customerPhoneError = document.getElementById("customerPhoneError");
	
	var regEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/g;
	var returnValue = true;
	
	if(firstName === "" || firstName.length > 30){
		firstNameError.style.visibility = "visible";
		returnValue = false;
	}else {
		firstNameError.style.visibility = "hidden";
	}
	
	if(lastName === "" || lastName.length > 30){
		lastNameError.style.visibility = "visible";
		returnValue = false;
	}else {
		lastNameError.style.visibility = "hidden";
	}
	
	if(email === "" || email.length > 40 || !regEmail.test(email)){
		emailError.style.visibility = "visible";
		returnValue = false;
	}else {
		emailError.style.visibility = "hidden";
	}
	
	if(password.length < 6 || password.length > 30 ){
		passwordError.style.visibility = "visible";
		returnValue = false;
	}else {
		passwordError.style.visibility = "hidden";
	}
	
	if(billAddress === "" || billAddress.length > 75 ){
		billAddressError.style.visibility = "visible";
		returnValue = false;
	}else {
		billAddressError.style.visibility = "hidden";
	}
	
	if(billCity === "" || billCity.length > 40 ){
		billCityError.style.visibility = "visible";
		returnValue = false;
	}else {
		billCityError.style.visibility = "hidden";
	}
	
	if(billState === "" || billState.length > 40 ){
		billStateError.style.visibility = "visible";
		returnValue = false;
	}else {
		billStateError.style.visibility = "hidden";
	}
	
	if(billPostcode.length < 5 || billPostcode.length > 10 ){
		billPostcodeError.style.visibility = "visible";
		returnValue = false;
	}else {
		billPostcodeError.style.visibility = "hidden";
	}
	
	if(billCountry === "" || billCountry.length > 40 ){
		billCountryError.style.visibility = "visible";
		returnValue = false;
	}else {
		billCountryError.style.visibility = "hidden";
	}
	
	if(shippAddress === "" || shippAddress.length > 75 ){
		shippAddressError.style.visibility = "visible";
		returnValue = false;
	}else {
		shippAddressError.style.visibility = "hidden";
	}
	
	if(shippCity === "" || shippCity.length > 40 ){
		shippCityError.style.visibility = "visible";
		returnValue = false;
	}else {
		shippCityError.style.visibility = "hidden";
	}
	
	if(shippState === "" || shippState.length > 40 ){
		shippStateError.style.visibility = "visible";
		returnValue = false;
	}else {
		shippStateError.style.visibility = "hidden";
	}
	
	if(shippPostcode.length < 5 || shippPostcode.length > 10 ){
		shippPostcodeError.style.visibility = "visible";
		returnValue = false;
	}else {
		shippPostcodeError.style.visibility = "hidden";
	}
	
	if(shippCountry === "" || shippCountry.length > 40 ){
		shippCountryError.style.visibility = "visible";
		returnValue = false;
	}else {
		shippCountryError.style.visibility = "hidden";
	}
	
	if(customerPhone.length < 9 || customerPhone.length > 15){
		customerPhoneError.style.visibility = "visible";
		returnValue = false;
	}else {
		customerPhoneError.style.visibility = "hidden";
	}
	
	return returnValue;
}

function validateBillingAddress(){
	
	var address = document.getElementById("address").value;
	var city = document.getElementById("city").value;
	var state = document.getElementById("state").value;
	var postcode = document.getElementById("postcode").value;
	var country = document.getElementById("country").value;
	
	var addressError = document.getElementById("addressError");
	var cityError = document.getElementById("cityError");
	var stateError = document.getElementById("stateError");
	var postcodeError = document.getElementById("postcodeError");
	var countryError = document.getElementById("countryError");
	
	var returnValue = true;
	
	if(address === "" || address.length > 75){
		addressError.style.visibility = "visible";
		returnValue = false;
	}else {
		addressError.style.visibility = "hidden";
	}
	
	if(city === "" || city.length > 40){
		cityError.style.visibility = "visible";
		returnValue = false;
	}else {
		cityError.style.visibility = "hidden";
	}
	
	if(state === "" || state.length > 40){
		stateError.style.visibility = "visible";
		returnValue = false;
	}else {
		stateError.style.visibility = "hidden";
	}
	
	if(postcode.length < 5 || postcode.length > 10){
		postcodeError.style.visibility = "visible";
		returnValue = false;
	}else {
		postcodeError.style.visibility = "hidden";
	}
	
	if(country === "" || country.length > 40){
		countryError.style.visibility = "visible";
		returnValue = false;
	}else {
		countryError.style.visibility = "hidden";
	}
	
	return returnValue;
	
	
}


function validateShippingAddress(){
	
	var address = document.getElementById("address").value;
	var city = document.getElementById("city").value;
	var state = document.getElementById("state").value;
	var postcode = document.getElementById("postcode").value;
	var country = document.getElementById("country").value;
	
	var addressError = document.getElementById("addressError");
	var cityError = document.getElementById("cityError");
	var stateError = document.getElementById("stateError");
	var postcodeError = document.getElementById("postcodeError");
	var countryError = document.getElementById("countryError");
	
	var returnValue = true;
	
	if(address === "" || address.length > 75){
		addressError.style.visibility = "visible";
		returnValue = false;
	}else {
		addressError.style.visibility = "hidden";
	}
	
	if(city === "" || city.length > 40){
		cityError.style.visibility = "visible";
		returnValue = false;
	}else {
		cityError.style.visibility = "hidden";
	}
	
	if(state === "" || state.length > 40){
		stateError.style.visibility = "visible";
		returnValue = false;
	}else {
		stateError.style.visibility = "hidden";
	}
	
	if(postcode.length < 5 || postcode.length > 10){
		postcodeError.style.visibility = "visible";
		returnValue = false;
	}else {
		postcodeError.style.visibility = "hidden";
	}
	
	if(country === "" || country.length > 40){
		countryError.style.visibility = "visible";
		returnValue = false;
	}else {
		countryError.style.visibility = "hidden";
	}
	
	return returnValue;
	
}

function ValidatePassword() {
	var password = document.getElementById("password").value;
	var confirmpass = document.getElementById("confirmpass").value;
	if (password != confirmpass) {
		alert("Password does Not Match.");
		return false;
	}
	return true;
};

function validateNumber(e) {
	var pattern = /^\d{0,4}(\.\d{0,4})?$/g;

	return pattern.test(e.key)
};

function validatePhoneNumber() {
	var customerPhone = document.getElementById("customerPhone").value;
	var customerPhoneError = document.getElementById("customerPhoneError");

	var returnValue = true;

	if (customerPhone.length < 9 || customerPhone.length > 15) {
		customerPhoneError.style.visibility = "visible";
		returnValue = false;
	} else {
		customerPhoneError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateMessage(){
	var text = document.getElementById("text").value;
	var textError = document.getElementById("textError");
	text = text.trim();
	
	var returnValue = true;
	
	if(text === "" || text.length > 255){
		textError.style.visibility = "visible";
		returnValue = false;
	}else {
		textError.style.visibility = "hidden";
	}
	
	return returnValue;
}
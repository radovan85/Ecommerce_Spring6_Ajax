window.onload = redirectHome;

function redirectLogin() {
	$("#ajaxLoadedContent").load("/login");
}

function redirectRegister() {
	$("#ajaxLoadedContent").load("/userRegistration");
}

function redirectHome() {
	$("#ajaxLoadedContent").load("/home");
}

function redirectAdmin() {
	$("#ajaxLoadedContent").load("/admin/");
}

function redirectProductList() {
	$("#ajaxLoadedContent").load("/products/allProducts");
}

function redirectNewProduct() {
	$("#ajaxLoadedContent").load("/admin/addNewProduct");
}

function redirectAbout() {
	$("#ajaxLoadedContent").load("/aboutUs");
}

function redirectAllCustomers() {
	$("#ajaxLoadedContent").load("/admin/allCustomers");
}

function redirectCart() {
	$("#ajaxLoadedContent").load("/cart/viewCart");
}

function redirectAddMessage() {
	$("#ajaxLoadedContent").load("/message/sendMessage");
}

function redirectConfirmShipping() {
	$("#ajaxLoadedContent").load("/order/shippingConfirmation");
}

function redirectPhoneConfirmation() {
	$("#ajaxLoadedContent").load("/order/phoneConfirmation");
}

function cancelCheckout() {
	$("#ajaxLoadedContent").load("/order/cancel");
}



function redirectMessageDetails(messageId) {
	$("#ajaxLoadedContent").load("/admin/messageDetails/" + messageId);
}

function redirectAllMessages() {
	$("#ajaxLoadedContent").load("/admin/allMessages");
}

function redirectOrderDetails(orderId) {
	$("#ajaxLoadedContent").load("/admin/getOrder/" + orderId);
}

function redirectAllOrders() {
	$("#ajaxLoadedContent").load("/admin/allOrders");
}

function redirectCustomerDetails(customerId){
	$("#ajaxLoadedContent").load("/admin/getCustomer/" + customerId);
}

function redirectProductDetails(productId){
	$("#ajaxLoadedContent").load("/products/getProduct/" + productId);
}



function confirmLoginPass() {
	$.ajax({
		url : "http://localhost:8080/loginPassConfirm",
		type : "POST"	
	})
	.done(function(){
		checkForSuspension();
	})
	.fail(function(){
		$("#ajaxLoadedContent").load("/loginErrorPage");
	})
}

function checkForSuspension() {
	$.ajax({
		url : "http://localhost:8080/suspensionChecker",
		type : "POST"
	})
	.done(function(){
		window.location.href = "/";
	})
	.fail(function(){
		alert("Account suspended!");
		redirectLogout();
	})
}

function redirectLogout() {
	$.ajax({
		url : "http://localhost:8080/loggedout",
		type : "POST"
	})
	.done(function(){
		window.location.href = "/";
	})
	.fail(function(){
		alert("Failed!");
	})
}


function updateProduct(productId) {
	$("#ajaxLoadedContent").load("/admin/updateProduct" + "/" + productId);
};

function deleteProduct(productId) {
	if (confirm("Are you sure you want to remove this product?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteProduct/" + productId
		})
		.done(function(){
			redirectProductList();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
	;
};


function executeOrder() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/order/createOrder"
	})
	.done(function(){
		$("#ajaxLoadedContent").load("/order/orderExecuted");
	})
	.fail(function(){
		$("#ajaxLoadedContent").load("/order/stockProblem");
	})
};

function deleteItem(cartId, itemId) {
	if (confirm("Remove item from cart?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/cart/removeCartItem/" + cartId + "/"
					+ itemId
		})
		.done(function(){
			redirectCart();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function clearCart(cartId) {
	if (confirm("Are you sure you want to clear cart?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/cart/removeAllItems/" + cartId
		})
		.done(function(){
			redirectCart();
		})
		.fail(function(){
			alert("Failed!");
		})
	}	
};

function addToCart(productId) {
	console.log(productId)
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/cart/add/" + productId
	})
	.done(function(){
		alert("Added To Cart")
		$("#ajaxLoadedContent").load("/products/getProduct/" + productId);
	})
	.fail(function(){
		alert("Failed!");
	})
};

function redirectCheckout() {
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/order/billingConfirmation"
	})
	.done(function(){
		$("#ajaxLoadedContent").load("/order/billingConfirmation");
	})
	.fail(function(){
		$("#ajaxLoadedContent").load("/order/cartError");
	})
};


function deleteMessage(messageId) {
	if (confirm("Are you sure you want to clear this message?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteMessage/" + messageId
		})
		.done(function(){
			redirectAllMessages();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteCustomer(customerId) {
	if (confirm("Are you sure you want to remove this customer?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteCustomer/" + customerId
		})
		.done(function(){
			redirectAllCustomers();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function suspendUser(userId) {
	if (confirm("Are you sure you want to suspend this user?")) {
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/admin/suspendUser/" + userId
		})
		.done(function(){
			redirectAllCustomers();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteOrder(orderId) {
	if (confirm("Are you sure you want to clear this order?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteOrder/" + orderId
		})
		.done(function(){
			redirectAllOrders();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};



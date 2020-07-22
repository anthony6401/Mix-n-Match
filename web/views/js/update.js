const form = document.getElementById("form");



// Checking the address
setInterval(function () {
    validateAddress();
}, 1000);


// Used for validating address
var valBefore = "";


function validateAddress() {
    var addr = document.getElementById("address");
    var addr_value = document.getElementById("autocomplete").value;
    if (addr_value != "" && addr_value != valBefore) {
        var geocoder = new google.maps.Geocoder();

        geocoder.geocode({
            'address': addr_value
        }, function (results, status) {
            var addressError = document.getElementById("addressError");
            if (status === google.maps.GeocoderStatus.OK && results.length > 0) {
                if (addressError != null) {
                    addressError.parentNode.removeChild(addressError);
                }
            } else {
                if (addressError == null) {
                    var createAddressError = document.createElement("P");
                    createAddressError.setAttribute("id", "addressError");
                    createAddressError.setAttribute("class", "error");
                    createAddressError.innerText = "Invalid address!";
                    addr.appendChild(createAddressError);
                }
            }

        });

        valBefore = addr_value;
    }

};
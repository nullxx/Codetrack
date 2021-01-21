function onLogin() {
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var data = {
        email,
        password,
    }
    loginFalse(jsToSwtCallback("LOGIN", JSON.stringify(data)));
}

function loginFalse(success) {
    document.getElementById('loginFalse').style.display = success ? 'none' : 'block';
}
function showUser() {
    document.getElementById("loginSection").style.visibility="false";
    document.getElementById("username").innerText = getCookie("username");
}

function signOut() {
    document.getElementById("username").innerText = "";
    document.getElementById("loginSection").style.visibility="true";
    setCookie("username", "", -1);
    setCookie("password","", -1);

}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
    }
    return "";
}
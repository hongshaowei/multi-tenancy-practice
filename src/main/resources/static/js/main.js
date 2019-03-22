$(document).ready(function() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        url: "/users",
        type: "POST",
        data: {tenantId : tenantId},
        datatype: "text",
        async:false,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(users) {
            console.log(users);
        }
    });

    // navbar toggle icon
    $("#navbar-toggler-icon").click(function() {
        $(".sideBar").toggle();
    });
});


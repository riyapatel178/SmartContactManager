$(document).ready(function () {
        // Move the function outside the document ready callback
        function toggleSidebar() {
            if ($(".sidebar").is(":visible")) {
                $(".sidebar").css("display", "none");
                $(".content").css("margin-left", "0%");
            } else {
                $(".sidebar").css("display", "block");
                $(".content").css("margin-left", "20%");
            }
        }

        // Attach the click event handler to the icon
        $(".fas.fa-bars").click(function () {
            toggleSidebar();
        });
    });
    
    
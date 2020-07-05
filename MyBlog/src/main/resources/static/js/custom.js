/*global $, window, WOW*/

$(function () {
    
    "use strict";
    
    var nav = $(".vertical-nav"),
        progressCheck = false;
		
    /*========== Loading  ==========*/
    $('.preloader').delay(200).fadeOut(700, function () {
        $(this).remove();
    });
    
    /*========== Active Menu  ==========*/
    $(".vertical-nav .toggle-menu").on("click", function () {
        nav.toggleClass("menu-active");
    });
	
	/*========== Typed  ==========*/
    $(".home .home-intro p span").typed({
        strings: ["Coder.", "IT 民工.","ACG."],
        cursorChar: "",
        typeSpeed: 70,
        loop: true,
        backSpeed: 20
    });
    
    /*========== Smooth Scroll  ==========*/
    $(".vertical-nav .mini-menu > ul li a").on("click", function (e) {
        e.preventDefault();
		var selector = $(this);
		selector.addClass("active").parent().siblings("li").find("a").removeClass("active");
		$(selector.attr('href')).addClass('active').siblings("section").removeClass('active');
    });
	
	/*========== Skills Progress  ==========*/
    function skillsPogress() {
        $('.chart').easyPieChart({
            size: 140,
            barColor: '#2a2a2b',
            trackColor: '#121213',
            scaleColor: false,
            lineWidth: 2,
            scaleLength: 4,
            lineCap: 'circle',
            animate: {
                duration: 2000,
                enabled: true
            }
        });
    }
    
    if (!progressCheck && $("#about").scrollTop() >= $(".skills").offset().top + 250) {
        skillsPogress();
        progressCheck = true;
    }
    
    $("#about").on("scroll", function () {
        
        if (!progressCheck && $("#about").scrollTop() >= $(".skills").offset().top + 250) {
            skillsPogress();
            progressCheck = true;
        }
        
    });
	
    /*========== Start Magnigic Popup Js ==========*/
    if ($('.portfolio-content .item')[0]) {

        $('.portfolio-content .item').magnificPopup({
            delegate: '.icon-img',
            type: 'image',
            gallery: {
                enabled: true
            }
        });
    }
	
	/*========== Ajax Contact Form  ==========*/
	$('.contact-form').on("submit", function () {
		
		var myForm = $(this),
			data = {};
		
		myForm.find('[name]').each(function() {
			
			var that = $(this),
				name = that.attr('name'),
				value = that.val();
			
			data[name] = value; 
			
		});
		
		$.ajax({
			
			url: myForm.attr('action'),
			type: myForm.attr('method'),
			data: data,
			success: function (response) {
				
				if (response == "success") {
								
					$(".contact-form").find(".form-message").addClass("success");
					$(".form-message span").text("Message Sent!");
					
				} else {
					
					$(".contact-form").find(".form-message").addClass("error");
					$(".form-message span").text("Error Sending!");
					
				}
			}
			
		});
		
		return false;
		
	});
});
var latest = 0;

$(document).ready(function() {

    /**
     * Enable protection form cross site scripting.
     * Django doesn't like post data from Javascript unless 
     * this is run beforehand.
     */
    $('html').ajaxSend(function(event, xhr, settings) {
        function getCookie(name) {
            var cookieValue = null;
            if (document.cookie && document.cookie != '') {
                var cookies = document.cookie.split(';');
                for (var i = 0; i < cookies.length; i++) {
                    var cookie = jQuery.trim(cookies[i]);
                    // Does this cookie string begin with the name we want?
                    if (cookie.substring(0, name.length + 1) == (name + '=')) {
                        cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                        break;
                    }
                }
            }
            return cookieValue;
        }
        if (!(/^http:.*/.test(settings.url) || /^https:.*/.test(settings.url))) {
            // Only send the token to relative URLs i.e. locally.
            xhr.setRequestHeader("X-CSRFToken", getCookie('csrftoken'));
        }
    });

    loadNewPosts();

});

function loadNewPosts() {

    $('#loading').slideDown('slow');

	$.getJSON('/JSON/', function(data) {
		var items = [];

		$.each(data, function(key, value) {
			var meow = '<div class="post" id="' + value.mid + '">';
			meow += '<div class="meow">';
						
			if(value.media != undefined) {
			    meow += '<div class="media">';
			    
			    if(value.media.type == 'I')
			        meow += '<img src="' + value.media.URL + '" />';
			    
			    meow += '</div>';
			}
			
			meow += '<h3>' + value.username + '</h3>';
			meow += '<p>' + value.message + '</p>';
			meow += '<div class="date">' + elapsedTime(value.time) + '</div></div>';
			meow += '<div class="displaypic"><img src="/site_media/images/' + value.uid + '.jpg"></div>';
			meow += '<div class="clear"></div></div></div>';
			items.push(meow);
		});

        $('#loading').slideUp('slow');
		$(items.join('')).hide().insertAfter('#loading').fadeIn('slow');

	});
	
}	

function elapsedTime(createdAt) {
    var ageInSeconds = (new Date().getTime() - new Date(createdAt * 1000).getTime()) / 1000;

    var s = function(n) {
        return n == 1 ? '' : 's' 
    };

    if (ageInSeconds < 0) {
        return 'just now';
    }
    if (ageInSeconds < 60) {
        var n = ageInSeconds;
        return n + ' second' + s(n) + ' ago';
    }
    if (ageInSeconds < 60 * 60) {
        var n = Math.floor(ageInSeconds/60);
        return n + ' minute' + s(n) + ' ago';
    }
    if (ageInSeconds < 60 * 60 * 24) {
        var n = Math.floor(ageInSeconds/60/60);
        return n + ' hour' + s(n) + ' ago';
    }
    if (ageInSeconds < 60 * 60 * 24 * 7) {
        var n = Math.floor(ageInSeconds/60/60/24);
        return n + ' day' + s(n) + ' ago';
    }
    if (ageInSeconds < 60 * 60 * 24 * 31) {
        var n = Math.floor(ageInSeconds/60/60/24/7);
        return n + ' week' + s(n) + ' ago';
    }
    if (ageInSeconds < 60 * 60 * 24 * 365) {
        var n = Math.floor(ageInSeconds/60/60/24/31);
        return n + ' month' + s(n) + ' ago';
    }
    var n = Math.floor(ageInSeconds/60/60/24/365);
    return n + ' year' + s(n) + ' ago';
}

// Make date parseable in IE
function fixDate (d)
{
    var a = d.split(' ');
    var year = a.pop();
    return a.slice(0, 3).concat([year]).concat(a.slice(3)).join(' ');
}

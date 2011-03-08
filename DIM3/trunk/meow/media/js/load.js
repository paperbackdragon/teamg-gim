var latest = 0;
var limit = 15;
var loaded = false;

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

     
    //Background noise is sexy
    $('body').noisy({
        intensity: 0.9, 
        size: 200, 
        opacity: 0.06,
        monochrome: false
    });

    $('#lastmeow .meow').html(parse($('#lastmeow .meow').html()));

    // Load any new posts
    schedule();

});


/**
 * Get new posts on a regular interval
 */
function schedule() {
	loadNewPosts();
	limit = 0;
	setTimeout('schedule()', 30000);
}


/**
 * Turns mentions and hashtags into clickable URLs
 */
function parse(str) {
    return str.replace(/@([A-Z0-9]+)/gi, '@<a href="/user/$1/">$1</a>').replace(/\#([A-Z0-9]+)/gi, '<a href="/tag/$1/">#$1</a>');
}


/**
 * Make a request for news posts
 */
function loadNewPosts() {

    //$('#loading').slideDown('slow');

	$.getJSON('/JSON/' + latest + '/' + limit, function(data) {
		var items = [];

		$.each(data, function(key, value) {
		    if(parseInt(value.mid) > latest)
		        latest = parseInt(value.mid);
		
			var meow = '<div class="post" id="' + value.mid + '">';
			meow += '<div class="meow">';
						
			if(value.media != undefined) {
			    meow += '<div class="media">';
			    
			    if(value.media.type == 'I')
			        meow += '<img src="' + value.media.url + '" />';
			    
			    meow += '</div>';
			}
	
            // Escape any html and parse the message for metions and hashtags
            value.message = $('<div/>').text(value.message).html();
            value.message = parse(value.message);
    
			meow += '<span class="username"><a href="/user/' + value.username + '/">' + value.username + '</a></span> <span class="realname">';
            meow += value.firstname + ' ' + value.lastname + '</span>';
			meow += '<p>' + value.message + '</p>';
			meow += '<div class="date">' + elapsedTime(value.time) + '</div></div>';
			meow += '<div class="displaypic"><a href="/user/' + value.username + '/"><img src="/site_media/images/' + value.uid + '.jpg"></a></div>';
			meow += '<div class="clear"></div></div></div>';
			items.push(meow);
		});

        if(loaded) {
		    $(items.join('')).hide().insertAfter('#loading').fadeIn(1000);
        } else {
            $(items.join('')).insertAfter('#loading');
            $('#loading').slideUp('normal');
            loaded = true;
        }

	});
	
}	


/**
 * Add a meow from the form to the db and update the stream
 */
function addMeow() {
    $.post("/addMeow/", $("#addMeow").serialize());
    $("#addMeow #text").val("");
    loadNewPosts();
}


/**
 * Convert a time from ms into a human readable, relative, time.
 */
function elapsedTime(createdAt) {
    var ageInSeconds = (new Date().getTime() - new Date(createdAt * 1000).getTime()) / 1000;

    var s = function(n) {
        return n == 1 ? '' : 's' 
    };

    if (ageInSeconds < 0) {
        return 'just now';
    }
    if (ageInSeconds < 60) {
        var n = parseInt(ageInSeconds);
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


/*
 * Make the date parsable in IE
 */
function fixDate (d) {
    var a = d.split(' ');
    var year = a.pop();
    return a.slice(0, 3).concat([year]).concat(a.slice(3)).join(' ');
}

@(username: String)

$(function() {
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
    var auctionSocket = 
        new WS("@routes.Application.auction(username).webSocketURL(request)")

    var sendMessage = function() {
    	var timestamp = showTime();
    	
        auctionSocket.send(JSON.stringify(
            {text: $("#bid").val() + ' [ sent at ' + timestamp + ' ] '}
        ))
        $("#bid").val('')
    }

　　var showTime = function() {
        var date = new Date();
        
        return date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds() + '.' + date.getMilliseconds()
    }
    var receiveEvent = function(event) {
    	var timestamp = showTime();
    	
        var data = JSON.parse(event.data)

        // Handle errors
        if(data.error) {
            auctionSocket.close()
            $("#onError span").text(data.error)
            $("#onError").show()
            return
        } else {
            $("#onAuction").show()
        }

        // Create the message element
        var el = $('<div class="message"><span></span><p></p></div>')
        
        if ( data.user == '@username'){
        	$("span", el).text("ご本人様")
        } else　if(data.user != 'ロッボト'){
        	$("span", el).text(data.user + "　様")
        } else{
        	$("span", el).text(data.user);
        }
        $("p", el).text(data.message+ ' [ received at ' + timestamp + ' ] ')
        $(el).addClass(data.kind)
        if(data.user == '@username') $(el).addClass('me')
        $('#messages').append(el)

        // Update the members list
        $("#members").html('')
        $(data.members).each(function() {
            var li = document.createElement('li');
            li.textContent = this　+ "様";
            $("#members").append(li);
        })
    }

    var handleReturnKey = function(e) {
        if(e.charCode == 13 || e.keyCode == 13) {
            e.preventDefault()
            sendMessage()
        }
    }

    $("#bid").keypress(handleReturnKey)

    auctionSocket.onmessage = receiveEvent

})
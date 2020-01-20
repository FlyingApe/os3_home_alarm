var ws;

function loadpage() {
    connect();

    $.getJSON( "/api/alarm/getAlarmsFromUser", function( data ) {
        var tableString = "";
        $.each( data, function( key, val ) {
            tableString += "<tr>";
            tableString += "<td>"+val.token+"</td>";
            tableString += "<td><form><input type='button' value='show alarm' onclick='loadlivealarm(\""+val.token+"\")' /></form></td>";
            tableString += "</tr>";
        });
        $("#alarm-table-body").html(tableString);

    });
}

function loadlivealarm(token){
    sendData(token);
}

function connect() {
    ws = new WebSocket('ws://localhost:8080/api/socket');
    ws.onmessage = function(data) {
        parseData(data.data);
    }
}

function disconnect() {
    if (ws != null) {
        ws.close();
    }
    console.log("Websocket is in disconnected state");
}

function sendData(token) {
    var data = JSON.stringify({
        'token' : token
    });
    ws.send(data);
}

function parseData(message) {
    const data = JSON.parse(message);
    $("#live-token").html(data.token);
    $("#live-status").html(data.status);
    $("#live-audio").html(data.alarmAudioOn);
    $("#live-sensor-data").html("Distance: " + data.distance + " - Movement: " + data.movement + " - Microphone: " + data.microphone);
    $("#hidden-alarmview-container").css("visibility", "visible");
}

$(function() {
    $("form").on('submit', function(e) {
        e.preventDefault();
    });
    $("#add-alarm-button").click(function(){
        var linkString = "/api/alarm/registerAlarmByToken/"+$("#add-alarm-token").val();
        $.post(linkString, function(){
            loadpage();
        });
    });
    $("#command-send").click(function(){
        var linkString = "/api/alarm/sendcommand/"+$("#live-token").html();
        $.ajax({
            type: 'POST',
            url: linkString,
            data: '{ "command" : "'+$("#command-selection").val()+'" }', // or JSON.stringify ({name: 'jonas'}),
            success: function(data) {
                console.log('data: ' + data);},
            contentType: "application/json",
            dataType: 'json'
        });
    });
});

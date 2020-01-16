import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { AlarmComponent } from './alarm/alarm.component';

export class WebSocketService {
    webSocketEndPoint = 'http://localhost:8080/ws';

    sensorData = 'user/topic/sensordata';
    stompClient: Stomp.Client;
    parentComponent: AlarmComponent;
    constructor(parentComponent: AlarmComponent) {
        this.parentComponent = parentComponent;
    }

    connect() {
        console.log('Initialize WebSocket Connection');
        const ws = new SockJS(this.webSocketEndPoint);
        this.stompClient = Stomp.over(ws);
        this.stompClient.connect({}, (frame) => {
            this.stompClient.subscribe('/user/' + frame.headers['user-name'] + '/sensordata', (sdkEvent) => {
                this.onMessageReceived(sdkEvent);
            });
            // _this.stompClient.reconnect_delay = 2000;
        }, this.errorCallBack);
    }

    disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect(null, {});
        }
        console.log('Disconnected');
    }

    // On error, schedule a reconnection attempt;
    /// TODO: Validate input model;
    errorCallBack(error: Stomp.Message) {
        console.log('errorCallBack -> ' + error);
        setTimeout(() => {
            this.connect();
        }, 5000);
    }

    sendCommand(message: string, token: string) {
        console.log('Sending command: ' + message);
        this.stompClient.send('/app/alarm/command' + '/' + token, {}, JSON.stringify(message));
    }

    /// TODO: Validate input model;
    onMessageReceived(message) {
        console.log('Received sensordata, value: ' + message);
        this.parentComponent.handleMessage(JSON.stringify(message.body));
    }
}

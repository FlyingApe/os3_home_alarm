import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { AppComponent } from './app.component';

export class WebSocketService {
    webSocketEndPoint = 'http://localhost:8080/ws';

    sensorData = 'user/topic/sensordata';
    stompClient: Stomp.Client;
    parentComponent: AppComponent;
    constructor(parentComponent: AppComponent) {
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

    sendCommand(message: string) {
        console.log('Sending command: ' + message);
        this.stompClient.send('/app/alarm/command', {}, JSON.stringify(message));
    }

    /// TODO: Validate input model;
    onMessageReceived(message) {
        console.log('Received sensordata, value: ' + message);
        this.parentComponent.handleMessage(JSON.stringify(message.body));
    }
}

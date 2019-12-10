import { Component, OnInit } from '@angular/core';
import { WebSocketService } from './WebSocketService';
import { StyleCompiler } from '@angular/compiler';

/// TODO: Limit import to required static types only;
// import * as Stomp from 'stompjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Home Alarm 3000';

  webSocketService: WebSocketService;
  sensorData: string;
  command: string;
  isConnected = false;

  ngOnInit() {
    this.webSocketService = new WebSocketService(new AppComponent());
  }

  connect(){
    this.webSocketService.connect();
    /// TODO: Check WebSocket connection state before returning state;
    this.isConnected = true;
  }

  disconnect(){
    this.webSocketService.disconnect();
    /// TODO: Check WebSocket connection state before returning state;
    this.isConnected = false;
  }

  sendCommand(){
    this.webSocketService.sendCommand(this.command);
  }

  /// TODO: Validate input model;
  handleMessage(message){
    this.sensorData = message;
  }
}

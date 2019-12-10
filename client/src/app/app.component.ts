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

  webSocketAPI: WebSocketService;
  sensorData: string;
  command: string;

  ngOnInit() {
    this.webSocketAPI = new WebSocketService(new AppComponent());
  }

  connect(){
    this.webSocketAPI.connect();
  }

  disconnect(){
    this.webSocketAPI.disconnect();
  }

  sendCommand(){
    this.webSocketAPI.sendCommand(this.command);
  }

  /// TODO: Validate input model;
  handleMessage(message){
    this.sensorData = message;
  }
}

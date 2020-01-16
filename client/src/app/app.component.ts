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
export class AppComponent {
  title = 'Home Alarm 3000';
}

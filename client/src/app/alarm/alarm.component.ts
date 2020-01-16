import { Component, OnInit } from '@angular/core';
import { Alarm, AlarmStatus } from '../models/alarm';
import { AlarmService } from './alarm.service';
import { WebSocketService } from '../WebSocketService';
import { Observable, BehaviorSubject } from 'rxjs';
import { MatDialog } from '@angular/material';
import { AlarmDialogComponent } from '../alarm-dialog/alarm-dialog.component';
import { SecurityService } from '../security.service';

@Component({
  selector: 'app-alarm',
  templateUrl: './alarm.component.html',
  styleUrls: ['./alarm.component.css']
})
export class AlarmComponent implements OnInit {
  alarms: Alarm[];
  filteredAlarms: Alarm[] = [];
  alarm: Alarm;
  alarmStatus = AlarmStatus;

  private _isLoadingFilteredAlarms: BehaviorSubject<boolean> = new BehaviorSubject(true);
  isLoadingFilteredAlarms: Observable<boolean> = this.getIsLoadingFilteredAlarms();

  headElements: string[] = ['token', 'status', 'audioOn'];

  webSocketService: WebSocketService;
  command: string;
  isConnected = false;

  private _alarmData: BehaviorSubject<string> = new BehaviorSubject("empty");
  alarmData: Observable<string> = this.getAlarmData();

  token: string;
  username: string;

  constructor(
    private alarmService: AlarmService,
    private dialog: MatDialog,
    private securityService: SecurityService
    ) { }

  ngOnInit() {
    this.alarmService.getAlarms()
        .subscribe(res => {
          this.alarms = res;
        });

    this.webSocketService = new WebSocketService(new AlarmComponent(this.alarmService, this.dialog, this.securityService));
  }

  connect() {
    this.webSocketService.connect();
    /// TODO: Check WebSocket connection state before returning state;
    this.isConnected = true;
    // this.getUsername(); /// TODO: Implement username checking.
    this.filterAlarmsForUser();
  }

  disconnect() {
    this.webSocketService.disconnect();
    /// TODO: Check WebSocket connection state before returning state;
    this.isConnected = false;
  }

  sendCommand() {
    this.webSocketService.sendCommand(this.command);
  }

  /// TODO: Validate input model;
  handleMessage(message: string) {
    this.setAlarmData(message);
  }

  getAlarmData(): Observable<string> {
    return this._alarmData.asObservable();
  }

  setAlarmData(message: string): void {
    this._alarmData.next(message);
  }

  appropriateAlarm(token: string): void {
    this.alarmService.appropriateAlarm(token)
    .subscribe(alarm => {
      this.alarm = alarm; /// TODO: Remove singular reference.
      let containsDuplicate = false;
      this.filteredAlarms.forEach(element => {
        if (element.token === alarm.token) {
          containsDuplicate = true;
        }
      });
      if (containsDuplicate) {
        this.filteredAlarms.push(alarm);
      }
    });
  }

  openDialog(alarm: Alarm): void {
    const dialogRef = this.dialog.open(AlarmDialogComponent, {
      width: '250px',
      data: this.alarmData
    });
  }

  filterAlarmsForUser(): void {
    this.alarms.forEach(alarm => {
      if (alarm.user === this.username) {
        this.filteredAlarms.push(alarm);
        this.setIsLoadingFilteredAlarms(false);
      }
    });
  }

  getIsLoadingFilteredAlarms(): Observable<boolean> {
    return this._isLoadingFilteredAlarms.asObservable();
  }

  setIsLoadingFilteredAlarms(state: boolean): void {
    this._isLoadingFilteredAlarms.next(state);
  }

  /// TODO: Implement username checking.
  // getUsername(): void {
  //   this.securityService.getUsername()
  //     .subscribe(username => {
  //       this.username = username;
  //     });
  // }
}

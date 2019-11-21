import { Component, OnInit } from '@angular/core';
import { Alarm, AlarmStatus } from '../models/alarm';
import { AlarmService } from './alarm.service';
import { LoginService } from '../login/login.service';

@Component({
  selector: 'app-alarm',
  templateUrl: './alarm.component.html',
  styleUrls: ['./alarm.component.css']
})
export class AlarmComponent implements OnInit {
  alarms: Alarm[];
  isLoadingResults = true;
  alarmStatus = AlarmStatus;

  headElements: string[] = ['id', 'name', 'status'];

  constructor(private alarmService: AlarmService, private loginService: LoginService) { }

  ngOnInit() {
    this.alarmService.getAlarms()
        .subscribe(res => {
          this.alarms = res;
          this.isLoadingResults = false;
        });
  }

  authenticated() { return this.loginService.authenticated; }
}

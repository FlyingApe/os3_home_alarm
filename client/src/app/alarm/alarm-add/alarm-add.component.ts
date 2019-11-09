import { Component, OnInit } from '@angular/core';
import { AlarmService } from '../alarm.service';
import { Alarm, AlarmStatus } from '../../models/alarm';
import { Location } from '@angular/common';

@Component({
  selector: 'app-alarm-add',
  templateUrl: './alarm-add.component.html',
  styleUrls: ['./alarm-add.component.css']
})
export class AlarmAddComponent implements OnInit {
  alarm: Alarm = new Alarm();
  alarmStatus = AlarmStatus;

  constructor(
      private alarmService: AlarmService,
      private location: Location
  ) {
  }

  ngOnInit() {
    this.alarm.status = this.alarmStatus.Disconnected;
  }

  add(): void {
    this.alarmService.addAlarm(this.alarm).subscribe(() => this.goBack());
  }

  clear(): void {
    this.alarm = new Alarm();
    this.alarm.status = this.alarmStatus.Disconnected;
  }

  goBack(): void {
    this.location.back();
  }
}

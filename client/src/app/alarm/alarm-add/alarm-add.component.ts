import { Component, OnInit } from '@angular/core';
import { AlarmService } from '../alarm.service';
import { Alarm } from '../../models/alarm';
import { Location } from '@angular/common';

@Component({
  selector: 'app-alarm-add',
  templateUrl: './alarm-add.component.html',
  styleUrls: ['./alarm-add.component.css']
})
export class AlarmAddComponent implements OnInit {
  alarm: Alarm = new Alarm();

  constructor(
      private alarmService: AlarmService,
      private location: Location
  ) { }

  ngOnInit() {
  }

  add(): void {
    this.alarmService.addAlarm(this.alarm).subscribe(() => this.goBack());
  }

  clear(): void {
    this.alarm = new Alarm();
  }

  goBack(): void {
    this.location.back();
  }
}

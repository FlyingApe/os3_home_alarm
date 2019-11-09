import { Component, OnInit } from '@angular/core';
import { Alarm, AlarmStatus } from '../models/alarm';
import { AlarmService } from './alarm.service';

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

  constructor(private alarmService: AlarmService) { }

  ngOnInit() {
    this.alarmService.getAlarms()
        .subscribe(res => {
          this.alarms = res;
          this.isLoadingResults = false;
        });
  }

  /// TODO: Implement delete function.
  delete(alarm: Alarm): void {
    this.alarms = this.alarms.filter(i => i !== alarm);
    this.alarmService.deleteAlarm(alarm).subscribe();
  }
}

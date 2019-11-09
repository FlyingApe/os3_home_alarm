import { Component, Input, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { AlarmService } from '../alarm.service';
import { Alarm } from '../../models/alarm';

@Component({
  selector: 'app-alarm-detail',
  templateUrl: './alarm-detail.component.html',
  styleUrls: ['./alarm-detail.component.css']
})
export class AlarmDetailComponent implements OnInit {
  /// TODO: Check parent inheritance.
  @Input() alarm: Alarm;

  constructor(
      private alarmService: AlarmService,
      private route: ActivatedRoute,
      private location: Location
  ) { }

  ngOnInit() {
    this.getAlarm();
  }

  getAlarm(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.alarmService.getAlarm(id)
        .subscribe(a => this.alarm = a);
  }

  updateAlarm(): void {
    this.alarmService.updateAlarm(this.alarm)
        .subscribe(() => this.goBack());
  }

  goBack(): void {
    this.location.back();
  }
}

import { Component, OnInit, Inject } from '@angular/core';
import { AlarmComponent } from '../alarm/alarm.component';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Alarm } from '../models/alarm';

export interface AlarmDialogData {
  distance: string;
  movement: string;
  microphone: string;
}

@Component({
  selector: 'app-alarm-dialog',
  templateUrl: './alarm-dialog.component.html',
  styleUrls: ['./alarm-dialog.component.css']
})
export class AlarmDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<AlarmComponent>,
    @Inject(MAT_DIALOG_DATA) public data: string) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}

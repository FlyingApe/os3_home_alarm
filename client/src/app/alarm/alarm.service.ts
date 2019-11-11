import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ConfigService } from '../global/config.service';
import { Alarm } from '../models/alarm';
import { Observable } from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AlarmService {
  private alarmUrl = ConfigService.apiUrl + '/alarm';

  constructor(private http: HttpClient) { }

  getAlarms(): Observable<Alarm[]> {
    return this.http.get<Alarm[]>(this.alarmUrl);
  }

  getAlarm(id: number): Observable<Alarm> {
    const url = `${this.alarmUrl}/${id}`;
    return this.http.get<Alarm>(url);
  }

  addAlarm(alarm: Alarm): Observable<Alarm> {
    return this.http.post<Alarm>(this.alarmUrl, alarm, httpOptions);
  }

  updateAlarm(alarm: Alarm): Observable<any> {
    return this.http.put(this.alarmUrl, alarm, httpOptions);
  }

  deleteAlarm(alarm: Alarm | number): Observable<Alarm> {
    const id = typeof alarm === 'number' ? alarm : alarm.id;
    const url = `${this.alarmUrl}/${id}`;

    return this.http.delete<Alarm>(url, httpOptions);
  }
}
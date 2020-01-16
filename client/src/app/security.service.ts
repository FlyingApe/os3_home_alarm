import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ConfigService } from './global/config.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SecurityService {
  private securityUrl = ConfigService.apiUrl + '/security';

  constructor(private http: HttpClient) { }

  getUsername(username: string, password: string): Observable<string> {
    const httpHeaders: HttpHeaders = new HttpHeaders();
    httpHeaders.append('Authorization', 'Basic ' + btoa(`${username}:${password}`));
    const httpOptions = {
      headers: httpHeaders
    };

    const url = `${this.securityUrl}/username`;
    return this.http.get<string>(url, httpOptions);
  }
}

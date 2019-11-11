import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  /// TODO: Verify apiUrl value.
  public static apiUrl = 'http://localhost:8080/api';

  constructor() { }
}

import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { LoginService } from './login/login.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private loginService: LoginService, private http: HttpClient, private router: Router) {
    this.loginService.authenticate(undefined, undefined);
  }

  logout() {
    this.http.post('logout', {}).pipe(finalize(() => {
      this.loginService.authenticated = false;
      this.router.navigateByUrl('/login');
    })).subscribe();
  }
}

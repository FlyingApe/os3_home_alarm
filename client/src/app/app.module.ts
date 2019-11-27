import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AlarmComponent } from './alarm/alarm.component';
import { UserComponent } from './user/user.component';
import { LoginComponent } from './login/login.component';
import { AlarmAddComponent } from './alarm/alarm-add/alarm-add.component';
import { AlarmDetailComponent } from './alarm/alarm-detail/alarm-detail.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule, MatFormFieldModule, MatListModule, MatSelectModule, MatTableModule, MatTabsModule} from '@angular/material';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoginService } from './login/login.service';
import { XhrInterceptor } from './XhrInterceptor';

@NgModule({
  declarations: [
    AppComponent,
    AlarmComponent,
    UserComponent,
    AlarmAddComponent,
    AlarmDetailComponent,
    LoginComponent,
  ],
    imports: [
        BrowserModule,
        HttpClientModule,
        BrowserAnimationsModule,
        MatTabsModule,
        MatTableModule,
        MatCardModule,
        MatFormFieldModule,
        MatSelectModule,
        FormsModule,
        RouterModule.forRoot([
          { path: '', pathMatch: 'full', redirectTo: 'home'},
          { path: 'login', component: LoginComponent},
          { path: 'alarm', component: AlarmComponent },
          { path: 'alarm/:id', component: AlarmDetailComponent },
        ]),
        MatListModule,
    ],
  providers: [LoginService, { provide: HTTP_INTERCEPTORS, useClass: XhrInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }

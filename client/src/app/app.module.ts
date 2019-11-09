import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AlarmComponent } from './alarm/alarm.component';
import { UserComponent } from './user/user.component';
import { AlarmAddComponent } from './alarm/alarm-add/alarm-add.component';
import { AlarmDetailComponent } from './alarm/alarm-detail/alarm-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    AlarmComponent,
    UserComponent,
    AlarmAddComponent,
    AlarmDetailComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AlarmComponent } from './alarm/alarm.component';
import { UserComponent } from './user/user.component';
import { AlarmAddComponent } from './alarm/alarm-add/alarm-add.component';
import { AlarmDetailComponent } from './alarm/alarm-detail/alarm-detail.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatCardModule, MatFormFieldModule, MatSelectModule, MatTableModule, MatTabsModule} from '@angular/material';
import {FormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {HttpClientModule} from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    AlarmComponent,
    UserComponent,
    AlarmAddComponent,
    AlarmDetailComponent,
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
            {path: 'alarms', component: AlarmComponent},
            {path: 'alarms/:id', component: AlarmDetailComponent}
        ]),
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AlarmComponent } from './alarm/alarm.component';
import { AlarmDetailComponent } from './alarm/alarm-detail/alarm-detail.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatCardModule, MatFormFieldModule, MatListModule, MatSelectModule, MatTableModule, MatTabsModule} from '@angular/material';
import {FormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {HttpClientModule} from '@angular/common/http';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import { AlarmDialogComponent } from './alarm-dialog/alarm-dialog.component';
import {MatInputModule} from '@angular/material/input';

@NgModule({
  declarations: [
    AppComponent,
    AlarmComponent,
    AlarmDetailComponent,
    AlarmDialogComponent,
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
          {path: 'alarm', component: AlarmComponent},
          {path: 'alarm/:id', component: AlarmDetailComponent}
      ]),
      MatListModule,
      MatButtonModule,
      MatDialogModule,
      MatInputModule,
  ],
  entryComponents: [AlarmDialogComponent],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlarmAddComponent } from './alarm-add.component';

describe('AlarmAddComponent', () => {
  let component: AlarmAddComponent;
  let fixture: ComponentFixture<AlarmAddComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlarmAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlarmAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

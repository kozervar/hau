import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentPressureComponent } from './current-pressure.component';

describe('CurrentPressureComponent', () => {
  let component: CurrentPressureComponent;
  let fixture: ComponentFixture<CurrentPressureComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CurrentPressureComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrentPressureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});

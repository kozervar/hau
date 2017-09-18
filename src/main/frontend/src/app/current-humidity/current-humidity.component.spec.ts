import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentHumidityComponent } from './current-humidity.component';

describe('CurrentHumidityComponent', () => {
  let component: CurrentHumidityComponent;
  let fixture: ComponentFixture<CurrentHumidityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CurrentHumidityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrentHumidityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentLightComponent } from './current-light.component';

describe('CurrentLightComponent', () => {
  let component: CurrentLightComponent;
  let fixture: ComponentFixture<CurrentLightComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CurrentLightComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrentLightComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});

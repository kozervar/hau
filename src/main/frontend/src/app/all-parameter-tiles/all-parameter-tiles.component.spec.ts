import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AllParameterTilesComponent } from './all-parameter-tiles.component';

describe('AllParameterTilesComponent', () => {
  let component: AllParameterTilesComponent;
  let fixture: ComponentFixture<AllParameterTilesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AllParameterTilesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AllParameterTilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});

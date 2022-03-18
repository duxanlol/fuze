import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SessionsComponentComponent } from './sessions-component.component';

describe('SessionsComponentComponent', () => {
  let component: SessionsComponentComponent;
  let fixture: ComponentFixture<SessionsComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SessionsComponentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SessionsComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

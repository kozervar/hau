import { Component, Input, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_URL } from '../app.constants';

@Component({
  selector: 'app-current-pressure',
  templateUrl: './current-pressure.component.html',
  styleUrls: ['./current-pressure.component.scss']
})
export class CurrentPressureComponent implements OnInit {

  @Input()
  public refreshInterval:number = 10000;

  public pressure:number;

  constructor(private http: Http) { }

  ngOnInit() {
    Observable.timer(0, this.refreshInterval).subscribe(() => {
      this.http.get(`${SERVER_URL}/pressure/current`)
        .subscribe(response => {
          this.pressure = Number.parseFloat(response.text());
        });
    });
  }

}

import { Component, Input, OnInit } from '@angular/core';
import { SERVER_URL } from '../app.constants';
import { Observable } from 'rxjs/Observable';
import { Http } from '@angular/http';
import * as c3 from 'c3';
import {ChartAPI} from "c3";

@Component({
  selector: 'app-current-humidity',
  templateUrl: './current-humidity.component.html',
  styleUrls: ['./current-humidity.component.scss']
})
export class CurrentHumidityComponent implements OnInit {

  @Input()
  public refreshInterval:number = 10000;

  public humidityChart: ChartAPI;

  private baseConfig = {
    data: {
      columns: [
        ['humidity', 0]
      ],
      type: 'gauge'
    },
    gauge: {
      min: 0,
      max: 100,
      units: '%',
      label: {
        // format: function (value, ratio) {
        //   return value / 10 + ' %';
        // },
        show: false // to turn off the min/max labels.
      },
    },
    color: {
      pattern: ['#00A4D2'], // the three color levels for the percentage values.
      threshold: {
        // unit: 'value', // percentage is default
//            max: 200, // 100 is default
//         values: [250, 500, 750]
      }
    },
    // size: {
    //   height: 500
    // }
  };

  constructor(private http: Http) { }

  ngOnInit() {
    this.humidityChart = c3.generate(Object.assign({ bindto: '#currentHumidityChart' }, this.baseConfig));

    Observable.timer(0, this.refreshInterval).subscribe(() => {
      this.http.get(`${SERVER_URL}/humidity/current`)
        .subscribe(response => {
          this.humidityChart.load({
            columns: [
              ['humidity', parseFloat(response.json()).toFixed(2)]
            ],
          })
        });
    });
  }

}

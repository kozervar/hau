import {Component, Input, OnInit} from '@angular/core';
import * as c3 from 'c3';
import {Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {SERVER_URL} from "../app.constants";
import {ChartAPI} from "c3";

@Component({
  selector: 'app-current-light',
  templateUrl: './current-light.component.html',
  styleUrls: ['./current-light.component.scss']
})
export class CurrentLightComponent implements OnInit {

  @Input()
  public refreshInterval:number = 10000;

  public lightTempChart: ChartAPI;

  private baseConfig = {
    data: {
      columns: [
        ['light', 0]
      ],
      type: 'gauge'
    },
    gauge: {
      min: 0,
      max: 1000,
      units: '%',
      label: {
        format: function (value, ratio) {
          return value / 10 + ' %';
        },
        show: false // to turn off the min/max labels.
      },
    },
    color: {
      pattern: ['#000000', '#524700', '#A49900', '#FFF400'], // the three color levels for the percentage values.
      threshold: {
        // unit: 'value', // percentage is default
//            max: 200, // 100 is default
        values: [250, 500, 750]
      }
    },
    // size: {
    //   height: 500
    // }
  };

  constructor(private http: Http) { }

  ngOnInit() {
    this.lightTempChart = c3.generate(Object.assign({ bindto: '#currentLightChart' }, this.baseConfig));

    Observable.timer(0, this.refreshInterval).subscribe(() => {
      this.http.get(`${SERVER_URL}/light/current`)
        .subscribe(response => {
          this.lightTempChart.load({
            columns: [
              ['light', parseFloat(response.json()).toFixed(2)]
            ],
          })
        });
    });
  }

}

import {Component, Input, OnInit} from '@angular/core';
import * as c3 from 'c3';
import {ChartAPI} from 'c3';
import {Http} from "@angular/http";
import {SERVER_URL} from "../app.constants";
import {Observable} from "rxjs/Observable";
import "rxjs/add/observable/timer";
import "rxjs/add/observable/interval";

@Component({
  selector: 'app-current-temperature',
  templateUrl: './current-temperature.component.html',
  styleUrls: ['./current-temperature.component.scss']
})
export class CurrentTemperatureComponent implements OnInit {

  @Input()
  public refreshInterval:number = 10000;

  public insideTempChart: ChartAPI;

  public outsideTempChart: ChartAPI;

  private baseConfig = {
    data: {
      columns: [
        ['temp.', 0]
      ],
      type: 'gauge'
    },
    gauge: {
      min: -10,
      max: 30,
      units: '°C',
      label: {
        format: function (value, ratio) {
          return value + ' °C';
        },
        show: false // to turn off the min/max labels.
      },
    },
    color: {
      pattern: ['#3254b0', '#F9F7A4', '#F97600', '#FF0000'], // the three color levels for the percentage values.
      threshold: {
        unit: 'value', // percentage is default
//            max: 200, // 100 is default
        values: [4, 15, 20, 25]
      }
    },
    // size: {
    //   height: 500
    // }
  };

  constructor(private http: Http) {
  }

  ngOnInit() {

    this.insideTempChart = c3.generate(Object.assign({ bindto: '#currentInsideTempChart' }, this.baseConfig));
    this.outsideTempChart = c3.generate(Object.assign({ bindto: '#currentOutsideTempChart' }, this.baseConfig));

    Observable.timer(0, this.refreshInterval).subscribe(() => {
      this.http.get(`${SERVER_URL}/temperature/current/inside`)
        .subscribe(response => {
          this.insideTempChart.load({
            columns: [
              ['temp.', parseFloat(response.json()).toFixed(2)]
            ],
          })
        });
      this.http.get(`${SERVER_URL}/temperature/current/outside`)
        .subscribe(response => {
          this.outsideTempChart.load({
            columns: [
              ['temp.', parseFloat(response.json()).toFixed(2)]
            ],
          })
        });
    });
  }

}

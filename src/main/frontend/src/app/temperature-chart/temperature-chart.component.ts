import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { isUndefined } from "util";
import {SERVER_URL} from "../app.constants";

class TemperatureRecord {
  id: string;
  deviceName: string;
  light: number;
  insideTemp: number;
  outsideTemp: number;
  creationTime: Date
}

class Temp {
  hour:number;
  tempOut:number;
  tempIn:number;
  light:number;
}

@Component({
  selector: 'app-temperature-chart',
  templateUrl: './temperature-chart.component.html',
  styleUrls: ['./temperature-chart.component.scss']
})
export class TemperatureChartComponent implements OnInit {

  temperatureRecords: TemperatureRecord[] = [];

  daysOfMonth = [];

  constructor(private http: Http) {
    const currDate = new Date();
    this.daysOfMonth = Array.from({length:currDate.getDate()},(v,k)=>k+1);
  }

  ngOnInit() {
    const currDate = new Date();
    this.getTemperatureRecords(currDate.getDate());
  }

  public getTemperatureRecords(day:number) {
    this.http.get(`${SERVER_URL}/temperature?fd=${day}&td=${day}`)
      .subscribe(r => {
        this.lineChartLabels.splice(0, this.lineChartLabels.length);
        this.lineChartData = [];

        this.temperatureRecords = r.json();

        let temperatures = this.temperatureRecords.map(tr => {
          let date = new Date(tr.creationTime);
          let temp = new Temp();
          temp.hour = date.getHours();
          temp.tempOut = tr.outsideTemp;
          temp.tempIn = tr.insideTemp;
          temp.light = tr.light;
          return temp;
        });
        let insideTemp = [];
        let outsideTemp = [];
        let light = [];
        for (let h = 0; h <= 23; h++) {
          let outsideTemps = temperatures.filter(t => t.hour == h).map(t => t.tempOut);
          let insideTemps = temperatures.filter(t => t.hour == h).map(t => t.tempIn);
          let lights = temperatures.filter(t => t.hour == h).map(t => t.light / 10);
          let tin = outsideTemps.reduce((a,b) => a+b, 0) / outsideTemps.length;
          let tout = insideTemps.reduce((a,b) => a+b, 0) / insideTemps.length;
          let l = lights.reduce((a,b) => a+b, 0) / lights.length;

          tin = isNaN(tin) ? 0 : Math.round(tin);
          tout = isNaN(tout) ? 0 : Math.round(tout);
          l = isNaN(l) ? 0 : Math.round(l);

          insideTemp.push(tin);
          outsideTemp.push(tout);
          light.push(l);
          this.lineChartLabels.push('G: ' + h);
        }
        this.lineChartData = [
          {data: insideTemp, label: 'Temperatura zewnętrzna'},
          {data: outsideTemp, label: 'Temperatura wewnętrzna'},
          // {data: light, label: 'Światło'}
        ];

      });
  }

  // lineChart
  public lineChartData: Array<any> = [
    {data: [], label: 'Temperatura zewnętrzna'},
    {data: [], label: 'Temperatura wewnętrzna'},
    // {data: [], label: 'Światło'},
  ];
  public lineChartLabels: Array<any> =  [];
  public lineChartOptions: any = {
    responsive: true
  };
  public lineChartColors: Array<any> = [
    { // temp zew
      backgroundColor: 'rgba(124, 200, 255,0.2)',
      borderColor: 'rgba(124, 200, 255,1)',
      pointBackgroundColor: 'rgba(124, 200, 255,1)',
      pointBorderColor: '#98caff',
      pointHoverBackgroundColor: '#98caff',
      pointHoverBorderColor: 'rgba(124, 200, 255,0.8)'
    },
    { // temp wew
      backgroundColor: 'rgba(247, 96, 93,0.2)',
      borderColor: 'rgba(247, 96, 93,1)',
      pointBackgroundColor: 'rgba(247, 96, 93,1)',
      pointBorderColor: '#ff6855',
      pointHoverBackgroundColor: '#ff6855',
      pointHoverBorderColor: 'rgba(247, 96, 93,0.8)'
    },
    { // światło
      backgroundColor: 'rgba(247, 234, 93,0.2)',
      borderColor: 'rgba(247, 234, 93,1)',
      pointBackgroundColor: 'rgba(247, 234, 93,1)',
      pointBorderColor: '#ffed59',
      pointHoverBackgroundColor: '#ffed59',
      pointHoverBorderColor: 'rgba(247, 234, 93,0.8)'
    }
  ];
  public lineChartLegend: boolean = true;
  public lineChartType: string = 'line';

  public randomize(): void {
    let _lineChartData: Array<any> = new Array(this.lineChartData.length);
    for (let i = 0; i < this.lineChartData.length; i++) {
      _lineChartData[i] = {data: new Array(this.lineChartData[i].data.length), label: this.lineChartData[i].label};
      for (let j = 0; j < this.lineChartData[i].data.length; j++) {
        _lineChartData[i].data[j] = Math.floor((Math.random() * 100) + 1);
      }
    }
    this.lineChartData = _lineChartData;
  }

  // events
  public chartClicked(e: any): void {
    console.log(e);
  }

  public chartHovered(e: any): void {
    console.log(e);
  }

}

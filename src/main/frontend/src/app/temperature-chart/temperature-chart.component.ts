import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { isUndefined } from "util";

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
  temp:number;
}

@Component({
  selector: 'app-temperature-chart',
  templateUrl: './temperature-chart.component.html',
  styleUrls: ['./temperature-chart.component.scss']
})
export class TemperatureChartComponent implements OnInit {

  temperatureRecords: TemperatureRecord[] = [];

  constructor(private http: Http) {
  }

  ngOnInit() {
    this.getTemperatureRecords();
  }

  private getTemperatureRecords() {
    const day = new Date().getDate();
    this.http.get('http://itworkswell.pl:3000/temperature?fd=' + day)
      .subscribe(r => {
        this.lineChartLabels.splice(0, this.lineChartLabels.length);
        this.lineChartData = [];

        this.temperatureRecords = r.json();

        let temperatures = this.temperatureRecords.map(tr => {
          let date = new Date(tr.creationTime);
          let temp = new Temp();
          temp.hour = date.getHours();
          temp.temp = tr.outsideTemp;
          return temp;
        });
        let tempsData = [];
        for (let h = 0; h <= 23; h++) {
          let temps = temperatures.filter(t => t.hour == h).map(t => t.temp);
          let maxTemp = temps.reduce((a,b) => a+b, 0) / temps.length;

          if(isNaN(maxTemp)) {
            maxTemp = 0;
          }
          maxTemp = Math.round(maxTemp);
          console.log(maxTemp);
          tempsData.push(maxTemp);
          this.lineChartLabels.push('G: ' + h);
        }
        this.lineChartData = [{data: tempsData, label: 'Temperatura zewnętrzna'}];

      });
  }

  // lineChart
  public lineChartData: Array<any> = [
    {data: [], label: 'Temperatura zewnętrzna'},
  ];
  public lineChartLabels: Array<any> =  [];
  public lineChartOptions: any = {
    responsive: true
  };
  public lineChartColors: Array<any> = [
    { // grey
      backgroundColor: 'rgba(124, 200, 255,0.2)',
      borderColor: 'rgba(124, 200, 255,1)',
      pointBackgroundColor: 'rgba(148,159,177,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
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

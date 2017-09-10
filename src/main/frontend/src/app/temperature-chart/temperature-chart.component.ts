import {Component, OnInit} from '@angular/core';
import {Http} from '@angular/http';
import * as c3 from 'c3';
import {ChartAPI} from 'c3';
import * as moment from 'moment';
import {Moment} from 'moment';
import {SERVER_URL} from "../app.constants";
import "rxjs/add/operator/map";
import {Observable} from "rxjs/Observable";
import "rxjs/add/observable/forkJoin";
import {IMyDate, IMyDateModel, IMyDpOptions} from "mydatepicker";

class Temperature {
  dateTime: string;
  date: Date;
  value: number;
}

@Component({
  selector: 'app-temperature-chart',
  templateUrl: './temperature-chart.component.html',
  styleUrls: ['./temperature-chart.component.scss']
})
export class TemperatureChartComponent implements OnInit {

  public temperatureRecords: Temperature[] = [];

  public temperatureChart: ChartAPI;

  private myDatePickerOptions: IMyDpOptions = {
    dateFormat: 'dd.mm.yyyy',
  };

  private selDateFrom: IMyDate = {year: 0, month: 0, day: 0};

  private selDateTo: IMyDate = {year: 0, month: 0, day: 0};

  private interval:string = 'HOUR';

  constructor(private http: Http) {
    this.selDateTo = this.getIMyDateFromMoment(moment());
    this.selDateFrom = this.getIMyDateFromMoment(moment().subtract(1, 'days'));
    this.myDatePickerOptions.disableSince = this.getIMyDateFromMoment(moment().add(1, 'days'));
    this.myDatePickerOptions.disableUntil = this.getIMyDateFromMoment(moment().subtract(8, 'days'));
  }

  ngOnInit() {
    let currDate = new Date();
    this.temperatureChart = c3.generate({
      bindto: '#tempChart',
      data: {
        x: 'date',
        columns: [
          ['date'],
          ['inside'],
          ['outside']
        ],
        types: {
          inside: 'area-spline',
          outside: 'area-spline'
        },
        names: {
          inside: 'wewnętrzna',
          outside: 'zewnętrzna'
        }
      },
      axis: {
        x: {
          label: 'Godzina',
          type: 'timeseries',
          tick: {
            format: (x: number | Date) => {
              if(this.interval == 'HOUR' || this.interval == 'MINUTE')
                return moment(x).format('DD.MM.YYYY HH:mm');
              else
                return moment(x).format('DD.MM.YYYY');
            }
          }
        },
        y: {
          label: 'Temperatura',
          tick: {
            format: (x: number) => {
              return `${x} °C`
            }
          }
        }
      },
      grid: {
        x: {
          show: true
        },
        y: {
          show: true
        }
      },
      // regions: [
      //   {start: new Date('2017-09-10T00:00:00'), end: new Date('2017-09-10T07:00:00'), class: 'regionNight'},
      //   {start: new Date('2017-09-10T07:00:00'), class: 'regionDay'},
      // ]
    });
    this.getTemperatureData();
  }

  public setInterval(str) {
    this.interval = str;
  }

  public getIntervalTranslation() {
    switch (this.interval) {
      case 'MONTH': return 'Miesiąc';
      case 'DAY': return 'Dzień';
      case 'HOUR': return 'Godzina';
      default: return 'Godzina';
    }
  }

  public onFromDateChanged(event: IMyDateModel) {
    this.selDateFrom = event.date;
    console.log(event)
  }

  public onToDateChanged(event: IMyDateModel) {
    this.selDateTo = event.date;
    console.log(event)
  }

  public reloadData() {
    console.log('From ', this.selDateFrom, ' to ', this.selDateTo);
    this.getTemperatureData();
  }

  private getTemperatureData() {
    let filter = `?fy=${this.selDateFrom.year}&fm=${this.selDateFrom.month}&fd=${this.selDateFrom.day}` +
      `&ty=${this.selDateTo.year}&tm=${this.selDateTo.month}&td=${this.selDateTo.day}` +
      `&fh=0&interval=${this.interval}`;
    let outsideTemps = this.http.get(`${SERVER_URL}/temperature/outside${filter}`).map(res => {
      let resp: Temperature[] = res.json();
      resp.forEach(t => t.date = new Date(t.dateTime));
      return resp;
    });
    let insideTemps = this.http.get(`${SERVER_URL}/temperature/inside${filter}`).map(res => {
      let resp: Temperature[] = res.json();
      resp.forEach(t => t.date = new Date(t.dateTime));
      return resp;
    });

    Observable.forkJoin([outsideTemps, insideTemps]).subscribe(results => {
      let outside = results[0];
      let inside = results[1];
      let dates: any = ['date', ...outside.map(t => t.date)];
      let outsideTemps = ['outside', ...outside.map(t => t.value)];
      let insideTemps = ['inside', ...inside.map(t => t.value)];
      this.temperatureChart.load({
        columns: [
          dates,
          outsideTemps,
          insideTemps
        ]
      });
    });
  }

  private getCopyOfDatePickerOptions() {
    return JSON.parse(JSON.stringify(this.myDatePickerOptions));
  }

  private getIMyDateFromMoment(date: Moment): IMyDate {
    return {
      year: date.year(),
      month: date.month() + 1,
      day: date.date()
    };
  }
}

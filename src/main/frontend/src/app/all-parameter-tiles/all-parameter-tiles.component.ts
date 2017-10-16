import {Component, Input, OnInit} from '@angular/core';
import {Http} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {SERVER_URL} from "../app.constants";
import * as moment from 'moment';
import {isNumber} from "util";

class SunriseSunset {
  sunrise: string = '';
  sunset: string = '';
  dayLength:string = '';
}

interface MiningStats {
  rigName:string,
  name:string,
  temp:number,
  power:number,
  fan:string,
}

@Component({
  selector: 'app-all-parameter-tiles',
  templateUrl: './all-parameter-tiles.component.html',
  styleUrls: ['./all-parameter-tiles.component.scss']
})
export class AllParameterTilesComponent implements OnInit {

  @Input()
  public refreshInterval: number = 60000;

  public insideTemp: number = 0.0;

  public outsideTemp: number = 0.0;

  public humidity: number = 0;

  public pressure: number = 0;

  public light: number = 0;

  public currentHour: Date = new Date();

  public currentDate: string = moment().format('DD . MM . YYYY');

  public sunriseSunset:SunriseSunset = new SunriseSunset();

  public rigData: MiningStats[] = [];

  public rigPower: number = 0.0;

  constructor(private http: Http) {
  }

  ngOnInit() {
    this.http.get("https://api.sunrise-sunset.org/json?lat=52.4167&lng=16.9667&formatted=0")
      .subscribe((response) => {
        let r = response.json();
        this.sunriseSunset.sunrise = moment(r.results.sunrise).format('HH : mm : ss');
        this.sunriseSunset.sunset = moment(r.results.sunset).format('HH : mm : ss');
        this.sunriseSunset.dayLength = moment().startOf('day')
          .seconds(r.results.day_length)
          .format('HH:mm:ss');
      });
    Observable.timer(0, this.refreshInterval).subscribe(() => {
      this.http.get(`${SERVER_URL}/humidity/current`)
        .subscribe(response => {
          let val = parseInt(response.json());
          if(!isNaN(val)) {
            this.humidity = val;
          }
        });
    });
    Observable.timer(0, this.refreshInterval).subscribe(() => {
      this.http.get(`${SERVER_URL}/pressure/current`)
        .subscribe(response => {
          let val = parseInt(response.json());
          if(!isNaN(val)) {
            this.pressure = val;
          }
        });
    });
    Observable.timer(0, this.refreshInterval).subscribe(() => {
      this.http.get(`${SERVER_URL}/light/current`)
        .subscribe(response => {
          let val = parseInt(response.json());
          if(!isNaN(val)) {
            this.light = val;
          }
        });
    });
    Observable.timer(0, this.refreshInterval).subscribe(() => {
      this.http.get(`${SERVER_URL}/temperature/current/inside`)
        .subscribe(response => {
          let val = parseInt(response.json());
          if(!isNaN(val)) {
            this.insideTemp = val;
          }
        });
    });
    Observable.timer(0, this.refreshInterval).subscribe(() => {
      this.http.get(`${SERVER_URL}/temperature/current/outside`)
        .subscribe(response => {
          let val = parseInt(response.json());
          if(!isNaN(val)) {
            this.outsideTemp = val;
          }
        });
    });
    Observable.timer(0, this.refreshInterval).subscribe(() => {
      this.http.get(`${SERVER_URL}/mining/stats/KRIG`)
        .subscribe(response => {
          this.rigData = response.json();
          for(let rd of this.rigData) {
            rd.power = Math.round(rd.power)
          }
        });
    });
    Observable.timer(0, this.refreshInterval).subscribe(() => {
      this.http.get(`${SERVER_URL}/mining/watts/KRIG`)
        .subscribe(response => {
          this.rigPower = Math.round(response.json())
        });
    });
    Observable.timer(0, 1000).subscribe(() => {
      this.currentHour = new Date();
    });
  }

}

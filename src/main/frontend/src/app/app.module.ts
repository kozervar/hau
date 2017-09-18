import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {BsDropdownModule} from 'ngx-bootstrap/dropdown';

import {AppComponent} from './app.component';
import {TemperatureChartComponent} from './temperature-chart/temperature-chart.component';
import {HttpModule} from '@angular/http';
import {CurrentTemperatureComponent} from './current-temperature/current-temperature.component';
import {CurrentLightComponent} from './current-light/current-light.component';
import {MyDatePickerModule} from "mydatepicker";
import { CurrentPressureComponent } from './current-pressure/current-pressure.component';
import { CurrentHumidityComponent } from './current-humidity/current-humidity.component';

@NgModule({
  declarations: [
    AppComponent,
    TemperatureChartComponent,
    CurrentTemperatureComponent,
    CurrentLightComponent,
    CurrentPressureComponent,
    CurrentHumidityComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    BsDropdownModule.forRoot(),
    MyDatePickerModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}

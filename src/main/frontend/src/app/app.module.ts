import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ChartsModule } from 'ng2-charts';

import { AppComponent } from './app.component';
import { TemperatureChartComponent } from './temperature-chart/temperature-chart.component';
import { HttpModule } from '@angular/http';
import { CurrentTemperatureComponent } from './current-temperature/current-temperature.component';
import { CurrentLightComponent } from './current-light/current-light.component';

@NgModule({
  declarations: [
    AppComponent,
    TemperatureChartComponent,
    CurrentTemperatureComponent,
    CurrentLightComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    BsDropdownModule.forRoot(),
    ChartsModule
  ],
  providers: [

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

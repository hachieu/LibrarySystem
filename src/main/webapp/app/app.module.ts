import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { LibaraySystemSharedModule } from 'app/shared/shared.module';
import { LibaraySystemCoreModule } from 'app/core/core.module';
import { LibaraySystemAppRoutingModule } from './app-routing.module';
import { LibaraySystemHomeModule } from './home/home.module';
import { LibaraySystemEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { SidebarComponent } from './layouts/sidebar/sidebar.component';
import { NgLoadingSpinnerModule } from 'ng-loading-spinner';

@NgModule({
  imports: [
    BrowserModule,
    LibaraySystemSharedModule,
    LibaraySystemCoreModule,
    LibaraySystemHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    LibaraySystemEntityModule,
    LibaraySystemAppRoutingModule,
    NgLoadingSpinnerModule,
  ],
  declarations: [
    MainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    FooterComponent,
    SidebarComponent,
  ],
  bootstrap: [MainComponent],
})
export class LibaraySystemAppModule {}

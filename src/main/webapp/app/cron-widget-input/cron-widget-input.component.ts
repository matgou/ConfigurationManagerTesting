import { EventEmitter, Output, Component, Input } from '@angular/core';

@Component({
  selector: 'jhi-cron-widget-input',
  templateUrl: './cron-widget-input.component.html'
})
export class CronWidgetInputComponent {
    @Input()
    @Output()
    rule: String;

      
    popoverEnable = false;
    tabName = 'Minutes';
    popoverTabMinutesEnable = 'active';
    popoverTabHourlyEnable = '';
    popoverTabDailyEnable = '';
    popoverTabWeeklyEnable = '';
    popoverTabMonthlyEnable = '';
    popoverTabYearlyEnable = '';

    everyMinutesText;
    
    showPopover() {
        if(!this.popoverEnable) {
            this.popoverEnable=true;
        } else {
            this.popoverEnable=false;
        }
        console.log('popoverEnable = ' + this.popoverEnable);
    }

    cleanAllPopoverTabEnable() {
        this.popoverTabMinutesEnable = '';
        this.popoverTabHourlyEnable = '';
        this.popoverTabDailyEnable = '';
        this.popoverTabWeeklyEnable = '';
        this.popoverTabMonthlyEnable = '';
        this.popoverTabYearlyEnable = '';
    }

    showPopoverTab(tabName: string) {
        this.tabName = tabName;
        switch(this.tabName) {
            case 'Minutes':
                this.cleanAllPopoverTabEnable();
                this.popoverTabMinutesEnable = 'active';
                break;
            case 'Hourly':
                this.cleanAllPopoverTabEnable();
                this.popoverTabHourlyEnable = 'active';
                break;
            case 'Daily':
                this.cleanAllPopoverTabEnable();
                this.popoverTabDailyEnable = 'active';
                break;
            case 'Weekly':
                this.cleanAllPopoverTabEnable();
                this.popoverTabWeeklyEnable = 'active';
                break;
            case 'Monthly':
                this.cleanAllPopoverTabEnable();
                this.popoverTabMonthlyEnable = 'active';
                break;
            case 'Yearly':
                this.cleanAllPopoverTabEnable();
                this.popoverTabYearlyEnable = 'active';
                break;
            default:
                this.cleanAllPopoverTabEnable();
                this.popoverTabMinutesEnable = 'active';
        }
    }

    onChange(event) {
        switch(this.tabName) {
            case 'Minutes':
                this.rule = '*/' + this.everyMinutesText + ' * * * *';
                break;
            case 'Hourly':
                break;
            case 'Daily':
                break;
            case 'Weekly':
                break;
            case 'Monthly':
                break;
            case 'Yearly':
                break;
            default:
        }

    }
}


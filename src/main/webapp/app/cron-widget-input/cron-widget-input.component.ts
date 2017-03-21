import { EventEmitter, Output, Component, Input } from '@angular/core';

@Component({
  selector: 'jhi-cron-widget-input',
  templateUrl: './cron-widget-input.component.html'
})
export class CronWidgetInputComponent {
    @Input()
    rule: String;

    @Output()
    ruleChange = new EventEmitter();
      
    popoverEnable = false;
    tabName = 'Minutes';
    popoverTabMinutesEnable = 'active';
    popoverTabHourlyEnable = '';
    popoverTabDailyEnable = '';
    popoverTabWeeklyEnable = '';
    popoverTabMonthlyEnable = '';
    popoverTabYearlyEnable = '';

    everyMinutesText=5;
    hourlyRadio='1';
    hoursInput='1';
    atHours='16';
    atMinutes='00';
    daysInput='1';
    dailyRadio='1';
  
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
        console.log("Change value : " + this.tabName);
        switch(this.tabName) {
            case 'Minutes':
                this.rule = '*/' + this.everyMinutesText + ' * * * *';
                break;
            case 'Hourly':
                switch(this.hourlyRadio) {
                  case '1':
                    this.rule = '* */' + this.hoursInput + ' * * *';
                    break;
                  case '2':
                    this.rule = this.atMinutes + ' ' + this.atHours + ' * * *';
                    break;
                }
                break;
            case 'Daily':
                switch(this.dailyRadio) {
                  case '1':
                    this.rule = this.atMinutes + ' ' + this.atHours + ' */' + this.daysInput + ' * *';
                    break;
                  case '2':
                    this.rule = this.atMinutes + ' ' + this.atHours + ' ? MON-FRI *';
                    break;
                }
                break;
            case 'Weekly':
                break;
            case 'Monthly':
                break;
            case 'Yearly':
                break;
            default:
        }
      console.log(this.rule);
      this.ruleChange.emit(this.rule);

    }
}


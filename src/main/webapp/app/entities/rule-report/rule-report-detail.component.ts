import { PipeTransform, Pipe, Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataUtils } from 'ng-jhipster';
import { RuleReport } from './rule-report.model';
import { RuleReportService } from './rule-report.service';

@Pipe({name: 'jsonToHTML'})
export class JsonToHTMLPipe implements PipeTransform {
  transform(value, args:string[]) : any {
      let value_return: string = "<ul>";
      let object = JSON.parse(value);
      
      for(let propertyName in object) {
          value_return = value_return + "<li>" + propertyName + " = " + object[propertyName] + "</li>";
      }
      value_return = value_return + "</ul>";
      return value_return;
  }
}
@Pipe({name: 'toClass'})
export class ToClassPipe implements PipeTransform {
  transform(value, args:string[]) : any {
    switch(value) {
            case "Success":
                return "table-success";
            case "Unknown":
                return "table-warning ";
            case "Running":
                return "table-info";
            case "SoftFail":
                return "table-danger";
            case "HardFail":
                return "table-danger";
            case "ForceSuccess":
                return "table-success";
            default:
                return "";
    }
  }
}

@Component({
    selector: 'jhi-rule-report-detail',
    templateUrl: './rule-report-detail.component.html'
})
export class RuleReportDetailComponent implements OnInit, OnDestroy {

    ruleReport: RuleReport;
    private subscription: any;

    constructor(
        private dataUtils: DataUtils,
        private ruleReportService: RuleReportService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.ruleReportService.find(id).subscribe(ruleReport => {
            this.ruleReport = ruleReport;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}

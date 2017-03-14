import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataUtils } from 'ng-jhipster';
import { RuleReport } from './rule-report.model';
import { RuleReportService } from './rule-report.service';

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

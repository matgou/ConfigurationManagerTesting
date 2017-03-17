import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { RuleReport } from './rule-report.model';
import { RuleReportService } from './rule-report.service';
@Injectable()
export class RuleReportPopupService {
    private isOpen = false;
    constructor (
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private ruleReportService: RuleReportService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.ruleReportService.find(id).subscribe(ruleReport => {
                if (ruleReport.reportDate) {
                    ruleReport.reportDate = {
                        year: ruleReport.reportDate.getFullYear(),
                        month: ruleReport.reportDate.getMonth() + 1,
                        day: ruleReport.reportDate.getDate()
                    };
                }
                ruleReport.submitAt = this.datePipe
                    .transform(ruleReport.submitAt, 'yyyy-MM-ddThh:mm');
                ruleReport.updatedAt = this.datePipe
                    .transform(ruleReport.updatedAt, 'yyyy-MM-ddThh:mm');
                ruleReport.finishAt = this.datePipe
                    .transform(ruleReport.finishAt, 'yyyy-MM-ddThh:mm');
                this.ruleReportModalRef(component, ruleReport);
            });
        } else {
            return this.ruleReportModalRef(component, new RuleReport());
        }
    }

    ruleReportModalRef(component: Component, ruleReport: RuleReport): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.ruleReport = ruleReport;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { RuleReport } from './rule-report.model';
import { RuleReportPopupService } from './rule-report-popup.service';
import { RuleReportService } from './rule-report.service';

@Component({
    selector: 'jhi-rule-report-delete-dialog',
    templateUrl: './rule-report-delete-dialog.component.html'
})
export class RuleReportDeleteDialogComponent {

    ruleReport: RuleReport;

    constructor(
        private ruleReportService: RuleReportService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.ruleReportService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'ruleReportListModification',
                content: 'Deleted an ruleReport'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rule-report-delete-popup',
    template: ''
})
export class RuleReportDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private ruleReportPopupService: RuleReportPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.ruleReportPopupService
                .open(RuleReportDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

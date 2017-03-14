import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { RuleReport } from './rule-report.model';
import { RuleReportPopupService } from './rule-report-popup.service';
import { RuleReportService } from './rule-report.service';
import { Rule, RuleService } from '../rule';
@Component({
    selector: 'jhi-rule-report-dialog',
    templateUrl: './rule-report-dialog.component.html'
})
export class RuleReportDialogComponent implements OnInit {

    ruleReport: RuleReport;
    authorities: any[];
    isSaving: boolean;

    rules: Rule[];
    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private ruleReportService: RuleReportService,
        private ruleService: RuleService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.ruleService.query().subscribe(
            (res: Response) => { this.rules = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData($event, ruleReport, field, isImage) {
        if ($event.target.files && $event.target.files[0]) {
            let $file = $event.target.files[0];
            if (isImage && !/^image\//.test($file.type)) {
                return;
            }
            this.dataUtils.toBase64($file, (base64Data) => {
                ruleReport[field] = base64Data;
                ruleReport[`${field}ContentType`] = $file.type;
            });
        }
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.ruleReport.id !== undefined) {
            this.ruleReportService.update(this.ruleReport)
                .subscribe((res: RuleReport) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.ruleReportService.create(this.ruleReport)
                .subscribe((res: RuleReport) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: RuleReport) {
        this.eventManager.broadcast({ name: 'ruleReportListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    trackRuleById(index: number, item: Rule) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-rule-report-popup',
    template: ''
})
export class RuleReportPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private ruleReportPopupService: RuleReportPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.ruleReportPopupService
                    .open(RuleReportDialogComponent, params['id']);
            } else {
                this.modalRef = this.ruleReportPopupService
                    .open(RuleReportDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

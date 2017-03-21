import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { Scheduling } from './scheduling.model';
import { SchedulingPopupService } from './scheduling-popup.service';
import { SchedulingService } from './scheduling.service';
import { Rule, RuleService } from '../rule';

@Component({
    selector: 'jhi-scheduling-dialog',
    templateUrl: './scheduling-dialog.component.html'
})
export class SchedulingDialogComponent implements OnInit {

    scheduling: Scheduling;
    authorities: any[];
    isSaving: boolean;

    rules: Rule[];
    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private schedulingService: SchedulingService,
        private ruleService: RuleService,
        private eventManager: EventManager
    ) {
    }

    updateRule(event) {
        console.log(event);
        this.scheduling.rule = event;
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

    setFileData($event, scheduling, field, isImage) {
        if ($event.target.files && $event.target.files[0]) {
            let $file = $event.target.files[0];
            if (isImage && !/^image\//.test($file.type)) {
                return;
            }
            this.dataUtils.toBase64($file, (base64Data) => {
                scheduling[field] = base64Data;
                scheduling[`${field}ContentType`] = $file.type;
            });
        }
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.scheduling.id !== undefined) {
            this.schedulingService.update(this.scheduling)
                .subscribe((res: Scheduling) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.schedulingService.create(this.scheduling)
                .subscribe((res: Scheduling) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Scheduling) {
        this.eventManager.broadcast({ name: 'schedulingListModification', content: 'OK'});
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-scheduling-popup',
    template: ''
})
export class SchedulingPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private schedulingPopupService: SchedulingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.schedulingPopupService
                    .open(SchedulingDialogComponent, params['id']);
            } else {
                this.modalRef = this.schedulingPopupService
                    .open(SchedulingDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

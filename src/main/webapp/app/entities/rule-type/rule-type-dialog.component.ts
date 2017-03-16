import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { RuleType } from './rule-type.model';
import { RuleTypePopupService } from './rule-type-popup.service';
import { RuleTypeService } from './rule-type.service';


@Component({
    selector: 'jhi-rule-type-dialog',
    templateUrl: './rule-type-dialog.component.html'
})
export class RuleTypeDialogComponent implements OnInit {

    ruleType: RuleType;
    authorities: any[];
    isSaving: boolean;
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private ruleTypeService: RuleTypeService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.ruleType.id !== undefined) {
            this.ruleTypeService.update(this.ruleType)
                .subscribe((res: RuleType) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.ruleTypeService.create(this.ruleType)
                .subscribe((res: RuleType) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: RuleType) {
        this.eventManager.broadcast({ name: 'ruleTypeListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-rule-type-popup',
    template: ''
})
export class RuleTypePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private ruleTypePopupService: RuleTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.ruleTypePopupService
                    .open(RuleTypeDialogComponent, params['id']);
            } else {
                this.modalRef = this.ruleTypePopupService
                    .open(RuleTypeDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

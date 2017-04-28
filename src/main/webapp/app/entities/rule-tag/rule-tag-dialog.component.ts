import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { RuleTag } from './rule-tag.model';
import { RuleTagPopupService } from './rule-tag-popup.service';
import { RuleTagService } from './rule-tag.service';
import { Rule, RuleService } from '../rule';
@Component({
    selector: 'jhi-rule-tag-dialog',
    templateUrl: './rule-tag-dialog.component.html'
})
export class RuleTagDialogComponent implements OnInit {

    ruleTag: RuleTag;
    authorities: any[];
    isSaving: boolean;

    rules: Rule[];
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private ruleTagService: RuleTagService,
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
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.ruleTag.id !== undefined) {
            this.ruleTagService.update(this.ruleTag)
                .subscribe((res: RuleTag) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.ruleTagService.create(this.ruleTag)
                .subscribe((res: RuleTag) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: RuleTag) {
        this.eventManager.broadcast({ name: 'ruleTagListModification', content: 'OK'});
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
    selector: 'jhi-rule-tag-popup',
    template: ''
})
export class RuleTagPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private ruleTagPopupService: RuleTagPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.ruleTagPopupService
                    .open(RuleTagDialogComponent, params['id']);
            } else {
                this.modalRef = this.ruleTagPopupService
                    .open(RuleTagDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

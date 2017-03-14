import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { RuleType } from './rule-type.model';
import { RuleTypePopupService } from './rule-type-popup.service';
import { RuleTypeService } from './rule-type.service';

@Component({
    selector: 'jhi-rule-type-delete-dialog',
    templateUrl: './rule-type-delete-dialog.component.html'
})
export class RuleTypeDeleteDialogComponent {

    ruleType: RuleType;

    constructor(
        private ruleTypeService: RuleTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.ruleTypeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'ruleTypeListModification',
                content: 'Deleted an ruleType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rule-type-delete-popup',
    template: ''
})
export class RuleTypeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private ruleTypePopupService: RuleTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.ruleTypePopupService
                .open(RuleTypeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

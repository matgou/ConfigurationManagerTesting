import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { RuleTag } from './rule-tag.model';
import { RuleTagPopupService } from './rule-tag-popup.service';
import { RuleTagService } from './rule-tag.service';

@Component({
    selector: 'jhi-rule-tag-delete-dialog',
    templateUrl: './rule-tag-delete-dialog.component.html'
})
export class RuleTagDeleteDialogComponent {

    ruleTag: RuleTag;

    constructor(
        private ruleTagService: RuleTagService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.ruleTagService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'ruleTagListModification',
                content: 'Deleted an ruleTag'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rule-tag-delete-popup',
    template: ''
})
export class RuleTagDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private ruleTagPopupService: RuleTagPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.ruleTagPopupService
                .open(RuleTagDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

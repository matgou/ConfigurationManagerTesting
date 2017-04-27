import { PipeTransform, Pipe, Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Rule } from './rule.model';
import { RulePopupService } from './rule-popup.service';
import { RuleService } from './rule.service';

@Component({
    selector: 'jhi-rule-force-ack-dialog',
    templateUrl: './rule-force-ack.component.html'
})
export class RuleForceAckComponent {

    rule: Rule;

    constructor(
        private ruleService: RuleService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmAck (id: number) {
        this.ruleService.update(this.rule).subscribe(response => {
            this.eventManager.broadcast({
                name: 'ruleListModification',
                content: 'Updated an rule'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rule-force-ack-popup',
    template: ''
})
export class RuleForceAckPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private rulePopupService: RulePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.rulePopupService
                .open(RuleForceAckComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

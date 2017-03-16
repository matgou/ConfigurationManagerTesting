import { PipeTransform, Pipe, Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Rule } from './rule.model';
import { RulePopupService } from './rule-popup.service';
import { RuleService } from './rule.service';


@Pipe({name: 'keys'})
export class KeysPipe implements PipeTransform {
  transform(value, args:string[]) : any {
    let keys = [];
    for (let key in value) {
      keys.push({key: key, value: value[key]});
    }
    return keys;
  }
}

@Component({
    selector: 'jhi-rule-execute-dialog',
    templateUrl: './rule-execute.component.html'
})
export class RuleExecuteComponent {

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

    confirmExecute (id: number) {
        this.ruleService.execute(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'ruleListModification',
                content: 'Executed an rule'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rule-execute-popup',
    template: ''
})
export class RuleExecutePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private rulePopupService: RulePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.rulePopupService
                .open(RuleExecuteComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Param } from './param.model';
import { ParamPopupService } from './param-popup.service';
import { ParamService } from './param.service';

@Component({
    selector: 'jhi-param-delete-dialog',
    templateUrl: './param-delete-dialog.component.html'
})
export class ParamDeleteDialogComponent {

    param: Param;

    constructor(
        private paramService: ParamService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.paramService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'paramListModification',
                content: 'Deleted an param'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-param-delete-popup',
    template: ''
})
export class ParamDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private paramPopupService: ParamPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.paramPopupService
                .open(ParamDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Scheduling } from './scheduling.model';
import { SchedulingPopupService } from './scheduling-popup.service';
import { SchedulingService } from './scheduling.service';

@Component({
    selector: 'jhi-scheduling-delete-dialog',
    templateUrl: './scheduling-delete-dialog.component.html'
})
export class SchedulingDeleteDialogComponent {

    scheduling: Scheduling;

    constructor(
        private schedulingService: SchedulingService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.schedulingService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'schedulingListModification',
                content: 'Deleted an scheduling'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-scheduling-delete-popup',
    template: ''
})
export class SchedulingDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private schedulingPopupService: SchedulingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.schedulingPopupService
                .open(SchedulingDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

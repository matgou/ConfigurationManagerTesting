import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Process } from './process.model';
import { ProcessPopupService } from './process-popup.service';
import { ProcessService } from './process.service';
@Component({
    selector: 'jhi-process-dialog',
    templateUrl: './process-dialog.component.html'
})
export class ProcessDialogComponent implements OnInit {

    process: Process;
    authorities: any[];
    isSaving: boolean;

    processes: Process[];
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private processService: ProcessService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.processService.query().subscribe(
            (res: Response) => { this.processes = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.process.id !== undefined) {
            this.processService.update(this.process)
                .subscribe((res: Process) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.processService.create(this.process)
                .subscribe((res: Process) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Process) {
        this.eventManager.broadcast({ name: 'processListModification', content: 'OK'});
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

    trackProcessById(index: number, item: Process) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-process-popup',
    template: ''
})
export class ProcessPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private processPopupService: ProcessPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.processPopupService
                    .open(ProcessDialogComponent, params['id']);
            } else {
                this.modalRef = this.processPopupService
                    .open(ProcessDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

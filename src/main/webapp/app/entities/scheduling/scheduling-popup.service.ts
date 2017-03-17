import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Scheduling } from './scheduling.model';
import { SchedulingService } from './scheduling.service';
@Injectable()
export class SchedulingPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private schedulingService: SchedulingService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.schedulingService.find(id).subscribe(scheduling => {
                this.schedulingModalRef(component, scheduling);
            });
        } else {
            return this.schedulingModalRef(component, new Scheduling());
        }
    }

    schedulingModalRef(component: Component, scheduling: Scheduling): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.scheduling = scheduling;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}

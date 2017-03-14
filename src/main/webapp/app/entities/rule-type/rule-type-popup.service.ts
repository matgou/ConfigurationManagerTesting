import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RuleType } from './rule-type.model';
import { RuleTypeService } from './rule-type.service';
@Injectable()
export class RuleTypePopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private ruleTypeService: RuleTypeService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.ruleTypeService.find(id).subscribe(ruleType => {
                this.ruleTypeModalRef(component, ruleType);
            });
        } else {
            return this.ruleTypeModalRef(component, new RuleType());
        }
    }

    ruleTypeModalRef(component: Component, ruleType: RuleType): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.ruleType = ruleType;
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

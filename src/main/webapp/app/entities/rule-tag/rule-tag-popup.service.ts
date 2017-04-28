import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RuleTag } from './rule-tag.model';
import { RuleTagService } from './rule-tag.service';
@Injectable()
export class RuleTagPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private ruleTagService: RuleTagService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.ruleTagService.find(id).subscribe(ruleTag => {
                this.ruleTagModalRef(component, ruleTag);
            });
        } else {
            return this.ruleTagModalRef(component, new RuleTag());
        }
    }

    ruleTagModalRef(component: Component, ruleTag: RuleTag): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.ruleTag = ruleTag;
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

import { PipeTransform, Pipe, Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { Rule } from './rule.model';
import { RuleTag } from '../rule-tag/rule-tag.model';
import { RuleTagService } from '../rule-tag/rule-tag.service';
import { RulePopupService } from './rule-popup.service';
import { RuleService } from './rule.service';
import { RuleType, RuleTypeService } from '../rule-type';
import { Process, ProcessService } from '../process';

@Pipe({name: 'splitString'})
export class SplitStringPipe implements PipeTransform {
  transform(val:string, params:string[]):string[] {
    return val.split(params[0]);
  }
}

@Component({
    selector: 'jhi-rule-dialog',
    templateUrl: './rule-dialog.component.html'
})
export class RuleDialogComponent implements OnInit {

    rule: Rule;
    authorities: any[];
    isSaving: boolean;

    ruletypes: RuleType[];
    tabActive:string = 'main';

    processes: Process[];
    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private ruleService: RuleService,
        private ruleTypeService: RuleTypeService,
        private ruleTagService: RuleTagService,
        private processService: ProcessService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.ruleTypeService.query().subscribe(
            (res: Response) => { this.ruletypes = res.json(); }, (res: Response) => this.onError(res.json()));
        this.processService.query().subscribe(
            (res: Response) => { this.processes = res.json(); }, (res: Response) => this.onError(res.json()));
        
        /* Calculate ruleArgs (transfroming json encoded object to tab) from response */
        let keys = [];
        let a;
        try {
            if(a != null) {
                a =JSON.parse(this.rule.ruleArgs);
                for (let key in a) {
                     keys[key]=a[key];
                }
            }
        } catch(e) {
            
        }
        
        this.rule.tab = keys;
            
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData($event, rule, field, isImage) {
        if ($event.target.files && $event.target.files[0]) {
            let $file = $event.target.files[0];
            if (isImage && !/^image\//.test($file.type)) {
                return;
            }
            this.dataUtils.toBase64($file, (base64Data) => {
                rule[field] = base64Data;
                rule[`${field}ContentType`] = $file.type;
            });
        }
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    addTag(tagName:string) {
        if(this.rule.id != null) {
            console.log("Add new Tag : " + tagName);
            this.ruleService.addTag(this.rule.id, tagName).subscribe(
                (res: RuleTag) => { this.rule.tags.push(res); },
                (res: Response) =>{ console.log(res.json) });
            this.eventManager.broadcast({ name: 'ruleListModification', content: 'OK'});
        }
    }
    
    deleteTag(tagId:number) {
        if(this.rule.id != null) {
            console.log("Delete tag : " + tagId);
            this.ruleTagService.delete(tagId).subscribe(
                (res: RuleTag) => {
                    for(let i=0; i< this.rule.tags.length; i++) {
                        if(this.rule.tags[i].id == tagId) {
                            this.rule.tags.splice(i, 1);
                        }
                    }
                },
                (res: Response) =>{ console.log(res.json) });
            this.eventManager.broadcast({ name: 'ruleListModification', content: 'OK'});
        }
    }
    
    save () {
        this.isSaving = true;
        this.rule.ruleArgs = Rule.ruleArgsJson(this.rule);
        console.log(this.rule);
        if (this.rule.id !== undefined) {
            this.ruleService.update(this.rule)
                .subscribe((res: Rule) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.ruleService.create(this.rule)
                .subscribe((res: Rule) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Rule) {
        this.eventManager.broadcast({ name: 'ruleListModification', content: 'OK'});
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

    trackRuleTypeById(index: number, item: RuleType) {
        return item.id;
    }

    trackProcessById(index: number, item: Process) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-rule-popup',
    template: ''
})
export class RulePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private rulePopupService: RulePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.rulePopupService
                    .open(RuleDialogComponent, params['id']);
            } else {
                this.modalRef = this.rulePopupService
                    .open(RuleDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, AlertService } from 'ng-jhipster';

import { RuleTag } from './rule-tag.model';
import { RuleTagService } from './rule-tag.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-rule-tag',
    templateUrl: './rule-tag.component.html'
})
export class RuleTagComponent implements OnInit, OnDestroy {
ruleTags: RuleTag[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private ruleTagService: RuleTagService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.ruleTagService.query().subscribe(
            (res: Response) => {
                this.ruleTags = res.json();
            },
            (res: Response) => this.onError(res.json())
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInRuleTags();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId (index: number, item: RuleTag) {
        return item.id;
    }



    registerChangeInRuleTags() {
        this.eventSubscriber = this.eventManager.subscribe('ruleTagListModification', (response) => this.loadAll());
    }


    private onError (error) {
        this.alertService.error(error.message, null, null);
    }
}

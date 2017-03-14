import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataUtils } from 'ng-jhipster';
import { Rule } from './rule.model';
import { RuleService } from './rule.service';

@Component({
    selector: 'jhi-rule-detail',
    templateUrl: './rule-detail.component.html'
})
export class RuleDetailComponent implements OnInit, OnDestroy {

    rule: Rule;
    private subscription: any;

    constructor(
        private dataUtils: DataUtils,
        private ruleService: RuleService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.ruleService.find(id).subscribe(rule => {
            this.rule = rule;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}

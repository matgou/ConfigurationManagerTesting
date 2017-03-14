import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RuleType } from './rule-type.model';
import { RuleTypeService } from './rule-type.service';

@Component({
    selector: 'jhi-rule-type-detail',
    templateUrl: './rule-type-detail.component.html'
})
export class RuleTypeDetailComponent implements OnInit, OnDestroy {

    ruleType: RuleType;
    private subscription: any;

    constructor(
        private ruleTypeService: RuleTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.ruleTypeService.find(id).subscribe(ruleType => {
            this.ruleType = ruleType;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}

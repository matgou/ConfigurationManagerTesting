import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RuleTag } from './rule-tag.model';
import { RuleTagService } from './rule-tag.service';

@Component({
    selector: 'jhi-rule-tag-detail',
    templateUrl: './rule-tag-detail.component.html'
})
export class RuleTagDetailComponent implements OnInit, OnDestroy {

    ruleTag: RuleTag;
    private subscription: any;

    constructor(
        private ruleTagService: RuleTagService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.ruleTagService.find(id).subscribe(ruleTag => {
            this.ruleTag = ruleTag;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}

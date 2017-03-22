import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Param } from './param.model';
import { ParamService } from './param.service';

@Component({
    selector: 'jhi-param-detail',
    templateUrl: './param-detail.component.html'
})
export class ParamDetailComponent implements OnInit, OnDestroy {

    param: Param;
    private subscription: any;

    constructor(
        private paramService: ParamService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.paramService.find(id).subscribe(param => {
            this.param = param;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}

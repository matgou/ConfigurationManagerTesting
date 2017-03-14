import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RuleDetailComponent } from '../../../../../../main/webapp/app/entities/rule/rule-detail.component';
import { RuleService } from '../../../../../../main/webapp/app/entities/rule/rule.service';
import { Rule } from '../../../../../../main/webapp/app/entities/rule/rule.model';

describe('Component Tests', () => {

    describe('Rule Management Detail Component', () => {
        let comp: RuleDetailComponent;
        let fixture: ComponentFixture<RuleDetailComponent>;
        let service: RuleService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [RuleDetailComponent],
                providers: [
                    MockBackend,
                    BaseRequestOptions,
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    {
                        provide: Http,
                        useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
                            return new Http(backendInstance, defaultOptions);
                        },
                        deps: [MockBackend, BaseRequestOptions]
                    },
                    RuleService
                ]
            }).overrideComponent(RuleDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RuleDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RuleService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Rule(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.rule).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});

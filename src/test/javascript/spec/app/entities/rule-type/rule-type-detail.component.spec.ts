import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RuleTypeDetailComponent } from '../../../../../../main/webapp/app/entities/rule-type/rule-type-detail.component';
import { RuleTypeService } from '../../../../../../main/webapp/app/entities/rule-type/rule-type.service';
import { RuleType } from '../../../../../../main/webapp/app/entities/rule-type/rule-type.model';

describe('Component Tests', () => {

    describe('RuleType Management Detail Component', () => {
        let comp: RuleTypeDetailComponent;
        let fixture: ComponentFixture<RuleTypeDetailComponent>;
        let service: RuleTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [RuleTypeDetailComponent],
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
                    RuleTypeService
                ]
            }).overrideComponent(RuleTypeDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RuleTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RuleTypeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RuleType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.ruleType).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});

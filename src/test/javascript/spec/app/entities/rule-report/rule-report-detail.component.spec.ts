import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RuleReportDetailComponent } from '../../../../../../main/webapp/app/entities/rule-report/rule-report-detail.component';
import { RuleReportService } from '../../../../../../main/webapp/app/entities/rule-report/rule-report.service';
import { RuleReport } from '../../../../../../main/webapp/app/entities/rule-report/rule-report.model';

describe('Component Tests', () => {

    describe('RuleReport Management Detail Component', () => {
        let comp: RuleReportDetailComponent;
        let fixture: ComponentFixture<RuleReportDetailComponent>;
        let service: RuleReportService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [RuleReportDetailComponent],
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
                    RuleReportService
                ]
            }).overrideComponent(RuleReportDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RuleReportDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RuleReportService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RuleReport(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.ruleReport).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});

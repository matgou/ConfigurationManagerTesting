import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { RuleReport } from './rule-report.model';
import { DateUtils } from 'ng-jhipster';
@Injectable()
export class RuleReportService {

    private resourceUrl = 'api/rule-reports';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(ruleReport: RuleReport): Observable<RuleReport> {
        let copy: RuleReport = Object.assign({}, ruleReport);
        copy.reportDate = this.dateUtils
            .convertLocalDateToServer(ruleReport.reportDate);
        copy.submitAt = this.dateUtils.toDate(ruleReport.submitAt);
        copy.updatedAt = this.dateUtils.toDate(ruleReport.updatedAt);
        copy.finishAt = this.dateUtils.toDate(ruleReport.finishAt);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(ruleReport: RuleReport): Observable<RuleReport> {
        let copy: RuleReport = Object.assign({}, ruleReport);
        copy.reportDate = this.dateUtils
            .convertLocalDateToServer(ruleReport.reportDate);

        copy.submitAt = this.dateUtils.toDate(ruleReport.submitAt);

        copy.updatedAt = this.dateUtils.toDate(ruleReport.updatedAt);

        copy.finishAt = this.dateUtils.toDate(ruleReport.finishAt);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<RuleReport> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            let jsonResponse = res.json();
            jsonResponse.reportDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse.reportDate);
            jsonResponse.submitAt = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.submitAt);
            jsonResponse.updatedAt = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.updatedAt);
            jsonResponse.finishAt = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.finishAt);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: any) => this.convertResponse(res))
        ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }


    private convertResponse(res: any): any {
        let jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            jsonResponse[i].reportDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse[i].reportDate);
            jsonResponse[i].submitAt = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].submitAt);
            jsonResponse[i].updatedAt = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].updatedAt);
            jsonResponse[i].finishAt = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].finishAt);
        }
        res._body = jsonResponse;
        return res;
    }

    private createRequestOption(req?: any): BaseRequestOptions {
        let options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            let params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }
}

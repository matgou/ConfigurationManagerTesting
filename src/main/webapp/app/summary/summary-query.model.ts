
const enum StatusEnum {
    'Unknown', 'Success', 'Running', 'SoftFail', 'HardFail', 'ForceSuccess'
};
export class SummaryQuery {
    public displayStatusCSV = 'Unknown,Success,SoftFail,Running,HardFail,ForceSuccess';
    public tagsCSV = '*';
}

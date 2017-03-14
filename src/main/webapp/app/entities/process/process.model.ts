export class Process {
    constructor(
        public id?: number,
        public processName?: string,
        public parent?: Process,
    ) {
    }
}

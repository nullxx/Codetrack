const { readFile } = require('../utils/file.utils');
describe('File utils', () => {
    it(`Read file`, async () => {
        expect(await readFile('./src/test/static/auth.utils.test.file.example')).toBe('TESTFILE_FOR_READ_FILE');
    });
});
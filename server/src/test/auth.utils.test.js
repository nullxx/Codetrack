const { sign, decode } = require('../utils/auth.utils');
describe('JsonWebToken', () => {
    const result = sign({ id: 32 }, process.env.JWT_SECRET, '1 DAY');
    it(`Obtaining token`, () => {
        expect(result).not.toBeUndefined();
    });
    it(`Decode token`, () => {
        expect(decode(result, process.env.JWT_SECRET)).toMatchObject({ id: 32 });
    });
});
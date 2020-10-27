const { sign, decode } = require('../utils/auth.utils');
describe('JsonWebToken', () => {
    const object = { id: 32 };
    const result = sign(object, process.env.JWT_SECRET, '1 DAY');
    it(`Obtaining token`, () => {
        expect(result).not.toBeUndefined();
    });
    it(`Decode token`, () => {
        expect(decode(result, process.env.JWT_SECRET)).toMatchObject(object);
    });
});
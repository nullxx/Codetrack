if (!('toJSON' in Error.prototype))
    Object.defineProperty(Error.prototype, 'toJSON', {
        value: function () {
            var alt = {};

            Object.getOwnPropertyNames(this).forEach(function (key) {
                alt[key] = this[key];
            }, this);
            if (process.env.NODE_ENV === 'production') delete alt.stack;
            return alt;
        },
        configurable: true,
        writable: true
    });

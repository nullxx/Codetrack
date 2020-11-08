module.exports = {
    env: {
        es6: true,
        node: true,
        jest: true,
    },
    extends: "eslint:recommended",
    globals: {
        Atomics: "readonly",
        SharedArrayBuffer: "readonly",
    },
    // parser: "babel-eslint",
    parserOptions: {
        ecmaVersion: 2018,
        sourceType: "module",
    },
    // plugins: [],
    rules: {},
    settings: {},
};

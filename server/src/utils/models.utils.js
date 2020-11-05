/**
 * Define model in Sequelize connection
 * @param {Sequelize} seqInst current connection
 * @param {object} preModel Object with model data
 */
module.exports.defineModel = (seqInst, preModel) => {
    const model = seqInst.define(
        preModel.name,
        preModel.attributes,
        preModel.options
    )
    return model;
}
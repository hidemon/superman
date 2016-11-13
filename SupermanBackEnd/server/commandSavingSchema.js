/**
 * Created by lijingjiang on 11/12/16 11:45 PM.
 */

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var NLPProcessing = new Schema({
    nlpTranslationResult: String,
    nlpTranslationError: String
});

var CMDExecution = new Schema({
    CMDExecutionResult: String,
    CMDExecutionError: String
});

module.exports = {
    NLPProcessing:NLPProcessing,
    CMDExecution: CMDExecution
};

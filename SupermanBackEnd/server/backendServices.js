/**
 * Created by lijingjiang on 11/12/16 11:27 PM.
 */

Meteor.methods({

    submitNaturalLanguage: function(naturalString) {

        var spawn = require('child_process').spawn;
        var commandType = "nlp";
        var parameters = [naturalString];
        var executionResult = spawn(commandType, parameters);

        var schema = require('./commandSavingSchema');
        var NLPProcessing = mongoose.model('NLPProcessing', schema.NLPProcessing);
        var CMDExecution = mongoose.model('CMDExecution', schema.CMDExecution);

        var stringResult = "";
        var stringError = "";



        var resultOutputFirst = false;
        var resultErrorFirst = false;

        executionResult.stdout.on('data', function(data) {
            if (resultErrorFirst == false && resultOutputFirst == false) {
                resultOutputFirst = true;
            }

            stringResult = `${data}`;

            var currentResult = new NLPProcessing({
                nlpTranslationResult: stringResult
            });

            NLPProcessing.find().exec(function(err, outputs) {
                if (err) {
                    throw err;
                } else {
                    if (Object.keys(outputs).length == 0) {
                        currentResult.save(function(err) {
                            if (err) {
                                throw err;
                            }
                        })
                    } else {
                        var existingOutput = outputs[0];
                        existingOutput.nlpTranslationResult = stringResult;
                        if (resultOutputFirst) {
                            existingOutput.nlpTranslationError = "";
                        }
                        existingOutput.save(function(err) {
                            if (err) {
                                throw err;
                            }
                        })
                    }
                }
            })


            CMDExecution.find().exec(function(err, outputs) {
                if (err) {
                    throw err;
                } else {
                    if (Object.keys(outputs).length != 0) {
                        var existingOutput = outputs[0];
                        existingOutput.CMDExecutionResult = "";
                        existingOutput.CMDExecutionError = "";
                        existingOutput.save(function(err) {
                            if (err) {
                                throw err;
                            }
                        })
                    } 
                }
            })
        });

        executionResult.stderr.on('data', function(data) {

            if (resultOutputFirst == false && resultErrorFirst == false) {
                resultErrorFirst = true;
            }

            stringError = `${data}`;

            var currentResult = new NLPProcessing({
                nlpTranslationError: stringError
            });

            NLPProcessing.find().exec(function(err, outputs) {
                if (err) {
                    throw err;
                } else {
                    if (Object.keys(outputs).length == 0) {
                        currentResult.save(function(err) {
                            if (err) {
                                throw err;
                            }
                        })
                    } else {
                        var existingOutput = outputs[0];
                        existingOutput.nlpTranslationError = stringError;
                        if (resultErrorFirst) {
                            existingOutput.nlpTranslationResult = "";
                        }
                        existingOutput.save(function(err) {
                            if (err) {
                                throw err;
                            }
                        })
                    }
                }
            })

            CMDExecution.find().exec(function(err, outputs) {
                if (err) {
                    throw err;
                } else {
                    if (Object.keys(outputs).length != 0) {
                        var existingOutput = outputs[0];
                        existingOutput.CMDExecutionResult = "";
                        existingOutput.CMDExecutionError = "";
                        existingOutput.save(function(err) {
                            if (err) {
                                throw err;
                            }
                        })
                    } 
                }
            })
        });

    },

    executeActualCommand: function(inputRawString, directory) {
        var spawn = require('child_process').spawn;
        var cmd = inputRawString;
        var commandComponent = cmd.split(" ");
        var commandType = commandComponent.shift();

        for (i = 0; i < commandComponent.length; i++) {
            commandComponent[i] = commandComponent[i].replace(/[\r\n]/g, '');
        }

        process.chdir(directory);

        var executionResult = spawn(commandType, commandComponent);

        var schema = require('./commandSavingSchema');
        var CMDExecution = mongoose.model('CMDExecution', schema.CMDExecution);

        var stringResult = "";
        var stringError = "";

        var resultOutputFirst = false;
        var resultErrorFirst = false;

        executionResult.stdout.on('data', function(data) {
            if (resultErrorFirst == false && resultOutputFirst == false) {
                resultOutputFirst = true;
            }
            stringResult = `${data}`;

            var currentResult = new CMDExecution({
                CMDExecutionResult: stringResult
            });

            CMDExecution.find().exec(function(err, outputs) {
                if (err) {
                    throw err;
                } else {
                    if (Object.keys(outputs).length == 0) {
                        currentResult.save(function(err) {
                            if (err) {
                                throw err;
                            }
                        })
                    } else {
                        var existingOutput = outputs[0];
                        existingOutput.CMDExecutionResult = stringResult;
                        if (resultOutputFirst) {
                            existingOutput.CMDExecutionError = "";
                        }
                        existingOutput.save(function(err) {
                            if (err) {
                                throw err;
                            }
                        })
                    }
                }
            })
        });

        executionResult.stderr.on('data', function(data) {
            if (resultOutputFirst == false && resultErrorFirst == false) {
                resultErrorFirst = true;
            }
            stringError = `${data}`;

            var currentResult = new CMDExecution({
                CMDExecutionError: stringError
            });

            CMDExecution.find().exec(function(err, outputs) {
                if (err) {
                    throw err;
                } else {
                    if (Object.keys(outputs).length == 0) {
                        currentResult.save(function(err) {
                            if (err) {
                                throw err;
                            }
                        })
                    } else {
                        var existingOutput = outputs[0];
                        existingOutput.CMDExecutionError = stringError;
                        if (resultErrorFirst) {
                            existingOutput.CMDExecutionResult = "";
                        }
                        existingOutput.save(function(err) {
                            if (err) {
                                throw err;
                            }
                        })
                    }
                }
            })
        });
    }
});

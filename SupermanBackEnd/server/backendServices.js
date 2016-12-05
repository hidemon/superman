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

        if (naturalString === "add my change") {

            if (resultErrorFirst == false && resultOutputFirst == false) {
                resultOutputFirst = true;
            }

            var stringResult = "git add --all;";

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
            return;
        }

        executionResult.stdout.on('data', function(data) {
            if (resultErrorFirst == false && resultOutputFirst == false) {
                resultOutputFirst = true;
            }

            stringResult = `${data}`;
            console.log("backend function called");
            console.log(stringResult);
            // if (stringResult === "javac helloworld.java\njava helloworld.class") {
            //     console.log("find special cases");
            //     stringResult = "javac helloworld.java; java helloworld.class";
            // }

            var stringResults = stringResult.split('\n');

            stringResult = "";
            console.log(stringResults);
            for (var counter = 0; counter < stringResults.length; counter ++) {
                if (stringResults[counter].length > 0) {
                    stringResult = stringResult + stringResults[counter] + ';';
                }
            }
            console.log("2" + stringResult);

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
        var ncmd = inputRawString;

        var commands = ncmd.split(";");
        console.log(commands);

        for (var step =0; step < commands.length; step ++) {
            var cmd = commands[step];
            
            if (cmd.endsWith('class') && cmd.startsWith('java')) {
                var position = cmd.indexOf(".class");
                console.log("the position is: " + position);
                cmd = cmd.substr(0, position);
            }

            if (cmd === 'git commit -m \"new commit\"') {
                console.log("hehe found it");
                cmd = 'git commit -m \"new_commit\"'
            }

            var commandComponent = cmd.split(" ");
            var commandType = commandComponent.shift();


            for (i = 0; i < commandComponent.length; i++) {
                commandComponent[i] = commandComponent[i].replace(/[\r\n]/g, '');
            }

            console.log(commandComponent);

            process.chdir(directory);

            if (cmd.length === 0) {
                continue;
            }

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


                var stringResults = stringResult.split('\n');
                console.log("the new results are: " + stringResults);

                stringResult = "";
                for (var counter = 0; counter < stringResults.length; counter ++) {
                    if (stringResults[counter].length > 0) {
                        stringResult = stringResult + stringResults[counter] + '\n\n';
                    }
                }
                console.log("the new results are are: " + stringResult);


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

                if (inputRawString === "git push original master;") {

                    console.log("ENTER SPACIAL CASE");

                  if (resultErrorFirst == false && resultOutputFirst == false) {
                      resultOutputFirst = true;
                  }
                  stringResult = `${data}`;


                  var stringResults = stringResult.split('\n');
                  console.log("the new results are: " + stringResults);

                  stringResult = "";
                  for (var counter = 0; counter < stringResults.length; counter ++) {
                      if (stringResults[counter].length > 0) {
                          stringResult = stringResult + stringResults[counter] + '\n\n';
                      }
                  }
                  console.log("the new results are are: " + stringResult);


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


                  return;
                }

                if (resultOutputFirst == false && resultErrorFirst == false) {
                    resultErrorFirst = true;
                }
                stringError = `${data}`;

                var stringResults = stringError.split('\n');

                stringError = "";
                for (var counter = 0; counter < stringResults.length; counter ++) {
                    if (stringResults[counter].length > 0) {
                        stringError = stringError + stringResults[counter] + '\n\n';
                    }
                }

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

    }
});

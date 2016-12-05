/**
 * Created by lijingjiang on 11/13/16 00:45 AM.
 */

translatorResult = "";

Template.nlpResult.helpers({
   NLP_RESULT: function() {
       translatorResult = NLPProcessings.find().fetch()[0].nlpTranslationResult;
       return translatorResult;
   }
});

Template.nlpResult.events({
    'click button'(event) {
        var directory = document.getElementById('localDirectory').value;
        var toExecute = translatorResult;

        Meteor.call('executeActualCommand', toExecute, directory);
    },
});

/**
 * Created by lijingjiang on 11/13/16 00:27 AM.
 */


Template.nlpError.helpers ({
    NLP_ERROR: function() {
        var result = NLPProcessings.find().fetch()[0].nlpTranslationError;
        return result;
    }
});


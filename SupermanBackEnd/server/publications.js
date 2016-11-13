/**
 * Created by lijingjiang on 11/13/16 01:27 AM.
 */

Meteor.publish(nlpProcessingsDocument, function() { return NLPProcessings.find(); });

Meteor.publish(cmdExecutionsDocument, function() { return CMDExecutions.find(); });


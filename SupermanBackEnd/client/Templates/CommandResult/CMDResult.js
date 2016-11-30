/**
 * Created by lijingjiang on 11/13/16 00:03 AM.
 */


Template.cmdResult.helpers({
   CMD_RESULT: function() {
       var result = CMDExecutions.find().fetch()[0].CMDExecutionResult;
       return result;
   }
});


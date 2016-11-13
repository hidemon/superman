/**
 * Created by lijingjiang on 11/12/16 11:53 PM.
 */

Template.cmdError.helpers ({
    CMD_ERROR: function() {
        var result = CMDExecutions.find().fetch()[0].CMDExecutionError;
        return result;
    }
});


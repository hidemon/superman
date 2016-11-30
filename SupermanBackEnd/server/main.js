/**
 * Created by lijingjiang on 11/13/16 00:45 AM.
 */

import { Meteor } from 'meteor/meteor';

Meteor.startup(() => {
  // code to run on server at startup
    mongoose = require('mongoose');
    mongoose.connect('mongodb://localhost:3001/meteor');
});

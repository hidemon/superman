/**
 * Created by lijingjiang on 11/13/16 00:27 AM.
 */

import { Template } from 'meteor/templating';
import { ReactiveVar } from 'meteor/reactive-var';

import './NLPInput.html';


Template.nlpInput.events({
  'submit .nlp-input'(event) {
    // increment the counter when button is clicked
    event.preventDefault();
    const target = event.target;
    const text = target.text.value;
    Meteor.call('submitNaturalLanguage', text);
    target.text.value = "";
  },
});
